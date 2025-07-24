package com.edutrack.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutrack.entities.PagoStudent;
import com.edutrack.services.PagoStudentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoStudentController {

    private final PagoStudentService service;

    @PostMapping
    public PagoStudent registrarPago(@RequestBody PagoStudent pago) {
        return service.registrarPago(pago);
    }

    @GetMapping("/verificar-matricula/{studentId}")
    public boolean verificarPagoMatricula(@PathVariable Long studentId) {
        return service.verificarPagoMatricula(studentId);
    }

    @GetMapping("/student/{studentId}")
    public List<PagoStudent> getPagosByStudent(@PathVariable Long studentId) {
        return service.getPagosByStudent(studentId);
    }

}
