/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.data;

import com.kav128.data.DataWriter;
import com.kav128.data.Date;

import java.util.UUID;

public class TaskModifyTrigger
{
    private DataWriter writer;
    private UUID uuid;

    public TaskModifyTrigger(DataWriter writer, UUID uuid)
    {
        this.writer = writer;
        this.uuid = uuid;
    }

    public void titleModified(String newValue)
    {
        writer.modifyRecord(uuid);
        writer.setField("title", newValue);
        writer.accept();
    }

    public void descriptionModified(String newValue)
    {
        writer.modifyRecord(uuid);
        writer.setField("description", newValue);
        writer.accept();
    }

    public void deadlineModified(Date newValue)
    {
        writer.modifyRecord(uuid);
        writer.setField("deadline", newValue);
        writer.accept();
    }

    public void completedModified(boolean newValue)
    {
        writer.modifyRecord(uuid);
        writer.setField("completed", newValue);
        writer.accept();
    }
}
