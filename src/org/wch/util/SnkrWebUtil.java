package org.wch.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import redis.clients.jedis.Jedis;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class SnkrWebUtil {

    private  String published = "&orderBy=published";
    private   String updated = "&orderBy=lastUpdated";

    private String cn = "&country=CN&language=zh-Hans";
    private String us = "&country=US&language=en";
    private String jp = "&country=JP&language=ja";

    private String cnLanchUrl = "https://www.nike.com/cn/launch/t/";
    private String usLanchUrl = "https://www.nike.com/us/launch/t/";
    private String jpLanchUrl = "https://www.nike.com/jp/launch/t/";

    private static List<ProductBean> pblist =new ArrayList<>();
    private static Jedis jedis;


    public  String getSnkrProInfo(String language,String act){
        String url = "https://api.nike.com/snkrs/content/v1/?";
        if(language.equals("CN")){
            url += cn;
        }else if(language.equals("US")){
            url += us;
        }else if(language.equals("JP")){
            url += jp;
        }

        if(act.equals("published")){
            act = published;
        }else if(act.equals("updated")){
            act = updated;
        }

        String requetUrl = url+"&offset=0"+act;
        StringBuilder sb=new StringBuilder();
       try {
        //   System.out.println(requetUrl);
           URL url1 = new URL(requetUrl);
           HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
           conn.setRequestMethod("GET");
           InputStream is = conn.getInputStream();
           InputStreamReader isr = new InputStreamReader(is, "utf-8"); // 设置读取流的编码格式，自定义编码
           BufferedReader reader = new BufferedReader(isr);
           String line = null;
           while ((line = reader.readLine()) != null)
               sb.append(line + " ");
           reader.close();
       } catch (Exception e) {
           // TODO Auto-generated catch block
           System.out.println("API调用出错！"+e.getMessage());
           //e.printStackTrace();
       }
       // System.out.println(sb.toString());
        return sb.toString();

    }

    public List getData(String productData,String country,String act) {
        List<ProductBean> productList = new ArrayList<>();
        Map<String, Object> innerMap = JSON.parseObject(productData).getInnerMap();
        JSONArray threads = JSON.parseArray((innerMap.get("threads") + ""));
        jedis = new Jedis("localhost");
        for (int i = 0; i < threads.size(); i++) {  //50
            Map<String, Object> productMap = JSON.parseObject(threads.getJSONObject(i).getString("product")).getInnerMap();
            String id = productMap.get("globalPid")+"";
            boolean rid = jedis.exists(id);
            if(!rid){
                String rtitle = threads.getJSONObject(i).getString("seoSlug");
                System.out.println("出现新ID："+id+" title :"+rtitle);
                jedis.set(id,rtitle);
                jedis.set("new"+id,"{image:"+productMap.get("imageUrl")+",url:"+cnLanchUrl+rtitle+"}");
            }
            ProductBean pb = new ProductBean();
            String lastDate = threads.getJSONObject(i).getString("lastUpdatedDate");
            lastDate = setDate(lastDate);
            pb.setLastUpdatedDate(lastDate);
            String pubDate = threads.getJSONObject(i).getString("publishedDate");
            pubDate = setDate(pubDate);
            pb.setPublishedDate(pubDate);
            String onsale = threads.getJSONObject(i).getString("restricted");
            if(onsale.equals("false")){
                pb.setOnSale("正常购买");
            }else {
                pb.setOnSale("专属购买");
            }
           if(productMap.get("title") !=null){
               pb.setTitle(productMap.get("title")+"");
           }else {
               pb.setTitle(threads.getJSONObject(i).getString("seoSlug"));
           }
            pb.setId(id);
            pb.setColorCode(productMap.get("style")+"-"+productMap.get("colorCode"));
            pb.setMerchStatus(productMap.get("merchStatus")+"");
            pb.setColorDescription(productMap.get("colorDescription")+"");
            if(productMap.get("price") != null){
                Map<String, Object> priceMap = JSON.parseObject(productMap.get("price")+"").getInnerMap();
                pb.setPrice(priceMap.get("fullRetailPrice")+"");
            }else{
                pb.setPrice("暂无价格");
            }
            pb.setImageUrl(productMap.get("imageUrl")+"");
            String sellDate = productMap.get("startSellDate")+"";
            if(!sellDate.equals("null")){
               sellDate =  setDate(sellDate);
            }
            pb.setStartSellDate(sellDate);
            String stopDate = productMap.get("stopSellDate")+"";
            if(!stopDate.equals("null")){
                stopDate =  setDate(stopDate);
            }
            pb.setStopSellDate(stopDate);
            String selectionEngine = productMap.get("selectionEngine")+"";
            if(selectionEngine.equals("DAN")){
                pb.setSelectionEngine("抽签");
            }else{
                pb.setSelectionEngine("限量");
            }
            String seoSlug = threads.getJSONObject(i).getString("seoSlug");
            if(country.equals("CN")){
                seoSlug = cnLanchUrl+seoSlug;
                pb.setSeoSlug(seoSlug);
            }else if(country.equals("US")){
                seoSlug = usLanchUrl+seoSlug;
                pb.setSeoSlug(seoSlug);
            }else if(country.equals("JP")){
                seoSlug = jpLanchUrl+seoSlug;
                pb.setSeoSlug(seoSlug);
            }
            productList.add(pb);
        }
//        for(int temp=0;temp<productList.size();temp++){
//            ProductBean productBean = productList.get(temp);
//            System.out.println(productBean.getTitle()+"  【"+productBean.getOnSale()+"】"+"  id:"+productBean.getId());
//            System.out.println(productBean.getImageUrl());
//            System.out.println("价格:"+productBean.getPrice());
//            System.out.println("购买地址:"+productBean.getSeoSlug());
//            System.out.println("货号:"+productBean.getColorCode()+" "+productBean.getColorDescription());
//            System.out.println("发售方式:"+productBean.getSelectionEngine());
//            if(productBean.getStopSellDate().equals("null")){
//                System.out.println("发售时间:" + productBean.getStartSellDate() + "" + "  发布时间:" + productBean.getPublishedDate());
//            }else {
//                System.out.println("发售时间:" + productBean.getStartSellDate() + "" + "  停售时间:" + productBean.getStopSellDate());
//            }
//            System.out.println("-------------------------------------------");
//        }
        return  productList;

    }

    public String setDate(String sellDate) {
        String dates = sellDate.replace('T',' ');
        try {
            dates = dates.substring(0, 19);
            SimpleDateFormat aDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date a = aDate.parse(dates);
            Calendar cal = Calendar.getInstance();
            cal.setTime(a);
            cal.add(Calendar.HOUR, 8);
            a = cal.getTime();
            cal = null;
            dates = aDate.format(a);
        }catch(ParseException e){
            System.out.println("日期转换出错啦！！"+e.getMessage());
        }
        return  dates;
    }

    public void findNewShoe(String productData,String country,String act){
        List<ProductBean> productList = new ArrayList<>();
        Map<String, Object> innerMap = JSON.parseObject(productData).getInnerMap();
        JSONArray threads = JSON.parseArray((innerMap.get("threads") + ""));
        for (int i = 0; i < threads.size(); i++) {  //50
            Map<String, Object> productMap = JSON.parseObject(threads.getJSONObject(i).getString("product")).getInnerMap();
            String id = productMap.get("globalPid")+"";
        }

    }


    public static void  main(String[] args) {
        //连接本地的 Redis 服务
        //  Jedis jedis = new Jedis("localhost");
        ScheduledExecutorUtil se = new ScheduledExecutorUtil();
        ScheduledExecutorService scheduledThreadPool = se.startScheduledExecutor();
         /*   scheduledThreadPool.execute(new Thread());
            try{
                //scheduledThreadPool.shutdown();
                if(!scheduledThreadPool.awaitTermination(5, TimeUnit.SECONDS)){
                    System.out.println(" 到达指定时间，还有线程没执行完，不再等待，关闭线程池!");
                    scheduledThreadPool.shutdown();
                }
            }catch (Throwable e){
                scheduledThreadPool.shutdown();
                e.printStackTrace();
            }*/
        boolean isDone;
        try{
        // 等待线程池终止
        do {
            isDone = scheduledThreadPool.awaitTermination(5, TimeUnit.SECONDS);
            System.out.println("连接超时...");
        } while (!isDone);
        if (!isDone) {
            System.out.println("任务停止了...");
            // isDone = true;
        }
        System.out.println("正在重连...");
        Thread.sleep(10000);
        }catch (Exception e){
            System.out.println(e.getMessage());
            // 关闭线程池
            scheduledThreadPool.shutdown();
        }

    }
    }


