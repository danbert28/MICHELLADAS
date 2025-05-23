package com.dany.michelladas.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

    @Controller
    public class AuthPageController {

        @GetMapping("/login")
        public String mostrarLogin(@RequestParam(value = "error", required = false) String error,
                                   Model model) {
            if (error != null) {
                model.addAttribute("loginError", "Correo o contraseña incorrectos");
            }
            return "login"; // se refiere a login.html en /templates
        }

        @GetMapping("/register")
        public String mostrarRegistro() {
            return "register"; // se refiere a register.html en /templates
        }
    }


