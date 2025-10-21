package base.api.controller;

import base.api.dto.request.CarRentalRequest;
import base.api.dto.response.CarRentalResponse;
import base.api.model.CarRental;
import base.api.security.CustomUserDetails;
import base.api.service.CarRentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rentals")
public class CarRentalController {

    private final CarRentalService carRentalService;

    // Helper to convert CarRental entity to CarRentalResponse DTO
    private CarRentalResponse convertToDto(CarRental rental) {
        CarRentalResponse dto = new CarRentalResponse();
        dto.setCustomerId(rental.getCustomer().getId());
        dto.setCustomerName(rental.getCustomer().getCustomerName());
        dto.setCarId(rental.getCar().getId());
        dto.setCarName(rental.getCar().getCarName());
        dto.setPickupDate(rental.getPickupDate());
        dto.setReturnDate(rental.getReturnDate());
        dto.setRentPrice(rental.getRentPrice());
        dto.setStatus(rental.getStatus());
        return dto;
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')") // Only CUSTOMER can create rentals
    public ResponseEntity<CarRentalResponse> createRental(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestBody CarRentalRequest request) {
        try {
            CarRental newRental = carRentalService.createRental(
                    currentUser.getId(), // Get customer ID from authenticated user
                    request.getCarId(),
                    request.getPickupDate(),
                    request.getReturnDate()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(newRental));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Or a more specific error DTO
        }
    }

    @GetMapping("/my-history")
    @PreAuthorize("hasRole('CUSTOMER')") // Only CUSTOMER can view their own history
    public ResponseEntity<List<CarRentalResponse>> getMyRentalHistory(@AuthenticationPrincipal CustomUserDetails currentUser) {
        List<CarRentalResponse> history = carRentalService.getRentalHistoryForCustomer(currentUser.getId()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(history);
    }

    @GetMapping("/report")
    @PreAuthorize("hasRole('ADMIN')") // Only ADMIN can generate reports
    public ResponseEntity<List<CarRentalResponse>> getRentalReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<CarRentalResponse> report = carRentalService.getRentalReport(startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(report);
    }
}