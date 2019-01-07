/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo;

import com.kav128.todo.data.DataRecord;
import com.kav128.todo.data.TaskModifyTrigger;
import com.kav128.todo.data.TasksDAO;

import java.time.LocalDate;

public class TaskController
{
    private TasksDAO dao;
    private TaskList taskList;
    private ToDoApp app;

    TaskController(ToDoApp app, Session session)
    {
        try
        {
            this.app = app;
            dao = app.getDbManager().getTasksDAO();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        taskList = new TaskList(dao, session);
    }

    public Task createTask(String title, String description, TaskPurpose purposeTag, LocalDate deadline)
    {
        if (taskList != null)
        {
            User author = app.getUserController().getCurUser();
            int id = dao.insertTask(title, description, deadline, author.getId());
            int purpose = TaskPurposeUtils.toInt(purposeTag);
            if (purpose >= 0)
                dao.setTaskTag(id, "purpose", purpose);
            TaskModifyTrigger trigger = new TaskModifyTrigger(dao, id, app.getUserController().getSession());
            Task task = new Task(id, title, description, deadline, false, purposeTag, author, trigger);
            taskList.insert(task);
            return task;
        }
        else
            return null;
    }

    public void remove(int index)
    {
        taskList.remove(index);
    }

    public void load()
    {
        try
        {
            DataRecord[] taskRecords = dao.getTasksByUser(app.getUserController().getCurUser().getId());
            taskList.erase();
            for (DataRecord record : taskRecords)
            {
                int id = record.getInt("id");
                String title = record.getString("title");
                String description = record.getString("description");
                LocalDate deadline = record.getDate("deadline");
                Boolean completed = record.getBoolean("completed");
                TaskModifyTrigger trigger = new TaskModifyTrigger(dao, id, app.getUserController().getSession());
                TaskPurpose purposeTag;
                int purposeIntTag = dao.getTaskTag(id, "purpose");
                purposeTag = TaskPurposeUtils.parseFromInt(purposeIntTag);
                User author = app.getUserController().getUserById(dao.getTaskAuthor(id));
                Task task = new Task(id, title, description, deadline, completed, purposeTag, author, trigger);
                taskList.insert(task);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public TaskList getTaskList()
    {
        return taskList;
    }

    public void assign(Task task, User user)
    {
        app.getNotificationController().addNotification(app.getUserController().getCurUser(), user, task);
    }
}
