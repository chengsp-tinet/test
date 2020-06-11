package com.example.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author chengsp
 * @date 2019/11/19 15:10
 */
@Controller
public class TestController {
    @Autowired
    private ThreadPoolExecutor executor;
    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        return "this the value of the method return";
    }
}
