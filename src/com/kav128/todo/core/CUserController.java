/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2019
 */

package com.kav128.todo.core;

import com.kav128.todo.data.DataRecord;
import com.kav128.todo.data.DatabaseManager;
import com.kav128.todo.data.UsersDAO;

import java.util.function.Consumer;

class CUserController implements UserController
{
    private Session session;
    private final UsersDAO dao;
    private Consumer<Session> onLogin;
    private Consumer<User> onLogout;
    private final UserList userList;

    CUserController(DatabaseManager dbManager) throws Exception
    {
        this.dao = dbManager.getUsersDAO();
        this.userList = new UserList();
    }

    @Override
    public boolean register(String username, String password)
    {
        try
        {
            boolean regSuccess = dao.insertUser(username, password);
            if (!regSuccess)
                return false;
            return login(username, password);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean login(String username, String password)
    {
        try
        {
            int uid = dao.authUser(username, password);
            if (uid >= 0)
            {
                session = new Session(new User(uid, username));
                onLogin.accept(session);
                return true;
            }
            else
                return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void logout()
    {
        if (session != null)
        {
            onLogout.accept(session.getUser());
            session.close();
        }
        session = null;
    }

    @Override
    public boolean isAuthorized()
    {
        return session != null && session.isOpened();
    }

    @Override
    public User getCurUser()
    {
        return session.getUser();
    }

    Session getSession()
    {
        return session;
    }

    User getUserById(int id)
    {
        if (userList.containsUser(id))
            return userList.getUserById(id);
        DataRecord record = dao.getUserByID(id);
        String name = record.getString("name");
        return userList.putUser(id, name);
    }

    @Override
    public User getUserByName(String name)
    {
        if (userList.containsUser(name))
            return userList.getUserByName(name);
        DataRecord record = dao.getUserByName(name);
        if (record == null)
            return null;
        int id = record.getInt("id");
        return userList.putUser(id, name);
    }

    void setOnLogin(Consumer<Session> onLogin)
    {
        this.onLogin = onLogin;
    }

    void setOnLogout(Consumer<User> onLogout)
    {
        this.onLogout = onLogout;
    }

    @Override
    public void close()
    {
        logout();
    }
}
