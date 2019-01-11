/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.core;

import com.kav128.todo.data.DataRecord;
import com.kav128.todo.data.NotificationsDAO;

import java.util.ArrayList;
import java.util.List;

public class NotificationController
{
    private Session session;
    private NotificationsDAO dao;
    private ToDoApp app;

    NotificationController(ToDoApp app, Session session)
    {
        this.session = session;
        this.app = app;
        try
        {
            dao = app.getDbManager().getNotificationsDAO();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int getNotificationCount()
    {
        if (!session.isOpened())
            throw new RuntimeException("Session is closed");
        return dao.getUnseenNotificationCount(session.getUser().getId());
    }

    public Notification[] getUnseenNotifications()
    {
        if (!session.isOpened())
            throw new RuntimeException("Session is closed");
        DataRecord[] notifications = dao.getUnseenNotificationsByUser(session.getUser().getId());
        List<Notification> notificationList = new ArrayList<>();
        for (DataRecord notification : notifications)
            notificationList.add(getNotificationFromDataRecord(notification));
        return notificationList.toArray(new Notification[0]);
    }

    public Notification[] getNotifications()
    {
        if (!session.isOpened())
            throw new RuntimeException("Session is closed");
        DataRecord[] notifications = dao.getNotificationsByUser(session.getUser().getId());
        List<Notification> notificationList = new ArrayList<>();
        for (DataRecord notification : notifications)
            notificationList.add(getNotificationFromDataRecord(notification));
        return notificationList.toArray(new Notification[0]);
    }

    private Notification getNotificationFromDataRecord(DataRecord notificationRecord)
    {
        int id = notificationRecord.getInt("id");
        int sender = notificationRecord.getInt("senderUser");
        int taskId = notificationRecord.getInt("taskId");
        boolean seen = notificationRecord.getBoolean("seen");
        boolean accepted = notificationRecord.getBoolean("accepted");

        User user = app.getUserController().getUserById(sender);
        return new Notification(this, id, user, taskId, seen, accepted);
    }

    public void addNotification(User sender, User user,  Task task)
    {
        dao.addNotification(sender.getId(), user.getId(), task.getId());
    }

    public void acceptNotification(Notification notification)
    {
        dao.acceptNotification(notification.getId());
    }

    public void setSeen(Notification notification)
    {
        dao.setSeen(notification.getId());
    }
}
