package ru.zagrebin.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.zagrebin.models.Car;
import ru.zagrebin.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class CarDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CarDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Car> index() {
        return jdbcTemplate.query("SELECT * FROM Car", new BeanPropertyRowMapper<Car>(Car.class));
    }

    public Car show(int id) {
        return jdbcTemplate.query("SELECT * FROM Car WHERE id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Car.class))
                .stream().findAny().orElse(null);
    }

    public void save(Car car) {
        jdbcTemplate.update("INSERT INTO Car(model, color, year, state_number) VALUES (?, ?, ?, ?)", car.getModel(), car.getColor(), car.getYear(), car.getState_number());
    }

    public void update(int id, Car updateCar) {
        jdbcTemplate.update("UPDATE Car SET model=?, color=?, year=?, state_number=? WHERE id=?", updateCar.getModel(), updateCar.getColor(), updateCar.getYear(), updateCar.getState_number(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Car WHERE id=?", id);
    }

    public Optional<Person> getCarOwner(int id) {
        return jdbcTemplate.query("SELECT Person.* FROM Person JOIN Car ON Person.id = Car.person_id WHERE Car.id = ?" ,
                new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    //Освобождает авто
    public void release(int id) {
        jdbcTemplate.update("UPDATE Car SET person_id=NULL WHERE id=?", id);
    }

    //Назначает авто человеку
    public void assign(int id, Person selectedPerson) {
        jdbcTemplate.update("UPDATE Car SET person_id=? WHERE id=?", selectedPerson.getId(), id);
    }

    public Optional<Car> getCarByStateNumber(String state_number) {
        return jdbcTemplate.query("SELECT * FROM Car WHERE state_number = ?", new Object[]{state_number}, new BeanPropertyRowMapper<>(Car.class))
        .stream().findAny();
    }
}
