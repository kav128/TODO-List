/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.gui;

import com.kav128.todo.core.ToDoApp;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class LoginController
{
    @FXML
    public ProgressIndicator progressIndicator;

    @FXML
    private TextField loginTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Label incorrectLabel;

    private ToDoApp app;
    private LoginEventManager loginEventManager;


    void init()
    {
        progressIndicator.setVisible(false);
        EventListener listener = new EventListener()
        {
            @Override
            public void update(double progress)
            {
                Platform.runLater(() -> progressIndicator.setProgress(progress));
            }

            @Override
            public void complete(boolean success)
            {
                if (success)
                {
                    resetIncorrect();
                    Stage window = (Stage)loginTextField.getScene().getWindow();
                    Platform.runLater(() -> {
                        clearFields();
                        loginTextField.requestFocus();
                        window.close();
                    });
                }
                else
                {
                    setIncorrect();
                    Platform.runLater(() -> {
                        clearFields();
                        loginTextField.requestFocus();
                    });
                }
                Platform.runLater(() -> progressIndicator.setVisible(false));
            }
        };

        loginEventManager = new LoginEventManager(app);
        loginEventManager.subscribe(listener);
    }

    @FXML
    private void signInClick()
    {
        progressIndicator.setVisible(true);
        String login = loginTextField.getText();
        String pass = passwordTextField.getText();

        loginEventManager.login(login, pass);
    }

    @FXML
    private void signUpClick()
    {
        progressIndicator.setVisible(true);
        String login = loginTextField.getText();
        String pass = passwordTextField.getText();

        loginEventManager.register(login, pass);
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
