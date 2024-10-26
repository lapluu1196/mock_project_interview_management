package com.team3.repositories;

import com.team3.entities.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    @Query("SELECT o FROM Offer o " +
            "WHERE (:search IS NULL OR LOWER(o.candidate.fullName) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:department IS NULL OR LOWER(o.department) = LOWER(:department)) " +
            "AND (:status IS NULL OR LOWER(o.offerStatus) = LOWER(:status)) " +
            "ORDER BY o.candidate.fullName ASC")
    Page<Offer> searchOffers(@Param("search") String search,
                             @Param("department") String department,
                             @Param("status") String status,
                             Pageable pageable);

    @Query("SELECT o FROM Offer o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<Offer> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                @Param("endDate") LocalDateTime endDate);
}
