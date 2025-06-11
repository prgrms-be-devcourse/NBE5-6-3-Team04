package com.grepp.nbe562team04.app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/serviceInfo")
    public String serviceInfo() {
        return "serviceInfo";
    }

}
