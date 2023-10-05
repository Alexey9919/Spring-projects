package ru.zagrebin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zagrebin.dao.CarDAO;
import ru.zagrebin.dao.PersonDAO;
import ru.zagrebin.models.Car;
import ru.zagrebin.models.Person;
import ru.zagrebin.util.CarValidator;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/cars")
public class CarsController {

    private final CarDAO carDAO;
    private final PersonDAO personDAO;
    private CarValidator carValidator;

    @Autowired
    public CarsController(CarDAO carDAO, PersonDAO personDAO, CarValidator carValidator) {
        this.carDAO = carDAO;
        this.personDAO = personDAO;
        this.carValidator = carValidator;
    }



    @GetMapping()
    public String index(Model model) {
        model.addAttribute("cars", carDAO.index());
        return "cars/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("car", carDAO.show(id));

        Optional<Person> carOwner = carDAO.getCarOwner(id);

        if (carOwner.isPresent())
            model.addAttribute("owner", carOwner.get());
        else
            model.addAttribute("people", personDAO.index());

        return "cars/show";
    }

    @GetMapping("/new")
    public String newCar(@ModelAttribute("car") Car car) {
        return "cars/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("car") @Valid Car car, BindingResult bindingResult) {
        carValidator.validate(car, bindingResult);
        if(bindingResult.hasErrors()) {
            return "cars/new";
        }
        carDAO.save(car);
        return "redirect:/cars";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("car", carDAO.show(id));
        return "cars/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("car") @Valid Car car, BindingResult bindingResult, @PathVariable("id") int id) {
        carValidator.validate(car, bindingResult);
        if (bindingResult.hasErrors())
            return "cars/edit";

        carDAO.update(id, car);
        return "redirect:/cars";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        carDAO.delete(id);
        return "redirect:/cars";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        carDAO.release(id);
        return "redirect:/cars/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        carDAO.assign(id, selectedPerson);
        return "redirect:/cars/" + id;
    }

}
