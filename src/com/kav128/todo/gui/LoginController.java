/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.gui;

import com.kav128.todo.core.ToDoApp;
import com.kav128.todo.core.UserController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class LoginController
{
    @FXML
    private TextField loginTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Label incorrectLabel;

    private ToDoApp app;

    @FXML
    private void signInClick()
    {
        String login = loginTextField.getText();
        String pass = passwordTextField.getText();

        UserController uc = app.getUserController();
        if (uc.login(login, pass))
        {
            resetIncorrect();
            Stage window = (Stage)loginTextField.getScene().getWindow();
            clearFields();
            loginTextField.requestFocus();
            window.close();
        }
        else
        {
            setIncorrect();
            clearFields();
            loginTextField.requestFocus();
        }
    }

    @FXML
    private void signUpClick()
    {
        String login = loginTextField.getText();
        String pass = passwordTextField.getText();

        UserController uc = app.getUserController();
        if (uc.register(login, pass))
        {
            resetIncorrect();
            Stage window = (Stage)loginTextField.getScene().getWindow();
            clearFields();
            loginTextField.requestFocus();
            window.close();
        }
        else
        {
            setIncorrect();
            clearFields();
            loginTextField.requestFocus();
        }
    }

    private void clearFields()
    {
        loginTextField.clear();
        passwordTextField.clear();
    }

    private void setWrong(TextField textField)
    {
        ObservableList<String> styleClass = textField.getStyleClass();
        if (!styleClass.contains("bad"))
            styleClass.add("bad");
    }

    private void resetWrong(TextField textField)
    {
        textField.getStyleClass().remove("bad");
    }

    private void setIncorrect()
    {
        setWrong(loginTextField);
        setWrong(passwordTextField);
        incorrectLabel.setVisible(true);
    }

    private void resetIncorrect()
    {
        resetWrong(loginTextField);
        resetWrong(passwordTextField);
        incorrectLabel.setVisible(false);
    }

    void setApp(ToDoApp app)
    {
        this.app = app;
    }
}
