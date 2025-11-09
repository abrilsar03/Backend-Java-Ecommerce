package com.ecommerce.api.services;

import java.util.UUID;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ecommerce.api.entities.SearchLogEntity;
import com.ecommerce.api.repositories.SearchLogRepository;


@Service
public class SearchLogService {
    private final SearchLogRepository repo;

    public SearchLogService(SearchLogRepository repo) {
        this.repo = repo;
    }

    @Async("loggingExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAsync(UUID userId, String endpoint, String query, String ip, String userAgent) {
        try {
            var e = new SearchLogEntity();
            e.setUserId(userId);
            e.setEndpoint(endpoint);
            e.setQuery(query);
            e.setIp(ip);
            e.setUserAgent(userAgent);
            repo.save(e);
        } catch (Exception ex) {
        }
    }
}
