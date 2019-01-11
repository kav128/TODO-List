/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.gui;

public interface EventListener
{
    void update(double progress);

    void complete(boolean success);
}
