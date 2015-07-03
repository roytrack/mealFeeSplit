package com.roytrack.mealfeesplit.service;


import com.roytrack.mealfeesplit.model.Meal;
import com.roytrack.mealfeesplit.model.OtherFee;
import com.roytrack.mealfeesplit.model.Total;
import com.roytrack.mealfeesplit.util.CalcUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ruanchangming on 2015/7/1.
 */
@Service
public class CalcService {


    public String calc(String origin) {
        origin = origin.replace("\r\n", "\n").replace("\n", "\r\n")
                .replaceAll("(\\r\\n\\d){5}\\r\\n.+\\r\\n", "")
                .replace("该状态下不能点评","")
                .replace("过期不能点评","");
        String[] strArray = origin.split("\r\n");
        List<Meal> mealList = new ArrayList<Meal>();
        List<OtherFee> otherFees = new ArrayList<OtherFee>();
        short i = 1;//行数
        int flag = 0;// 0 无状态  1 美食篮子  2 其他费用
        Total total=new Total();
        for (String aline : strArray) {
            if (aline.startsWith("美食篮子")) {
                flag = 1;
                continue;
            };
            if (aline.startsWith("其他费用")) {
                flag = 2;
                continue;
            };
            if(aline.startsWith("合计")){
                String[] tmp = aline.split("\t");
                total.setId(i++);
                total.setTotalName("合计");
                short j = 0;
                while (tmp[++j].length() == 0) continue;
                total.setQuantity(Double.parseDouble(tmp[j]));
                while (tmp[++j].length() == 0) continue;
                total.setAmount(Double.parseDouble(tmp[j].substring(1)));
                continue;
            }
            System.out.println(aline.replace("\t", "@"));
            if (flag == 1) {//美食
                Meal meal = new Meal();
                String[] tmp = aline.split("\t");
                meal.setId(i++);
                meal.setMealName(tmp[0]);
                short j = 0;
                while (tmp[++j].length() == 0) continue;
                meal.setPrice(Double.parseDouble(tmp[j].substring(1)));
                while (tmp[++j].length() == 0) continue;
                meal.setQuantity(Double.parseDouble(tmp[j]));
                while (tmp[++j].length() == 0) continue;
                meal.setAmount(Double.parseDouble(tmp[j].substring(1)));
                mealList.add(meal);
            } else if (flag == 2) {
                OtherFee otherFee = new OtherFee();
                String[] tmp = aline.split("\t");
                otherFee.setId(i++);
                otherFee.setDiscountName(tmp[0]);
                short j = 0;
                if (tmp[0].startsWith("红包")) {
                    while (tmp[++j].length() == 0) continue;
                    otherFee.setAmount(Double.parseDouble(tmp[j].substring(1)));
                } else {
                    while (tmp[++j].length() == 0) continue;
                    otherFee.setPrice(Double.parseDouble(tmp[j].substring(1)));
                    while (tmp[++j].length() == 0) continue;
                    otherFee.setQuantity(Double.parseDouble(tmp[j]));
                    while (tmp[++j].length() == 0) continue;
                    otherFee.setAmount(Double.parseDouble(tmp[j].substring(1)));
                }
                otherFees.add(otherFee);
                continue;
            }
        }

            double orderAmount = 0;
            for (Meal m : mealList) {
                orderAmount += m.getAmount();
            }
            for(OtherFee fee:otherFees){
                if(fee.getAmount()>0){
                    orderAmount+=fee.getAmount();
                }
            }
            double orderRealAmount = orderAmount;
            for (OtherFee fee : otherFees) {
                if(fee.getAmount()<0){
                    orderRealAmount += fee.getAmount();
                }
            }
            double off = CalcUtil.divide(orderRealAmount, orderAmount).doubleValue();
            for (Meal m : mealList) {
                m.setNet(CalcUtil.multiply(m.getAmount(), off).setScale(2).doubleValue());
            }

        for (OtherFee fee : otherFees) {
            fee.setNet(CalcUtil.multiply(fee.getAmount(), off).setScale(2).doubleValue());
        }

            StringBuffer stringBuffer = new StringBuffer("<table id='tab1' border='1'><tr><th>序号</th><th>类目</th><th>单价</th>" +
                    "<th>数量</th><th>小计</th><th>折扣金额</th><th>所属人(多人逗号分开)</th></tr>");
            for (Meal m : mealList) {
                stringBuffer.append("<tr><td>").append(m.getId()).append("</td><td>").append(m.getMealName()).append("</td><td>")
                        .append(m.getPrice()).append("</td><td>").append(m.getQuantity()).append("</td><td>").append(m.getAmount())
                        .append("</td><td>").append(m.getNet()).append("</td><td><input type='text' class='owner" + m.getId() + "'/></td></tr>");
            }
        for (OtherFee fee : otherFees) {
            if(fee.getAmount()>0){
                stringBuffer.append("<tr><td>").append(fee.getId()).append("</td><td>").append(fee.getDiscountName()).append("</td><td>")
                        .append(fee.getPrice()).append("</td><td>").append(fee.getQuantity()).append("</td><td>").append(fee.getAmount())
                        .append("</td><td>").append(fee.getNet()).append("</td><td><input type='text' class='owner" + fee.getId() + "'/></td></tr>");
            }

        }
            stringBuffer.append("</table>");
            return stringBuffer.toString();
        }

    public static void main(String[] args) throws IOException {
        File f1 = new File("E:\\个人\\饿了吗结账\\评分后.txt");
        File f2 = new File("E:\\个人\\饿了吗结账\\下单后.txt");
        File f3 = new File("E:\\个人\\饿了吗结账\\下单未评分.txt");
        FileReader fr = new FileReader(f1);
        System.out.println(f1.length());
        int length = (int) f1.length();
        char[] contentByte = new char[length];
        fr.read(contentByte);
        int realLength = length;
        for (char c : contentByte) {
            if ((int) c == 0)
                realLength--;
        }
        String content = new String(contentByte).substring(0, realLength);
        System.out.println(content);
        System.out.println("---------------------------------");
        content = content.replaceAll("(\\r\\n\\d){5}\\r\\n.+\\r\\n", "");
        System.out.println(content);

    }
}


