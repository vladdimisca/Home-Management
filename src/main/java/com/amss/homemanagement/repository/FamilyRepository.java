package com.amss.homemanagement.repository;

import com.amss.homemanagement.model.Family;
import com.amss.homemanagement.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FamilyRepository extends JpaRepository<Family, UUID> {

    @Query("""
        SELECT f FROM Family f
        JOIN FamilyMember fm ON fm.family = f
        WHERE fm.member.id = :userId
        """)
    List<Family> findAllByUserId(@Param("userId") UUID userId);
}
