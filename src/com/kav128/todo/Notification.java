/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo;

public class Notification
{
    private NotificationController nc;
    private int id;
    private User sender;
    private int taskId;
    private boolean seen;
    private boolean accepted;

    Notification(NotificationController nc,
                 int id,
                 User sender,
                 int taskId, boolean seen, boolean accepted)
    {
        this.nc = nc;
        this.id = id;
        this.sender = sender;
        this.taskId = taskId;
        this.seen = seen;
        this.accepted = accepted;
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

    public boolean isSeen()
    {
        return seen;
    }

    public boolean isAccepted()
    {
        return accepted;
    }

    public void setSeen()
    {
        if (!seen)
            nc.setSeen(this);
        seen = true;
    }

    public void accept()
    {
        if (!accepted)
            nc.acceptNotification(this);
        accepted = true;
    }

    @Override
    public String toString()
    {
        return "Задача от " + sender.getUsername();
    }
}
