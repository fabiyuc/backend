package com.guardias.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

}
