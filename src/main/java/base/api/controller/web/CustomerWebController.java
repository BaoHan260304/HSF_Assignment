package base.api.controller.web;

import base.api.model.Car;
import base.api.model.CarRental;
import base.api.security.CustomUserDetails;
import base.api.service.CarRentalService;
import base.api.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;


import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerWebController {

    private final CarRentalService carRentalService;
    private final CarService carService;

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

    @GetMapping("/browse-cars")
    public String browseAvailableCars(Model model) {
        List<Car> availableCars = carService.findAvailableCars();
        model.addAttribute("cars", availableCars);
        return "customer/browse-cars";
    }

    @GetMapping("/book-car/{id}")
    public String showBookCarForm(@PathVariable("id") Long carId, Model model) {
        Car car = carService.findCarById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid car Id:" + carId));
        model.addAttribute("car", car);
        return "customer/book-car";
    }

    @PostMapping("/book-car")
    public String bookCar(@RequestParam("carId") Long carId,
                          @RequestParam("pickupDate") LocalDate pickupDate,
                          @RequestParam("returnDate") LocalDate returnDate,
                          @AuthenticationPrincipal CustomUserDetails currentUser,
                          RedirectAttributes redirectAttributes) {
        carRentalService.createRental(currentUser.getId(), carId, pickupDate, returnDate);
        redirectAttributes.addFlashAttribute("successMessage", "Your booking request has been sent! Please wait for admin confirmation.");
        return "redirect:/customer/rental-history";
    }

}