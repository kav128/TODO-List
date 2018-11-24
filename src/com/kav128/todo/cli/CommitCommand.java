/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.todo.cli;

import com.kav128.data.DataSource;

class CommitCommand implements Command
{
    private final DataSource dataSource;

    private CommitCommand(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    static Command parse(String[] args)
    {
        return new CommitCommand(UI.instance().getDataSource());
    }

    @Override
    public void execute()
    {
        dataSource.commit();
    }
}
