package org.wch.action;

import org.wch.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeviceQrCodeAct {

    static PreparedStatement ps = null;
    static PreparedStatement ps2 = null;
    static PreparedStatement ps3 = null;
    static PreparedStatement ps4 = null;
    static PreparedStatement ps5 = null;

    static ResultSet rs = null;
    static ResultSet rs2 = null;

    //查找旧数据裤中的设备编号；
    public static List getOdbDeviceId(Connection conn){
        List deviceList =  new ArrayList();
        try{
            String sql = "select p_deviceid from plug_gx_qrcode where 1=1 GROUP BY p_deviceid";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                deviceList.add(rs.getString("p_deviceid"));
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return deviceList;
    }

    //把旧数据库的设备编号导入新数据库
    public static Boolean addDeviceIdToNdb(Connection conn,List deviceId){
        Boolean result = false;
        try {
            conn.setAutoCommit(false);
            String sql = "insert into EQUIPINFO(code,state,isstock,type,server,id) values (?,?,?,?,?,?)";
            String sql2 = "insert into QRCODE_STORE(code,isonecode,qrcode,equip,remark,operator) values(1,2,1,?,'系统处理',1)";
            ps = conn.prepareStatement(sql);
            ps2 = conn.prepareStatement(sql2);
            int indexnum = 90;
            for (int i = 0;i<deviceId.size();i++){
                ps.setString(1,deviceId.get(i).toString());
                ps.setInt(2,2);
                ps.setInt(3,1);
                ps.setInt(4,1);
                ps.setInt(5,1);
                ps.setInt(6,i+indexnum);
                ps2.setInt(1,i+indexnum);
                ps.addBatch();
                ps2.addBatch();
            }
            ps.executeBatch();
            ps2.executeBatch();
            conn.commit();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
        return result;
    }

    //根据旧数据的设备编号查找二维码
    public static void checkOdbQrcode(Connection sqlconn,Connection mysqlconn){
        try{
            String sql = "select p_usertype,p_ticket,p_ticketurl,p_picurl from plug_gx_qrcode where p_deviceid=?";
            String sql2 = "select b.id from equipinfo a,qrcode_store b where a.id = b.equip and a.code=?";
            String sql3 = "update qrcode_store set ali_qrcode=?,isbind=1,registime='2018-08-29' where id=?";
            String sql4 =  "update qrcode_store set wechat_qrcode=?,wechat_ticket=?,isbind=1,registime='2018-08-29' where id=?";
            ps5 = mysqlconn.prepareStatement(sql2);
            ps2 = sqlconn.prepareStatement(sql);
            ps3 = mysqlconn.prepareStatement(sql3);
            ps4 = mysqlconn.prepareStatement(sql4);
            List dev = getOdbDeviceId(sqlconn);
            for(int i =0 ;i<dev.size();i++) {
                String devicecode = dev.get(i).toString();
                ps5.setString(1,devicecode);
                ps2.setString(1,devicecode);
                rs = ps5.executeQuery();
                rs2 = ps2.executeQuery();
                while(rs.next()){
                    int qrcode_id = rs.getInt("id");
                    System.out.println("正在处理设备号:"+devicecode+" ;二维码ID:"+qrcode_id);
                    if(devicecode.equals("") || devicecode.isEmpty()){
                        System.out.println("此设备号:"+devicecode+" 、二维码ID:"+qrcode_id+" 无法处理。");
                    }else {
                        while (rs2.next()) {
                            String type = rs2.getString("p_usertype");
                            String ticket = rs2.getString("p_ticket");
                            String ticket_url = rs2.getString("p_ticketurl");
                            String p_picture = rs2.getString("p_picurl");
                            if (type.equals("支付宝")) {
                                ps3.setString(1, p_picture);
                                ps3.setInt(2, qrcode_id);
                                ps3.executeUpdate();
                            } else {
                                type.equals("微信");
                                ps4.setString(1, ticket_url);
                                ps4.setString(2, ticket);
                                ps4.setInt(3, qrcode_id);
                                ps4.executeUpdate();
                            }
                        }
                    }
                }
            }

        }catch (SQLException e){
        e.printStackTrace();
    }finally {
        DbConnection.closeDB();
    }
    }

    public static void updateGxCode(Connection sqlconn,Connection mysqlconn){
        try{
            String sql="SELECT p_usertype,p_Scene_Str,p_picurl,p_DeviceID FROM Plug_GX_QrCode WHERE p_UserType = '支付宝' and p_DeviceID != ''";
            String sql2 = "update qrcode_store set gx_qrcode=? where EQUIP in (select id from equipinfo where code=?)";
            ps = sqlconn.prepareStatement(sql);
            ps2 = mysqlconn.prepareStatement(sql2);
            rs = ps.executeQuery();
            while(rs.next()){
                String gxcode = rs.getString("p_Scene_Str");
                String dcode = rs.getString("p_DeviceID");
                ps2.setString(1,gxcode);
                ps2.setString(2,dcode);
                ps2.addBatch();
            }
            ps2.executeBatch();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbConnection.closeDB();
        }
    }



    public String beginDeviceAct(Connection sqlconn,Connection mysqlconn){
        sqlconn = DbConnection.getSqlConnection();
        mysqlconn= DbConnection.getMysqlConnection();
        List list = getOdbDeviceId(sqlconn);
        addDeviceIdToNdb(mysqlconn,list);
        return "设备信息导入执行完毕。";
    }

    public String beginQrCodeAct(Connection sqlconn,Connection mysqlconn){
        sqlconn = DbConnection.getSqlConnection();
        mysqlconn= DbConnection.getMysqlConnection();
        checkOdbQrcode(sqlconn,mysqlconn);
        return "二维码信息导入执行完毕。";
    }

    public String beginUpdateGxcode(Connection sqlconn,Connection mysqlconn){
        sqlconn = DbConnection.getSqlConnection();
        mysqlconn= DbConnection.getMysqlConnection();
        updateGxCode(sqlconn,mysqlconn);
        return "更新GXCODE执行完毕。";
    }






}
