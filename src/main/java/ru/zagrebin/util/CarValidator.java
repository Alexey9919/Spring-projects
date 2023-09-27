package ru.zagrebin.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.zagrebin.dao.CarDAO;
import ru.zagrebin.models.Car;

@Component
public class CarValidator implements Validator {

    private final CarDAO carDAO;


    @Autowired
    public CarValidator(CarDAO carDAO) {
        this.carDAO = carDAO;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Car.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Car car = (Car) o;

        if(!car.getState_number().matches("[А-Яа-я]\\d{3}+[А-Яа-я]{2}\\d{2,3}"))
        errors.rejectValue("state_number", "", "Введите корректный гос номер пример: А111АА799");

        if(carDAO.getCarByStateNumber(car.getState_number()).isPresent())
            errors.rejectValue("state_number", "", "Такой гос номер уже существует");
    }
}
