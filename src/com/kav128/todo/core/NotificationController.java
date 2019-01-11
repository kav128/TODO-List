/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.core;

public interface NotificationController
{
    int getNotificationCount();

    Notification[] getNotifications();

    void addNotification(User sender, User user, Task task);

    void acceptNotification(Notification notification);

    void setSeen(Notification notification);
}
