/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.gui;

import com.kav128.todo.core.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainController
{
    @FXML
    public Label titleLabel;

    @FXML
    public Label descriptionLabel;

    @FXML
    public Label deadlineLabel;

    @FXML
    public Label completedLabel;

    @FXML
    public Label tagsLabel;

    @FXML
    public Label authorLabel;

    @FXML
    public Button editButton;

    @FXML
    public Button deleteButton;

    @FXML
    public Button completeButton;

    @FXML
    public Label backgroundLabel;

    @FXML
    public ProgressIndicator backgroundIndicator;

    @FXML
    private Label usernameLabel;

    @FXML
    private Hyperlink notificationsLabel;

    @FXML
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task, String> titleColumn;

    @FXML
    private TableColumn<Task, LocalDate> deadlineColumn;

    @FXML
    public void logoutClick()
    {
        gui.logout();
    }

    @FXML
    public void notificationsClick()
    {
        gui.showNotifications();
        initData();
    }

    private ToDoApp app;
    private ToDoGUI gui;

    void setApp(ToDoApp app)
    {
        this.app = app;
    }

    void setGui(ToDoGUI gui)
    {
        this.gui = gui;
    }

    @FXML
    private void initialize()
    {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));

        taskTable.setRowFactory(row -> new TableRow<>() {
            @Override
            protected void updateItem(Task item, boolean empty)
            {
                super.updateItem(item, empty);

                getStyleClass().remove("good");
                if (item != null
                        && !empty
                        && item.isCompleted()
                        && !isSelected())
                    getStyleClass().add("good");
            }
        });

        taskTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showTask(newValue);
            if (newValue != null)
            {
                int curUserId = app.getUserController().getCurUser().getId();
                int taskAuthorId = newValue.getAuthor().getId();
                boolean canEdit = taskAuthorId == curUserId;
                editButton.setDisable(!canEdit);
                deleteButton.setDisable(!canEdit);
                completeButton.setVisible(canEdit);
            }
            completeButton.setVisible(newValue != null && !newValue.isCompleted());
        });
    }

    void initData()
    {
        UserController uc = app.getUserController();
        TaskController tc = app.getTaskController();

        String username = uc.getCurUser().getUsername();
        usernameLabel.setText(username);
        initNotificationsCounter();

        tc.load();
        TaskList taskList = tc.getTaskList();
        taskTable.getItems().clear();
        for (Task task : taskList)
            taskTable.getItems().addAll(task);
    }

    private void initNotificationsCounter()
    {
        NotificationController nc = app.getNotificationController();
        int notificationCount = nc.getNotificationCount();
        notificationsLabel.setText(String.format("Уведомления (%d)", notificationCount));
        if (notificationCount > 0)
            notificationsLabel.setStyle("-fx-font-weight: bold");
        else
            notificationsLabel.setStyle("");
    }

    private void showTask(Task task)
    {
        if (task == null)
        {
            titleLabel.setText("");
            descriptionLabel.setText("");
            deadlineLabel.setText("");
            completedLabel.setText("");
            completedLabel.setStyle("");
            tagsLabel.setText("");
            authorLabel.setText("");
            return;
        }

        titleLabel.setText(task.getTitle());
        descriptionLabel.setText(task.getDescription());
        deadlineLabel.setText(String.format("Выполнить до %s", task.getDeadline().format(DateTimeFormatter.ISO_DATE)));
        if (task.isCompleted())
        {
            completedLabel.setText("Задача выполнена");
            completedLabel.getStyleClass().clear();
            completedLabel.getStyleClass().add("good");
        }
        else
        {
            completedLabel.setText("Задача не выполнена");
            completedLabel.getStyleClass().clear();
            completedLabel.getStyleClass().add("bad");
        }

        switch (task.getPurposeTag())
        {
            case work:
                tagsLabel.setText("Рабочая задача");
                break;
            case personal:
                tagsLabel.setText("Личная задача");
                break;
            default:
                tagsLabel.setText("");
        }

        if (task.getAuthor().getId() != app.getUserController().getCurUser().getId())
            authorLabel.setText(String.format("Автор задачи: %s", task.getAuthor().getUsername()));
        else
            authorLabel.setText("");
    }

    @FXML
    public void addClick()
    {
        Task task = gui.addTask();
        if (task != null)
            taskTable.getItems().add(task);
    }

    @FXML
    public void editClick()
    {
        Task task = taskTable.getSelectionModel().getSelectedItem();
        gui.editTask(task);
        showTask(task);
        taskTable.refresh();
    }

    @FXML
    public void deleteClick()
    {
        TaskController tc = app.getTaskController();
        Task selectedItem = taskTable.getSelectionModel().getSelectedItem();
        int id = selectedItem.getId();
        tc.remove(id);
        taskTable.getItems().remove(selectedItem);
    }

    @FXML
    public void completeClick()
    {
        Task task = taskTable.getSelectionModel().getSelectedItem();
        task.setCompleted(true);
        showTask(task);
    }

    @FXML
    public void exportClicked()
    {
        TaskController tc = app.getTaskController();
        String filename = app.getUserController().getCurUser().getUsername() + "export.xml";
        BackgroundTask export = tc.exportToXML(filename);
        ProgressListener listener = new MainSceneProgressListener(backgroundLabel, backgroundIndicator);
        export.subscribe(listener);
        export.run();
    }
}
