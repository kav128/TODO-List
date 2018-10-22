/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128;

import com.kav128.data.CSVDataSource;
import com.kav128.data.DataReader;
import com.kav128.data.DataWriter;
import com.kav128.todo.TaskList;
import com.kav128.todo.cli.CommandLineInterpreter;

public class Main
{
    public static void main(String[] args)
    {
        String[] todoFields = new String[]{"uuid", "title", "description", "deadline", "completed"};
        CSVDataSource source = new CSVDataSource("tasks.csv", todoFields);
        source.open();
        DataReader reader = new DataReader(source);
        DataWriter writer = new DataWriter(source);
        TaskList taskList = new TaskList(reader, writer);

        CommandLineInterpreter cli = new CommandLineInterpreter(taskList, source);
        cli.run();

        source.close();
    }
}
