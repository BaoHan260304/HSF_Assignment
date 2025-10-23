package base.api.service;

import base.api.model.Car;

import java.util.List;
import java.util.Optional;

public interface CarService {
    List<Car> findAllCars();

    List<Car> findAvailableCars();

    Optional<Car> findCarById(Long id);

    Car saveCar(Car car);

    void deleteCar(Long id);
}