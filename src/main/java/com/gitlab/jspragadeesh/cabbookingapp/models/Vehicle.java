package com.gitlab.jspragadeesh.cabbookingapp.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collection = "vehicles")
@Data
@NoArgsConstructor
public class Vehicle {
    @Id
    private String id;
    private String driverId;
    private String ownerId;
    private String type;
    private List<String> features;
    @JsonProperty(required = true)
    private String plateNumber;
    @JsonProperty(required = true)
    private String model;
    private String brand;
    private String status;


    public Vehicle(String driverId, String ownerId, String type, List<String> features, String plateNumber, String model, String brand, String status) {
        this.id = UUID.randomUUID().toString();
        this.driverId = driverId;
        this.ownerId = ownerId;
        this.type = type;
        this.features = features;
        this.plateNumber = plateNumber;
        this.model = model;
        this.brand = brand;
        this.status = status;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return plateNumber.equals(vehicle.plateNumber);
    }
}

