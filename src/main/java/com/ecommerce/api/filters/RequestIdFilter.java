package com.ecommerce.api.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestIdFilter implements Filter {
    public static final String REQ_ID_ATTR = "requestId";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        try {
            UUID requestId = UUID.randomUUID();
            req.setAttribute(REQ_ID_ATTR, requestId);
            MDC.put("requestId", requestId.toString());
            chain.doFilter(req, res);
        } finally {
            MDC.remove("requestId");
        }
    }
}
