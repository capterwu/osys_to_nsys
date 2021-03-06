package org.wch.action;

import org.wch.bean.DepositBean;
import org.wch.bean.EquipBean;
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
    static PreparedStatement ps6 = null;
    static PreparedStatement ps7 = null;
    static PreparedStatement ps8 = null;
    static ResultSet rs = null;


    //根据用户OPENID查找用户ID
    public static int getUserIdByOpenid(String openid,Connection mysql_conn){
        int mid = 0;
        try{
            String sql = "select id from member where openid='"+openid+"'";
            ps7 = mysql_conn.prepareStatement(sql);
            ResultSet idRs = ps7.executeQuery();
            while(idRs.next()){
                mid = idRs.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return mid;
    }

    //根据设备编号查找设备ID
    public static  int getEquipIdByCode(String code,Connection mysql_conn){
        int eid = 0;
        try{
            String sql = "select id from equipinfo where code='"+code+"'";
            ps6 = mysql_conn.prepareStatement(sql);
            ResultSet idRs2 = ps6.executeQuery();
            while(idRs2.next()){
                eid = idRs2.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return eid;
    }

    //根据店铺编号查找店铺ID
    public static int getShopIdByCode(String shopCode,Connection mysql_conn){
        int shop_id = 0;
        try{
            String sql = "select id from rank_shop where code='"+shopCode+"'";
            PreparedStatement shopPre = mysql_conn.prepareStatement(sql);
            ResultSet shopRs = shopPre.executeQuery();
            while(shopRs.next()) {
                shop_id = shopRs.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return shop_id;
    }


    //获取旧订单数据
    public static List getOldOrderData(Connection mssqlconn,int begin,int end){
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
                    "  FROM [DJX_NoPublic].[dbo].[Plug_GX_Buy_1]" +
                    "  where p_OrderID not in (SELECT [p_OrderID] FROM [DJX_NoPublic].[dbo].[Plug_GX_Buy_1] GROUP BY [p_OrderID] HAVING (COUNT(*)>1) )" ;
           //         + "  AND p_id between "+ begin+" and "+end;
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
                String orderStatus = rs.getString("p_BorrowState");
                String payState = rs.getString("p_PayState");
                if(orderStatus.equals("租借中")) {
                    ob.setOrder_status(1);
                    ob.setPay_status(1);
                }else if(orderStatus.equals("已归还")){
                    ob.setOrder_status(2);
                    ob.setPay_status(2);
                    double bill = rs.getDouble("p_PayPrice");
                    double shop_money = (rs.getDouble("p_AgentMoney")/100)*bill;
                    double sales_money = (rs.getDouble("p_AgentMoney1")/100)*bill;
                    double area_money = (rs.getDouble("p_AgentMoney2")/100)*bill;
                    double city_money = (rs.getDouble("p_AgentMoney3")/100)*bill;
                    double province_money = (rs.getDouble("p_AgentMoney4")/100)*bill;
                    double plat_money = (rs.getDouble("p_AgentMoney5")/100)*bill;
                    ob.setShop_moeny(shop_money);
                    ob.setSales_money(sales_money);
                    ob.setArea_money(area_money);
                    ob.setCity_money(city_money);
                    ob.setProvince_money(province_money);
                    ob.setPlat_money(plat_money);
                }else if(orderStatus.equals("已撤销")){
                    ob.setOrder_status(3);
                    ob.setPay_status(1);
                }else{
                    ob.setOrder_status(4);
                    ob.setPay_status(1);
                }
//                if(payState.equals("已支付")){
//                    ob.setPay_status(2);
//                }else{
//                    ob.setPay_status(1);
//                }
                ob.setBw_time(rs.getString("p_BorrowTime"));
                ob.setBk_time(rs.getString("p_ReturnTime"));
                ob.setRe_shop_code(rs.getString("p_ReturnShopID"));
                ob.setBill(rs.getDouble("p_PayPrice"));
                ob.setUse_minute(rs.getInt("p_UseTime"));
                ob.setUnit_price(rs.getDouble("p_Price"));
                ob.setOrdertime(rs.getString("p_AddTime"));
                orderBeanList.add(ob);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return orderBeanList;
    }

    //根据旧系统单号查询旧的押金记录
//    public static List getDepositByOrderNum(String ordercode,Connection mssqlconn) throws SQLException{
//        List<DepositBean> depositBeanList = new ArrayList<>();
//        try{
//            String sql = "SELECT [p_OrderID]\n" +
//                    "      ,[p_Yajin]\n" +
//                    "      ,[p_Pay]\n" +
//                    "      ,[p_Yue] \n" +
//                    "      ,[p_Statu]\n" +
//                    "      ,[p_AddTime]\n" +
//                    "  FROM [DJX_NoPublic].[dbo].[Plug_GX_Yajin]\n" +
//                    "  WHERE [p_OrderID]='"+ordercode+"'";
//            ps = mssqlconn.prepareStatement(sql);
//            rs = ps.executeQuery();
//            while(rs.next()){
//                DepositBean db = new DepositBean();
//                db.setAccrual(rs.getDouble("p_Yajin"));
//                db.setChangeTime(rs.getString("p_AddTime"));
//              //  db.setCurrent();
//            }
//        }catch (SQLException e){
//            e.printStackTrace();
//        }finally {
//            DbConnection.closeDB();
//        }
//        return depositBeanList;
//    }

    //查找重复店铺名称的设备
    public static List getOldReShopDevice(Connection mssqlconn) throws SQLException{
        List<EquipBean> equipBeanList = new ArrayList<>();
        try{
            String sql =
                    "SELECT [p_DeviceID],[p_shopID],[p_shopName]\n" +
                    "  FROM [DJX_NoPublic].[dbo].[Plug_GX_Device]\n" +
                    "  where [p_ShopID] IN (\n" +
                    "SELECT [p_NewID]\n" +
                    "  FROM [DJX_NoPublic].[dbo].[Plug_GX_Shop]\n" +
                    "  WHERE [p_ShopName] IN (SELECT p_ShopName  FROM" +
                            " [DJX_NoPublic].[dbo].[Plug_GX_Shop] GROUP BY p_ShopName  HAVING (COUNT(*)>1)))";
            ps4 = mssqlconn.prepareStatement(sql);
            ResultSet ps4Rs = ps4.executeQuery();
            while(ps4Rs.next()){
                EquipBean eb = new EquipBean();
                eb.setShop_code(rs.getString("p_shopID"));
                eb.setCode(rs.getString("p_DeviceID"));
                equipBeanList.add(eb);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return equipBeanList;
    }

    //更新重复店铺的订单以及分成数据，一般不要调用该方法
    public static void processReShopOrderMethod(Connection mssqlconn,Connection mysqlconn) throws SQLException{
        List<EquipBean> equipBeanList = getOldReShopDevice(mssqlconn);
        try{
            String sql = "update rank_shop set bw_shop=?,bk_shop=? where id=? ";
            String sql2 = "update order_divide set bw_shop=?,bk_shop=?,PLAT_MONEY=?,PROVINCE_MONEY=? " +
                    " ,CITY_MONEY=?,AREA_MONEY=?,SALES_MONEY=?,SHOP_MONEY=? where CODE=? ";
            ps5 = mysqlconn.prepareStatement(sql);
            ps8 = mysqlconn.prepareStatement(sql2);
            mysqlconn.setAutoCommit(false);
            mysqlconn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            for (int i=0;i<equipBeanList.size();i++){
                EquipBean eb = equipBeanList.get(i);
                int shopId = getShopIdByCode(eb.getShop_code(),mysqlconn);

            }
        }catch (SQLException e){
            mysqlconn.rollback();
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
    }

    //导入旧系统订单数据以及分成数据
    public  static void pushOldOrderToNew(Connection mssqlconn,Connection mysqlconn,int begin,int end) throws SQLException{
        long startTime=System.currentTimeMillis();   //获取开始时间
        List<OrderBean> orderBeanList = getOldOrderData(mssqlconn,begin,end);
        try{
            //插入订单数据
            String sql = "INSERT into orderinfo(code,member,ordertime,equip,order_status\n" +
                    ",pay_status,pat_type,pay_code,power_bi,bw_shop,bk_shop\n" +
                    ",bk_time,use_minute,bill,unit_price)\n" +
                    "values(?,?,?,?,?\n" +
                    ",?,?,?,?,?,?\n" +
                    ",?,?,?,?)";
            //插入分成数据
            String sql2 = "INSERT INTO order_divide(CODE,BR_TIME,BK_TIME,BW_SHOP, BK_SHOP\n" +
                    ",DI_TIME,OR_MONEY,PLAT_MONEY,PROVINCE_MONEY,CITY_MONEY\n" +
                    ",AREA_MONEY,SALES_MONEY,SHOP_MONEY,ISBLOCK)\n" +
                    "VALUES(?,?,?,?,?\n" +
                    ",?,?,?,?,?\n" +
                    ",?,?,?,?)";
            //插入押金记录明细;
            String sql3 = "INSERT INTO member_deposit_record(mid,numbers,accrual,tally_type,odd_numbers,trading_time,trading_type)\n" +
                    " values (?,?,?,?,?,?,?)";
            //插入押金记录表;
            String sql4 = "INSERT INTO MEMBER_DEPOSIT(MID,ACCRUAL,CURRENT,LAST_BALANCE,CHANGE_TIME) values(?,?,?,?,?)";
            System.out.println("此次要处理的订单数据记录总共有: "+orderBeanList.size()+" 条");
            ps2 = mysqlconn.prepareStatement(sql);
            ps3 = mysqlconn.prepareStatement(sql2);
//            ps4 = mysqlconn.prepareStatement(sql3);
//            ps5 = mysqlconn.prepareStatement(sql4);
            mysqlconn.setAutoCommit(false);
            mysqlconn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            for(int i=0;i<orderBeanList.size();i++){
                OrderBean ob = orderBeanList.get(i);
                int mid = getUserIdByOpenid(ob.getMoid(),mysqlconn);
                ps2.setString(1,ob.getCode());
                ps2.setInt(2,mid);
                ps2.setString(3,ob.getOrdertime());
                ps2.setInt(4,getEquipIdByCode(ob.getEquip_code(),mysqlconn));
                ps2.setInt(5,ob.getOrder_status());
                ps2.setInt(6,ob.getPay_status());
                ps2.setInt(7,ob.getPay_type());
                ps2.setString(8,"");
                ps2.setString(9,ob.getPower_bi());
                int borrow_shopid = getShopIdByCode(ob.getShop_code(),mysqlconn);
                int return_shopid = getShopIdByCode(ob.getRe_shop_code(),mysqlconn);
                ps2.setInt(10,borrow_shopid);
                ps2.setInt(11,return_shopid );
                ps2.setString(12,ob.getBk_time());
                ps2.setInt(13,ob.getUse_minute());
                ps2.setDouble(14,ob.getBill());
                ps2.setDouble(15,ob.getUnit_price());

//                //处理押金数据；
//                if(ob.getOrder_status() == 1){
//                    ps4.setInt(1,mid);
//                    ps4.setString(2,ob.getCode());
//                    ps4.setDouble(3,100);
//                    ps4.setInt(4,1);
//                    ps4.setString(5,ob.getCode());
//                    ps4.setString(6,ob.getOrdertime());
//                    ps4.setInt(7,3);
//                    ps4.addBatch();
//
//                    ps5.setInt(1,mid);
//                    ps5.setDouble(2,100);
//                    ps5.setDouble(3,0);
//                    ps5.setDouble(4,100);
//                    ps5.setString(5,ob.getOrdertime());
//                    ps5.addBatch();
//                }

                //处理分成数据；
                if(ob.getOrder_status() == 2){
                    ps3.setString(1,ob.getCode());
                    ps3.setString(2,ob.getBw_time());
                    ps3.setString(3,ob.getBk_time());
                    ps3.setInt(4,borrow_shopid);
                    ps3.setInt(5,return_shopid);
                    ps3.setString(6,ob.getOrdertime());
                    ps3.setDouble(7,ob.getBill());
                    ps3.setDouble(8,ob.getPlat_money());
                    ps3.setDouble(9,ob.getProvince_money());
                    ps3.setDouble(10,ob.getCity_money());
                    ps3.setDouble(11,ob.getArea_money());
                    ps3.setDouble(12,ob.getSales_money());
                    ps3.setDouble(13,ob.getShop_moeny());
                    ps3.setInt(14,1);
                    ps3.addBatch();
                }

                ps2.addBatch();
            }
            System.out.println("订单数据正在准备中......");
            ps2.executeBatch();
//            System.out.println("用户押金明细数据正在准备中......");
//            ps4.executeBatch();
//            System.out.println("用户押金数据正在准备中......");
//            ps5.executeBatch();
            System.out.println("订单分成数据正在准备中......");
            ps3.executeBatch();
            System.out.println("准备处理数据，请耐心等待，开始处理中......");
            mysqlconn.commit();
            System.out.println("恭喜你，数据处理完毕了。");
        }catch (SQLException e){
            mysqlconn.rollback();
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+((endTime-startTime)/1000)/60+"分钟");
    }


    public String beginOldOrderToNew(Connection sqlconn,Connection mysqlconn,int begin,int end) throws SQLException {
        sqlconn = DbConnection.getSqlConnection();
        mysqlconn= DbConnection.getMysqlConnection();
        pushOldOrderToNew(sqlconn, mysqlconn,begin,end);
        return "订单相关数据已经全部导入。";
    }
}
