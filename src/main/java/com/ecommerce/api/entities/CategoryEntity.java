package com.ecommerce.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "categories")
public class CategoryEntity extends BasicEntity {

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, unique = true)
    private String name;

    @Size(max = 4000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private CategoryEntity parent;


    private String icon;

    private String imageUrl;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryEntity getParent() {
        return parent;
    }

    public void setParent(CategoryEntity parent) {
        this.parent = parent;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

