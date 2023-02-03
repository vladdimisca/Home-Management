package com.amss.homemanagement.repository;

import com.amss.homemanagement.model.Request;
import com.amss.homemanagement.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, UUID> {

    @Query("""
           SELECT request FROM Request request
           WHERE request.family.id = :familyId AND (:status IS NULL OR request.status = :status)
           """)
    List<Request> findByFamily_IdAndStatus(@Param("familyId") UUID familyId, @Param("status") RequestStatus status);

}
