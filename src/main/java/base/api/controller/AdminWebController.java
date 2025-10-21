package base.api.controller;

import base.api.model.Car;
import base.api.model.CarProducer;
import base.api.model.Customer;
import base.api.service.CarService;
import base.api.service.CustomerService;
import base.api.repository.CarProducerRepository; // Assuming direct repository access for simplicity in web controller
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminWebController {

    private final CustomerService customerService;
    private final CarService carService;
    private final CarProducerRepository carProducerRepository;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("title", "Admin Dashboard");
        return "admin/dashboard"; // Renders src/main/resources/templates/admin/dashboard.html
    }

    @GetMapping("/customers")
    public String manageCustomers(Model model) {
        List<Customer> customers = customerService.findAllCustomers();
        model.addAttribute("customers", customers);
        return "admin/customers"; // Renders src/main/resources/templates/admin/customers.html
    }

    @GetMapping("/cars")
    public String manageCars(Model model) {
        List<Car> cars = carService.findAllCars();
        model.addAttribute("cars", cars);
        return "admin/cars"; // Renders src/main/resources/templates/admin/cars.html
    }

    @GetMapping("/producers")
    public String manageProducers(Model model) {
        List<CarProducer> producers = carProducerRepository.findAll();
        model.addAttribute("producers", producers);
        return "admin/producers"; // Renders src/main/resources/templates/admin/producers.html
    }
}