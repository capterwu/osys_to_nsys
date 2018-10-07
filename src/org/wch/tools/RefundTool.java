package org.wch.tools;

import org.wch.action.RefundAct;

import java.sql.Connection;
import java.sql.SQLException;

public class RefundTool {

    public static void main(String[] args) throws SQLException {
        Connection sqlconn = null;
        Connection mysqlconn = null;
        RefundAct refundAct = new RefundAct();
        String result = refundAct.beginOldRefundToNew(sqlconn,mysqlconn);
        System.out.println(result);
    }
}

