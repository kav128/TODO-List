/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128;

import com.kav128.todo.ToDoApp;
import com.kav128.todo.cli.UI;

class Main
{
    public static void main(String[] args) throws Exception
    {
        try (ToDoApp app = new ToDoApp())
        {
            UI.init(app);
            UI.instance().run();
        }
    }
}
