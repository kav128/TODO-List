/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128;

import com.kav128.todo.core.ToDoApp;
import com.kav128.todo.cli.UI;
import com.kav128.todo.gui.ToDoGUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class Main extends Application
{
    private static ToDoApp app;

    private FXMLLoader getLoaderFromFile(String path)
    {
        return new FXMLLoader(getClass().getResource(path));
    }


    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void init() throws Exception
    {
        super.init();
        app = new ToDoApp();
        UI.init(app);
    }

    @Override
    public void start(Stage primaryStage)
    {
        ToDoGUI gui = new ToDoGUI(
                app,
                primaryStage,
                getLoaderFromFile("resources/main.fxml"),
                getLoaderFromFile("resources/login.fxml"),
                getLoaderFromFile("resources/editor.fxml"),
                getLoaderFromFile("resources/prompt.fxml"),
                getLoaderFromFile("resources/notifications.fxml"));
        gui.run();
    }

    @Override
    public void stop() throws Exception
    {
        app.close();
        super.stop();
    }
}
