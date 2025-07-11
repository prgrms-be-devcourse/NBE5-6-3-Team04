package com.grepp.nbe563team04.app.model.admin;

import com.grepp.nbe563team04.app.model.admin.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

}
