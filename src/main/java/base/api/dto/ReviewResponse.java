package base.api.dto;

import lombok.Data;

@Data
public class ReviewResponse {
    private Long customerId;
    private String customerName;
    private Long carId;
    private String carName;
    private Integer reviewStar;
    private String comment;
}