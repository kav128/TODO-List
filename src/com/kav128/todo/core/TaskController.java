/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.core;

import java.time.LocalDate;

public interface TaskController
{
    Task createTask(String title, String description, TaskPurpose purposeTag, LocalDate deadline);

    void remove(int index);

    void load();

    TaskList getTaskList();

    void assign(Task task, User user);

    String getTaskTitle(int taskId);
}
