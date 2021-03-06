/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.gui;

import com.kav128.todo.core.ProgressListener;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

class MainSceneProgressListener implements ProgressListener
{
    private Label label;
    private ProgressIndicator indicator;
    private Button lockableButton;

    MainSceneProgressListener(Label label, ProgressIndicator indicator, Button lockableButton)
    {
        this.label = label;
        this.indicator = indicator;
        this.lockableButton = lockableButton;
    }

    @Override
    public void onStart()
    {
        Platform.runLater(() -> {
            label.setText("Выполнение задачи...");
            indicator.setProgress(0);
            lockableButton.setDisable(true);
        });
    }

    @Override
    public void onProgressChange(double progress)
    {
        Platform.runLater(() -> indicator.setProgress(progress));
    }

    @Override
    public void onComplete(boolean success)
    {
        if (success)
            Platform.runLater(() -> {
                label.setText("Задача выполнена успешно");
                indicator.setProgress(1);
                lockableButton.setDisable(false);
            });
        else
            Platform.runLater(() -> {
                label.setText("Ошибка при выполнении задачи");
                indicator.setProgress(0);
                lockableButton.setDisable(false);
            });
    }
}
