package com.archita.skillswaphub.repository;

import com.archita.skillswaphub.model.SwapRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwapRequestRepository extends JpaRepository<SwapRequest, Long> {

    // Requests received by a user (inbox)
    List<SwapRequest> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);

    // Requests sent by a user (outbox)
    List<SwapRequest> findBySenderIdOrderByCreatedAtDesc(Long senderId);

    // Check if a request already exists between two users
    boolean existsBySenderIdAndReceiverIdAndStatus(Long senderId, Long receiverId, String status);

    // Count pending requests for a user
    long countByReceiverIdAndStatus(Long receiverId, String status);
}