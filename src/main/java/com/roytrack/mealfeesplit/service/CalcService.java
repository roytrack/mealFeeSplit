package com.roytrack.mealfeesplit.service;


import com.roytrack.mealfeesplit.util.CalcUtil;
import com.roytrack.mealfeesplit.model.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ruanchangming on 2015/7/1.
 */
@Service
public class CalcService {


    public String calc(String origin) {
        origin=origin.replaceAll("(\\r\\n\\d){5}\\r\\n.+\\r\\n","");
        String [] strArray=origin.split("\r\n");
        List<Meal> mealList=new ArrayList<Meal>();
        List<Discount> discounts=new ArrayList<Discount>();
        short i=1;
        for(String aline:strArray){
        if(aline.startsWith("美食篮子")||aline.startsWith("其他费用")||aline.startsWith("合计")){
            continue;
        }
            System.out.println(aline.replace("\t","@"));
        if (aline.startsWith("红包抵扣")||aline.startsWith("在线支付立减优惠")){
            Discount discount=new Discount();
            String[] tmp=aline.split("\t");
            discount.setId(i++);
            discount.setDiscountName(tmp[0]);
            discount.setDiscountNum(Double.parseDouble(tmp[4].substring(1)));
            discounts.add(discount);
            continue;
        }
            Meal meal=new Meal();
            String [] tmp=aline.split("\t");
            meal.setId(i++);
            int j=0;
            if(aline.startsWith("餐盒")){
                meal.setMealName(tmp[0]);
                meal.setPrice(Double.parseDouble(tmp[2].substring(1)));
                meal.setQuantity(Double.parseDouble(tmp[3]));
                meal.setAmount(Double.parseDouble(tmp[4].substring(1)));
            }else{
                meal.setMealName(tmp[0]);
                meal.setPrice(Double.parseDouble(tmp[1].substring(1)));
                meal.setQuantity(Double.parseDouble(tmp[2]));
                meal.setAmount(Double.parseDouble(tmp[3].substring(1)));
            }

            mealList.add(meal);
        }

        double orderAmount=0;
        for(Meal m:mealList){
            orderAmount+=m.getAmount();
        }
        double orderRealAmount=orderAmount;
        for(Discount d:discounts){
            orderRealAmount+=d.getDiscountNum();
        }
        double off= CalcUtil.divide(orderRealAmount, orderAmount).doubleValue();
        for(Meal m:mealList){
            m.setNet(CalcUtil.multiply(m.getAmount(),off).setScale(2).doubleValue());
        }
        StringBuffer stringBuffer=new StringBuffer("<html>" +
                "<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>" +
                "<body><table id='tab1' border='1'><th><tr><td>序号</td><td>类目</td><td>单价</td>" +
                "<td>数量</td><td>小计</td><td>折扣金额</td><td>所属人(多人逗号分开)</td></tr></th>");
        for(Meal m:mealList){
                stringBuffer.append("<tr><td>").append(m.getId()).append("</td><td>").append(m.getMealName()).append("</td><td>")
                        .append(m.getPrice()).append("</td><td>").append(m.getQuantity()).append("</td><td>").append(m.getAmount())
                        .append("</td><td>").append(m.getNet()).append("</td><td><input type='text' class='owner"+m.getId()+"'/></td></tr>");
        }
        stringBuffer.append("</table></body></html>");
        return stringBuffer.toString();
    }

    public static void main(String[] args) throws IOException {
        File f1=new File("E:\\个人\\饿了吗结账\\评分后.txt");
        File f2=new File("E:\\个人\\饿了吗结账\\下单后.txt");
        File f3=new File("E:\\个人\\饿了吗结账\\下单未评分.txt");
        FileReader fr=new FileReader(f1);
        System.out.println(f1.length());
        int length=(int)f1.length();
        char [] contentByte=new char[length];
        fr.read(contentByte);
        int realLength=length;
        for (char c:contentByte){
            if((int)c==0)
                realLength--;
        }
        String content=new String(contentByte).substring(0,realLength);
        System.out.println(content);
        System.out.println("---------------------------------");
        content=content.replaceAll("(\\r\\n\\d){5}\\r\\n.+\\r\\n", "");
        System.out.println(content);

    }
}


