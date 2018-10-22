/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128;

import com.kav128.data.DataReader;
import com.kav128.data.DataSource;
import com.kav128.data.DataWriter;
import com.kav128.data.XMLDataSource;
import com.kav128.todo.TaskList;
import com.kav128.todo.cli.CommandLineInterpreter;

public class Main
{
    public static void main(String[] args)
    {
        DataSource source = new XMLDataSource("tasks.xml", "tasklist");
        source.open();
        DataReader reader = new DataReader(source);
        DataWriter writer = new DataWriter(source);
        TaskList taskList = new TaskList(reader, writer);

        CommandLineInterpreter cli = new CommandLineInterpreter(taskList, source);
        cli.run();

        source.close();
    }
}
