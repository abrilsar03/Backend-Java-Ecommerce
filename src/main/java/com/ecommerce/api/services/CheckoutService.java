package com.ecommerce.api.services;

import com.ecommerce.api.entities.*;
import com.ecommerce.api.enums.*;
import com.ecommerce.api.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.security.SecureRandom;

@Service
public class CheckoutService {

    private final OrderRepository orders;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PaymentRepository payments;
    private final CardTokenRepository cardTokens;
    private final SystemParamService params;
    private final EventLogService eventLog;
    private final MailService mailer;

    private final SecureRandom rnd = new SecureRandom();

    public CheckoutService(OrderRepository orders, PaymentRepository payments,
            CardTokenRepository cardTokens, SystemParamService params, EventLogService eventLog,
            MailService mailer, UserRepository userRepository, CartRepository cartRepository) {
        this.orders = orders;
        this.payments = payments;
        this.cardTokens = cardTokens;
        this.params = params;
        this.eventLog = eventLog;
        this.mailer = mailer;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
    public OrderEntity placeOrder(UUID userId, String shippingAddress, String cardToken) {

        var user = userRepository.findById(userId).orElseThrow();

        CartEntity cart = cartRepository.findByUserAndStatusWithItems(userId, CartStatusType.ACTIVE)
                .orElseThrow(() -> new RuntimeException("active_cart_not_found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("cart_without_items");
        }

        var lines = cart
                .getItems().stream().map(i -> new Line(i.getProduct(),
                        i.getProduct().getPriceCents(), i.getQuantity(), java.math.BigDecimal.ZERO))
                .collect(Collectors.toList());

        Totals totals = computeTotals(lines);


        var order = new OrderEntity();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setStatus(OrderStatusType.PENDING_PAYMENT);
        order.setSubtotalCents(totals.subtotalCents());
        order.setTaxCents(totals.taxCents());
        order.setTotalCents(totals.totalCents());

        for (var ln : lines) {
            var it = new OrderItemEntity();
            it.setOrder(order);
            it.setProduct(ln.product());
            it.setNameSnapshot(ln.product().getTitle());
            it.setSkuSnapshot(ln.product().getSku());
            it.setPriceCents(ln.priceCents());
            it.setTaxPercent(ln.taxPercent()); //
            it.setQty(ln.qty());
            it.setLineTotalCents(ln.priceCents() * ln.qty());
            order.addItem(it);
        }

        order = orders.save(order);

        eventLog.info(EventType.ORDER_PLACED, EntityType.ORDER, order.getId(),
                Map.of("total", order.getTotalCents()));


        var payment = new PaymentEntity();
        payment.setOrder(order);
        payment.setPaymentType(PaymentType.CARD);
        payment.setStatus(PaymentStatusType.PENDING);
        var token = cardTokens.findByToken(cardToken).orElseThrow();
        payment.setCardToken(token);
        payment.setAttempts(0);
        payments.save(payment);



        int maxRetries = params.getAsInt(SystemParamType.payment_retry_max, 3);
        double pReject = params.getAsDouble(SystemParamType.prob_payment_reject, 0.15);

        boolean success = false;
        for (int i = 1; i <= maxRetries; i++) {
            boolean accepted = rnd.nextDouble() >= pReject;
            payment.setAttempts(i);
            if (accepted) {

                payment.setStatus(PaymentStatusType.SUCCESS);
                payments.save(payment);

                order.setStatus(OrderStatusType.PAID);
                orders.save(order);

                cart.setStatus(CartStatusType.CLOSED);
                cartRepository.save(cart);

                eventLog.info(EventType.PAYMENT_CONFIRMED, EntityType.PAYMENT, payment.getId(),
                        Map.of("attempt", i));
                mailer.paymentSuccess(user.getEmail(), order.getId().toString());
                success = true;
                break;
            } else {
                eventLog.warn(EventType.PAYMENT_ATTEMPT_FAILED, EntityType.PAYMENT, payment.getId(),
                        Map.of("attempt", i));
                payments.save(payment);
            }
        }

        if (!success) {
            payment.setStatus(PaymentStatusType.FAILED);
            payments.save(payment);

            order.setStatus(OrderStatusType.FAILED);
            orders.save(order);

            eventLog.error(EventType.PAYMENT_FAILED, EntityType.PAYMENT, payment.getId(),
                    Map.of("attempts", payment.getAttempts()));

            mailer.paymentFailed(user.getEmail(), order.getId().toString(), payment.getAttempts());
        }

        return order;
    }

    private Totals computeTotals(List<Line> lines) {
        int subtotal = 0;
        int tax = 0;
        for (var ln : lines) {
            if (ln.qty() <= 0)
                throw new RuntimeException("invalid_qty");
            if (ln.product() == null || Boolean.FALSE.equals(ln.product().getActive())) {
                throw new RuntimeException("product_inactive");
            }
            subtotal += ln.priceCents() * ln.qty();
        }
        return new Totals(subtotal, tax, subtotal + tax);
    }

    public record Line(ProductEntity product, int priceCents, int qty,
            java.math.BigDecimal taxPercent) {
    }
    public record Totals(int subtotalCents, int taxCents, int totalCents) {
    }


}
