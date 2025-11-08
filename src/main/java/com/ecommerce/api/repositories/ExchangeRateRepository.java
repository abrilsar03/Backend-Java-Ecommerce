package com.ecommerce.api.repositories;


import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import com.ecommerce.api.entities.CountryEntity;
import com.ecommerce.api.entities.CurrencyEntity;
import com.ecommerce.api.entities.ExchangeRateEntity;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRateEntity, UUID> {

  @Query("""
        SELECT exchange FROM ExchangeRateEntity exchange
        WHERE exchange.baseCurrency = :base
          AND exchange.quoteCurrency = :quote
          AND ( :country IS NULL OR exchange.country = :country )
          AND (exchange.validTo IS NULL OR exchange.validTo > :at)
          AND exchange.validFrom <= :at
          AND exchange.source = :source
        ORDER BY exchange.validFrom DESC
      """)

  Optional<ExchangeRateEntity> findActiveRate(@Param("base") CurrencyEntity base,
      @Param("quote") CurrencyEntity quote, @Param("country") CountryEntity countryOrNull,
      @Param("source") String source, @Param("at") OffsetDateTime at);
}
