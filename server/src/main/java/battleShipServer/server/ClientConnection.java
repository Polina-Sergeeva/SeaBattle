package battleShipServer.server;

import battleShipServer.common.*;
import java.net.*;
import java.nio.*;

public class ClientConnection extends Connection {
    private final Server server;
    private ServerGame serverGame;
    private boolean isValidated;
    private boolean isVal = false;
    
    public ClientConnection(Socket socket, Server server) {
        super(socket);
        this.server = server;
    }
    
    public void setGame(ServerGame srvGame) {
        this.serverGame = srvGame;
    }
    
    public void clearGame() {
        serverGame = null;
    }
    
    public boolean isInGame() {
        return serverGame != null;
    }
    
    public void farewell() {
        prepareMessage(MessageType.BYE);
        send();
    }
    
    @Override
    public void disconnect() {
        super.disconnect();        
        server.removeClient(this);
    }
    
    @Override
    protected void receiveMessage(int msgType, ByteBuffer data) {
        if (isValidated) {
            if (msgType == MessageType.HELLO) {
                prepareMessage(MessageType.HELLO);
                send();
                server.makeGame(this);
                isVal = true;
            } else if (msgType == 999) {
                prepareMessage(MessageType.HELLO);
                send();
                server.makeGame2(this);
                isVal = true;
            } else if (isInGame()) {
                serverGame.parseMessage(this, msgType, data);
            }
        } else if (msgType == MessageType.HELLO) {
            firstContact();
            isVal = true;
        }else if(msgType == 999){
            firstContact1();
            isVal = true;
        }
        else if(msgType == 999 && isVal == true){
            firstContact2();
        }
        else {
            disconnect();
        }
    }
    
    private void firstContact() {
        isValidated = true;
        isVal = true;
        prepareMessage(MessageType.HELLO);
        send();
        if (server.addClient(this)) {
            server.makeGame(this);
        } else {
            prepareMessage(MessageType.SERVER_FULL);
            send();
            disconnect();
        }
    }

    private void firstContact2() {
        isValidated = true;
        isVal = true;
        prepareMessage(MessageType.HELLO);
        send();
        if (server.addClient(this)) {
            server.makeGame2(this);
        } else {
            prepareMessage(MessageType.SERVER_FULL);
            send();
            disconnect();
        }
    }
    
    private void firstContact1() {
        isVal = true;
        isValidated = true;
        prepareMessage(MessageType.HELLO);
        send();
        if (server.addClient(this)) {
            server.makeGame1(this);
        } else {
            prepareMessage(MessageType.SERVER_FULL);
            send();
            disconnect();
        }
    }
}