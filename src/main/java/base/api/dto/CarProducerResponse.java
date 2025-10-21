package base.api.dto;

import lombok.Data;

@Data
public class CarProducerResponse {
    private Long id;
    private String producerName;
    private String address;
    private String country;
}