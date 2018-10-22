/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo;

import com.kav128.data.DataReader;
import com.kav128.data.DataWriter;
import com.kav128.todo.data.TaskModifyTrigger;

import java.util.*;

public class TaskList implements Iterable<Task>
{
    private List<Task> taskList;
    private DataReader reader;
    private DataWriter writer;

    public TaskList(DataReader reader, DataWriter writer)
    {
        this.reader = reader;
        this.writer = writer;
        taskList = new ArrayList<>();
        load();
    }

    public Task createTask(String title, String description, Date deadline)
    {
        UUID uuid = writer.newRecord();
        TaskModifyTrigger trigger = new TaskModifyTrigger(writer, uuid);
        Task task = new Task(uuid, title, description, deadline, false, trigger);
        taskList.add(task);
        writer.setField("title", title);
        writer.setField("description", description);
        writer.setField("deadline", deadline);
        writer.setField("completed", false);
        writer.accept();
        return task;
    }

    public Task get(int index)
    {
        return taskList.get(index);
    }

    public void remove(int index)
    {
        Task task = taskList.get(index);
        taskList.remove(task);
        writer.remove(task.getUuid());
    }

    @Override
    public Iterator<Task> iterator()
    {
        return taskList.iterator();
    }

    private void load()
    {
        while (reader.read())
        {
            UUID uuid = reader.getUUID();
            String title = reader.getString("title");
            String description = reader.getString("description");
            Date deadline = reader.getDate("deadline");
            Boolean completed = reader.getBoolean("completed");
            TaskModifyTrigger trigger = new TaskModifyTrigger(writer, uuid);
            Task task = new Task(uuid, title, description, deadline, completed, trigger);
            taskList.add(task);
        }
    }

    public int count()
    {
        return taskList.size();
    }
}
