package com.edutrack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.edutrack.entities.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
