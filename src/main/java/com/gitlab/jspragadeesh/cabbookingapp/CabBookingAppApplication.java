package com.gitlab.jspragadeesh.cabbookingapp;

import com.gitlab.jspragadeesh.cabbookingapp.models.Person;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;

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
