package com.Javeriana.zoo_fantastico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/")
    public String dashboard() { return "forward:/index.html"; }

    @GetMapping("/zona")
    public String zonas() { return "forward:/zona.html"; }

    @GetMapping("/criatura")
    public String criaturas() { return "forward:/criatura.html"; }
}
