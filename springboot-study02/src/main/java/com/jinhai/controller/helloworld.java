package com.jinhai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class helloworld {

    @RequestMapping("/hello")
    public String hello() {
        return "hello world";
    }
}
