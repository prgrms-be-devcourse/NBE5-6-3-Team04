package com.grepp.nbe563team04.app.model.interest;

import com.grepp.nbe563team04.app.model.interest.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

}
