/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.core;

public interface UserController extends AutoCloseable
{
    boolean register(String username, String password);

    boolean login(String username, String password);

    void logout();

    boolean isAuthorized();

    User getCurUser();

    User getUserByName(String name);

    @Override
    void close();
}
