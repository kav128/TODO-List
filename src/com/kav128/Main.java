/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128;

import com.kav128.data.DatabaseManager;
import com.kav128.data.TasksSingleDAO;
import com.kav128.todo.TaskList;
import com.kav128.todo.cli.UI;

class Main
{
    public static void main(String[] args) throws Exception
    {
        try (DatabaseManager dbm = new DatabaseManager();
             TasksSingleDAO dao = dbm.getTasksSingleDAO())
        {
            TaskList taskList = new TaskList(dao);
            taskList.load();

            UI.init(taskList);
            UI.instance().run();
        }
    }
}
