package lab6.commands;

import lab6.excepcions.MyException;
import lab6.tools.CommandParameters;
import lab6.tools.StopSignal;
import lab6.tools.serverIOManagers.ServerOutputManager;

public abstract class Command {
    /**
     * Имя команды
     */
    private final String name;

    /**
     * Описание команды
     */
    private final String description;

    /**
     * Количество аргументов команды
     */
    private final int numberOfArguments;

    /**
     * Нужен ли команде элемент {@link lab6.items.MusicBand} в качестве аргумента
     */
    private final boolean needElementAsArgument;

    /**
     * Параметры команды {@link CommandParameters}
     */
    protected CommandParameters commandParameters;
    protected ServerOutputManager outputManager;

    public Command(String name, String description, int numberOfArguments, boolean needElementAsArgument, ServerOutputManager outputManager) {
        this.name = name;
        this.description = description;
        this.numberOfArguments = numberOfArguments;
        this.needElementAsArgument = needElementAsArgument;
        this.outputManager = outputManager;
    }

    public abstract void execute() throws MyException, StopSignal;

    public void setParameters(CommandParameters commandParameters){
        this.commandParameters = commandParameters;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getNumberOfArguments() {
        return numberOfArguments;
    }

    public boolean isNeedElementAsArgument() {
        return needElementAsArgument;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
