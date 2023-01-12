package ge.pozdniakov.springrestapp.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

public class PersonDTO {

    @NotEmpty(message = "Name Should not be Empty")
    @Size(min = 6, max = 50, message = "Name should be between 6 and 50 characters")
    private String name;

    @Min(value = 0, message = "Age should be greater than 0")
    @Max(value = 125)
    private int age;

    @Email(message = "This is an Email, you make mistake")
    @NotEmpty(message = "Email Should not be Empty")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
