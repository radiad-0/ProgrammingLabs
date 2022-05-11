package lab6.tools;

import lab6.excepcions.InvalidConnectException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * Класс, отправляющий сериализованные данные через {@link DatagramChannel}
 */
public class UDPConnection {
    private DatagramChannel datagramChannel;
    private int port;
    private SocketAddress address;

    /**
     * Конструктор, открывает {@link #datagramChannel}
     * @param port порт
     * @throws InvalidConnectException ошибка соединения
     */
    public UDPConnection(int port) throws InvalidConnectException {
        this.port = port;
        try {
            datagramChannel = DatagramChannel.open();
        } catch (IOException e) {
            throw new InvalidConnectException("не удалось открыть UPD канал");
        }
    }

    /**
     * Принимает запрос, размер буфера 10000000
     * @return массив байтов
     * @throws InvalidConnectException ошибка соединения
     */
    public byte[] receiveData() throws InvalidConnectException {
        return receiveData(10000000);
    }

    /**
     *
     * @param capacity размер буфера
     * @return массив байтов
     * @throws InvalidConnectException ошибка соединения
     */
    public byte[] receiveData(int capacity) throws InvalidConnectException {
        ByteBuffer buffer = ByteBuffer.allocate(capacity);
        try {
            address = datagramChannel.receive(buffer);
        } catch (IOException e){
            throw new InvalidConnectException("не удалось получить данные");
        }

        return buffer.array();
    }

    /**
     * Принимает запрос, кладет их в массив байтов
     * @param receiveBytes массив байтов
     * @return массив байтов
     * @throws InvalidConnectException ошибка соединения
     */
    public byte[] receiveData(byte[] receiveBytes) throws InvalidConnectException {
        ByteBuffer buffer = ByteBuffer.wrap(receiveBytes);
        try {
            address = datagramChannel.receive(buffer);
        } catch (IOException e){
            throw new InvalidConnectException("не удалось получить данные");
        }

        return receiveBytes;
    }

    /**
     * Отправляет массив байтов по адресу последнего принятого запроса
     * @param bytesToSend массив байтов
     * @throws InvalidConnectException ошибка соединения
     */
    public void sendData(byte[] bytesToSend) throws InvalidConnectException {
        sendData(bytesToSend, address);
    }

    /**
     * Отправляет массив байтов по адресу
     * @param bytesToSend массив байтов
     * @param addressToSend адрес
     * @throws InvalidConnectException ошибка соединения
     */
    public void sendData(byte[] bytesToSend, SocketAddress addressToSend) throws InvalidConnectException {
        ByteBuffer bufferToSend = ByteBuffer.wrap(bytesToSend);
        try {
            datagramChannel.send(bufferToSend, addressToSend);
            bufferToSend.clear();
        } catch (IOException e) {
            throw new InvalidConnectException("не удалось отправить данные");
        }
    }

    /**
     * Связывает канал с портом
     * @throws InvalidConnectException ошибка соединения
     */
    public void bindAddress() throws InvalidConnectException {
        bindAddress(new InetSocketAddress(port));
    }

    /**
     * Связывает канал с адресом
     * @param address адрес
     * @throws InvalidConnectException ошибка соединения
     */
    public void bindAddress(SocketAddress address) throws InvalidConnectException {
        try {
            datagramChannel.bind(address);
        } catch (IOException e) {
            throw new InvalidConnectException("не удалось связать канал с адресом");
        }
    }

}
