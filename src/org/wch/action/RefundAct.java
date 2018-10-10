package org.wch.action;

import org.wch.bean.RechargeBean;
import org.wch.bean.RefundBean;
import org.wch.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RefundAct {


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


    //根据充值交易号查找充值ID
    public static String getRechargeIdByNum(String num,Connection mysql_conn){
        String cid = "";
        try{
            String sql = "select idpk from MEMBER_RECHARGE_RECORD where ODD_NUMBERS='"+num+"'";
            ps5 = mysql_conn.prepareStatement(sql);
            ResultSet idRs = ps5.executeQuery();
            while(idRs.next()){
                cid = idRs.getString(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return cid;
    }

    //查询旧系统用户退款记录
    public static List getOldRefundData(Connection mssqlconn){
        List<RefundBean> refundBeanList = new ArrayList<>();
        try{
            String sql = "SELECT [p_id]\n" +
                    "      ,[p_postData]\n" +
                    "      ,[p_post_url]\n" +
                    "      ,[p_getMCHID]\n" +
                    "      ,[p_UserType]\n" +
                    "      ,[p_OpenID]\n" +
                    "      ,[p_WxName]\n" +
                    "      ,[p_WxPic]\n" +
                    "      ,[p_returnXml]\n" +
                    "      ,[p_result_code]\n" +
                    "      ,[p_transaction_id]\n" +
                    "      ,[p_out_trade_no]\n" +
                    "      ,[p_out_refund_no]\n" +
                    "      ,[p_refund_id]\n" +
                    "      ,[p_refund_fee]\n" +
                    "      ,[p_coupon_refund_fee]\n" +
                    "      ,[p_total_fee]\n" +
                    "      ,[p_cash_fee]\n" +
                    "      ,[p_coupon_refund_count]\n" +
                    "      ,[p_cash_refund_fee]\n" +
                    "      ,[p_AddTime]\n" +
                    "      ,[p_GetIP]\n" +
                    "      ,[p_err_code_des]\n" +
                    "  FROM [DJX_NoPublic].[dbo].[Plug_GX_TuiKuan_Log]" +
                    "  WHERE [p_result_code]='SUCCESS'";
            ps = mssqlconn.prepareStatement(sql);
            rs = ps.executeQuery();
            System.out.println("开始读取退款数据：");
            while (rs.next()){
                RefundBean rb = new RefundBean();
                rb.setCashFee(rs.getInt("p_cash_fee"));
                rb.setCashRefundFee(rs.getInt("p_refund_fee"));
                rb.setCouponRefundCount(rs.getInt("p_coupon_refund_count"));
                rb.setCouponRefundFee(rs.getInt("p_coupon_refund_fee"));
                rb.setTransactionId(rs.getString("p_transaction_id"));
                rb.setOpenid(rs.getString("p_OpenID"));
                rb.setNumbers("TK"+rs.getString("p_id"));
                rb.setOutTradeNo(rs.getString("p_out_trade_no"));
                rb.setRefundFee(rs.getInt("p_refund_fee"));
                rb.setRefundId(rs.getString("p_refund_id"));
                rb.setRefundStatus(2);
                rb.setRefundTime(rs.getString("p_AddTime"));
                rb.setTotalFee(rs.getInt("p_total_fee"));
                refundBeanList.add(rb);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return refundBeanList;
    }

    //导入旧系统退款数据
    public  static void pushOldRefundToNew(Connection mssqlconn,Connection mysqlconn) throws SQLException{
        long startTime=System.currentTimeMillis();   //获取开始时间
        List<RefundBean> refundBeanList = getOldRefundData(mssqlconn);
        try{
            String sql = "INSERT INTO MEMBER_REFUND_RECORD(MID,MEMBER_RECHARGE_RECORD_ID,NUMBERS,TRANSACTION_ID,OUT_TRADE_NO" +
                    ",REFUND_ID,REFUND_FEE,CASH_FEE,CASH_REFUND_FEE,COUPON_REFUND_FEE,REFUND_TIME,REFUND_STATUS,TOTAL_FEE,coupon_refund_count)" +
                    " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            System.out.println("此次要处理的退款数据记录总共有: "+refundBeanList.size()+" 条");
            ps2 = mysqlconn.prepareStatement(sql);
            mysqlconn.setAutoCommit(false);
            mysqlconn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            for(int i=0;i<refundBeanList.size();i++){
                RefundBean rb = refundBeanList.get(i);
                ps2.setInt(1,getUserIdByOpenid(rb.getOpenid(),mysqlconn));
                String rid = getRechargeIdByNum(rb.getTransactionId(),mysqlconn);
                if(!rid.equals("")) {
                    ps2.setString(2, rid);
                    ps2.setString(3, rb.getNumbers());
                    ps2.setString(4, rb.getTransactionId());
                    ps2.setString(5, rb.getOutTradeNo());
                    ps2.setString(6, rb.getRefundId());
                    ps2.setInt(7, rb.getRefundFee());
                    ps2.setInt(8, rb.getCashFee());
                    ps2.setInt(9, rb.getCashRefundFee());
                    ps2.setInt(10, rb.getCouponRefundFee());
                    ps2.setString(11, rb.getRefundTime());
                    ps2.setInt(12, rb.getRefundStatus());
                    ps2.setInt(13,rb.getTotalFee());
                    ps2.setInt(14,rb.getCouponRefundCount());
                    ps2.addBatch();
                }
            }
            System.out.println("退款数据正在准备中......");
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

    public String beginOldRefundToNew(Connection sqlconn,Connection mysqlconn) throws SQLException {
        sqlconn = DbConnection.getSqlConnection();
        mysqlconn= DbConnection.getMysqlConnection();
        pushOldRefundToNew(sqlconn, mysqlconn);
        return "退款数据已经全部导入。";
    }

}
