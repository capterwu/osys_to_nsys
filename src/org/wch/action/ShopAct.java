package org.wch.action;

import org.wch.bean.ShopBean;
import org.wch.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        try{
            String sql = "SELECT [p_id]\n" +
                    "      ,[p_NewID]\n" +
                    "      ,[p_ShopName]\n" +
                    "      ,[p_Dist]\n" +
                    "      ,[p_Tel]\n" +
                    "      ,[p_Lat]\n" +
                    "      ,[p_Lng]\n" +
                    "      ,[p_GeoHash]\n" +
                    "      ,[p_Address]\n" +
                    "      ,[p_Remark]\n" +
                    "      ,[p_Info]\n" +
                    "      ,[p_Logo]\n" +
                    "      ,[p_DeviceID]\n" +
                    "      ,[p_aID]\n" +
                    "      ,[p_aID1]\n" +
                    "      ,[p_aID2]\n" +
                    "      ,[p_aID3]\n" +
                    "      ,[p_aID4]\n" +
                    "      ,[p_aID5]\n" +
                    "      ,[p_AgentID]\n" +
                    "      ,[p_AgentID1]\n" +
                    "      ,[p_AgentID2]\n" +
                    "      ,[p_AgentID3]\n" +
                    "      ,[p_AgentID4]\n" +
                    "      ,[p_AgentID5]\n" +
                    "      ,[p_TiCheng_Shop]\n" +
                    "      ,[p_FreeTime]\n" +
                    "      ,[p_Price]\n" +
                    "      ,[p_Price_Max]\n" +
                    "      ,[p_AddTime]\n" +
                    "      ,[p_Admin_OpenID]\n" +
                    "      ,[p_AgentMoney]\n" +
                    "      ,[p_AgentMoney1]\n" +
                    "      ,[p_AgentMoney2]\n" +
                    "      ,[p_AgentMoney3]\n" +
                    "      ,[p_AgentMoney4]\n" +
                    "      ,[p_AgentMoney5]\n" +
                    "      ,[p_ShopBaseMoney]\n" +
                    "  FROM [DJX_NoPublic].[dbo].[Plug_GX_Shop]";
            ps = mssqlconn.prepareStatement(sql);
            rs = ps.executeQuery();
            System.out.println("开始读取旧的店铺数据，总共有：");
            while(rs.next()) {
                ShopBean sb = new ShopBean();
                sb.setCode(rs.getString("p_NewID"));
                sb.setS_name(rs.getString("p_ShopName"));
                sb.setS_phone(rs.getString("[p_Tel"));
                sb.setTemp_area(rs.getString("p_Dist"));
                sb.setAddress(rs.getString("p_Address"));
                sb.setS_coordinate(rs.getString("p_Lat")+","+rs.getString("p_Lng"));
                sb.setRegisttime(rs.getString("p_AddTime"));
                sb.setFree_usetime(rs.getInt("p_FreeTime"));
                sb.setHigh_cost(rs.getInt("p_Price_Max"));
                sb.setRent_cost(rs.getInt("p_Price"));
                sb.setPlat_extract(rs.getInt("p_AgentMoney5"));
                sb.setProvince_oid(rs.getString("p_AgentID4"));
                sb.setProvince_extract(rs.getInt("p_AgentMoney4"));
                sb.setCity_oid(rs.getString("p_AgentID3"));
                sb.setCity_extract(rs.getInt("p_AgentMoney3"));
                sb.setArea_oid(rs.getString("p_AgentID2"));
                sb.setArea_extract(rs.getInt("p_AgentMoney2"));
                sb.setSales_oid(rs.getString("p_AgentID1"));
                sb.setSales_extract(rs.getInt("p_AgentMoney1"));
                sb.setShop_oid(rs.getString("p_AgentID"));
                sb.setShop_extract(rs.getInt("p_AgentMoney"));
                sb.setShop_profit_money(rs.getInt("p_ShopBaseMoney"));
                sb.setIsblock(2);
                sb.setIsquick(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return  shopBeanList;
    }

    //导入旧系统店铺数据到新系统
    public  static void pushOldShopToNew(Connection mssqlconn,Connection mysqlconn,String logoUrl) throws SQLException{
        long startTime=System.currentTimeMillis();   //获取开始时间
        List<ShopBean> shopBeanList = getOdbShopData(mssqlconn);
        try{
            String sql = "";
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+((endTime-startTime)/1000)/60+"分钟");
    }

}
