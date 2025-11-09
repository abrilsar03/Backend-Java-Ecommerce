package com.ecommerce.api.utils;

import org.springframework.web.context.request.*;

import java.util.UUID;

public final class RequestContext {
    private RequestContext() {}

    public static UUID currentRequestId() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes sra) {
            Object v = sra.getRequest().getAttribute("requestId");
            if (v instanceof UUID)
                return (UUID) v;
        }
        return null;
    }
}
