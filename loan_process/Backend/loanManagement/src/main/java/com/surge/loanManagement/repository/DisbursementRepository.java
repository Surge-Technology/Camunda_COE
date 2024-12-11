package com.surge.loanManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.surge.loanManagement.model.DisbursementDetails;

@Repository
public interface DisbursementRepository extends JpaRepository<DisbursementDetails,Long>{

}
