package com.gitlab.jspragadeesh.cabbookingapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Amount {
    private double rideFare;
    private double tip;
    private double total;
}
