/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.cli;

class ShowCommand
{
    static Command parse(String[] args)
    {
        switch (args[0])
        {
            case "task":
                int index = Integer.parseInt(args[1]) - 1;
                return new ShowTaskCommand(UI.instance().getTaskList().get(index));
            case "tasks":
                return new ShowListCommand(UI.instance().getTaskList());
            default:
                return null;
        }
    }
}
