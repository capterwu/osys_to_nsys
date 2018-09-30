package org.wch.tools;

import org.wch.action.UserAct;

import java.sql.Connection;
import java.sql.SQLException;

public class UserTool {

    public static  void main(String[] agrs) throws SQLException {
        Connection sqlconn = null;
        Connection mysqlconn = null;
        UserAct au = new UserAct();
        String result = au.beginOldMemToNew(sqlconn,mysqlconn);
        System.out.println(result);
    }
}
