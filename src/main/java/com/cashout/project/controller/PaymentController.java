package com.cashout.project.controller;

import com.cashout.project.controller.dto.PaymentDto;
import com.cashout.project.exeptions.Error400Exception;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @PostMapping("/{userId}/{amount}")
    public Mono<PaymentDto> payment(@PathVariable long userId, @PathVariable double amount) {
        System.out.println("Payment request received for user: " + userId + " with amount: " + amount + " mod " + amount % 3);
        if (amount % 3 != 0) {
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setPaymentStatus("approved");
            return Mono.just(paymentDto);
        } else  {
            return Mono.error(new Error400Exception("Payment failed"));
        }
    }
}
