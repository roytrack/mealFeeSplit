package com.roytrack.mealfeesplit.service;


import com.roytrack.mealfeesplit.model.Meal;
import com.roytrack.mealfeesplit.model.OtherFee;
import com.roytrack.mealfeesplit.model.Person;
import com.roytrack.mealfeesplit.model.Total;
import com.roytrack.mealfeesplit.util.CalcUtil;
import com.roytrack.mealfeesplit.util.JSonUtils;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;


/**
 * Created by ruanchangming on 2015/7/1.
 */
@Service
public class CalcService {
    Base64 base64 = new Base64();

    public String calc(String origin, HttpServletResponse response) throws IOException {
        if (origin.startsWith("美食篮子")) {
            return calcComplex(origin, response);
        } else {
            return calcSample(origin, response);
        }
    }

    public String calcSample(String origin, HttpServletResponse response) throws IOException {
        try {
            origin = origin.replace("\r\n", "\n").replace("\n", "\r\n");

            String[] strArray = origin.split("\r\n");
            List<Meal> mealList = new ArrayList<Meal>();
            List<OtherFee> FeeList = new ArrayList<OtherFee>();
            short i = 1;//行数
            double orderRealAmount = 0;
            Total total = new Total();
            for (String aline : strArray) {
                if (aline.startsWith("菜品")) {

                    continue;
                }
                ;
                if (aline.startsWith("实际支付")) {
                    orderRealAmount = Double.parseDouble(aline.substring(5));
                    continue;
                }
                System.out.println(aline.replace("\t", "@"));
                Meal meal = new Meal();
                String[] tmp = aline.split("\t");
                short j = 0;
                meal.setId(i++);
                meal.setMealName(tmp[0]);
                meal.setQuantity(tmp[++j].length() == 0 ? 0 : Double.parseDouble(tmp[j]));
                meal.setPrice(tmp[j].length() == 0 ? 0 : (CalcUtil.divide(tmp[j + 1], tmp[j]).doubleValue()));
                meal.setAmount(Double.parseDouble(tmp[j + 1]));
                mealList.add(meal);
            }

            double orderAmount = 0;
            for (Meal m : mealList) {
                if (m.getAmount() > 0)
                    orderAmount += m.getAmount();
            }

            double off = CalcUtil.divide(orderRealAmount, orderAmount).doubleValue();
            for (Meal m : mealList) {
                m.setNet(CalcUtil.multiply(m.getAmount(), off).setScale(2).doubleValue());
            }

            String feeJson = JSonUtils.toJSon(FeeList);
            String mealJson = JSonUtils.toJSon(mealList);

            System.out.println("----------json-----------------");
            System.out.println("feeJson " + feeJson);
            System.out.println("mealJson " + mealJson);
            System.out.println("------------encode---------------");
            feeJson = new String(base64.encode(JSonUtils.toJSon(FeeList).getBytes()));
            mealJson = new String(base64.encode(JSonUtils.toJSon(mealList).getBytes()));
            System.out.println(feeJson);
            System.out.println(mealJson);
            System.out.println("------------decode---------------");
            System.out.println(new String(base64.decode(feeJson)));
            System.out.println(new String(base64.decode(mealJson)));
            Cookie feeCookie = new Cookie("fee",
                    URLEncoder.encode(new String(base64.encode(JSonUtils.toJSon(FeeList).getBytes())), "UTF-8"));
            Cookie mealCookie = new Cookie("meal",
                    URLEncoder.encode(new String(base64.encode(JSonUtils.toJSon(mealList).getBytes())), "UTF-8"));
            response.addCookie(feeCookie);
            response.addCookie(mealCookie);

            StringBuffer stringBuffer = new StringBuffer("<table id='tab1' border='1' class='sum" + i + "'><tr><th>序号</th><th>类目</th><th>单价</th>" +
                    "<th>数量</th><th>小计</th><th>折扣金额</th><th>所属人(多人逗号分开)</th></tr>");

            for (Meal m : mealList) {
                if (m.getAmount() > 0)
                    stringBuffer.append("<tr><td>").append(m.getId()).append("</td><td>").append(m.getMealName()).append("</td><td>")
                            .append(m.getPrice()).append("</td><td>").append(m.getQuantity()).append("</td><td>").append(m.getAmount())
                            .append("</td><td>").append(m.getNet()).append("</td><td><input type='text' class='owner" + m.getId() + "'/></td></tr>");
            }
            stringBuffer.append("</table>");
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }

    }


    public String calcComplex(String origin, HttpServletResponse response) throws IOException {
        try {
            origin = origin.replace("\r\n", "\n").replace("\n", "\r\n")
                    .replaceAll("(\\r\\n\\d){5}\\r\\n.+\\r\\n", "")
                    .replace("该状态下不能点评", "")
                    .replace("\r\n尚未打分\r\n", "")
                    .replace("下单5分钟后才能评论", "")
                    .replace("过期不能点评", "");
            String[] strArray = origin.split("\r\n");
            List<Meal> mealList = new ArrayList<Meal>();
            List<OtherFee> FeeList = new ArrayList<OtherFee>();
            short i = 1;//行数
            int flag = 0;// 0 无状态  1 美食篮子  2 其他费用

            Total total = new Total();
            for (String aline : strArray) {
                if (aline.startsWith("美食篮子")) {
                    flag = 1;
                    continue;
                }
                ;
                if (aline.startsWith("其他费用")) {
                    flag = 2;
                    continue;
                }
                ;
                if (aline.startsWith("合计")) {
                    String[] tmp = aline.split("\t");
                    short j = 0;
                    total.setId(i++);
                    total.setTotalName("合计");
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
                    short j = 0;
                    meal.setId(i++);
                    meal.setMealName(tmp[0]);
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
                    FeeList.add(otherFee);
                    continue;
                }
            }

            double orderAmount = 0;
            for (Meal m : mealList) {
                orderAmount += m.getAmount();
            }
            for (OtherFee fee : FeeList) {
                if (fee.getAmount() > 0) {
                    orderAmount += fee.getAmount();
                }
            }
            double orderRealAmount = orderAmount;
            for (OtherFee fee : FeeList) {
                if (fee.getAmount() < 0) {
                    orderRealAmount += fee.getAmount();
                }
            }
            double off = CalcUtil.divide(orderRealAmount, orderAmount).doubleValue();
            for (Meal m : mealList) {
                m.setNet(CalcUtil.multiply(m.getAmount(), off).setScale(2).doubleValue());
            }

            for (OtherFee fee : FeeList) {
                fee.setNet(CalcUtil.multiply(fee.getAmount(), off).setScale(2).doubleValue());
            }
            String feeJson = JSonUtils.toJSon(FeeList);
            String mealJson = JSonUtils.toJSon(mealList);

            System.out.println("----------json-----------------");
            System.out.println("feeJson " + feeJson);
            System.out.println("mealJson " + mealJson);
            System.out.println("------------encode---------------");
            feeJson = new String(base64.encode(JSonUtils.toJSon(FeeList).getBytes()));
            mealJson = new String(base64.encode(JSonUtils.toJSon(mealList).getBytes()));
            System.out.println(feeJson);
            System.out.println(mealJson);
            System.out.println("------------decode---------------");
            System.out.println(new String(base64.decode(feeJson)));
            System.out.println(new String(base64.decode(mealJson)));
            Cookie feeCookie = new Cookie("fee",
                    URLEncoder.encode(new String(base64.encode(JSonUtils.toJSon(FeeList).getBytes())), "UTF-8"));
            Cookie mealCookie = new Cookie("meal",
                    URLEncoder.encode(new String(base64.encode(JSonUtils.toJSon(mealList).getBytes())), "UTF-8"));
            response.addCookie(feeCookie);
            response.addCookie(mealCookie);

            StringBuffer stringBuffer = new StringBuffer("<table id='tab1' border='1' class='sum" + i + "'><tr><th>序号</th><th>类目</th><th>单价</th>" +
                    "<th>数量</th><th>小计</th><th>折扣金额</th><th>所属人(多人逗号分开)</th></tr>");

            for (Meal m : mealList) {
                stringBuffer.append("<tr><td>").append(m.getId()).append("</td><td>").append(m.getMealName()).append("</td><td>")
                        .append(m.getPrice()).append("</td><td>").append(m.getQuantity()).append("</td><td>").append(m.getAmount())
                        .append("</td><td>").append(m.getNet()).append("</td><td><input type='text' class='owner" + m.getId() + "'/></td></tr>");
            }
            for (OtherFee fee : FeeList) {
                if (fee.getAmount() > 0) {
                    stringBuffer.append("<tr><td>").append(fee.getId()).append("</td><td>").append(fee.getDiscountName()).append("</td><td>")
                            .append(fee.getPrice()).append("</td><td>").append(fee.getQuantity()).append("</td><td>").append(fee.getAmount())
                            .append("</td><td>").append(fee.getNet()).append("</td><td><input type='text' class='owner" + fee.getId() + "'/></td></tr>");
                }

            }
            stringBuffer.append("</table>");
            return stringBuffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }


    public String splitPerson(String personInfo, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String jsonFee = "", jsonMeal = "";
        try {
            for (Cookie c : cookies) {
                if (c.getName().equalsIgnoreCase("fee")) {
                    jsonFee = new String(base64.decode(URLDecoder.decode(c.getValue(), "UTF-8")));
                }
                if (c.getName().equalsIgnoreCase("meal")) {
                    jsonMeal = new String(base64.decode(URLDecoder.decode(c.getValue(), "UTF-8")));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        List<OtherFee> fees = Arrays.asList(JSonUtils.readValue(jsonFee, OtherFee[].class));
        List<Meal> meals = Arrays.asList(JSonUtils.readValue(jsonMeal, Meal[].class));
        //personInfo 数据格式  X@1,2,3#Y@4,5
        String[] lines = personInfo.split("#");
        Map<String, Person> persons = new HashMap<String, Person>();
        int id_generator = 0;
        for (String line : lines) {
            int id = Integer.valueOf(line.split("@")[0]);
            String[] personName = line.split("@")[1].replace("，", ",").split(",");
            double fee = 0;
            StringBuffer feeDetail = new StringBuffer();
            for (Meal m : meals) {
                if (m.getId() == id) {
                    fee = CalcUtil.divide(m.getNet(), personName.length).doubleValue();
                    feeDetail.append(m.getMealName()).append(":").append(fee).append(",");
                    break;
                }
            }
            for (OtherFee f : fees) {
                if (f.getId() == id) {
                    fee = CalcUtil.divide(f.getNet(), personName.length).doubleValue();
                    feeDetail.append(f.getDiscountName()).append(":").append(fee).append(",");
                    break;
                }
            }
            for (String name : personName) {
                Person p = persons.get(name);
                if (null == p) {
                    p = new Person();
                    p.setId(id_generator++);
                    p.setName(name);
                    p.setFee(fee);
                    p.setDetail(feeDetail.toString());
                } else {
                    p.setFee(CalcUtil.add(p.getFee(), fee).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    p.setDetail(p.getDetail() + feeDetail.toString());
                }
                persons.put(name, p);
            }

        }


        StringBuffer stringBuffer = new StringBuffer("<table id='tab2' border='1'><tr><th>序号</th><th>姓名</th><th>费用</th><th>明细</th></tr>");
        Set<String> keys = persons.keySet();
        for (String p : keys) {
            stringBuffer.append("<tr><td>").append(persons.get(p).getId()).append("</td><td>").
                    append(persons.get(p).getName()).append("</td><td>")
                    .append(persons.get(p).getFee()).append("</td><td>")
                    .append(persons.get(p).getDetail()).append("</td></tr>");
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


