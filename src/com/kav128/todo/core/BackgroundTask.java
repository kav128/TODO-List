/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.core;

public interface BackgroundTask extends Runnable
{
    void subscribe(ProgressListener listener);
}
