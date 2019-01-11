/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.data;

public interface DatabaseManager extends AutoCloseable
{
    @Override
    void close() throws Exception;

    UsersDAO getUsersDAO() throws Exception;

    TasksDAO getTasksDAO() throws Exception;

    NotificationsDAO getNotificationsDAO() throws Exception;
}
