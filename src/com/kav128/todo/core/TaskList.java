/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.core;

import com.kav128.todo.data.TaskModifyTrigger;
import com.kav128.todo.data.TasksDAO;

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

    Task createTask(String title, String description, LocalDate deadline, TaskPurpose purposeTag, User author)
    {
        try
        {
            int id = dao.insertTask(title, description, deadline, author.getId());
            int purpose = TaskPurposeUtils.toInt(purposeTag);
            if (purpose >= 0)
                dao.setTaskTag(id, "purpose", purpose);
            TaskModifyTrigger trigger = new TaskModifyTrigger(dao, id, session);
            Task task = new Task(id, title, description, deadline, false, purposeTag, author, trigger);
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

    void insert(Task task)
    {
        taskList.add(task);
    }

    @Override
    public Iterator<Task> iterator()
    {
        return taskList.iterator();
    }

    public int count()
    {
        return taskList.size();
    }

    public void erase()
    {
        taskList.clear();
    }
}
