package com.gitlab.jspragadeesh.cabbookingapp.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponse<T> {
    private String message;
    private T data;

    public GenericResponse(String message) {
        this.message = message;
        this.data = null;
    }

    public GenericResponse(T data) {
        this.message = "Success";
        this.data = data;
    }
}
