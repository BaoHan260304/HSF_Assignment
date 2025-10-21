package base.api.dto.request;

import lombok.Data;

@Data
public class ReviewRequest {
    private Long carId;
    private Integer reviewStar;
    private String comment;
}