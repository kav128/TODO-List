/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.cli;

import com.kav128.data.DataSource;

public class CommitCommand implements Command
{
    private DataSource dataSource;

    public CommitCommand(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    @Override
    public void execute()
    {
        dataSource.commit();
    }
}
