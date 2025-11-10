package com.ecommerce.api.repositories;

import com.ecommerce.api.entities.SystemParamEntity;
import com.ecommerce.api.enums.SystemParamType;
import java.util.Optional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;


public interface SystemParamRepository extends JpaRepository<SystemParamEntity, SystemParamType> {

    Optional<SystemParamEntity> findByKey(SystemParamType key);

    @Query(value = """
              SELECT * FROM system_params
              WHERE (:query IS NULL
                     OR LOWER(CAST(key AS TEXT)) LIKE LOWER(CONCAT('%', :query, '%'))
                     OR LOWER(CAST(value AS TEXT)) LIKE LOWER(CONCAT('%', :query, '%')))
            """, nativeQuery = true)
    Page<SystemParamEntity> search(@Param("query") String query, Pageable pageable);
}
