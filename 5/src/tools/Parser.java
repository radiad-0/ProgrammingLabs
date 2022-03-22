package tools;

import items.Album;
import items.Coordinates;
import items.MusicBand;
import items.MusicGenre;
import excepcions.InvalidCommandException;
import excepcions.InvalidEnterException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Parser {
    private final Scanner scanner;
    private boolean inputHints = true;

    public Parser(Scanner scanner){
        this.scanner = scanner;
    }

    public Parser(Scanner scanner, Boolean inputHints){
        this.scanner = scanner;
        this.inputHints = inputHints;
    }
    public void closeScanner(){
        scanner.close();
    }

    public String getCommand(HashSet<String> availableCommand) {
        String command;
        while (true) {
            try {
                if (inputHints) System.out.print("\033[35mLab5$ \033[0m");
                try {
                    command = scanner.next();
                    if (!inputHints) System.out.println("\033[35m" + command + ":\033[0m");
                } catch (NoSuchElementException e){
                    command = "exit";
                    System.out.println("\033[35mscript completed\033[0m");
                }
                if (availableCommand.contains(command)) break;
                else throw new InvalidCommandException();
            } catch (InvalidCommandException e) {
                scanner.nextLine();
                System.out.println("\033[31m" + e.getMessage() + "\033[0m");
            }
        }
        return command;
    }

    public String getString(){
        String string = scanner.next();
        scanner.nextLine();
        return string;
    }

    public int getInt(){
        int int_;
        while (true) {
            try {
                int_ = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                if (inputHints) {
                    String notInt = scanner.nextLine();
                    System.out.print("\033[31m\"" + notInt + "\" не является числом типа int,\nвведите число заново: \033[0m");
                }
            }
        }
        return int_;
    }

    public long getLong(){
        long long_;
        while (true) {
            try {
                long_ = scanner.nextLong();
                break;
            } catch (InputMismatchException e) {
                if (inputHints) {
                    String notLong = scanner.nextLine();
                    System.out.print("\033[31m\"" + notLong + "\" не является числом типа long,\nвведите число заново: \033[0m");
                }
            }
        }
        return long_;
    }

    public double getDouble(){
        double double_;
        while (true) {
            try {
                double_ = scanner.nextDouble();
                break;
            } catch (InputMismatchException e) {
                if (inputHints) {
                    String notLong = scanner.nextLine();
                    System.out.print("\033[31m\"" + notLong + "\" не является числом типа double,\nвведите число заново: \033[0m");
                }
            }
        }
        return double_;
    }

    public Coordinates getCoordinates(){
        if (inputHints) System.out.print("coordinates:\n\tint x: ");
        int x = getInt();

        if (inputHints) System.out.print("\tdouble y: ");
        double y = getDouble();

        return new Coordinates(x, y);
    }

    public MusicGenre getMusicGenre(){
        MusicGenre musicGenre;
        String notMusicGenre = null;
        while (true) {
            try {
                notMusicGenre = getString();
                musicGenre = MusicGenre.valueOf(notMusicGenre);
                break;
            } catch (IllegalArgumentException e) {
                if (inputHints) System.out.print("\033[31m\"" + notMusicGenre + "\" не является жанром,\nвведите жанр заново: \033[0m");
            }
        }
        return musicGenre;
    }

    public Album getAlbum() throws InvalidEnterException {
        if (inputHints) System.out.print("bestAlbum:\n\tString name(не null, не \"\"): ");
        String bestAlbumName = getString();

        if (inputHints) System.out.print("\tint length(должно быть > 0): ");
        int length = getInt();

        return new Album(bestAlbumName, length);
    }

    public MusicBand getMusicBand() {
        while (true) {
            try {
                if (inputHints) System.out.print("String name(не null, не \"\"): ");
                String name = getString();

                Coordinates coordinates = getCoordinates();

                if (inputHints) System.out.print("long numberOfParticipants(должно быть > 0): ");
                long numberOfParticipants = getLong();

                if (inputHints) System.out.print("выберете один из жанров - ROCK, JAZZ или BRIT_POP\ngenre: ");
                MusicGenre genre = getMusicGenre();

                Album bestAlbum = getAlbum();

                return new MusicBand(name, coordinates, numberOfParticipants, genre, bestAlbum);
            } catch (InvalidEnterException e){
                System.out.println("\033[31m" + e.getMessage() + "\033[0m");
                System.out.println("\033[35mпопытайтесь еще раз сначала, читайте подсказки в скобках\033[0m");
            }
        }
    }

    public HashSet<MusicBand> parseXmlWithMusicBands(String fileName) throws ParserConfigurationException, IOException, SAXException, InvalidEnterException {
        HashSet<MusicBand> musicBands = new HashSet<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(fileName));

        NodeList musicBandElements = document.getDocumentElement().getElementsByTagName("musicBand");

        for (int i = 0; i < musicBandElements.getLength(); i++) {
            Node musicBand = musicBandElements.item(i);
            NamedNodeMap musicBandAttributes = musicBand.getAttributes();

            if (((Element) musicBand).getElementsByTagName("coordinates").getLength() != 1 || ((Element) musicBand).getElementsByTagName("bestAlbum").getLength() != 1){
                throw new InvalidEnterException("входные данные", "неверное количество составных полей в элементе " + (1+i));
            }

            NamedNodeMap coordinatesAttributes = ((Element) musicBand).getElementsByTagName("coordinates").item(0).getAttributes();
            NamedNodeMap bestAlbumAttributes = ((Element) musicBand).getElementsByTagName("bestAlbum").item(0).getAttributes();

            if (musicBandAttributes.getLength() != 3 || coordinatesAttributes.getLength() != 2 || bestAlbumAttributes.getLength() != 2){
                throw new InvalidEnterException("входные данные", "неверное количество полей в элементе " + (1+i));
            }


            String name = musicBandAttributes.getNamedItem("name").getNodeValue();
            int x = Integer.parseInt(coordinatesAttributes.getNamedItem("x").getNodeValue());
            double y = Double.parseDouble(coordinatesAttributes.getNamedItem("y").getNodeValue());
            Coordinates coordinates = new Coordinates(x, y);
            long numberOfParticipants = Long.parseLong(musicBandAttributes.getNamedItem("numberOfParticipants").getNodeValue());
            MusicGenre genre = MusicGenre.valueOf(musicBandAttributes.getNamedItem("genre").getNodeValue());
            String nameOfBestAlbum = bestAlbumAttributes.getNamedItem("name").getNodeValue();
            int lengthOfBestAlbum = Integer.parseInt(bestAlbumAttributes.getNamedItem("length").getNodeValue());
            Album bestAlbum = new Album(nameOfBestAlbum, lengthOfBestAlbum);

            musicBands.add(new MusicBand(name, coordinates, numberOfParticipants, genre, bestAlbum));
        }

        return musicBands;
    }

}
