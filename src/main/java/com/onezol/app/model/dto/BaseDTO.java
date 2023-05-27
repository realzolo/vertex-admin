package com.onezol.app.model.dto;

import java.io.Serializable;

public class BaseDTO implements Serializable {
    private Long id;

    public BaseDTO() {
    }

    public BaseDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
