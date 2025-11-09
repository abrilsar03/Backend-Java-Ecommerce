package com.ecommerce.api.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ecommerce.api.entities.CartEntity;
import com.ecommerce.api.enums.CartStatusType;
import java.util.UUID;

public interface CartRepository extends JpaRepository<CartEntity, UUID> {



        @Query("""
                                   select cart from CartEntity cart
                                   left join fetch cart.items i
                                   where c.user = :user and c.status = :status
                        """)

        CartEntity findByUserIdWithItems(@Param("userId") UUID userId);

        CartEntity findByUserAndStatusWithItems(@Param("user") UUID user,
                        @Param("status") CartStatusType status);
}
