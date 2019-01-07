/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo;

public class TaskPurposeUtils
{
    public static int toInt(TaskPurpose purpose)
    {
        switch (purpose)
        {
            case personal:
                return 0;
            case work:
                return 1;
        }
        return -1;
    }

    public static TaskPurpose parseFromInt(int value)
    {
        switch (value)
        {
            case 0:
                return TaskPurpose.personal;
            case 1:
                return TaskPurpose.work;
        }
        return TaskPurpose.noPurpose;
    }
}
