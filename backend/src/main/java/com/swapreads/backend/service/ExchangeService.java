package com.swapreads.backend.service;

import com.swapreads.backend.dto.ExchangeRequestDTO;
import com.swapreads.backend.dto.ExchangeResponseDTO;
import com.swapreads.backend.entity.Book;
import com.swapreads.backend.entity.ExchangeRequest;
import com.swapreads.backend.entity.User;
import com.swapreads.backend.repository.BookRepository;
import com.swapreads.backend.repository.ExchangeRepository;
import com.swapreads.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExchangeService {
    private final ExchangeRepository exchangeRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public ExchangeResponseDTO createRequest(
            ExchangeRequestDTO dto, String requesterEmail) {

        User requester = getUserByEmail(requesterEmail);

        Book requestedBook = bookRepository
                .findById(dto.getRequestedBookId())
                .orElseThrow(() ->
                        new RuntimeException("Requested book not found"));

        if (requestedBook.getOwner().getEmail()
                .equals(requesterEmail)) {
            throw new RuntimeException(
                    "You cannot request your own book");
        }

        boolean alreadyRequested = exchangeRepository
                .existsByRequesterAndRequestedBookAndStatus(
                        requester, requestedBook,
                        ExchangeRequest.ExchangeStatus.PENDING);
        if (alreadyRequested) {
            throw new RuntimeException(
                    "You already have a pending request for this book");
        }

        ExchangeRequest request = new ExchangeRequest();
        request.setRequester(requester);
        request.setOwner(requestedBook.getOwner());
        request.setRequestedBook(requestedBook);
        request.setMessage(dto.getMessage());

        if (dto.getOfferedBookId() != null) {
            Book offeredBook = bookRepository
                    .findById(dto.getOfferedBookId())
                    .orElseThrow(() ->
                            new RuntimeException("Offered book not found"));

            if (!offeredBook.getOwner().getEmail()
                    .equals(requesterEmail)) {
                throw new RuntimeException(
                        "You can only offer your own books");
            }
            request.setOfferedBook(offeredBook);
        }
        return ExchangeResponseDTO.fromEntity(
                exchangeRepository.save(request));
    }

    @Transactional
    public ExchangeResponseDTO respondToRequest(
            Long requestId, String action,
            String responseMessage, String ownerEmail) {

        ExchangeRequest request = exchangeRepository
                .findById(requestId)
                .orElseThrow(() ->
                        new RuntimeException("Exchange request not found"));
        if (!request.getOwner().getEmail().equals(ownerEmail)) {
            throw new RuntimeException(
                    "Only the book owner can respond to this request");
        }

        if (request.getStatus() !=
                ExchangeRequest.ExchangeStatus.PENDING) {
            throw new RuntimeException(
                    "This request has already been responded to");
        }
        request.setResponseMessage(responseMessage);
        request.setRespondedAt(LocalDateTime.now());

        if (action.equalsIgnoreCase("accept")) {
            request.setStatus(ExchangeRequest.ExchangeStatus.ACCEPTED);
            completeExchange(request);
        } else if (action.equalsIgnoreCase("reject")) {
            request.setStatus(ExchangeRequest.ExchangeStatus.REJECTED);
        } else {
            throw new RuntimeException(
                    "Invalid action. Use 'accept' or 'reject'");
        }

        return ExchangeResponseDTO.fromEntity(
                exchangeRepository.save(request));
    }

    private void completeExchange(ExchangeRequest request) {
        Book requestedBook = request.getRequestedBook();
        User requester = request.getRequester();
        User owner = request.getOwner();

        requestedBook.setOwner(requester);
        requestedBook.setStatus(Book.BookStatus.EXCHANGED);
        bookRepository.save(requestedBook);

        if (request.getOfferedBook() != null) {
            Book offeredBook = request.getOfferedBook();
            offeredBook.setOwner(owner);
            offeredBook.setStatus(Book.BookStatus.EXCHANGED);
            bookRepository.save(offeredBook);
        } else {
            requester.setCreditPoints(
                    requester.getCreditPoints() - 10);
            owner.setCreditPoints(
                    owner.getCreditPoints() + 10);
            userRepository.save(requester);
            userRepository.save(owner);
        }

        request.setStatus(
                ExchangeRequest.ExchangeStatus.COMPLETED);
    }

    public ExchangeResponseDTO cancelRequest(
            Long requestId, String requesterEmail) {

        ExchangeRequest request = exchangeRepository
                .findById(requestId)
                .orElseThrow(() ->
                        new RuntimeException("Exchange request not found"));

        if (!request.getRequester().getEmail()
                .equals(requesterEmail)) {
            throw new RuntimeException(
                    "Only the requester can cancel this request");
        }

        if (request.getStatus() !=
                ExchangeRequest.ExchangeStatus.PENDING) {
            throw new RuntimeException(
                    "Only pending requests can be cancelled");
        }

        request.setStatus(ExchangeRequest.ExchangeStatus.CANCELLED);
        return ExchangeResponseDTO.fromEntity(
                exchangeRepository.save(request));
    }
    public List<ExchangeResponseDTO> getMyRequests(
            String email) {
        User user = getUserByEmail(email);
        return exchangeRepository
                .findByRequesterOrOwner(user, user)
                .stream()
                .map(ExchangeResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
    public List<ExchangeResponseDTO> getIncomingRequests(
            String email) {
        User user = getUserByEmail(email);
        return exchangeRepository
                .findByOwner(user)
                .stream()
                .map(ExchangeResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }
}
