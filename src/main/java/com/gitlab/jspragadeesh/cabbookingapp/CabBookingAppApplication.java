package com.gitlab.jspragadeesh.cabbookingapp;

import com.gitlab.jspragadeesh.cabbookingapp.models.Coordinates;
import com.gitlab.jspragadeesh.cabbookingapp.models.Node;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.NodeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CabBookingAppApplication implements CommandLineRunner {

    private final NodeRepository nodeRepository;

    public CabBookingAppApplication(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(CabBookingAppApplication.class, args);
    }

    @Override
    public void run(String... args){
        System.out.println("Cab Booking Application Started");
        nodeRepository.deleteAll();
        // Create 10 nodes distributed equally in a 100x100 grid

        Node node = new Node(new Coordinates(10.0, 20.0));
        nodeRepository.save(node);
        node = new Node(new Coordinates(40.0, 30.0));
        nodeRepository.save(node);
        node = new Node(new Coordinates(15.0, 70.0));
        nodeRepository.save(node);
        node = new Node(new Coordinates(55.0, 10.0));
        nodeRepository.save(node);
        node = new Node(new Coordinates(75.0, 80.0));
        nodeRepository.save(node);
        node = new Node(new Coordinates(95.0, 30.0));
        nodeRepository.save(node);
        node = new Node(new Coordinates(50.0, 90.0));
        nodeRepository.save(node);
        node = new Node(new Coordinates(45.0, 65.0));
        nodeRepository.save(node);
        node = new Node(new Coordinates(70.0, 45.0));
        nodeRepository.save(node);
        node = new Node(new Coordinates(25.0, 95.0));
        nodeRepository.save(node);
        node = new Node(new Coordinates(90.0, 65.0));
        nodeRepository.save(node);
    }
}
