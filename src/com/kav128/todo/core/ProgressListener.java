/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.core;

public interface ProgressListener
{
    void onStart();

    void onProgressChange(double progress);

    void onComplete(boolean success);
}
