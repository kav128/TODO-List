/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo;

public class Notification
{
    private int id;
    private User sender;
    private int taskId;

    public Notification(int id, User sender, int taskId)
    {
        this.id = id;
        this.sender = sender;
        this.taskId = taskId;
    }

    public int getId()
    {
        return id;
    }

    public User getSender()
    {
        return sender;
    }

    public int getTaskId()
    {
        return taskId;
    }
}
