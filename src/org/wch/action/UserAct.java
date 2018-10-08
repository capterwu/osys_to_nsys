package org.wch.action;

import org.wch.bean.UserBean;
import org.wch.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserAct {

    static PreparedStatement ps = null;
    static PreparedStatement ps2 = null;
    static PreparedStatement ps3 = null;
    static PreparedStatement ps4 = null;
    static PreparedStatement ps5 = null;

    static ResultSet rs = null;

    //获得旧数据库用户表的数据
    public static List getOdbUserData(Connection sqlconn){
        List<UserBean> userList = new ArrayList();
        try{
            String sql = "select a.p_ID,a.p_OpenID,a.p_WxName,a.p_WxPic,a.p_UserType,a.p_GroupName,a.p_Group_Time,a.p_WxSex,a.p_Account_My,a.p_Account_Tui,a.p_Account_YaJin " +
                    ",a.p_Account_TiCheng,a.p_Account_TiCheng_Tui,a.p_Account_Shop,a.p_Account_Shop_Tui,a.p_TiXian from Plug_GX_User as a" +
                    " order by p_ID ";
            ps = sqlconn.prepareStatement(sql);
            rs = ps.executeQuery();
            System.out.println("开始读取旧的用户数据，总共有：");
            while(rs.next()){
                UserBean userBean = new UserBean();
                userBean.setOpenid(rs.getString("p_OpenID"));
                userBean.setNickname(rs.getString("p_WxName"));
                userBean.setHeadPic(rs.getString("p_WxPic"));
                userBean.setId(rs.getInt("p_ID"));
                userBean.setRegistime(rs.getString("p_Group_Time"));
                String userType = rs.getString("p_UserType");
                if(userType.equals("微信用户") ){
                    userBean.setUserType(1);
                }else if(userType.equals("支付宝用户")){
                    userBean.setUserType(2);
                }
                String sex = rs.getString("p_WxSex");
                if(sex.equals("m")){
                    userBean.setSex(1);
                }else if(sex.equals("f")){
                    userBean.setSex(2);
                }else{
                    userBean.setSex(1);
                }
                userBean.setGroupType(1);
                String tixian = rs.getString("p_TiXian") == null?"":rs.getString("p_TiXian");
                if(tixian.equals("禁止")){
                    userBean.setIsTixian(2);
                }else{
                    userBean.setIsTixian(1);
                }
                double account_my = rs.getDouble("p_Account_My");
                double account_tui = rs.getDouble("p_Account_Tui");
                double account_yajin = rs.getDouble("p_Account_YaJin");
                double totalMoney = account_my+account_tui+account_yajin;
                userBean.setLastMoney(totalMoney);
                double p_Account_TiCheng = rs.getDouble("p_Account_TiCheng");
                double p_Account_TiCheng_Tui = rs.getDouble("p_Account_TiCheng_Tui");
                double p_Account_Shop = rs.getDouble("p_Account_Shop");
                double p_Account_Shop_Tui = rs.getDouble("p_Account_Shop_Tui");
                double agent_money = p_Account_Shop+p_Account_Shop_Tui+p_Account_TiCheng+p_Account_TiCheng_Tui;
                userBean.setAgent_wallet(agent_money);
                userList.add(userBean);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return  userList;
    }

    //插入旧数据到新数据库
    public  static void pushOldMemberToNew(Connection mssqlconn,Connection mysqlconn) throws SQLException {
        long startTime=System.currentTimeMillis();   //获取开始时间
        try{
            String sql="insert into member(id,openid,nick_name,head_img,m_type,m_group,sex,registtime,isreceive,iswithdraw,isblock)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?)";    //添加用户记录
            String sql2 = "insert into member_wallet(MID,ACCRUAL,CURRENT,LAST_BALANCE,CHANGE_TIME) values(?,?,?,?,?)";   //添加用户钱包
            String sql3 = "insert into MEMBER_SOURCE(OPENID,QRCODEID) values (?,?)";    //添加公众号用户来源;
            String sql4 = "insert into MEMBER_DEPOSIT(MID,ACCRUAL,CURRENT,LAST_BALANCE,CHANGE_TIME) values(?,?,?,?,?)";   //添加用户押金;

            mysqlconn.setAutoCommit(false);
            mysqlconn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            ps2 = mysqlconn.prepareStatement(sql);
            ps3 = mysqlconn.prepareStatement(sql2);
            ps4 = mysqlconn.prepareStatement(sql3);
            ps5 = mysqlconn.prepareStatement(sql4);

            List<UserBean> oldMember_list = getOdbUserData(mssqlconn);
            System.out.println("此次要处理的用户数据记录总共有: "+oldMember_list.size()+" 条");
            for(int i=0;i<oldMember_list.size();i++){
                UserBean userBean= oldMember_list.get(i);
                //【处理MEMBER表数据】
                ps2.setInt(1,userBean.getId());
                ps2.setString(2,userBean.getOpenid());
                ps2.setString(3,userBean.getNickname());
                ps2.setString(4,userBean.getHeadPic());
                System.out.println("userBean.getUserType():"+userBean.getUserType());
                ps2.setInt(5,userBean.getUserType());
                ps2.setInt(6,userBean.getGroupType());
                ps2.setInt(7,userBean.getSex());
                ps2.setString(8,userBean.getRegistime());
                ps2.setInt(9,1);
                ps2.setInt(10,userBean.getIsTixian());
                ps2.setInt(11,2);

                //【处理MEMBER_WALLET表数据】
                ps3.setInt(1,userBean.getId());
                ps3.setDouble(2,userBean.getLastMoney());
                ps3.setDouble(3,0);
                ps3.setDouble(4,userBean.getLastMoney());
                ps3.setString(5,setNowDate());

                //【处理MEMBER_SOURCE表数据】
                ps4.setString(1,userBean.getOpenid());
                ps4.setInt(2,1);

                ps2.addBatch();
                ps3.addBatch();
                ps4.addBatch();
            }
            System.out.println("用户基础数据正在准备中......");
            ps2.executeBatch();
            System.out.println("用户钱包数据正在准备中......");
            ps3.executeBatch();
            System.out.println("用户来源数据正在准备中......");
            ps4.executeBatch();
            System.out.println("准备处理数据，请耐心等待，开始处理中......");
            mysqlconn.commit();
            System.out.println("恭喜你，数据处理完毕了。");
        }catch (Exception e){
            mysqlconn.rollback();
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+((endTime-startTime)/1000)/60+"分钟");
    }

    public static String setNowDate() {
        SimpleDateFormat aDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDate = aDate.format(new Date());
        return nowDate;
    }

    public String beginOldMemToNew(Connection sqlconn,Connection mysqlconn) throws SQLException {
        sqlconn = DbConnection.getSqlConnection();
        mysqlconn= DbConnection.getMysqlConnection();
        pushOldMemberToNew(sqlconn,mysqlconn);
        return "旧用户数据已经全部导入。";
    }
}
