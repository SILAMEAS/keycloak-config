package com.me.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/public")
public class Controller {

    @GetMapping
    public String publicMethod() {
        return "publicMethod";
    }

    @GetMapping("/heath")
    public String heath() {
        return "heath";
    }

}
