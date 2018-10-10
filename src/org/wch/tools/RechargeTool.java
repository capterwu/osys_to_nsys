package org.wch.tools;

import org.wch.action.RechargeAct;

import java.sql.Connection;
import java.sql.SQLException;

public class RechargeTool {

    public static void main(String[] args) throws SQLException {
        Connection sqlconn = null;
        Connection mysqlconn = null;
        RechargeAct ra = new RechargeAct();
//        String result = ra.beginOldRechargeToNew(sqlconn,mysqlconn);
//        System.out.println(result);

        String result2 = ra.beginOldYajinToNew(sqlconn,mysqlconn);
        System.out.println(result2);
    }
}
