package com.s4.demo.repository;

import com.s4.demo.domain.Clazz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClazzRepository extends JpaRepository<Clazz, Long>, JpaSpecificationExecutor {
    Clazz findByCode(String code);
}
