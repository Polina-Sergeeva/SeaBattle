package battleShipClient.client;

import battleShipClient.client.gui.*;
import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException{
        ClientAction clientAction = new ClientAction();
        ClientReaction clientReaction = new ClientReaction();
        ClientGame clientGame = new ClientGame(clientAction, clientReaction);
        new ServerConnection(clientAction, clientReaction, clientGame);
        new MainWindow(clientAction, clientReaction);
    }
}