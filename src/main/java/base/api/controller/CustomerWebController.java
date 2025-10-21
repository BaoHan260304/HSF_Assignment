package base.api.controller;

import base.api.model.CarRental;
import base.api.security.CustomUserDetails;
import base.api.service.CarRentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerWebController {

    private final CarRentalService carRentalService;

    @GetMapping("/dashboard")
    public String customerDashboard(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        model.addAttribute("username", currentUser.getUsername());
        return "customer/dashboard"; // Renders src/main/resources/templates/customer/dashboard.html
    }

    @GetMapping("/rental-history")
    public String rentalHistory(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        List<CarRental> rentalHistory = carRentalService.getRentalHistoryForCustomer(currentUser.getId());
        model.addAttribute("rentalHistory", rentalHistory);
        return "customer/rental-history"; // Renders src/main/resources/templates/customer/rental-history.html
    }
}