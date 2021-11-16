package battleShipServer.server.game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Board {
    private final int size;
    private int remainingShips;
    private final List<Ship> ships = new ArrayList<Ship>();
    private final List<Point> shots = new ArrayList<Point>();
    
    public Board(int size) {
        this.size = size;
    }
    
    public void placeShips(int[][] shipsParams) {
        for (int[] shipParam : shipsParams) {
            int copies = shipParam[0];
            int length = shipParam[1];
            for (int i = 0; i < copies; i++) {
                placeShip(length);
            }
        }
        remainingShips = ships.size();
    }

    public void placeShips1() {
        String[][] board = new String[10][10];
        String x = "x";
        try {
            board = loadFile();
        }
        catch (IOException ex) {}
        List<Integer[]> shipsC = new ArrayList<Integer[]>(10); 
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if(board[i][j].equals(x)) {
                    for(Integer[] k : shipsC){
                        if((k[0] <= i && k[1] <= j) || (k[2] >= i && k[3] >= j)){
                            continue;
                        }
                    }   
                    int startX = i, startY = j, endX = i, endY = j;
                    while(endX < 9 && board[endX + 1][endY].equals(x)) {
                        endX++;
                    }
                    while(endY < 9 && board[endX][endY + 1].equals(x)) {
                        endY++;
                    }
                    Integer[] tmp = new Integer[4];
                    tmp[0] = startX;
                    tmp[1] = startY;
                    tmp[2] = endX;
                    tmp[3] = endY; 
                    shipsC.add(tmp);
                    if(startX == endX){
                        ships.add(new Ship(startY, startX, (endY - startY + 1), false, size));
                    }
                    else{
                        ships.add(new Ship(startY, startX, (endX - startX + 1), true, size));
                    }  
                }
            }
        } 
        remainingShips = 10;
    }


    public String[][] loadFile() throws IOException{
        /*int[][] board = new int[10][10];
        FileInputStream fileBoard = new FileInputStream("C:/kr/client/src/main/java/battleShipClient/client/fileBoard.txt");
        byte[] str = new byte[fileBoard.available()];
        fileBoard.read(str);
        String text = new String(str);
        String[] numbers = text.split("\\D");
        int i, j;
        for(i = 0; i < 10; i++){
            for(j = 0; j < 10; j++){
                board[i][j] = next(numbers);
            }
        }
        return board;*/

        BufferedReader br = new BufferedReader(new FileReader("C:/kr/client/src/main/java/battleShipClient/client/fileBoard.txt"));

        List<String> lines = new ArrayList<>();
        while (br.ready()) {
            lines.add(br.readLine());
        }
        int matrixWidth = lines.get(0).split(" ").length;
        int matrixHeight = lines.size();

        String[][] board = new String[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                String[] line = lines.get(i).split(" ");
                board[i][j] = line[j];
            }
        }

        return board;

    }
    
    private void placeShip(int length) {
        int x, y;
        boolean vertical;
        do {
            x = (int) (Math.random() * (size + 1 - length));
            y = (int) (Math.random() * size);
            
            vertical = Math.random() < 0.5d;
            if (vertical) {
                int xT = x;
                x = y;
                y = xT;
            }
        } while (!isFreeSpace(x, y, length, vertical));
        
        ships.add(new Ship(x, y, length, vertical, size));
    }
    
    private boolean isFreeSpace(int x, int y, int length, boolean vertical) {
        return ships.stream().noneMatch(ship -> ship.isInArea(x, y, length, vertical));
    }

    public int[] getShipsPositions() {
        int[] shipsPositions = new int[4 * ships.size()];
        int i = 0;
        for (Ship ship : ships) {
            for (int coord : ship.getPosition()) {
                shipsPositions[i] = coord;
                i++;
            }
        }
        return shipsPositions;
    }
    
    public boolean isValidShot(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size ?
               !shots.contains(new Point(x, y))
             : false;
    }
    
    public Ship checkForHit(int x, int y) {
        shots.add(new Point(x, y));
        
        for (Ship ship : ships) {
            if (ship.isInPosition(x, y)) {
                ship.hit();
                
                if (ship.isSunk()) {
                    remainingShips--;
                    feedShots(ship.getArea());   
                }
                return ship;
            }
        }
        return null;
    }
        
    private void feedShots(int[] shipArea) {
        int xA1 = shipArea[0];
        int yA1 = shipArea[1];
        int xA2 = shipArea[2];
        int yA2 = shipArea[3];
                    
        for (int i = xA1; i <= xA2; i++) {
            for (int j = yA1; j <= yA2; j++) {
                Point shot = new Point(i, j);
                if (!shots.contains(shot)) {
                    shots.add(shot);
                }
            }
        }
    }
    
    public int getRemainingShips() {
        return remainingShips;
    }
}