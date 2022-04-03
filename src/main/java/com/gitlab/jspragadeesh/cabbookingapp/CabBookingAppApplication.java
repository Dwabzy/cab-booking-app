package com.gitlab.jspragadeesh.cabbookingapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CabBookingAppApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CabBookingAppApplication.class, args);
    }

    @Override
    public void run(String... args){
        System.out.println("Cab Booking Application Started");
    }
}
