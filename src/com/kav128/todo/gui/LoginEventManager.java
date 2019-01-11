/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.gui;

import com.kav128.todo.core.ToDoApp;
import com.kav128.todo.core.UserController;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

public class LoginEventManager
{
    private List<EventListener> listeners;
    private ToDoApp app;

    public LoginEventManager(ToDoApp app)
    {
        listeners = new ArrayList<>();
        this.app = app;
    }

    public void subscribe(EventListener listener)
    {
        listeners.add(listener);
    }

    private void notifyProgress(double progress)
    {
        for (EventListener listener : listeners)
            listener.update(progress);
    }

    private void notifyCompleted(boolean success)
    {
        for (EventListener listener : listeners)
            listener.complete(success);
    }

    void login(String login, String password)
    {
        Task<Void> task = new Task<>()
        {
            @Override
            protected Void call()
            {
                notifyProgress(0);
                useless(0, 0.45, 100);
                UserController uc = app.getUserController();
                boolean success = uc.login(login, password);
                useless(0.5, 1, 100);
                notifyProgress(1);
                waitALittle(50);
                notifyCompleted(success);
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public void register(String login, String password)
    {
        Task<Void> task = new Task<>()
        {
            @Override
            protected Void call()
            {
                notifyProgress(0);
                useless(0, 0.45, 100);
                UserController uc = app.getUserController();
                boolean success = uc.register(login, password);
                useless(0.5, 1, 100);
                notifyProgress(1);
                waitALittle(50);
                notifyCompleted(success);
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    private void useless(double from, double to, int tick)
    {
        double d = from;
        do
        {
            waitALittle(tick);
            notifyProgress(d);
            d += 0.05;
        }
        while (d < to);
    }

    private void waitALittle(int time)
    {
        try
        {
            Thread.sleep(time);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
