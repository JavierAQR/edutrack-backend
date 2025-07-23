package com.edutrack.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "intitution_academic_levels")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionAcademicLevels {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "institution_id")
    @JsonIgnoreProperties("academicLevels")
    private Institution institution;

    @ManyToOne
    @JoinColumn(name = "academic_level_id")
    @JsonIgnoreProperties("institutionAcademicLevels")
    private AcademicLevel academicLevel;
}
