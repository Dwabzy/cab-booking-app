package com.gitlab.jspragadeesh.cabbookingapp.models.requests;

import com.gitlab.jspragadeesh.cabbookingapp.models.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LocationsRequest {
    List<Location> locations;
}