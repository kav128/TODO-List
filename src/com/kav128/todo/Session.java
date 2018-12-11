/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo;

import java.util.UUID;

public class Session
{
    private UUID sessionID;
    private User user;
    private boolean opened;

    public Session(User user)
    {
        this.user = user;
        sessionID = UUID.randomUUID();
        opened = true;
    }

    void close()
    {
        opened = false;
    }

    public UUID getSessionID()
    {
        return sessionID;
    }

    public User getUser()
    {
        return user;
    }

    public boolean isOpened()
    {
        return opened;
    }
}
