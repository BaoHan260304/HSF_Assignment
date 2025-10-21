package base.api.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CarResponse {
    private Long id;
    private String carName;
    private Integer carModelYear;
    private String color;
    private Integer capacity;
    private String description;
    private LocalDate importDate;
    private Double rentPrice;
    private String status;
    private String producerName; // From CarProducer
}