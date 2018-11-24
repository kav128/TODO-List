/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128;

import com.kav128.data.DataSource;
import com.kav128.data.XMLDataSource;
import com.kav128.todo.TaskList;
import com.kav128.todo.cli.UI;

class Main
{
    public static void main(String[] args)
    {
        DataSource source = new XMLDataSource("tasks.xml", "tasklist");
        source.open();
        TaskList taskList = new TaskList(source);
        taskList.load();

        UI.init(taskList, source);
        UI.instance().run();

        source.close();
    }
}
