package base.api.dto.request;

import lombok.Data;

@Data
public class CarProducerRequest {
    private String producerName;
    private String address;
    private String country;
}