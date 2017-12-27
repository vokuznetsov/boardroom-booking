package com.smartbics.booking.controllers;

import com.smartbics.booking.dto.InputFormatDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
public class TestController {

    @GetMapping("/test")
    public String testMethod() {
        return "test";
    }

    @PostMapping("/input")
    public InputFormatDto input(@RequestBody InputFormatDto input) {
        return input;
    }
}
