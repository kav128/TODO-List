/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.data;

import com.kav128.data.TasksDAO;
import com.kav128.todo.Session;

import java.time.LocalDate;

public class TaskModifyTrigger
{
    private final TasksDAO dao;
    private final int id;
    private final Session session;

    public TaskModifyTrigger(TasksDAO dao, int id, Session session)
    {
        this.dao = dao;
        this.id = id;
        this.session = session;
    }

    private boolean allowModification()
    {
        return session.isOpened();
    }

    public boolean titleModified(String newValue) throws Exception
    {
        boolean isAllowed = allowModification();
        if (isAllowed)
            dao.updateTaskTitle(id, newValue);
        return isAllowed;
    }

    public boolean descriptionModified(String newValue) throws Exception
    {
        boolean isAllowed = allowModification();
        if (isAllowed)
            dao.updateTaskDescription(id, newValue);
        return isAllowed;
    }

    public boolean deadlineModified(LocalDate newValue) throws Exception
    {
        boolean isAllowed = allowModification();
        if (isAllowed)
            dao.updateTaskDeadline(id, newValue);
        return isAllowed;
    }

    public boolean completedModified(boolean newValue) throws Exception
    {
        boolean isAllowed = allowModification();
        if (isAllowed)
            dao.updateTaskCompleted(id, newValue);
        return isAllowed;
    }
}
