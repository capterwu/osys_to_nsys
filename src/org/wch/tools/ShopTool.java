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
        String result = act.beginOldShopToNew(sqlconn,mysqlconn);   //开始处理旧系统店铺数据到新系统
        System.out.println(result);

        String result2 = act.beginShopEquipToNew(sqlconn,mysqlconn); //开始处理店铺设备数据到新系统
        System.out.println(result2);


    }
}
