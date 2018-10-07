package org.wch.action;

import org.wch.bean.RechargeBean;
import org.wch.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RechargeAct {


    static PreparedStatement ps = null;
    static PreparedStatement ps2 = null;
    static PreparedStatement ps3 = null;
    static PreparedStatement ps4 = null;
    static PreparedStatement ps5 = null;
    static PreparedStatement ps6 = null;
    static ResultSet rs = null;

    //根据用户OPENID查找用户ID
    public static int getUserIdByOpenid(String openid,Connection mysql_conn){
        int mid = 0;
        try{
            String sql = "select id from member where openid='"+openid+"'";
            ps5 = mysql_conn.prepareStatement(sql);
            ResultSet idRs = ps5.executeQuery();
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

    //查询旧系统用户充值记录
    public static List getOldRechargeData(Connection mssqlconn){
        List<RechargeBean> rechargeBeanList = new ArrayList<>();
        try{
            String sql = "SELECT \n" +
                    "       [Addtime]\n" +
                    "      ,[openid]\n" +
                    "      ,[total_fee]\n" +
                    "      ,[transaction_id]\n" +
                    "      ,[out_trade_no]\n" +
                    "  FROM [DJX_NoPublic].[dbo].[Plug_GX_Pay]\n";
            ps = mssqlconn.prepareStatement(sql);
            rs = ps.executeQuery();
            System.out.println("开始读取充值数据：");
            while (rs.next()){
                RechargeBean rb = new RechargeBean();
                rb.setMoid(rs.getString("openid"));
                rb.setNumbers(rs.getString("out_trade_no"));
                rb.setAccrual(rs.getDouble("total_fee"));
                rb.setTrade_time(rs.getString("Addtime"));
                rb.setOdd_numbers(rs.getString("transaction_id"));
                rechargeBeanList.add(rb);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return rechargeBeanList;
    }


    //导入旧系统充值数据
    public  static void pushOldRechargeToNew(Connection mssqlconn,Connection mysqlconn) throws SQLException{
        long startTime=System.currentTimeMillis();   //获取开始时间
        List<RechargeBean> rechargeBeanList = getOldRechargeData(mssqlconn);
        try{
            String sql = "INSERT INTO MEMBER_RECHARGE_RECORD(MID,NUMBERS,ACCRUAL,ODD_NUMBERS,PAY_STATUS,TRADING_TIME" +
                    ",TRADING_TYPE) VALUES(?,?,?,?,?,?,?)";
            System.out.println("此次要处理的充值数据记录总共有: "+rechargeBeanList.size()+" 条");
            ps2 = mysqlconn.prepareStatement(sql);
            mysqlconn.setAutoCommit(false);
            mysqlconn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            for(int i=0;i<rechargeBeanList.size();i++){
                RechargeBean rb = rechargeBeanList.get(i);
                ps2.setInt(1,getUserIdByOpenid(rb.getMoid(),mysqlconn));
                ps2.setString(2,rb.getNumbers());
                ps2.setDouble(3,rb.getAccrual()/100);
                ps2.setString(4,rb.getOdd_numbers());
                ps2.setInt(5,2);
                ps2.setString(6,rb.getTrade_time());
                ps2.setInt(7,1);
                ps2.addBatch();
            }
            System.out.println("充值数据正在准备中......");
            ps2.executeBatch();
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

    public String beginOldRechargeToNew(Connection sqlconn,Connection mysqlconn) throws SQLException {
        sqlconn = DbConnection.getSqlConnection();
        mysqlconn= DbConnection.getMysqlConnection();
        pushOldRechargeToNew(sqlconn, mysqlconn);
        return "充值数据已经全部导入。";
    }
}


