package com.swapreads.backend.repository;

import com.swapreads.backend.entity.ExchangeRequest;
import com.swapreads.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExchangeRepository extends JpaRepository<ExchangeRequest, Long>{
    List<ExchangeRequest> findByRequester(User requester);

    List<ExchangeRequest> findByOwner(User owner);

    List<ExchangeRequest> findByRequesterOrOwner(
            User requester, User owner);

    boolean existsByRequesterAndRequestedBookAndStatus(
            User requester,
            com.swapreads.backend.entity.Book requestedBook,
            ExchangeRequest.ExchangeStatus status);
}
