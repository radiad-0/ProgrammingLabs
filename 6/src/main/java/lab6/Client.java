package lab6;

import lab6.tools.CommandParameters;
import lab6.excepcions.MyException;
import lab6.tools.Serializer;
import lab6.tools.ServerRequest;
import lab6.tools.StopSignal;
import lab6.tools.clientIOManagers.ClientInputManager;
import lab6.tools.clientIOManagers.ClientOutputManager;
import lab6.tools.UDPConnection;

import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Scanner;

public class Client {
    private static final int port = 3468;
    private ClientOutputManager outputManager;
    private ClientInputManager inputManager;
    UDPConnection udpConnection;
    SocketAddress serverAddress;
    private boolean inRun;

    /**
     * Запускает клиентское приложение
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        Client lab6 = new Client();
        try {
            lab6.run();
        } catch (MyException e) {
            lab6.outputManager.print_LN_ManualModeError(e.getMessage());
        }
    }

    private Client(){
        outputManager = new ClientOutputManager();
        inputManager = new ClientInputManager(outputManager, new Scanner(System.in));
        serverAddress = new InetSocketAddress("localhost", port);

        inRun = true;
        outputManager.print_LN_ManualModeHighlightedMessage("Программа начала работу!\nПо команде help можно получить список доступных комманд.");
    }

    /**
     * Читает, обрабатывает команды и отправляет их серверу
     * @throws MyException ошибка сериализация {@link Serializer#serialize(Object)} или соединения
     */
    public void run() throws MyException {

        CommandParameters commandParameters;
        udpConnection = new UDPConnection(port);

        while (inRun) {
            try {
                commandParameters = inputManager.getCommandParameters();
                udpConnection.sendData(Serializer.serialize(commandParameters), serverAddress);

                processingRequest();
            } catch (MyException e) {
                outputManager.print_LN_ManualModeError(e.getMessage());
            } catch (StopSignal e){
                outputManager.print_LN_ManualModeHighlightedMessage("программа завершена");
                outputManager.print_LN_ScriptModeHighlightedMessage("script completed");
                if (isScriptMode()) {
                    udpConnection.sendData(Serializer.serialize(new CommandParameters("exit", "endOfScript")), serverAddress);
                    setManualMode();
                    continue;
                }

                if ("EOF".equals(e.getMessage())) {
                    udpConnection.sendData(Serializer.serialize(new CommandParameters("exit", "EOF")), serverAddress);
                }

                inRun = false;
                inputManager.closeScanner();
            }
        }
    }

    /**
     * Обработка серверного запроса
     * @throws MyException ошибка сериализация {@link Serializer#serialize(Object)} или соединения {@link UDPConnection#sendData(byte[])}
     * @throws StopSignal Сигнал, завершающий клиентское приложение
     */
    private void processingRequest() throws MyException, StopSignal {
        ServerRequest serverRequest = (ServerRequest) Serializer.deserialize(udpConnection.receiveData());

        if (serverRequest == null) return;
        outputManager.printServerRequest(serverRequest);
        if (serverRequest.isStopSignal()) throw new StopSignal("exit");
        if (serverRequest.isNeedElement()){
            udpConnection.sendData(Serializer.serialize(inputManager.getMusicBand()), serverAddress);
            processingRequest();
        }
        if (serverRequest.isSetManualMode()) setManualMode();
        if (serverRequest.isSetScriptMode()) {
            try {
                setScriptMode(new Scanner(serverRequest.getScriptFile()));
            } catch (FileNotFoundException e) {
                setManualMode();
                udpConnection.sendData(Serializer.serialize(new CommandParameters("exit", "endOfScript")), serverAddress);
                throw new MyException("Файл с таким именем не найден", "попробуйте еще раз");
            }
        }
    }

    public boolean isManualMode(){
        return outputManager.isManualMode();
    }

    public boolean isScriptMode(){
        return outputManager.isScriptMode();
    }

    public void setManualMode(){
        inputManager.setManualMode(new Scanner(System.in));
    }

    public void setScriptMode(Scanner scanner){
        inputManager.setScriptMode(scanner);
    }
}
