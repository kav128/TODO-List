/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.core;

public class User
{
    private final int id;
    private final String username;

    User(int id, String username)
    {
        this.id = id;
        this.username = username;
    }

    public int getId()
    {
        return id;
    }

    public String getUsername()
    {
        return username;
    }
}
