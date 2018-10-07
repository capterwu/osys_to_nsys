package org.wch.action;

import org.wch.bean.EquipBean;
import org.wch.bean.ShopBean;
import org.wch.db.DbConnection;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Random;

public class ShopAct {

    static PreparedStatement ps = null;
    static PreparedStatement ps2 = null;
    static PreparedStatement ps3 = null;
    static PreparedStatement ps4 = null;
    static PreparedStatement ps5 = null;

    static ResultSet rs = null;

    //根据地区名称找地区ID
    public static int getAreaId(String areaName, Connection mysql_conn){
        int area_id = 0;
        try {
            String sql = "SELECT id from area where cn_name like '%" + areaName + "%'";
            PreparedStatement arePre = mysql_conn.prepareStatement(sql);
            ResultSet areRs = arePre.executeQuery();
            while (areRs.next()) {
                area_id = areRs.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return area_id;
    }

    //根据openid找上级代理ID
    public static int getAgentId(String openid,Connection mysql_conn){
        int agent_id = 0;
        try {
            String sql = "SELECT id from agent_relation where OPENID='"+openid+"'";
            PreparedStatement agentPre = mysql_conn.prepareStatement(sql);
            ResultSet agentRs = agentPre.executeQuery();
            while(agentRs.next()) {
                agent_id = agentRs.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return agent_id;
    }

    //生成店铺编号
    public synchronized static String generateRankShopCode() {
        SimpleDateFormat formatter = new SimpleDateFormat("yy");
        String dateString = formatter.format(new Date());
        String rankShopCode = "SH" + dateString;
        rankShopCode +=(System.currentTimeMillis()+"").substring(11,13)+getRandomInt(2)+getRandomInt(3);
        return rankShopCode;
    }

    public static String getRandomInt(Integer length) {
        String str = "";
        Random code = new Random();
        while(length>0){
            int ram = code.nextInt(9);
            str += ram;
            length--;
        }
       return str;
    }

    //根据店铺名称查找店铺ID
    public static String getShopCodeByName(String shopName,Connection mysql_conn){
        String shop_code = "";
        try{
            String sql = "select code from rank_shop where s_name='"+shopName+"'";
            PreparedStatement shopPre = mysql_conn.prepareStatement(sql);
            ResultSet shopRs = shopPre.executeQuery();
            while(shopRs.next()) {
                shop_code = shopRs.getString(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        System.out.println("shop_name:"+shopName+" ; shop_code:"+shop_code);
        return shop_code;
    }

    //根据设备号查找设备二维码编号ID
    public static int getEquipQrCodeId(String equip_code,Connection mysql_conn){
        int equip_id = 0;
        try{
            String sql = "SELECT\n" +
                    "qrcode_store.ID,\n" +
                    "equipinfo.`CODE`,\n" +
                    "qrcode_store.QRCODE\n" +
                    "FROM\n" +
                    "equipinfo\n" +
                    "INNER JOIN qrcode_store ON equipinfo.ID = qrcode_store.EQUIP\n" +
                    "WHERE\n" +
                    "equipinfo.`CODE` = ?";
            PreparedStatement equipPre = mysql_conn.prepareStatement(sql);
            equipPre.setString(1,equip_code);
            ResultSet euquipRs = equipPre.executeQuery();
            while (euquipRs.next()){
                equip_id = euquipRs.getInt("ID");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return equip_id;
    }

    //查找旧系统店铺设备数据
    public static List getEquipByShopName(String shopName,Connection mssqlconn){
        List<EquipBean> equipBeanList = new ArrayList<>();
        try{
            String sql = "SELECT [p_id]\n" +
                    "      ,[p_DeviceID]\n" +
                    "      ,[p_ShopName]\n" +
                    "  FROM [DJX_NoPublic].[dbo].[Plug_GX_Device] WHERE [p_ShopName]='"+shopName+"'" +
                    " and [p_DeviceID]  in (SELECT  [p_DeviceID] FROM [DJX_NoPublic].[dbo].[Plug_GX_QrCode])";
            ps = mssqlconn.prepareStatement(sql);
            rs = ps.executeQuery();
            System.out.println("开始读取旧的店铺设备数据：");
            while(rs.next()) {
                EquipBean equipBean = new EquipBean();
                equipBean.setCode(rs.getString("p_DeviceID"));
                equipBean.setShop_name(rs.getString("p_ShopName"));
                equipBeanList.add(equipBean);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return equipBeanList;
    }

    //查找旧系统店铺编号设备数据
    public static List getEquipByShopCode(String shopCode,Connection mssqlconn){
        List<EquipBean> equipBeanList = new ArrayList<>();
        try{
            String sql = "SELECT [p_id],[p_ShopID]\n" +
                    "      ,[p_DeviceID]\n" +
                    "      ,[p_ShopName]\n" +
                    "  FROM [DJX_NoPublic].[dbo].[Plug_GX_Device] WHERE [p_ShopID]='"+shopCode+"'" +
                    " and [p_DeviceID]  in (SELECT  [p_DeviceID] FROM [DJX_NoPublic].[dbo].[Plug_GX_QrCode])";
            ps = mssqlconn.prepareStatement(sql);
            rs = ps.executeQuery();
            System.out.println("开始读取旧的店铺设备数据：");
            while(rs.next()) {
                EquipBean equipBean = new EquipBean();
                equipBean.setCode(rs.getString("p_DeviceID"));
                equipBean.setShop_name(rs.getString("p_ShopName"));
                equipBeanList.add(equipBean);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return equipBeanList;
    }


    public static String setNowDate() {
        SimpleDateFormat aDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDate = aDate.format(new Date());
        return nowDate;
    }

    //处理新系统店铺设备数据
    public  static void pushOldShopEquipToNew(Connection mssqlconn,Connection mysqlconn) throws SQLException{
        long startTime=System.currentTimeMillis();   //获取开始时间
        List<ShopBean> shopBeanList = getOdbShopData(mssqlconn);
        try{
            String sql = "INSERT into shop_equip(code,registtime,operator,qrcode_store) values (?,?,?,?)";
            System.out.println("此次要处理设备的店铺数据记录总共有: "+shopBeanList.size()+" 条");
            ps2 = mysqlconn.prepareStatement(sql);
            mysqlconn.setAutoCommit(false);
            mysqlconn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            for(int i=0;i<shopBeanList.size();i++){
                ShopBean sb = shopBeanList.get(i);
              //  List<EquipBean> equipBeanList = getEquipByShopName(sb.getS_name(),mssqlconn);
                List<EquipBean> equipBeanList = getEquipByShopCode(sb.getCode(),mssqlconn);
                String shop_name = sb.getS_name();
                String shop_code = getShopCodeByName(shop_name,mysqlconn);
                System.out.println("开始处理店铺:"+shop_code+" , "+shop_name+" 的设备数据,该店铺有"+equipBeanList.size()+" 台设备");
                for(int k=0;k<equipBeanList.size();k++){
                    EquipBean equipBean = equipBeanList.get(k);
                    int qrcode_store = getEquipQrCodeId(equipBean.getCode(),mysqlconn);
                    System.out.println("设备:"+equipBean.getCode()+" ; "+qrcode_store);
                    ps2.setString(1,shop_code);
                    ps2.setString(2,setNowDate());
                    ps2.setInt(3,1);
                    ps2.setInt(4,qrcode_store);
                    ps2.addBatch();
                }
                System.out.println("该店铺设备数据准备完毕，准备导入.......");
                ps2.executeBatch();
                mysqlconn.commit();
                System.out.println("该店铺设备数据导入完毕.");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+((endTime-startTime)/1000)/60+"分钟");
    }


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
                    "  FROM [djx_nopublic].[dbo].[Plug_GX_Shop]";
            ps = mssqlconn.prepareStatement(sql);
            rs = ps.executeQuery();
            System.out.println("开始读取旧的店铺数据，总共有：");
            while(rs.next()) {
                ShopBean sb = new ShopBean();
                sb.setId(rs.getInt("p_id"));
              //  sb.setCode(rs.getString("p_NewID"));
              //  String shop_code = generateRankShopCode();
             //   sb.setCode(shop_code);
                sb.setCode(rs.getString("p_NewID"));
                sb.setS_name(rs.getString("p_ShopName"));
                sb.setS_phone(rs.getString("p_Tel"));
                sb.setTemp_area(rs.getString("p_Dist"));
                sb.setAddress(rs.getString("p_Address"));
                sb.setS_coordinate(rs.getString("p_Lat")+","+rs.getString("p_Lng"));
                sb.setRegisttime(rs.getString("p_AddTime"));
                sb.setLogo(rs.getString("p_Logo"));
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
                shopBeanList.add(sb);
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
            String sql = "insert into RANK_SHOP(ID,AREA,MANAGER,PLAT,PROVINCE_AGENT,CITY_AGENT,AREA_AGENT,SALES_AGENT\n" +
                    ",CODE,S_NAME,S_PHONE,S_ADDRESS,S_LOGO,S_COORDINATE,FREE_USETIME,RENT_COST,HIGH_COST\n" +
                    ",PLAT_EXTRACT,PROVINCE_EXTACT,CITY_EXTACT,AREA_EXTACT,SALES_EXTACT,SHOP_EXTACT,SHOP_PROFIT_MONEY\n" +
                    ",ISBLOCK,ISQUICK,ISONECODE,REGISTTIME,OPERATOR)\n" +
                    "VALUES(?,?,?,?,?,?,?,?\n" +
                    ",?,?,?,?,?,?,?,?,?\n" +
                    ",?,?,?,?,?,?,?\n" +
                    ",?,?,?,?,?);\n";
            System.out.println("此次要处理的店铺数据记录总共有: "+shopBeanList.size()+" 条");
            ps2 = mysqlconn.prepareStatement(sql);
            mysqlconn.setAutoCommit(false);
            mysqlconn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            for (int i=0;i<shopBeanList.size();i++) {
                ShopBean sb = shopBeanList.get(i);
                ps2.setInt(1, sb.getId());
                String temp_area = sb.getTemp_area() == null ? "" : sb.getTemp_area();
                if (temp_area.equals("")) {
                    ps2.setInt(2, 440114);
                } else {
                    String area_name = temp_area.substring(0, 2);
                    int area_id = getAreaId(area_name, mysqlconn);
                    ps2.setInt(2, area_id);
                }
                String shop_agent = sb.getShop_oid() == null ? "" : sb.getShop_oid();
                String province_agent = sb.getProvince_oid() == null ? "" : sb.getProvince_oid();
                String city_agent = sb.getCity_oid() == null ? "" : sb.getCity_oid();
                String area_agent = sb.getArea_oid() == null ? "" : sb.getArea_oid();
                String sales_agent = sb.getSales_oid() == null ? "" : sb.getSales_oid();
                if (shop_agent.equals("")) {
                    ps2.setNull(3, Types.BIGINT);
                } else {
                    ps2.setInt(3, getAgentId(shop_agent, mysqlconn));
                }
                ps2.setInt(4, 1);
                if (province_agent.equals("")) {
                    ps2.setNull(5, Types.BIGINT);
                } else {
                    ps2.setInt(5, getAgentId(province_agent, mysqlconn));
                }
                if (city_agent.equals("")) {
                    ps2.setNull(6, Types.BIGINT);
                } else {
                    ps2.setInt(6, getAgentId(city_agent, mysqlconn));
                }
                if (area_agent.equals("")) {
                    ps2.setNull(7, Types.BIGINT);
                } else {
                    ps2.setInt(7, getAgentId(area_agent, mysqlconn));
                }
                if (sales_agent.equals("")) {
                    ps2.setNull(8, Types.BIGINT);
                } else {
                    ps2.setInt(8, getAgentId(sales_agent, mysqlconn));
                }
                ps2.setString(9, sb.getCode());
                ps2.setString(10, sb.getS_name());
                ps2.setString(11, sb.getS_phone());
                ps2.setString(12, sb.getAddress());
                ps2.setString(13, sb.getLogo());
                ps2.setString(14, sb.getS_coordinate());
                ps2.setInt(15, sb.getFree_usetime());
                ps2.setInt(16, sb.getRent_cost());
                ps2.setInt(17, sb.getHigh_cost());
                ps2.setInt(18, sb.getPlat_extract());
                ps2.setInt(19, sb.getProvince_extract());
                ps2.setInt(20, sb.getCity_extract());
                ps2.setInt(21, sb.getArea_extract());
                ps2.setInt(22, sb.getSales_extract());
                ps2.setInt(23, sb.getShop_extract());
                ps2.setDouble(24, sb.getShop_profit_money());
                ps2.setInt(25, 2);
                ps2.setInt(26, 1);
                ps2.setInt(27, 2);
                ps2.setString(28, sb.getRegisttime());
                ps2.setInt(29, 1);
                ps2.addBatch();
               // ps2.execute();
                System.out.println("店铺:"+sb.getS_name()+" 处理完毕!");
            }
              ps2.executeBatch();
              mysqlconn.commit();
            System.out.println("店铺数据更新完毕!......");
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+((endTime-startTime)/1000)/60+"分钟");
    }


    public String beginOldShopToNew(Connection sqlconn,Connection mysqlconn) throws SQLException {
        sqlconn = DbConnection.getSqlConnection();
        mysqlconn= DbConnection.getMysqlConnection();
        pushOldShopToNew(sqlconn, mysqlconn,"");
        return "店铺数据已经全部导入。";
    }

    public String beginShopEquipToNew(Connection sqlconn,Connection mysqlconn) throws SQLException{
        sqlconn = DbConnection.getSqlConnection();
        mysqlconn= DbConnection.getMysqlConnection();
        pushOldShopEquipToNew(sqlconn, mysqlconn);
        return "店铺设备数据已经全部导入。";
    }

}
