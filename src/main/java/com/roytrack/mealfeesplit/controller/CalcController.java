package com.roytrack.mealfeesplit.controller;

import com.roytrack.mealfeesplit.service.CalcService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by ruanchangming on 2015/7/1.
 */
@Controller
public class CalcController {


    @Resource
    CalcService calcService;

    @RequestMapping("/calc")
    @ResponseBody
    public String calcFee(@RequestParam String origin){
         return calcService.calc(origin);
    }

    @RequestMapping("/index")
    public String index(){
        return "index";
    }


}
