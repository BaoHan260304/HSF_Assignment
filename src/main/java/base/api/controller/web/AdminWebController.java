package base.api.controller.web;

import base.api.model.Car;
import base.api.model.CarProducer;
import base.api.model.CarRental;
import base.api.model.Customer;
import base.api.service.CarRentalService;
import base.api.service.CarService;
import base.api.service.CustomerService;
import base.api.repository.CarProducerRepository; // Assuming direct repository access for simplicity in web controller
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminWebController {

    private final CustomerService customerService;
    private final CarService carService;
    private final CarProducerRepository carProducerRepository;
    private final CarRentalService carRentalService;
    // trang chủ cho admin
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("title", "Admin Dashboard");
        return "admin/dashboard"; // Renders src/main/resources/templates/admin/dashboard.html
    }
    // trang quản lý khách hàng
    @GetMapping("/customers")
    public String manageCustomers(Model model) {
        List<Customer> customers = customerService.findAllCustomers();
        model.addAttribute("customers", customers);
        return "admin/customers"; // Renders src/main/resources/templates/admin/customers.html
    }

    @GetMapping("/customers/edit/{id}")
    public String showUpdateCustomerForm(@PathVariable("id") Long id, Model model) {
        Customer customer = customerService.findCustomerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        model.addAttribute("customer", customer);
        return "admin/customer-edit"; // Renders src/main/resources/templates/admin/customer-edit.html
    }

    @PostMapping("/customers/update/{id}")
    public String updateCustomer(@PathVariable("id") Long id, @ModelAttribute("customer") Customer customer) {
        // Chỉ cập nhật các trường cho phép, không cập nhật toàn bộ đối tượng để tránh lỗi bảo mật
        customerService.updateCustomer(id, customer);
        return "redirect:/admin/customers";
    }

    @GetMapping("/customers/inactive/{id}")
    public String deleteCustomer(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.inActiveCustomer(id);
            redirectAttributes.addFlashAttribute("successMessage", "Customer has been marked as inactive.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Could not find customer to inactive.");
        }
        return "redirect:/admin/customers"; // Luôn chuyển hướng về trang danh sách
    }

    // trang quản lý xe
    @GetMapping("/cars")
    public String manageCars(Model model) {
        List<Car> cars = carService.findAllCars();
        model.addAttribute("cars", cars);
        return "admin/cars"; // Renders src/main/resources/templates/admin/cars.html
    }

    @GetMapping("/cars/add")
    public String showAddCarForm(Model model) {
        model.addAttribute("car", new Car());
        // Lấy danh sách nhà sản xuất để hiển thị trong dropdown
        List<CarProducer> producers = carProducerRepository.findAll();
        model.addAttribute("producers", producers);
        return "admin/car-add";
    }

    @PostMapping("/cars/add")
    public String addNewCar(@ModelAttribute("car") Car car, RedirectAttributes redirectAttributes) {
        carService.saveCar(car);
        redirectAttributes.addFlashAttribute("successMessage", "New car added successfully!");
        return "redirect:/admin/cars";
    }

    @GetMapping("/cars/edit/{id}")
    public String showEditCarForm(@PathVariable("id") Long id, Model model) {
        Car car = carService.findCarById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid car Id:" + id));
        model.addAttribute("car", car);
        List<CarProducer> producers = carProducerRepository.findAll(); // For dropdown
        model.addAttribute("producers", producers);
        return "admin/car-edit";
    }

    @PostMapping("/cars/update/{id}")
    public String updateCar(@PathVariable("id") Long id, @ModelAttribute("car") Car car, RedirectAttributes redirectAttributes) {
        // Ensure the ID from path variable is used for the entity
        car.setId(id);
        carService.saveCar(car); // saveCar handles both add and update
        redirectAttributes.addFlashAttribute("successMessage", "Car updated successfully!");
        return "redirect:/admin/cars";
    }

    @GetMapping("/cars/delete/{id}")
    public String deleteCar(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            carService.deleteCar(id); // This method already handles soft delete / status change
            redirectAttributes.addFlashAttribute("successMessage", "Car status updated/deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting car: " + e.getMessage());
        }
        return "redirect:/admin/cars";
    }

    // trang quản lý thuê xe
    @GetMapping("/rentals")
    public String manageRentals(Model model) {
        List<CarRental> rentals = carRentalService.findAllRentals();
        model.addAttribute("rentals", rentals);
        return "admin/manage-rentals";
    }

    @GetMapping("/rentals/confirm/{customerId}/{carId}")
    public String confirmRental(@PathVariable Long customerId, @PathVariable Long carId, RedirectAttributes redirectAttributes) {
        carRentalService.confirmRental(customerId, carId);
        redirectAttributes.addFlashAttribute("successMessage", "Rental has been confirmed.");
        return "redirect:/admin/rentals";
    }

    @GetMapping("/rentals/cancel/{customerId}/{carId}")
    public String cancelRental(@PathVariable Long customerId, @PathVariable Long carId, RedirectAttributes redirectAttributes) {
        carRentalService.cancelRental(customerId, carId);
        redirectAttributes.addFlashAttribute("successMessage", "Rental has been cancelled.");
        return "redirect:/admin/rentals";
    }

    // trang quản lý nhà sản xuất
    @GetMapping("/producers")
    public String manageProducers(Model model) {
        List<CarProducer> producers = carProducerRepository.findAll();
        model.addAttribute("producers", producers);
        return "admin/producers"; // Renders src/main/resources/templates/admin/producers.html
    }

    @GetMapping("/producers/add")
    public String showAddProducerForm(Model model) {
        model.addAttribute("producer", new CarProducer());
        return "admin/producer-add";
    }

    @PostMapping("/producers/add")
    public String addNewProducer(@ModelAttribute("producer") CarProducer producer, RedirectAttributes redirectAttributes) {
        carProducerRepository.save(producer);
        redirectAttributes.addFlashAttribute("successMessage", "New producer added successfully!");
        return "redirect:/admin/producers";
    }

    @GetMapping("/producers/edit/{id}")
    public String showEditProducerForm(@PathVariable("id") Long id, Model model) {
        CarProducer producer = carProducerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid producer Id:" + id));
        model.addAttribute("producer", producer);
        return "admin/producer-edit";
    }

    @PostMapping("/producers/update/{id}")
    public String updateProducer(@PathVariable("id") Long id, @ModelAttribute("producer") CarProducer producer, RedirectAttributes redirectAttributes) {
        // Ensure the ID from path variable is used for the entity
        producer.setId(id);
        carProducerRepository.save(producer); // save method handles both insert and update
        redirectAttributes.addFlashAttribute("successMessage", "Producer updated successfully!");
        return "redirect:/admin/producers";
    }

    @GetMapping("/producers/delete/{id}")
    public String deleteProducer(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            carProducerRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Producer deleted successfully!");
        } catch (Exception e) { // Catch DataIntegrityViolationException if producer is linked to cars
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete producer: " + e.getMessage());
        }
        return "redirect:/admin/producers";
    }

    // trang báo cáo
    @GetMapping("/reports")
    public String viewRentalReports(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        if (startDate != null && endDate != null) {
            List<CarRental> rentals = carRentalService.getRentalReport(startDate, endDate);
            model.addAttribute("rentals", rentals);
            model.addAttribute("startDate", startDate.toString());
            model.addAttribute("endDate", endDate.toString());
        }
        return "admin/reports"; // Renders src/main/resources/templates/admin/reports.html
    }
}