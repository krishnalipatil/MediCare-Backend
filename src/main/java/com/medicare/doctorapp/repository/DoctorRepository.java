package com.medicare.doctorapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicare.doctorapp.entity.Doctor;

public interface DoctorRepository extends  JpaRepository<Doctor,Long>{
	List<Doctor> findBySpecializationIgnoreCaseAndActiveTrue(String specialization);
	List<Doctor> findByLocationIgnoreCaseAndActiveTrue(String location);


}
