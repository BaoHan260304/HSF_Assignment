package base.api.repository;

import base.api.model.CarRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CarRentalRepository extends JpaRepository<CarRental, CarRental.CarRentalId> {

    boolean existsByCar_Id(Long carId);

    List<CarRental> findByPickupDateBetweenOrderByPickupDateDesc(LocalDate startDate, LocalDate endDate);

    List<CarRental> findByCustomer_Id(Long customerId);
}