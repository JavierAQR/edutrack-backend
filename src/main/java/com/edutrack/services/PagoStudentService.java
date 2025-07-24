package com.edutrack.services;

import org.springframework.stereotype.Service;

import com.edutrack.entities.PagoStudent;
import com.edutrack.repositories.PagoStudentRepository;

import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoStudentService {

    private final PagoStudentRepository pagoRepo;

    public boolean verificarPagoMatricula(Long studentId) {
        return pagoRepo.existsByStudentIdAndPrecioInstitution_TipoAndEstadoPago(
                studentId, "matricula", "pagado");
    }

    public PagoStudent registrarPago(PagoStudent pago) {
        return pagoRepo.save(pago);
    }

    public List<PagoStudent> getPagosByStudent(Long studentId) {
        return pagoRepo.findByStudentId(studentId);
    }

}
