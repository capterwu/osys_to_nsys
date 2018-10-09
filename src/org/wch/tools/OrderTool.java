package org.wch.tools;


import org.wch.action.OrderAct;
import org.wch.action.ShopAct;

import java.sql.Connection;
import java.sql.SQLException;

public class OrderTool {

    public static  void main(String[] agrs) throws SQLException {
        Connection sqlconn = null;
        Connection mysqlconn = null;
        OrderAct act = new OrderAct();
        String result = act.beginOldOrderToNew(sqlconn,mysqlconn,1,50000);   //开始处理旧系统订单数据到新系统
        System.out.println(result);

    }

}
