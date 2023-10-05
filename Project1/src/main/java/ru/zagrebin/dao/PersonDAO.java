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
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<Person>(Person.class));
    }

    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Person(full_name, year_of_birth, contract_number) VALUES(?, ?, ?)",
                person.getFull_name(), person.getYear_of_birth(), person.getContract_number());
    }

    public void update(int id, Person updatePerson) {
        jdbcTemplate.update("UPDATE Person SET full_name=?, year_of_birth=?, contract_number=? WHERE id=?",
                updatePerson.getFull_name(), updatePerson.getYear_of_birth(), updatePerson.getContract_number(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id = ?", id);
    }

    public List<Car> getCarsByPersonId(int id) {
        return jdbcTemplate.query("SELECT * FROM Car WHERE person_id = ?",
                new Object[]{id}, new BeanPropertyRowMapper<>(Car.class));
    }

    public Optional<Person> getPersonFullName(String fullName) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE full_name=?", new Object[]{fullName},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }


}
