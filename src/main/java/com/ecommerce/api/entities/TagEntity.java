package com.ecommerce.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tags")
public class TagEntity extends BasicEntity {
    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, unique = true)
    private String name;

    private String icon;

    @Size(max = 20)
    private String color;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}

