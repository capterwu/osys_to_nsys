package org.wch.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorUtil {


    public ScheduledExecutorService startScheduledExecutor(){
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
        Task worker = new Task("CN","updated");
        // 只执行一次
//          scheduledThreadPool.schedule(worker, 5, TimeUnit.SECONDS);
        // 周期性执行，每5秒执行一次
        scheduledThreadPool.scheduleAtFixedRate(worker, 1,3, TimeUnit.SECONDS);
        return scheduledThreadPool;
    }
}

class Task implements Runnable {
    private SnkrWebUtil sw;
    private String country;
    private String act;
    private int counts = 0;
    public Task(String country,String act) {
        this.country = country;
        this.act = act;
        sw = new SnkrWebUtil();
    }

    @Override
    public void run() {
        counts ++;
        SimpleDateFormat aDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = new Date();
        System.out.println("name = SnkrBot start No." + counts + ", startTime = " + aDate.format(dt));
        try {
            String data = sw.getSnkrProInfo(country,act);
           // Thread.sleep(1000);
            sw.getData(data,country,act);
                //e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
        }
       // System.out.println("name = SnkrBot start No." + counts + ", endTime = " + aDate.format(dt));
    }
}
