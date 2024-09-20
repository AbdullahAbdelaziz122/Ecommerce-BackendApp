package com.BMS.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class securityController {

    @GetMapping("/secure")
    public String secure(){
        return "This is Secure";
    }
}
