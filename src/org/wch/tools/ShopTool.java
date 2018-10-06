package org.wch.tools;

import org.wch.action.AgentAct;
import org.wch.action.ShopAct;

import java.sql.Connection;
import java.sql.SQLException;

public class ShopTool {

    public static  void main(String[] agrs) throws SQLException {
        Connection sqlconn = null;
        Connection mysqlconn = null;
        ShopAct act = new ShopAct();
        String result = act.beginOldShopToNew(sqlconn,mysqlconn);
        System.out.println(result);
    }
}
