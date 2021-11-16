package battleShipClient.client;

import battleShipClient.common.*;
import battleShipClient.client.gui.*;

public class ClientReaction {
    private ServerConnection serverConnection;
    private ClientGame clientGame;
    private MainWindow mainWindow;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    
    public void setServerConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }
    
    public void setClientGame(ClientGame clientGame) {
        this.clientGame = clientGame;
    }
    
    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }
    
    public void setMenuPanel(MenuPanel menuPanel) {
        this.menuPanel = menuPanel;
    }
    
    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    
    public void disconnected() {
        menuPanel.unlockMenu();
        mainWindow.displayStatus(Text.DISCONNECTED, false);
        mainWindow.hideGameView();
        mainWindow.hidePlayAgain();
    }
    
    public void hello() {
        mainWindow.displayStatus(Text.WAITING, true);
        mainWindow.hideGameView();
        mainWindow.hidePlayAgain();
    }
    
    public void bye() {
        mainWindow.displayStatus(Text.SERVER_OFF, true);
        mainWindow.hideGameView();
        mainWindow.hidePlayAgain();
    }
    
    public void serverFull() {
        mainWindow.displayStatus(Text.SERVER_FULL, true);
    }
    
    public void configuration(int[] shipsPositions) {
        mainWindow.displayStatus(Text.CONFIGURATION, true);
        gamePanel.getOwnSidePanel().getBoardPanel().clear();
        gamePanel.getEnemySidePanel().getBoardPanel().clear();
        
        if (shipsPositions != null) {
            BoardPanel boardPanel = gamePanel.getOwnSidePanel().getBoardPanel();
            for (int i = 0; i < shipsPositions.length; i += 4) {
                int xP1 = shipsPositions[i];
                int yP1 = shipsPositions[i + 1];
                int xP2 = shipsPositions[i + 2];
                int yP2 = shipsPositions[i + 3];
                
                int prowSprite = Sprite.PROW_H;
                int boardSprite = Sprite.BOARD_H;
                
                if (yP1 == yP2) {
                    for (int j = xP1 + 1; j <= xP2; j++) {
                        boardPanel.markShip(j, yP1, boardSprite);
                    }
                } else {
                    prowSprite = Sprite.PROW_V;
                    boardSprite = Sprite.BOARD_V;
                    for (int j = yP1 + 1; j <= yP2; j++) {
                        boardPanel.markShip(xP1, j, boardSprite);
                    }
                }
                
                boardPanel.markShip(xP1, yP1, prowSprite);
            }
        
            boardPanel.repaint();
            gamePanel.getOwnSidePanel().displayShipsCounter(clientGame.getOwnShipsCounter());
            gamePanel.getEnemySidePanel().displayShipsCounter(clientGame.getEnemyShipsCounter());
        
            serverConnection.prepareMessage(MessageType.READY);
            serverConnection.send();
        }
    }

    /*public void configuration1(int[] shipsPositions) {
        mainWindow.displayStatus(Text.CONFIGURATION, true);
        gamePanel.getOwnSidePanel().getBoardPanel().clear();
        gamePanel.getEnemySidePanel().getBoardPanel().clear();
        
        BoardPanel boardPanel = gamePanel.getOwnSidePanel().getBoardPanel();
        for (int i = 0; i < shipsPositions.length; i += 2) {
            int x = shipsPositions[i];
            int y = shipsPositions[i + 1];
                
            int prowSprite = Sprite.PROW_H;
            int boardSprite = Sprite.BOARD_H;
                
            boardPanel.markShip(x, y, boardSprite);
        }
        
            boardPanel.repaint();
            gamePanel.getOwnSidePanel().displayShipsCounter(clientGame.getOwnShipsCounter());
            gamePanel.getEnemySidePanel().displayShipsCounter(clientGame.getEnemyShipsCounter());
        
            serverConnection.prepareMessage(MessageType.READY);
            serverConnection.send();
    }*/

    
    public void gameStart() {
        mainWindow.displayStatus(Text.GAME_STARTED, true);
        mainWindow.showGameView();
    }
    
    public void yourTurn() {
        mainWindow.displayStatus(Text.YOUR_TURN, false);
    }
    
    public void enemyTurn() {
        mainWindow.displayStatus(Text.ENEMY_TURN, false);
    }
    
    public void hit(int x, int y) {        
        mainWindow.displayStatus(Text.HIT, true);
        gamePanel.getEnemySidePanel().getBoardPanel().markShip(x, y, Sprite.REVEAL);
        gamePanel.getEnemySidePanel().getBoardPanel().markShot(x, y);
        gamePanel.getEnemySidePanel().getBoardPanel().repaint();
    }
    
    public void enemyHit(int x, int y) {
        mainWindow.displayStatus(Text.ENEMY_HIT, true);
        gamePanel.getOwnSidePanel().getBoardPanel().markShot(x, y);
        gamePanel.getOwnSidePanel().getBoardPanel().repaint();
    }
    
    public void missed(int x, int y) {
        mainWindow.displayStatus(Text.MISSED, true);
        gamePanel.getEnemySidePanel().getBoardPanel().markShot(x, y);
        gamePanel.getEnemySidePanel().getBoardPanel().repaint();
    }
    
    public void enemyMissed(int x, int y) {
        mainWindow.displayStatus(Text.ENEMY_MISSED, true);
        gamePanel.getOwnSidePanel().getBoardPanel().markShot(x, y);
        gamePanel.getOwnSidePanel().getBoardPanel().repaint();
    }
    
    public void sunk(int xP1, int yP1, int xP2, int yP2, int xO1, int yO1, int xO2, int yO2) {
        mainWindow.displayStatus(Text.SUNK, true);
        BoardPanel boardPanel = gamePanel.getEnemySidePanel().getBoardPanel();

        int prowSprite = Sprite.PROW_H;
        int boardSprite = Sprite.BOARD_H;

        if (yP1 == yP2) {
            for (int j = xP1 + 1; j <= xP2; j++) {
                boardPanel.markShip(j, yP1, boardSprite);
            }
        } else {
            prowSprite = Sprite.PROW_V;
            boardSprite = Sprite.BOARD_V;
            for (int j = yP1 + 1; j <= yP2; j++) {
                boardPanel.markShip(xP1, j, boardSprite);
            }
        }

        boardPanel.markShip(xP1, yP1, prowSprite);
        for (int i = xO1; i <= xO2; i++) {
            for (int j = yO1; j <= yO2; j++) {
                boardPanel.markShot(i, j);
            }
        }

        boardPanel.repaint();
        gamePanel.getEnemySidePanel().displayShipsCounter(clientGame.getEnemyShipsCounter());
    }
    
    public void enemySunk(int xP1, int yP1, int xP2, int yP2, int xO1, int yO1, int xO2, int yO2) {
        mainWindow.displayStatus(Text.ENEMY_SUNK, true);
        BoardPanel boardPanel = gamePanel.getOwnSidePanel().getBoardPanel();

        int prowSprite = Sprite.PROW_H;
        int boardSprite = Sprite.BOARD_H;

        if (yP1 == yP2) {
            for (int j = xP1 + 1; j <= xP2; j++) {
                boardPanel.markShip(j, yP1, boardSprite);
            }
        } else {
            prowSprite = Sprite.PROW_V;
            boardSprite = Sprite.BOARD_V;
            for (int j = yP1 + 1; j <= yP2; j++) {
                boardPanel.markShip(xP1, j, boardSprite);
            }
        }

        boardPanel.markShip(xP1, yP1, prowSprite);
        for (int i = xO1; i <= xO2; i++) {
            for (int j = yO1; j <= yO2; j++) {
                boardPanel.markShot(i, j);
            }
        }

        boardPanel.repaint();
        gamePanel.getOwnSidePanel().displayShipsCounter(clientGame.getOwnShipsCounter());
    }
            
    public void youWon() {
        mainWindow.displayStatus(Text.YOU_WON, true);
        mainWindow.showPlayAgain();
    }
    
    public void youLost() {
        mainWindow.displayStatus(Text.YOU_LOST, true);
        mainWindow.showPlayAgain();
    }
    
    public void enemyDisconnected() {
        mainWindow.displayStatus(Text.ENEMY_DISCONNECTED, true);
        mainWindow.showPlayAgain();
    }
}