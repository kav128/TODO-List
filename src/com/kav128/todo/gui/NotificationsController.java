/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.gui;

import com.kav128.todo.Notification;
import com.kav128.todo.NotificationController;
import com.kav128.todo.ToDoApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class NotificationsController
{
    @FXML
    public ListView<Notification> notificationsList;

    @FXML
    public Label titleLabel;

    @FXML
    public Label senderLabel;

    @FXML
    public Button acceptButton;

    @FXML
    public void acceptClick()
    {
        Notification item = notificationsList.getSelectionModel().getSelectedItem();
        item.accept();
        notificationsList.refresh();
    }

    private ToDoApp app;

    public void initialize()
    {
        notificationsList.setCellFactory(cell -> new ListCell<>()
        {
            @Override
            protected void updateItem(Notification item, boolean empty)
            {
                getStyleClass().remove("good");
                getStyleClass().remove("seen");
                getStyleClass().remove("unseen");
                setText("");
                if (item != null && !empty && !isSelected())
                {
                    if (item.isAccepted())
                        getStyleClass().add("good");
                    if (item.isSeen())
                        getStyleClass().add("seen");
                    else
                        getStyleClass().add("unseen");
                    setText(item.toString());
                }
                super.updateItem(item, empty);
            }
        });

        notificationsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            acceptButton.setVisible(newValue != null);
            if (newValue == null)
            {
                titleLabel.setText("");
                senderLabel.setText("");
            }
            else
            {
                int taskId = newValue.getTaskId();
                String title = app.getTaskController().getTaskTitle(taskId);
                titleLabel.setText(title);
                String sender = newValue.getSender().getUsername();
                senderLabel.setText("Отправлено пользователем " + sender);
                newValue.setSeen();
            }
        });
    }

    void init()
    {
        notificationsList.getItems().clear();
        NotificationController nc = app.getNotificationController();
        Notification[] notifications = nc.getNotifications();
        notificationsList.getItems().addAll(notifications);
    }

    public void setApp(ToDoApp app)
    {
        this.app = app;
    }
}
