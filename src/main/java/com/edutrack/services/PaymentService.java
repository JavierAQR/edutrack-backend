package com.edutrack.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.edutrack.entities.Payment;
import com.edutrack.repositories.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Payment findById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment update(Long id, Payment payment) {
        Optional<Payment> existingPayment = paymentRepository.findById(id);
        if (existingPayment.isPresent()) {
            Payment p = existingPayment.get();
            p.setUserId(payment.getUserId());
            p.setAmount(payment.getAmount());
            p.setPaymentDate(payment.getPaymentDate());
            p.setStatus(payment.getStatus());
            return paymentRepository.save(p);
        }
        return null;
    }

    public void delete(Long id) {
        paymentRepository.deleteById(id);
    }
}
