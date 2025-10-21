package base.api.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerResponse {
    private Long id;
    private String customerName;
    private String email;
    private String mobile;
    private LocalDate birthday;
    private String identityCard;
    private String licenceNumber;
    private LocalDate licenceDate;
    private String accountName; // From Account
    private String role;        // From Account
}