package com.human.tools.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shaonan.hu
 * @version V1.0
 * @Time 2019/4/9
 */
@RestController
public class IndexController {

    @RequestMapping("/index")
    public String index() {
        return "index.jsp";
    }
}
