package ru.zagrebin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zagrebin.models.Car;

import java.util.List;


@Repository
public interface CarsRepository extends JpaRepository<Car, Integer> {
    List<Car> findByModelStartingWith(String model);

    Car findByStateNumber(String stateNumber);
}