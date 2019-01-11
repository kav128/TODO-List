/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PromptController
{
    @FXML
    private TextField inputField;

    @FXML
    private Label promptText;

    @FXML
    private void okClicked()
    {
        value = inputField.getText();
        inputField.setPromptText("");
        inputField.clear();
        Stage window = (Stage) inputField.getScene().getWindow();
        window.close();
    }

    void init()
    {
        value = null;
    }

    private String value;

    String getValue()
    {
        return value;
    }

    void setPromptText(String value)
    {
        promptText.setText(value);
        inputField.setPromptText(value);
    }
}
