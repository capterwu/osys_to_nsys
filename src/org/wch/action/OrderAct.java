package org.wch.action;

import org.wch.bean.OrderBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderAct {

    static PreparedStatement ps = null;
    static PreparedStatement ps2 = null;
    static PreparedStatement ps3 = null;
    static PreparedStatement ps4 = null;
    static PreparedStatement ps5 = null;

    static ResultSet rs = null;

    //获取旧订单数据
    public static List getOldOrderData(Connection sqlconn){
        List<OrderBean> orderBeanList = new ArrayList<>();
        return orderBeanList;
    }
}
