package ge.pozdniakov.springrestapp.controllers;

import ge.pozdniakov.springrestapp.models.Person;
import ge.pozdniakov.springrestapp.services.PeopleService;
import ge.pozdniakov.springrestapp.util.PersonErrorResponse;
import ge.pozdniakov.springrestapp.util.PersonNotCreatedException;
import ge.pozdniakov.springrestapp.util.PersonNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping()
    public List<Person> getPeople(){
        return peopleService.findAll();
    }

    @GetMapping("/{id}")
    public Person getOne(@PathVariable("id") int id){
        return peopleService.findOne(id);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid Person person,
                                             BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error: errors) {
                errorMsg.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new PersonNotCreatedException(errorMsg.toString());
        }
        peopleService.save(person);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException personNotFoundException){
        PersonErrorResponse personErrorResponse = new PersonErrorResponse(
                "Person with this id was not found!",
                System.currentTimeMillis()
        );
        return new ResponseEntity<PersonErrorResponse>(personErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException personNotCreatedException){
        PersonErrorResponse personErrorResponse = new PersonErrorResponse(
                personNotCreatedException.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<PersonErrorResponse>(personErrorResponse, HttpStatus.BAD_REQUEST);
    }
}
