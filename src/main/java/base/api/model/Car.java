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
@Table(name = "car")
@AttributeOverride(name = "id", column = @Column(name = "car_id"))
public class Car extends BaseModel {

    @Column(name = "car_name", nullable = false)
    private String carName;

    @Column(name = "car_model_year", nullable = false)
    private Integer carModelYear;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private Integer capacity;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "import_date", nullable = false)
    private java.time.LocalDate importDate;

    @Column(name = "rent_price", nullable = false)
    private Double rentPrice;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "producer_id", nullable = false)
    private CarProducer producer;
}
