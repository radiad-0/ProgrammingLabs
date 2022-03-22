package tools;

import excepcions.InvalidEnterException;
import items.MusicBand;
import items.MusicGenre;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

/**
 * Класс, реализующий все комманды
 * и добавляющий возможность интерактивного взаимодействия пользователя с коллекцией через любой способ ввода,
 * для своей работы требует имя файла для загрузки коллекции, способ ввода и список доступных комманд
 */
public class Program {
    private HashSet<MusicBand> musicBands;
    private Date date;
    private HashSet<String> commands;
    private LinkedList<String> history;
    private boolean inRun;
    private Parser parser;

    /**
     * крнструктор
     * @param fileName
     * @param scanner
     * @param commands
     */
    public Program(String fileName, Scanner scanner, Collection<String> commands) {
        parser = new Parser(scanner);
        musicBands = new HashSet<>();
        try {
            musicBands = parser.parseXmlWithMusicBands(fileName);
        } catch (ParserConfigurationException | IOException | SAXException | InvalidEnterException e) {
            System.out.println("\033[31mпроблемы с загрузкой файла:\n" + e.getMessage() + "\033[0m");
        } catch (NullPointerException e){
            System.out.println("\033[31mпроблемы с загрузкой файла: переменная окружения не задана\033[0m");
        }
        date = new Date();
        this.commands = new HashSet<String>(commands);
        history = new LinkedList<>();
        inRun = true;
        System.out.println("\033[35mПрограмма начала работу!\nПо команде help можно получить список доступных комманд.\033[0m");
    }

    /**
     * Основной метод, который ждет от пользователя ввода команды,
     * и в зависимости от нее, вызывает нужную функцию на выполнение
     */
    public void run() {

        while (inRun) {
            String command = parser.getCommand(commands);

            updateHistory(command);

            switch (command){
                case "help": help(); break;
                case "info": info(); break;
                case "show": show(); break;
                case "add": add(); break;
                case "update": update(); break;
                case "remove_by_id": removeByID(); break;
                case "clear": musicBands.clear(); break;
                case "save": save(); break;
                case "execute_script": executeScript(); break;
                case "exit": exit(); break;
                case "remove_greater": removeGreater(); break;
                case "remove_lower": removeLower(); break;
                case "history": showHistory(); break;
                case "min_by_id": minById(); break;
                case "count_less_than_genre": countLessThanGenre(); break;
                case "filter_starts_with_name": filterStartsWithName(); break;
            }
        }
    }

    /**
     * вывести справку по доступным командам
     */
    private void help(){
        System.out.println("help : вывести справку по доступным командам\n" +
                "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "add {element} : добавить новый элемент в коллекцию\n" +
                "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "remove_by_id id : удалить элемент из коллекции по его id\n" +
                "clear : очистить коллекцию\n" +
                "save : сохранить коллекцию в файл\n" +
                "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                "exit : завершить программу (без сохранения в файл)\n" +
                "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный\n" +
                "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный\n" +
                "history : вывести последние 14 команд (без их аргументов)\n" +
                "min_by_id : вывести любой объект из коллекции, значение поля id которого является минимальным\n" +
                "count_less_than_genre genre : вывести количество элементов, значение поля genre которых меньше заданного\n" +
                "filter_starts_with_name name : вывести элементы, значение поля name которых начинается с заданной подстроки");
    }

    /**
     * вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
     */
    private void info(){
        System.out.println("тип: " + musicBands.getClass() + "\n" +
                "дата инициализации: " + date + "\n" +
                "количество элементов: " + musicBands.size());
    }

    /**
     * добавить новый элемент в коллекцию
     */
    private void add() {
        musicBands.add(parser.getMusicBand());
    }

    /**
     * вывести в стандартный поток вывода все элементы коллекции в строковом представлении
     */
    private void show() {
        for (MusicBand musicBand: musicBands) System.out.println(musicBand);
    }

    /**
     * обновить значение элемента коллекции, id которого равен заданному
     */
    private void update() {
        int id = parser.getInt();
        boolean removed = false;
        for (MusicBand musicBand:musicBands){
            if (musicBand.getId() == id){
                MusicBand newMusicBand = parser.getMusicBand();

                musicBand.setName(newMusicBand.getName());
                musicBand.setCoordinates(newMusicBand.getCoordinates());
                musicBand.setNumberOfParticipants(newMusicBand.getNumberOfParticipants());
                musicBand.setGenre(newMusicBand.getGenre());
                musicBand.setBestAlbum(newMusicBand.getBestAlbum());

                removed = true;
                break;
            }
        }
        if (!removed) System.out.println("\033[31mэлемента с id==" + id + " не существует\033[0m");
    }

    /**
     * удалить элемент из коллекции по его id
     */
    private void removeByID(){
        int removeId = parser.getInt();
        boolean removed = false;
        for (MusicBand musicBand:musicBands){
            if (musicBand.getId() == removeId){
                musicBands.remove(musicBand);
                removed = true;
                break;
            }
        }
        if (removed) MusicBand.addToBufferIds(removeId);
        else System.out.println("\033[31mэлемента с id==" + removeId + " не существует\033[0m");
    }

    /**
     * сохранить коллекцию в файл
     */
    private void save() {
        String outputFileName = "output.xml";
        File file = new File(outputFileName);
        try {
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));

            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<musicBands>\n");

            for (MusicBand musicBand : musicBands) {
                writer.write("\t<musicBand name=\"" + musicBand.getName() + "\" numberOfParticipants=\"" + musicBand.getNumberOfParticipants() + "\" genre=\"" + musicBand.getGenre() + "\">\n" +
                        "\t\t<coordinates x=\"" + musicBand.getXCoordinate() + "\" y=\"" + musicBand.getYCoordinate() + "\"/>\n" +
                        "\t\t<bestAlbum name=\"" + musicBand.getBestAlbumName() + "\" length=\"" + musicBand.getBestAlbumLength() + "\"/>\n" +
                        "\t</musicBand>\n");
            }
            writer.write("</musicBands>");

            writer.close();
            System.out.println("\033[35m save completed\nсохранено в файл output.xml в текущей директории\033[0m");
        } catch (IOException e) {
            System.out.println("\033[31m нет доступа к файлу, измените настройки доступа \033[0m");
        }
    }

    /**
     * считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
     */
    private void executeScript() {
        String fileName = parser.getString();
        Parser oldParser = parser;
        try {
            parser = new Parser(new Scanner(new File(fileName)), false);
        } catch (FileNotFoundException e) {
            System.out.println("\033[31m" + e.getMessage() + "\033[0m");
        }
        run();
        parser = oldParser;
        inRun = true;
    }

    /**
     * завершить программу (без сохранения в файл)
     */
    private void exit(){
        inRun = false;
        parser.closeScanner();
    }

    /**
     * вывести последние 14 команд (без их аргументов)
     */
    private void showHistory(){
        System.out.println("последние 14 команд:");
        for (String h:history){
            System.out.print(h + "; ");
        }
        System.out.println();
    }

    /**
     * удалить из коллекции все элементы, превышающие заданный
     */
    private void removeGreater() {
        MusicBand compareMusicBand = parser.getMusicBand();
        musicBands.removeIf(musicBand -> musicBand.compareTo(compareMusicBand) > 0);
    }

    /**
     * удалить из коллекции все элементы, меньшие, чем заданный
     */
    private void removeLower() {
        MusicBand compareMusicBand = parser.getMusicBand();
        musicBands.removeIf(musicBand -> musicBand.compareTo(compareMusicBand) < 0);
    }

    /**
     * вывести любой объект из коллекции, значение поля id которого является минимальным
     */
    private void minById(){
        int minId = Integer.MAX_VALUE;
        MusicBand musicBandForPrint = null;
        for (MusicBand musicBand: musicBands){
            if (musicBand.getId() <= minId){
                minId = musicBand.getId();
                musicBandForPrint = musicBand;
            }
        }
        System.out.println(musicBandForPrint);
    }

    /**
     * вывести количество элементов, значение поля genre которых меньше заданного
     */
    private void countLessThanGenre(){
        MusicGenre genre = parser.getMusicGenre();
        int count = 0;
        for (MusicBand musicBand: musicBands){
            if (musicBand.getGenre().compareTo(genre) < 0) count++;
        }
        System.out.println("count less than genre \"" + genre + "\": " + count);
    }

    /**
     * вывести элементы, значение поля name которых начинается с заданной подстроки
     */
    private void filterStartsWithName(){
        String subName = parser.getString();
        for (MusicBand musicBand:musicBands){
            if (musicBand.getName().startsWith(subName)) System.out.println(musicBand);
        }
    }

    /**
     * обновление истории комманд
     * @param command
     */
    private void updateHistory(String command){
        if (history.size() >= 14) history.removeFirst();
        history.add(command);
    }
}
