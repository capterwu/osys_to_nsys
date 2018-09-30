package org.wch.tools;

import org.wch.action.DeviceForAgentAct;
import org.wch.action.DeviceQrCodeAct;

import java.sql.Connection;

public class DeviceTool {


    public static void main(String[] args) {
        Connection sqlconn = null;
        Connection mysqlconn = null;
        DeviceQrCodeAct dc = new DeviceQrCodeAct();   //导入设备编号;
        String actR = dc.beginDeviceAct(sqlconn,mysqlconn);  //执行设备导入动作；
        System.out.println(actR);
        String act = dc.beginQrCodeAct(sqlconn,mysqlconn); //执行设备二维码导入;
        System.out.println(act);

        String result = dc.beginUpdateGxcode(sqlconn,mysqlconn);
        System.out.println(result);
    }
}

