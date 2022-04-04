package com.gitlab.jspragadeesh.cabbookingapp.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddRoleRequest {
    private String userId;
    private String role;
}
