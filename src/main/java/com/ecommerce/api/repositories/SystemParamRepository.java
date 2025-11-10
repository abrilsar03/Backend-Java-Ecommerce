package com.ecommerce.api.repositories;

import com.ecommerce.api.entities.SystemParamEntity;
import com.ecommerce.api.enums.SystemParamType;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;


public interface SystemParamRepository extends JpaRepository<SystemParamEntity, SystemParamType> {

    @Query("""
              select param from SystemParamEntity param
              where (:query is null
                     or lower(cast(param.key as string)) like lower(concat('%', :query, '%'))
                     or lower(param.value) like lower(concat('%', :query, '%')))
            """)
    Page<SystemParamEntity> search(@Param("query") String query, Pageable pageable);
}
