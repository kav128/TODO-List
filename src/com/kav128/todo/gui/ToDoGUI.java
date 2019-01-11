/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.gui;

import com.kav128.todo.*;
import com.kav128.todo.data.DataRecord;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ToDoGUI
{
    private ToDoApp app;
    private Scene mainScene;
    private Scene loginScene;
    private Scene editorScene;
    private Scene promptScene;
    private Scene notificationsScene;

    private MainController mainController;
    private LoginController loginController;
    private EditorController editorController;
    private PromptController promptController;
    private NotificationsController notificationsController;

    private Stage mainStage;

    public ToDoGUI(ToDoApp app,
                   Stage mainStage,
                   FXMLLoader mainLoader,
                   FXMLLoader loginLoader,
                   FXMLLoader editorLoader,
                   FXMLLoader promptLoader,
                   FXMLLoader notificationsLoader)
    {
        this.app = app;
        this.mainStage = mainStage;
        try
        {
            Parent mainRoot = mainLoader.load();
            mainScene = new Scene(mainRoot, 900, 680);

            Parent loginRoot = loginLoader.load();
            loginScene = new Scene(loginRoot, 600, 450);

            Parent editorRoot = editorLoader.load();
            editorScene = new Scene(editorRoot, 600, 484);

            Parent promptRoot = promptLoader.load();
            promptScene = new Scene(promptRoot, 600, 139);

            Parent notificationsRoot = notificationsLoader.load();
            notificationsScene = new Scene(notificationsRoot, 800, 450);

            mainController = mainLoader.getController();
            loginController = loginLoader.getController();
            editorController = editorLoader.getController();
            promptController = promptLoader.getController();
            notificationsController = notificationsLoader.getController();

            mainController.setApp(app);
            mainController.setGui(this);
            loginController.setApp(app);
            editorController.setApp(app);
            editorController.setGui(this);
            notificationsController.setApp(app);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void login()
    {
        Stage loginStage = new Stage();
        loginStage.initOwner(mainStage);
        loginStage.initModality(Modality.APPLICATION_MODAL);
        loginStage.setScene(loginScene);
        loginStage.setTitle("Авторизация");
        loginStage.showAndWait();
    }

    void logout()
    {
        UserController userController = app.getUserController();
        userController.logout();
        mainStage.hide();
        run();
    }

    public void run()
    {
        login();
        if (!app.getUserController().isAuthorized())
            return;

        mainController.initData();
        mainStage.setTitle("ToDo List by kav128");
        mainStage.setScene(mainScene);
        mainStage.show();
    }

    Task addTask()
    {
        editorController.setTask(null);
        openEditor();

        DataRecord record = editorController.getRecord();
        if (record == null)
            return null;
        String title = record.getString("title");
        String description = record.getString("description");
        LocalDate deadline = record.getDate("deadline");
        TaskPurpose purpose = TaskPurpose.valueOf(record.getString("purpose"));
        List<String> assignedUsers = editorController.getAssignedUsers();

        TaskController tc = app.getTaskController();
        UserController uc = app.getUserController();
        Task task = tc.createTask(title, description, purpose, deadline);
        for (String user : assignedUsers)
            tc.assign(task, uc.getUserByName(user));
        return task;
    }

    void editTask(Task task)
    {
        editorController.setTask(task);
        List<User> assignedUsers = task.getAssignedUsers();
        openEditor();

        DataRecord record = editorController.getRecord();
        if (record == null)
            return;
        String title = record.getString("title");
        String description = record.getString("description");
        LocalDate deadline = record.getDate("deadline");
        TaskPurpose purpose = TaskPurpose.valueOf(record.getString("purpose"));
        List<String> newAssignedUsers = editorController.getAssignedUsers();

        task.setTitle(title);
        task.setDescription(description);
        task.setDeadline(deadline);
        task.setPurposeTag(purpose);

        TaskController tc = app.getTaskController();
        UserController uc = app.getUserController();
        for (String username : newAssignedUsers)
        {
            User user = uc.getUserByName(username);
            if (!assignedUsers.contains(user))
                tc.assign(task, user);
        }
    }

    private void openEditor()
    {
        Stage editorStage = new Stage();
        editorStage.setScene(editorScene);
        editorStage.initOwner(mainStage);
        editorStage.initModality(Modality.APPLICATION_MODAL);
        editorStage.setTitle("Редактор задач");
        editorStage.showAndWait();
    }

    String prompt(String promptMessage)
    {
        promptController.init();
        promptController.setPromptText(promptMessage);
        Stage promptStage = new Stage();
        promptStage.initOwner(mainStage);
        promptStage.initModality(Modality.APPLICATION_MODAL);
        promptStage.setScene(promptScene);
        promptStage.setTitle(promptMessage);
        promptStage.showAndWait();
        return promptController.getValue();
    }

    void showNotifications()
    {
        notificationsController.init();
        Stage notificationsStage = new Stage();
        notificationsStage.initOwner(mainStage);
        notificationsStage.initModality(Modality.APPLICATION_MODAL);
        notificationsStage.setTitle("Уведомления");
        notificationsStage.setScene(notificationsScene);
        notificationsStage.showAndWait();
    }
}
