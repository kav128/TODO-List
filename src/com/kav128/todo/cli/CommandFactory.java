/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.cli;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

class CommandFactory
{
    private Map<String, Function<String[], Command>> commandsByName;

    CommandFactory()
    {
        this.commandsByName = new HashMap<>();
        commandsByName.put("add", NewTaskCommand::parse);
        commandsByName.put("edit", EditTaskCommand::parse);
        commandsByName.put("remove", RemoveTaskCommand::parse);
        commandsByName.put("show", ShowCommand::parse);
        commandsByName.put("commit", CommitCommand::parse);
    }

    Command getFromString(String commandName, String[] args)
    {
        return commandsByName.get(commandName).apply(args);
    }
}
