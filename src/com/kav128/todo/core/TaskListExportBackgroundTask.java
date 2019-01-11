/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.core;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TaskListExportBackgroundTask implements BackgroundTask
{
    private List<ProgressListener> listeners;
    private TaskController taskController;
    private String filename;

    TaskListExportBackgroundTask(ToDoApp app, String filename)
    {
        this.filename = filename;
        listeners = new ArrayList<>();
        taskController = app.getTaskController();
    }

    private void notifyStart()
    {
        for (ProgressListener listener : listeners)
            listener.onStart();
    }

    private void notifyProgressChange(double progress)
    {
        for (ProgressListener listener : listeners)
            listener.onProgressChange(progress);
    }

    private void notifyComplete(boolean success)
    {
        for (ProgressListener listener : listeners)
            listener.onComplete(success);
    }

    @Override
    public void subscribe(ProgressListener listener)
    {
        listeners.add(listener);
    }

    private void executor()
    {
        try
        {
            notifyStart();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document document = builder.newDocument();
            Element root = document.createElement("taskExport");
            document.appendChild(root);

            TaskList taskList = taskController.getTaskList();
            int taskCount = taskList.count();
            for (int i = 0; i < taskCount; i++)
            {
                Thread.sleep(500);
                Task task = taskList.get(i);
                Element taskElement = document.createElement("task");

                Element titleElement = document.createElement("title");
                titleElement.setTextContent(task.getTitle());
                taskElement.appendChild(titleElement);

                Element descriptionElement = document.createElement("description");
                descriptionElement.setTextContent(task.getDescription());
                taskElement.appendChild(descriptionElement);

                Element deadlineElement = document.createElement("deadline");
                deadlineElement.setTextContent(task.getDeadline().format(DateTimeFormatter.ISO_DATE));
                taskElement.appendChild(deadlineElement);

                Element purposeElement = document.createElement("purpose");
                purposeElement.setTextContent(task.getPurposeTag().toString());
                taskElement.appendChild(purposeElement);

                Element completedElement = document.createElement("completed");
                completedElement.setTextContent(Boolean.toString(task.isCompleted()));
                taskElement.appendChild(completedElement);

                List<User> assignedUsers = task.getAssignedUsers();
                Element assignedUsersElement = document.createElement("assignedUsers");
                for (User user : assignedUsers)
                {
                    Element userElement = document.createElement("user");
                    userElement.setTextContent(user.getUsername());
                    assignedUsersElement.appendChild(userElement);
                }
                taskElement.appendChild(assignedUsersElement);

                root.appendChild(taskElement);
                notifyProgressChange((double)(i + 1) / taskCount);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(document);
            FileOutputStream stream = new FileOutputStream(filename);
            StreamResult result = new StreamResult(stream);
            transformer.transform(source, result);
            notifyComplete(true);
        }
        catch (ParserConfigurationException | FileNotFoundException | TransformerException | InterruptedException e)
        {
            e.printStackTrace();
            notifyComplete(false);
        }
    }

    @Override
    public void run()
    {
        javafx.concurrent.Task<Void> worker = new javafx.concurrent.Task<>()
        {
            @Override
            protected Void call()
            {
                executor();
                return null;
            }
        };
        Thread thread = new Thread(worker);
        thread.start();
    }
}
