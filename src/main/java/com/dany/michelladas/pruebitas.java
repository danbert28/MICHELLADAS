package com.dany.michelladas;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class pruebitas {
    public static void main(String[] args) {
        String encoded = new BCryptPasswordEncoder().encode("1234");
        System.out.println(encoded);
    }
}
