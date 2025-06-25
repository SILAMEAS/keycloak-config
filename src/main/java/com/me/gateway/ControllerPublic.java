package com.me.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/public")
public class ControllerPublic {

    @GetMapping
    public String publicMethod() {
        return "publicMethod";
    }

    @GetMapping("/heath")
    public String heath() {
        return "heath";
    }

}
