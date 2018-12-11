/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo;

import com.kav128.data.DataRecord;
import com.kav128.data.TasksDAO;
import com.kav128.todo.data.TaskModifyTrigger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskList implements Iterable<Task>
{
    private final List<Task> taskList;
    private final TasksDAO dao;
    private final Session session;

    TaskList(TasksDAO dao, Session session)
    {
        // DAL, layer architecture, facade,
        taskList = new ArrayList<>();
        this.dao = dao;
        this.session = session;
    }

    Task createTask(String title, String description, LocalDate deadline, int authorId)
    {
        try
        {
            int id = dao.insertTask(title, description, deadline, authorId);
            TaskModifyTrigger trigger = new TaskModifyTrigger(dao, id, session);
            Task task = new Task(id, title, description, deadline, false, trigger);
            taskList.add(task);
            return task;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public Task get(int index)
    {
        return taskList.get(index);
    }

    void remove(int index)
    {
        try
        {
            Task task = taskList.get(index);
            dao.deleteTask(task.getId());
            taskList.remove(task);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Iterator<Task> iterator()
    {
        return taskList.iterator();
    }

    void load()
    {
        try
        {
            DataRecord[] taskRecords = dao.getTasksByUser(session.getUser().getId());
            for (DataRecord record : taskRecords)
            {
                int id = Integer.parseInt(record.getValue("id"));
                String title = record.getValue("title");
                String description = record.getValue("description");
                LocalDate deadline = LocalDate.parse(record.getValue("deadline"));
                Boolean completed = Boolean.valueOf(record.getValue("completed"));
                TaskModifyTrigger trigger = new TaskModifyTrigger(dao, id, session);
                Task task = new Task(id, title, description, deadline, completed, trigger);
                taskList.add(task);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int count()
    {
        return taskList.size();
    }
}
