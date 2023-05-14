package com.chatapi.sigmaapi.controllers;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chatapi.sigmaapi.constant.RouteConstant;

@RestController
public class HomeController {
    @RequestMapping(value = RouteConstant.HEAD_END_POINT, method = RequestMethod.GET)
    public String welcome() throws IOException {
        return "Welcome to SigmaAPI";
    }
}
