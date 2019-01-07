/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo;

import java.util.HashMap;
import java.util.Map;

class UserList
{
    private Map<Integer, User> idUserMap;
    private Map<String, User> nameUserMap;

    public UserList()
    {
        idUserMap = new HashMap<>();
        nameUserMap = new HashMap<>();
    }

    public boolean containsUser(int id)
    {
        return idUserMap.containsKey(id);
    }

    public boolean containsUser(String name)
    {
        return nameUserMap.containsKey(name);
    }

    public User getUserById(int id)
    {
        return idUserMap.get(id);
    }

    public User getUserByName(String name)
    {
        return nameUserMap.get(name);
    }

    User putUser(int id, String name)
    {
        User user = new User(id, name);
        idUserMap.put(id, user);
        nameUserMap.put(name, user);
        return user;
    }
}
