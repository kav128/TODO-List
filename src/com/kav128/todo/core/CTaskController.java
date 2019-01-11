/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.core;

import com.kav128.todo.data.DataRecord;
import com.kav128.todo.data.TaskModifyTrigger;
import com.kav128.todo.data.TasksDAO;

import java.time.LocalDate;
import java.util.List;

class CTaskController implements TaskController
{
    private TasksDAO dao;
    private TaskList taskList;
    private ToDoApp app;

    CTaskController(ToDoApp app, Session session)
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

    @Override
    public Task createTask(String title, String description, TaskPurpose purposeTag, LocalDate deadline)
    {
        if (taskList != null)
        {
            User author = app.getUserController().getCurUser();
            int id = dao.insertTask(title, description, deadline, author.getId());
            int purpose = TaskPurposeUtils.toInt(purposeTag);
            if (purpose >= 0)
                dao.setTaskTag(id, "purpose", purpose);
            TaskModifyTrigger trigger = new TaskModifyTrigger(dao, id, app.getUserControllerClass().getSession());
            Task task = new Task(id, title, description, deadline, false, purposeTag, author, trigger);
            taskList.insert(task);
            return task;
        }
        else
            return null;
    }

    @Override
    public void remove(int index)
    {
        taskList.remove(index);
    }

    @Override
    public void load()
    {
        try
        {
            CUserController uc = app.getUserControllerClass();
            DataRecord[] taskRecords = dao.getTasksByUser(uc.getCurUser().getId());
            taskList.erase();
            for (DataRecord record : taskRecords)
            {
                int id = record.getInt("id");
                String title = record.getString("title");
                String description = record.getString("description");
                LocalDate deadline = record.getDate("deadline");
                Boolean completed = record.getBoolean("completed");
                TaskModifyTrigger trigger = new TaskModifyTrigger(dao, id, uc.getSession());
                TaskPurpose purposeTag;
                int purposeIntTag = dao.getTaskTag(id, "purpose");
                purposeTag = TaskPurposeUtils.parseFromInt(purposeIntTag);
                User author = uc.getUserById(dao.getTaskAuthor(id));
                Task task = new Task(id, title, description, deadline, completed, purposeTag, author, trigger);

                List<Integer> userIDs = dao.getAssignedUserIDs(id);
                for (int userID : userIDs)
                {
                    User user = uc.getUserById(userID);
                    task.addAssignedUser(user);
                }
                taskList.insert(task);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public TaskList getTaskList()
    {
        return taskList;
    }

    @Override
    public void assign(Task task, User user)
    {
        app.getNotificationController().addNotification(app.getUserController().getCurUser(), user, task);
    }

    @Override
    public String getTaskTitle(int taskId)
    {
        DataRecord record = dao.getTaskByID(taskId);
        return record.getString("title");
    }
}
