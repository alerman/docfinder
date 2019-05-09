package com.ai.docfinder.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/search")
public class SearchController {

    @RequestMapping("/test")
    @ResponseBody
    public String testEndpoint(){
            return "Eureka!";
    }
}
