package com.gitlab.jspragadeesh.cabbookingapp.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;


@Data
@NoArgsConstructor
public class Coordinates {
    private Address formattedAddress;
    private Double latitude;
    private Double longitude;

    public Coordinates(Address address) {
        // TODO: Call GeoCode API to get the coordinates

        this.formattedAddress = address;

        // Generating Random Coordinates based on address
        this.latitude = Math.random() * 100;
        this.longitude = Math.random() * 100;
    }

    public Coordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Find K nearest nodes
    public List<Node> findNearestNodes(List<Node> nodes, int k) {
        // Sort the nodes based on distance from the given coordinates
        nodes.sort(Comparator.comparing(node -> this.getSquaredDistance(node.getCoordinates())));
        return nodes.subList(0, k);
    }

    public Double getSquaredDistance(Coordinates other) {
        return Math.pow(this.latitude - other.latitude, 2) + Math.pow(this.longitude - other.longitude, 2);
    }

    public Double getDistance(Coordinates other) {
        // Find distance between two coordinates
        return Math.sqrt(Math.pow(this.latitude - other.latitude, 2) + Math.pow(this.longitude - other.longitude, 2));
    }

    public String toString() {
        return "Coordinates: " + this.latitude + " " + this.longitude;
    }
}

