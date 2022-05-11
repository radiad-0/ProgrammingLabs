package lab6.tools.serverIOManagers;

import lab6.excepcions.MyException;
import lab6.tools.UDPConnection;
import lab6.tools.Serializer;
import lab6.tools.CommandParameters;
import lab6.items.MusicBand;

public class ServerInputManager {
    private UDPConnection UDPConnection;
    private ServerOutputManager outputManager;


    public ServerInputManager(ServerOutputManager serverOutputManager, UDPConnection UDPConnection) {
        this.outputManager = serverOutputManager;
        this.UDPConnection = UDPConnection;
    }

    public CommandParameters getCommandParameters() throws MyException {
        return (CommandParameters) Serializer.deserialize(UDPConnection.receiveData());
    }

    public MusicBand getMusicBand() throws MyException {
        return (MusicBand) Serializer.deserialize(UDPConnection.receiveData());
    }
}
