package ru.zagrebin.Project2Boot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.zagrebin.Project2Boot.models.Car;
import ru.zagrebin.Project2Boot.services.CarsService;

@Component
public class CarValidator implements Validator {


    private final CarsService carsService;

    @Autowired
    public CarValidator(CarsService carsService) {
        this.carsService = carsService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Car.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Car car = (Car) o;

        if(!car.getStateNumber().matches("[А-Яа-я]\\d{3}+[А-Яа-я]{2}\\d{2,3}"))
            errors.rejectValue("stateNumber", "", "Введите корректный гос номер пример: А111АА799");

        if(carsService.getCarByStateNumber(car.getStateNumber()) != null)
            errors.rejectValue("stateNumber", "", "Такой гос номер уже существует");

    }
}
