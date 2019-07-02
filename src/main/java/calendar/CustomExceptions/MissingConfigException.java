package calendar.CustomExceptions;

public class MissingConfigException extends Exception {

    public MissingConfigException(String errorMessage){
        super(errorMessage);
    }

}
