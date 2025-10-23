package base.api.controller;

import base.api.dto.response.CustomerResponse;
import base.api.model.Customer;
import base.api.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/customers")
@PreAuthorize("hasRole('ADMIN')") // Only ADMIN can manage customers
public class CustomerController {

    private final CustomerService customerService;

    // Helper to convert Customer entity to CustomerResponse DTO
    private CustomerResponse convertToDto(Customer customer) {
        CustomerResponse dto = new CustomerResponse();
        dto.setId(customer.getId());
        dto.setCustomerName(customer.getCustomerName());
        dto.setEmail(customer.getEmail());
        dto.setMobile(customer.getMobile());
        dto.setBirthday(customer.getBirthday());
        dto.setIdentityCard(customer.getIdentityCard());
        dto.setLicenceNumber(customer.getLicenceNumber());
        dto.setLicenceDate(customer.getLicenceDate());
        if (customer.getAccount() != null) {
            dto.setAccountName(customer.getAccount().getAccountName());
            dto.setRole(customer.getAccount().getRole());
        }
        return dto;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> customers = customerService.findAllCustomers().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        return customerService.findCustomerById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails) {
        try {
            Customer updatedCustomer = customerService.updateCustomer(id, customerDetails);
            return ResponseEntity.ok(convertToDto(updatedCustomer));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.inActiveCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}