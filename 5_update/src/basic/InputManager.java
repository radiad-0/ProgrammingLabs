package basic;

import commands.CommandParameter;
import excepcions.*;
import items.Album;
import items.Coordinates;
import items.MusicBand;
import items.MusicGenre;

import java.util.*;

public class InputManager {
    private OutputManager outputManager;
    private Scanner scanner;


    public InputManager(OutputManager outputManager, Scanner scanner) {
        this.outputManager = outputManager;
        this.scanner = scanner;
    }

    public CommandParameter getCommandParameters() throws StopSignal, InvalidArgumentException, InvalidCommandException {
        String[] commandAndArguments = getCommandAndArguments();

        String commandName = commandAndArguments[0];
        String[] arguments = Arrays.copyOfRange(commandAndArguments, 1, commandAndArguments.length);

        return new CommandParameter(commandName, arguments);
    }

    public String[] getCommandAndArguments() throws StopSignal, InvalidCommandException {
        while (true) try {
            outputManager.printManualModeHighlightedMessage("Lab5$ ");

            String command = getLine();
            String[] commandAndArguments = command.split("\\s");

            if (commandAndArguments.length == 0) throw new InvalidCommandException(command);
            command = commandAndArguments[0];
            outputManager.print_LN_ScriptModeHighlightedMessage(command + ":");

            return commandAndArguments;
        } catch (NoSuchElementException e) {
            throw new StopSignal();
        }
    }

    public String getLine() throws StopSignal {
        String line;
        try {
            line = scanner.nextLine().trim();
        } catch (NoSuchElementException e) {
            throw new StopSignal();
        }
        return line;
    }

    public int getInt() throws StopSignal {
        int int_;
        String argument = "";
        while (true) try {
            argument = getLine();
            int_ = Integer.parseInt(argument);
            break;
        } catch (NumberFormatException e) {
            outputManager.printManualModeError("\"" + argument + "\" ???? ???????????????? ???????????? ???????? int,\n?????????????? ?????????? ????????????: ");
        }
        return int_;
    }

    public long getLong() throws StopSignal {
        long long_;
        String argument = "";
        while (true) try {
            argument = getLine();
            long_ = Long.parseLong(argument);
            break;
        }
        catch (NumberFormatException e){
            outputManager.printManualModeError("\"" + argument + "\" ???? ???????????????? ???????????? ???????? lond,\n?????????????? ?????????? ????????????: ");
        }
        return long_;
    }

    public double getDouble() throws StopSignal {
        double double_;
        String argument = "";
        while (true) try {
            argument = getLine();
            double_ = Double.parseDouble(argument);
            break;
        }
        catch (NumberFormatException e){
            outputManager.printManualModeError("\"" + argument + "\" ???? ???????????????? ???????????? ???????? double,\n?????????????? ?????????? ????????????: ");
        }
        return double_;
    }

    public MusicGenre getMusicGenre() throws StopSignal {
        MusicGenre musicGenre = null;
        String argument = "";
        outputManager.printManualModeHighlightedMessage("???????????????? ???????? ???? ???????????? - " + Arrays.toString(MusicGenre.values()) + "\ngenre: ");
        while (true) try {
            argument = getLine();
            musicGenre = MusicGenre.valueOf(argument);
            break;
        } catch (IllegalArgumentException e) {
            outputManager.printManualModeError("\"" + argument + "\" ???? ???????????????? ????????????,\n?????????????? ???????? ????????????: ");
        }
        return musicGenre;
    }

    public Coordinates getCoordinates() throws StopSignal {
        outputManager.printManualModeHighlightedMessage("coordinates:\n\tint x: ");
        int x = getInt();

        outputManager.printManualModeHighlightedMessage("\tdouble y: ");
        double y = getDouble();

        return new Coordinates(x, y);
    }

    public Album getAlbum() throws StopSignal {
        while (true) try {
            outputManager.printManualModeHighlightedMessage("bestAlbum:\n\tString name(???? null, ???? \"\"): ");
            String bestAlbumName = getLine();

            outputManager.printManualModeHighlightedMessage("\tint length(???????????? ???????? > 0): ");
            int length = getInt();

            return new Album(bestAlbumName, length);
        } catch (InvalidEnterException e) {
            outputManager.print_LN_ManualModeError(e.getMessage());
            outputManager.print_LN_ManualModeHighlightedMessage("?????????????????????? ?????? ?????? ??????????????, ?????????????? ?????????????????? ?? ??????????????");
        }
    }

    public MusicBand getMusicBand() throws StopSignal {
        while (true) try {
            outputManager.printManualModeHighlightedMessage("String name(???? null, ???? \"\"): ");
            String name = getLine();

            Coordinates coordinates = getCoordinates();

            outputManager.printManualModeHighlightedMessage("long numberOfParticipants(???????????? ???????? > 0): ");
            long numberOfParticipants = getLong();

            MusicGenre genre = getMusicGenre();

            Album bestAlbum = getAlbum();

            return new MusicBand(name, coordinates, numberOfParticipants, genre, bestAlbum);
        } catch (InvalidEnterException e) {
            outputManager.print_LN_ManualModeError(e.getMessage());
            outputManager.print_LN_ManualModeHighlightedMessage("?????????????????????? ?????? ?????? ??????????????, ?????????????? ?????????????????? ?? ??????????????");
        }
    }

    public void setManualMode(Scanner scanner){
        this.scanner = scanner;
        outputManager.setManualMode();
    }

    public void setScriptMode(Scanner scanner){
        this.scanner = scanner;
        outputManager.setScriptMode();
    }

    public void closeScanner(){
        scanner.close();
    }
}
