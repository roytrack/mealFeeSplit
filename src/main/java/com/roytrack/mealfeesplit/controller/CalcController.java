package com.roytrack.mealfeesplit.controller;

import com.roytrack.mealfeesplit.service.CalcService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by ruanchangming on 2015/7/1.
 */
@Controller
public class CalcController {


    @Resource
    CalcService calcService;

    @RequestMapping("/calc")
    @ResponseBody
    public String calcFee(@RequestParam String origin,HttpServletResponse response) throws IOException {
         return calcService.calc(origin,response);
    }

    @RequestMapping("/split")
    @ResponseBody
    public String splitPerson(@RequestParam String personInfo,HttpServletRequest request){
        return calcService.splitPerson(personInfo,request);
    }

    @RequestMapping("/index")
    public String index(){
        return "index";
    }


}
