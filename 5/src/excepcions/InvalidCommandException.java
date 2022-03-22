package excepcions;

public class InvalidCommandException extends InvalidEnterException{
    public InvalidCommandException() {
        super("Неверная команда", "\nПо команде help можно получить список доступных комманд.");
    }
}
