package calendar.CustomExceptions;

public class MissingConfigParameterException extends Exception {

    public MissingConfigParameterException(String errorMessage){
        super(errorMessage);
    }

}
