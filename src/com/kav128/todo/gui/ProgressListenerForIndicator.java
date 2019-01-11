/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.gui;

import com.kav128.todo.core.ProgressListener;
import javafx.application.Platform;
import javafx.scene.control.ProgressIndicator;

public class ProgressListenerForIndicator implements ProgressListener
{
    private ProgressIndicator indicator;

    ProgressListenerForIndicator(ProgressIndicator indicator)
    {
        this.indicator = indicator;
    }

    @Override
    public void onStart()
    {

        Platform.runLater(() -> indicator.setProgress(0));
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
            Platform.runLater(() -> indicator.setProgress(1));
        else
            Platform.runLater(() -> indicator.setProgress(0));
    }
}
