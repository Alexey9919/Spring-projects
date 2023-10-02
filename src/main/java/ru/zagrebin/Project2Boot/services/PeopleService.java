package ru.zagrebin.Project2Boot.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zagrebin.Project2Boot.models.Car;
import ru.zagrebin.Project2Boot.models.Person;
import ru.zagrebin.Project2Boot.repositories.PeopleRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> person =  peopleRepository.findById(id);
        return person.orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public Optional<Person> getPersonByPersonId(String fullName) {
        return peopleRepository.findByFullName(fullName);
    }

    public List<Car> getCarByPersonId(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        if(person.isPresent()) {
            Hibernate.initialize(person.get().getCars());

            person.get().getCars().forEach(car -> {
                long diffInMillies = Math.abs(car.getTakenAt().getTime() - new Date().getTime());
                long totalRentalTime = car.getRentalPeriod() * 86400000;
                if(diffInMillies > totalRentalTime)
                    car.setExpired(true);
            });

            return person.get().getCars();
        }
        else {
            return Collections.emptyList();
        }
    }
}

