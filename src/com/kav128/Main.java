/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128;

import com.kav128.todo.Task;
import com.kav128.todo.ToDoApp;

import java.time.LocalDate;

class Main
{
    public static void main(String[] args) throws Exception
    {
        try (ToDoApp app = new ToDoApp())
        {
//            UI.init(app);
//            UI.instance().run();

            app.login("user1", "111");
            app.load();
            Task task = app.getTaskList().get(0);
            app.logout();
            app.createTask("sef", "awef", LocalDate.now());

            app.login("kav128", "111");
            task.setDescription("kav128");
            System.out.println(task);
        }
    }
}
