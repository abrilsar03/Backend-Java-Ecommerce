package com.ecommerce.api.services;

import com.ecommerce.api.dto.tokenization.TokenizeCardRequest;
import com.ecommerce.api.dto.tokenization.TokenizeCardResponse;
import org.springframework.beans.factory.annotation.Value;
import com.ecommerce.api.entities.*;
import com.ecommerce.api.enums.EventType;
import com.ecommerce.api.enums.SystemParamType;
import com.ecommerce.api.enums.EntityType;
import com.ecommerce.api.enums.TokenizationStatusType;
import com.ecommerce.api.exceptions.ExceptionFactory;
import com.ecommerce.api.repositories.*;
import com.ecommerce.api.utils.CardUtils;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;

@Service
public class TokenizationService {

    private final CardTokenRepository tokens;
    private final TokenizationRequestRepository requests;
    private final EventLogService eventLog;
    private final SystemParamService params;

    @Value("${app.tokenization.fingerprint-secret}")
    private final String fingerprintSecret;

    private final SecureRandom rnd = new SecureRandom();

    public TokenizationService(CardTokenRepository tokens, TokenizationRequestRepository requests,
            EventLogService eventLog, SystemParamService params) {
        this.tokens = tokens;
        this.requests = requests;
        this.eventLog = eventLog;
        this.params = params;
        this.fingerprintSecret = "";
    }

    public TokenizeCardResponse tokenize(ApiKeyEntity apiKey, TokenizeCardRequest req) {

        String pan = req.getPan().replaceAll("\\s+", "");

        if (!pan.matches("\\d{12,19}") || !CardUtils.luhnValid(pan)) {
            reject(apiKey, null, "INVALID_PAN");
        }

        if (req.getCvv() == null || !req.getCvv().matches("\\d{3,4}")) {
            reject(apiKey, null, "INVALID_CVV");
        }

        String fingerprint = CardUtils.hmacSha256Hex(fingerprintSecret,
                pan + "|" + req.getExpMonth() + "|" + req.getExpYear());


        double probabilityReject = params.getAsDouble(SystemParamType.prob_token_reject, 0.05);

        if (rnd.nextDouble() < probabilityReject) {
            reject(apiKey, fingerprint, "REJECTED_BY_PROBABILITY");
        }

        var existing = tokens.findByFingerprint(fingerprint).orElse(null);

        CardTokenEntity savedCard =
                existing != null ? existing : createNewToken(fingerprint, pan, req);

        var tokenizationRequest = new TokenizationRequestEntity(apiKey, fingerprint,
                TokenizationStatusType.ACCEPTED, null);

        requests.save(tokenizationRequest);

        eventLog.info(EventType.TOKEN_CREATED, EntityType.CARD_TOKEN, savedCard.getId(),
                Map.of("fingerprint", fingerprint.substring(0, 16) + "..."));

        return parserResponse(savedCard);
    }

    private CardTokenEntity createNewToken(String fingerprint, String pan,
            TokenizeCardRequest request) {

        String last4 = pan.substring(pan.length() - 4);
        Short expMonth = request.getExpMonth().shortValue();
        Short expYear = request.getExpYear().shortValue();

        var cardToken = new CardTokenEntity(generatePublicToken(), fingerprint, detectBrand(pan),
                last4, expMonth, expYear);

        return tokens.save(cardToken);
    }

    private void reject(ApiKeyEntity apiKey, String fingerprint, String reason) {
        var tokenizationRequest = new TokenizationRequestEntity(apiKey, fingerprint,
                TokenizationStatusType.REJECTED, reason);

        requests.save(tokenizationRequest);

        eventLog.warn(EventType.TOKEN_REJECTED, EntityType.CARD_TOKEN, null,
                Map.of("reason", reason));

        throw ExceptionFactory.tokenizationRejected();
    }

    private String detectBrand(String pan) {
        if (pan.startsWith("4"))
            return "VISA";
        if (pan.matches("^5[1-5].*"))
            return "MASTERCARD";
        if (pan.matches("^3[47].*"))
            return "AMEX";
        return "UNKNOWN";
    }

    private String generatePublicToken() {
        return "tok_" + Math.abs(rnd.nextLong());
    }

    public TokenizeCardResponse parserResponse(CardTokenEntity cardTokenEntity) {
        Integer expMonth = Integer.valueOf(cardTokenEntity.getExpMonth());
        Integer expYear = Integer.valueOf(cardTokenEntity.getExpMonth());

        var response = new TokenizeCardResponse(cardTokenEntity.getToken(),
                cardTokenEntity.getBrand(), cardTokenEntity.getLast4(), expMonth, expYear);

        return response;
    }
}
