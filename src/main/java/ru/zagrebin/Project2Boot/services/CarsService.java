package ru.zagrebin.Project2Boot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zagrebin.Project2Boot.models.Car;
import ru.zagrebin.Project2Boot.models.Person;
import ru.zagrebin.Project2Boot.repositories.CarsRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CarsService {

    private final CarsRepository carsRepository;

    @Autowired
    public CarsService(CarsRepository carsRepository) {
        this.carsRepository = carsRepository;
    }

    public List<Car> findAll(boolean sortByYear) {

        if(sortByYear)
            return carsRepository.findAll(Sort.by("year"));
        else
            return carsRepository.findAll();
    }

    public List<Car> findWithPagination(Integer page, Integer bookPerPage, boolean sortByYear) {
        if(sortByYear)
            return carsRepository.findAll(PageRequest.of(page, bookPerPage, Sort.by("year"))).getContent();
        else
            return carsRepository.findAll(PageRequest.of(page, bookPerPage)).getContent();
    }

    public Car findOne(int id) {
        Optional<Car> car = carsRepository.findById(id);
        return car.orElse(null);
    }

    public List<Car> searchByTitle(String query) {
        return carsRepository.findByModelStartingWith(query);
    }

    @Transactional
    public void save(Car newCar) {
        carsRepository.save(newCar);
    }

    @Transactional
    public void update(int id, Car updateCar) {
        Car carToBeUpdated = carsRepository.findById(id).get();

        updateCar.setId(id);
        updateCar.setOwner(carToBeUpdated.getOwner());

        carsRepository.save(updateCar);
    }

    @Transactional
    public void delete(int id) {
        carsRepository.deleteById(id);
    }

    public Person getBookOwner(int id) {
        // Тут Hibernate.initialize() не нужен, так как владелец (сторона One) загружается не лениво
        return carsRepository.findById(id).map(Car::getOwner).orElse(null);
    }

    @Transactional
    public void release(int id) {
        carsRepository.findById(id).ifPresent(
                car -> {
                    car.setOwner(null);
                    car.setTakenAt(null);
                });
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        carsRepository.findById(id).ifPresent(
                car -> {
                    car.setOwner(selectedPerson);
                    car.setTakenAt(new Date());
                }
        );
    }

    public Car getCarByStateNumber(String stateNumber) {
        return carsRepository.findByStateNumber(stateNumber);
    }
}

