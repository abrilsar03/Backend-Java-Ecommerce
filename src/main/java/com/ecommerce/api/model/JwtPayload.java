package com.ecommerce.api.model;



import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public abstract class JwtPayload {
    UUID id;

    public abstract Map<String, Object> toMap();

    public abstract void fromMap(Map<String, Object> map);
}
