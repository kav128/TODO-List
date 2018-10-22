/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.cli;

import com.kav128.todo.Task;

class TaskPrinter
{
    private static void printField(char initialChar, String fieldName, String value)
    {
        System.out.println(initialChar + fieldName + ":");
        System.out.println(initialChar + "\t" + value);
    }

    static void printTask(char initialChar, Task task)
    {
        printField(initialChar, "title", task.getTitle());
        printField(initialChar, "description", task.getDescription());
        printField(initialChar, "deadline", task.getDeadline().toString());
        printField(initialChar, "completed", Boolean.toString(task.isCompleted()));
    }
}
