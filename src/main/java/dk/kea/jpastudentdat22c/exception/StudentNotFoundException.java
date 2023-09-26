package dk.kea.jpastudentdat22c.exception;

public class StudentNotFoundException extends RuntimeException{
    public StudentNotFoundException(String message){
        super(message);
    }
}
