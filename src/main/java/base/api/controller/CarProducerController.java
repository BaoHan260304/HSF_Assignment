package base.api.controller;

import base.api.dto.request.CarProducerRequest;
import base.api.dto.CarProducerResponse;
import base.api.model.CarProducer;
import base.api.repository.CarProducerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/producers")
@PreAuthorize("hasRole('ADMIN')") // Only ADMIN can manage car producers
public class CarProducerController { // Renamed from AdminController for clarity

    private final CarProducerRepository carProducerRepository;

    // Helper to convert CarProducer entity to CarProducerResponse DTO
    private CarProducerResponse convertToDto(CarProducer producer) {
        CarProducerResponse dto = new CarProducerResponse();
        dto.setId(producer.getId());
        dto.setProducerName(producer.getProducerName());
        dto.setAddress(producer.getAddress());
        dto.setCountry(producer.getCountry());
        return dto;
    }

    @GetMapping
    public ResponseEntity<List<CarProducerResponse>> getAllProducers() {
        List<CarProducerResponse> producers = carProducerRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(producers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarProducerResponse> getProducerById(@PathVariable Long id) {
        return carProducerRepository.findById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CarProducerResponse> createProducer(@RequestBody CarProducerRequest request) {
        CarProducer producer = new CarProducer();
        producer.setProducerName(request.getProducerName());
        producer.setAddress(request.getAddress());
        producer.setCountry(request.getCountry());
        CarProducer savedProducer = carProducerRepository.save(producer);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(savedProducer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarProducerResponse> updateProducer(@PathVariable Long id, @RequestBody CarProducerRequest request) {
        return carProducerRepository.findById(id).map(existingProducer -> {
            existingProducer.setProducerName(request.getProducerName());
            existingProducer.setAddress(request.getAddress());
            existingProducer.setCountry(request.getCountry());
            CarProducer updatedProducer = carProducerRepository.save(existingProducer);
            return ResponseEntity.ok(convertToDto(updatedProducer));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducer(@PathVariable Long id) {
        if (carProducerRepository.existsById(id)) {
            carProducerRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}