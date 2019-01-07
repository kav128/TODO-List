/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.data;

import com.kav128.todo.Session;
import com.kav128.todo.TaskPurpose;
import com.kav128.todo.TaskPurposeUtils;

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

    public boolean allowModification()
    {
        return session.isOpened();
    }

    public boolean titleModified(String newValue)
    {
        boolean isAllowed = allowModification();
        if (isAllowed)
            dao.updateTaskTitle(id, newValue);
        return isAllowed;
    }

    public boolean descriptionModified(String newValue)
    {
        boolean isAllowed = allowModification();
        if (isAllowed)
            dao.updateTaskDescription(id, newValue);
        return isAllowed;
    }

    public boolean deadlineModified(LocalDate newValue)
    {
        boolean isAllowed = allowModification();
        if (isAllowed)
            dao.updateTaskDeadline(id, newValue);
        return isAllowed;
    }

    public boolean completedModified(boolean newValue)
    {
        boolean isAllowed = allowModification();
        if (isAllowed)
            dao.updateTaskCompleted(id, newValue);
        return isAllowed;
    }

    public boolean taskPurposeModified(TaskPurpose newValue)
    {
        boolean isAllowed = allowModification();
        if (isAllowed)
            return dao.updateTaskTag(id, "purpose", TaskPurposeUtils.toInt(newValue));
        return false;
    }

    public boolean taskPurposeSet(TaskPurpose newValue)
    {
        boolean isAllowed = allowModification();
        if (isAllowed)
            return dao.setTaskTag(id, "purpose", TaskPurposeUtils.toInt(newValue));
        return false;
    }
}
