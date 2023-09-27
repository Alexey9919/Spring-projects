package ru.zagrebin.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.zagrebin.dao.PersonDAO;
import ru.zagrebin.models.Person;

@Component
public class PersonValidator implements Validator {

    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        if(personDAO.getPersonFullName(person.getFull_name()).isPresent())
            errors.rejectValue("full_name", "", "Человек с таким ФИО уже существует");

        if(!(person.getContract_number() < 1000000 && person.getContract_number() >= 100000))
            errors.rejectValue("contract_number", "", "Номер договора должен состоять из 6 цифр");
    }
}
