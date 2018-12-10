/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.data;

import com.kav128.data.TasksSingleDAO;

import java.time.LocalDate;

public class TaskModifyTrigger
{
    private final TasksSingleDAO dao;
    private final int id;

    public TaskModifyTrigger(TasksSingleDAO dao, int id)
    {
        this.dao = dao;
        this.id = id;
    }

    public void titleModified(String newValue) throws Exception
    {
        dao.updateTaskTitle(id, newValue);
    }

    public void descriptionModified(String newValue) throws Exception
    {
        dao.updateTaskDescription(id, newValue);
    }

    public void deadlineModified(LocalDate newValue) throws Exception
    {
        dao.updateTaskDeadline(id, newValue);
    }

    public void completedModified(boolean newValue) throws Exception
    {
        dao.updateTaskCompleted(id, newValue);
    }
}
