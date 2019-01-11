/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.gui;

import com.kav128.todo.Task;
import com.kav128.todo.ToDoApp;
import com.kav128.todo.User;
import com.kav128.todo.UserController;
import com.kav128.todo.data.DataRecord;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class EditorController
{
    @FXML
    public TextField titleField;

    @FXML
    public TextArea descriptionField;

    @FXML
    public DatePicker deadlinePicker;

    @FXML
    public ToggleGroup tag;

    @FXML
    public ListView<String> assignedUserList;

    @FXML
    public RadioButton noPurpose;

    @FXML
    public RadioButton personal;

    @FXML
    public RadioButton work;


    @FXML
    public void assignUserClick()
    {
        UserController uc = app.getUserController();
        String username = gui.prompt("Введите имя пользователя");
        if (username == null)
            return;

        User user = uc.getUserByName(username);
        if (user == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Ошибка");
            alert.setContentText("Нет такого пользователя");
            alert.showAndWait();
        }
        else
        {
            ObservableList<String> items = assignedUserList.getItems();
            if (!items.contains(username) && !username.equals(uc.getCurUser().getUsername()))
                items.add(username);
        }
    }

    @FXML
    public void saveClick()
    {
        record = new DataRecord();
        record.setString("title", titleField.getText());
        record.setString("description", descriptionField.getText());
        record.setDate("deadline", deadlinePicker.getValue());
        RadioButton rb = (RadioButton) tag.getSelectedToggle();
        record.setString("purpose", rb.getId());

        assignedUsers = new ArrayList<>();
        assignedUsers.addAll(assignedUserList.getItems());

        Stage window = (Stage) titleField.getScene().getWindow();
        window.close();
    }

    private ToDoApp app;
    private ToDoGUI gui;
    private DataRecord record;
    private List<String> assignedUsers;

    void setTask(Task task)
    {
        record = null;
        assignedUsers = null;

        assignedUserList.getItems().clear();
        if (task == null)
        {
            titleField.clear();
            descriptionField.clear();
            deadlinePicker.setValue(null);
            tag.selectToggle(noPurpose);
            assignedUserList.getItems().clear();
        }
        else
        {
            titleField.setText(task.getTitle());
            descriptionField.setText(task.getDescription());
            deadlinePicker.setValue(task.getDeadline());
            switch (task.getPurposeTag())
            {
                case work:
                    tag.selectToggle(work);
                    break;
                case personal:
                    tag.selectToggle(personal);
                    break;
                default:
                    tag.selectToggle(noPurpose);
            }
            for (User assignedUser : task.getAssignedUsers())
                assignedUserList.getItems().add(assignedUser.getUsername());
        }
    }

    public void setApp(ToDoApp app)
    {
        this.app = app;
    }

    public void setGui(ToDoGUI gui)
    {
        this.gui = gui;
    }

    public DataRecord getRecord()
    {
        return record;
    }

    public List<String> getAssignedUsers()
    {
        return assignedUsers;
    }
}
