package com.edutrack.repositories;

import com.edutrack.entities.PagoStudent;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoStudentRepository extends JpaRepository<PagoStudent, Long> {

    boolean existsByStudentIdAndPrecioInstitution_TipoAndEstadoPago(Long studentId, String tipo, String estadoPago);

    List<PagoStudent> findByStudentId(Long studentId);

}
