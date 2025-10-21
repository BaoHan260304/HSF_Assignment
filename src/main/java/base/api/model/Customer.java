package base.api.model;

import base.api.model.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
@AttributeOverride(name = "id", column = @Column(name = "customer_id"))
public class Customer extends BaseModel {

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String mobile;

    @Column(nullable = false)
    private java.time.LocalDate birthday;

    @Column(name = "identity_card", nullable = false)
    private String identityCard;

    @Column(name = "licence_number", nullable = false)
    private String licenceNumber;

    @Column(name = "licence_date", nullable = false)
    private java.time.LocalDate licenceDate;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToMany(mappedBy = "customer")
    private java.util.List<CarRental> rentals;

    @OneToMany(mappedBy = "customer")
    private java.util.List<Review> reviews;
}
