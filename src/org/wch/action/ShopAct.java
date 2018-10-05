package org.wch.action;

import org.wch.bean.ShopBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ShopAct {

    static PreparedStatement ps = null;
    static PreparedStatement ps2 = null;
    static PreparedStatement ps3 = null;
    static PreparedStatement ps4 = null;
    static PreparedStatement ps5 = null;

    static ResultSet rs = null;

    //查询店铺旧系统数据
    public static List getOdbShopData(Connection mssqlconn){
        List<ShopBean> shopBeanList = new ArrayList<>();
        return  shopBeanList;
    }

}
