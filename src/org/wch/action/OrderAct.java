package org.wch.action;

import org.wch.bean.OrderBean;
import org.wch.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public static List getOldOrderData(Connection mssqlconn){
        List<OrderBean> orderBeanList = new ArrayList<>();
        try{
            String sql = "SELECT  [p_id]\n" +
                    "      ,[p_UserType]\n" +
                    "      ,[p_OpenID]\n" +
                    "      ,[p_DeviceType]\n" +
                    "      ,[p_BatteryID]\n" +
                    "      ,[p_DeviceID]\n" +
                    "      ,[p_BatteryPort]\n" +
                    "      ,[p_ShopID]\n" +
                    "      ,[p_ShopName]\n" +
                    "      ,[p_OrderID]\n" +
                    "      ,[p_PayState]\n" +
                    "      ,[p_BorrowState]\n" +
                    "      ,[p_BorrowTime]\n" +
                    "      ,[p_ReturnTime]\n" +
                    "      ,[p_ReturnShopID]\n" +
                    "      ,[p_ReturnShopName]\n" +
                    "      ,[p_PayPrice]\n" +
                    "      ,[p_UseTime]\n" +
                    "      ,[p_Price]\n" +
                    "      ,[p_Price_Max]\n" +
                    "      ,[p_AddTime]\n" +
                    "      ,[p_AliOrderID]\n" +
                    "      ,[p_TempBatteryID]\n" +
                    "      ,[p_AgentMoney]\n" +
                    "      ,[p_AgentMoney1]\n" +
                    "      ,[p_AgentMoney2]\n" +
                    "      ,[p_AgentMoney3]\n" +
                    "      ,[p_AgentMoney4]\n" +
                    "      ,[p_AgentMoney5]\n" +
                    "  FROM [DJX_NoPublic].[dbo].[Plug_GX_Buy]";
            ps = mssqlconn.prepareStatement(sql);
            rs = ps.executeQuery();
            System.out.println("开始读取订单数据：");
            while(rs.next()) {
                OrderBean ob = new OrderBean();
                if(rs.getString("p_UserType").equals("微信用户")) {
                    ob.setPay_type(1);
                }else if(rs.getString("p_UserType").equals("支付宝用户")){
                    ob.setPay_type(2);
                }else{
                    ob.setPay_type(3);
                }
                ob.setMoid(rs.getString("p_OpenID"));
                ob.setPower_bi(rs.getString("p_BatteryID"));
                ob.setEquip_code(rs.getString("p_DeviceID"));
                ob.setShop_code(rs.getString("p_ShopID"));
                ob.setCode(rs.getString("p_OrderID"));
                String payStatus = rs.getString("p_BorrowState");
                String payState = rs.getString("p_PayState");
                if(payStatus.equals("租借中")) {
                    ob.setOrder_status(1);
                }else if(payStatus.equals("已归还")){
                    ob.setOrder_status(2);
                    double bill = rs.getDouble("p_PayPrice");
                    double shop_money = bill*(rs.getInt("p_AgentMoney")/100);
                    double sales_money = bill*(rs.getInt("p_AgentMoney1")/100);
                    double area_money = bill*(rs.getInt("p_AgentMoney2")/100);
                    double city_money = bill*(rs.getInt("p_AgentMoney3")/100);
                    double province_money = bill*(rs.getInt("p_AgentMoney4")/100);
                    double plat_money = bill*(rs.getInt("p_AgentMoney5")/100);
                    ob.setShop_moeny(shop_money);
                    ob.setSales_money(sales_money);
                    ob.setArea_money(area_money);
                    ob.setCity_money(city_money);
                    ob.setProvince_money(province_money);
                    ob.setPlat_money(plat_money);
                }else if(payStatus.equals("已撤销")){
                    ob.setOrder_status(3);
                }else{
                    ob.setOrder_status(4);
                }
                if(payState.equals("已支付")){
                    ob.setPay_type(2);
                }else{
                    ob.setPay_type(1);
                }
                ob.setBw_time(rs.getString("p_BorrowTime"));
                ob.setBk_time(rs.getString("p_ReturnTime"));
                ob.setRe_shop_code(rs.getString("p_ReturnShopID"));
                ob.setBill(rs.getDouble("p_PayPrice"));
                ob.setUse_minute(rs.getInt("p_UseTime"));
                ob.setUnit_price(rs.getDouble("p_Price"));
                ob.setOrdertime(rs.getString("p_AddTime"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return orderBeanList;
    }
}
