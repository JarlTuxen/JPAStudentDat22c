package dk.kea.jpastudentdat22c.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record StudentDTO(int id, String name, LocalDate bornDate, LocalTime bornTime) {

}
