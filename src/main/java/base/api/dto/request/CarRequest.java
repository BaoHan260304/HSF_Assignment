package base.api.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CarRequest {
    private String carName;
    private Integer carModelYear;
    private String color;
    private Integer capacity;
    private String description;
    private LocalDate importDate;
    private Double rentPrice;
    private String status;
    private Long producerId; // ID of the CarProducer
}