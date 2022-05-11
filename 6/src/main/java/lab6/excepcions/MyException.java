package lab6.excepcions;

public class MyException extends Exception{
    public MyException(String someThing, String errorDescription ){
        super("Ошибка! " + someThing + ": " + errorDescription );
    }
}
