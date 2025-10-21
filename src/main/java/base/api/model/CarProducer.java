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
@Table(name = "car_producer")
@AttributeOverride(name = "id", column = @Column(name = "producer_id"))
public class CarProducer extends BaseModel {

    @Column(name = "producer_name", nullable = false)
    private String producerName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String country;
}