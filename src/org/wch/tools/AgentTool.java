package org.wch.tools;

import org.wch.action.AgentAct;

import java.sql.Connection;
import java.sql.SQLException;

public class AgentTool {

    public static  void main(String[] agrs) throws SQLException {
        Connection sqlconn = null;
        Connection mysqlconn = null;
        AgentAct act = new AgentAct();
        String result = act.beginOldAgentToNew(sqlconn,mysqlconn);
        System.out.println(result);
    }
}
