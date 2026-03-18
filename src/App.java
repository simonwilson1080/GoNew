/***
 * Author: Simon Wilson
 * Description: Go Simulator with basic rules and no territory scoring.
 * Suicide Check is there but only for basic cases and fails on edge cases.
 ***/

import java.util.Scanner;

public class App {

    //static String[][] goBoard = new String[9][9];
    static String[][] goBoard = {
                                    {null,null,null,null,null,null,null,null,null},
                                    {null,null,null,null,null,null,null,null,null},
                                    {null,null,null,null,null,null,null,null,null},
                                    {null,null,null,null,"@",null,null,null,null},
                                    {null,null,null,"@",null,"@",null,null,null},
                                    {null,null,null,null,"@",null,null,null,null},
                                    {null,null,null,null,null,null,null,null,null},
                                    {null,null,null,null,null,null,null,null,null},
                                    {null,null,null,null,null,null,null,null,null}
                                };

    static boolean[][] visited = new boolean[9][9];
    static int blackCaptured = 0;
    static int whiteCaptured = 0;

    static void printGoBoard(String[][] goBoard) {

        for(int i = 0; i < goBoard.length; i++){
            for(int j = 0; j < goBoard.length; j++){
                if(goBoard[i][j] == null){
                    System.out.print(" + ");
                }
                else if(goBoard[i][j] == "o"){
                    System.out.print(" o ");
                }
                else if(goBoard[i][j] == "@"){
                    System.out.print(" @ ");
                }
                else{
                    System.out.print(" 8 ");
                }
            }
            System.out.println();
        }
    }


    static boolean hasLiberties(String[][] board, boolean[][] visited, int x, int y, String pieceColor) {

        if(x < 0 || x > board.length || y < 0 || y > board.length) return false;

        String cell = board[x][y];

        if(cell == null) return true;

        if(cell != pieceColor || visited[x][y]) return false;

        visited[x][y] = true;

        // check adjacent pieces
        boolean hasLiberty =    hasLiberties(board, visited, x+1, y, pieceColor) ||
                                hasLiberties(board, visited, x-1, y, pieceColor) ||
                                hasLiberties(board, visited, x, y+1, pieceColor) ||
                                hasLiberties(board, visited, x, y-1, pieceColor);

        return hasLiberty;
    }


    static int removeGroup(String[][] board, int x, int y, String pieceColor) {

        if(x < 0 || x >= board.length || y < 0 || y >= board.length) return 0;

        String cell = board[x][y];

        if(cell == null || cell != pieceColor) return 0;

        // remove piece
        board[x][y] = null;
        int count = 1;

        // remove connected pieces that are the same color
        count += removeGroup(board, x+1, y, pieceColor);
        count += removeGroup(board, x-1, y, pieceColor);
        count += removeGroup(board, x, y+1, pieceColor);
        count += removeGroup(board, x, y-1, pieceColor);

        return count;
    }


    static boolean suicideCheck(String[][] board, int x, int y, String pieceColor) {
        /*
        if I call hasLiberties here pretending that the space im checking in this function is
        occupied with pieceColor and it returns false, then I can just return true on this
        function, then suicideCheck might work for large groups of surrounded pieces.
        */

        if(x < 0 || x > board.length || y < 0 || y > board.length) return false;

        String opp = (pieceColor == "@") ? "o" : "@";

        if((board[x+1][y] == opp || x+1 > board.length) && (board[x-1][y] == opp || x-1 < 0) && (board[x][y+1] == opp || y+1 > board.length) && (board[x][y-1] == opp || y-1 < 0)) {
                return true;
        }
        return false;
        //function works but fails on edge cases...
    }


    static void captureCheck(String[][] board) {
        // check all positions on board
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j] != null) {
                    String pieceColor = board[i][j];
                    
                    boolean[][] tempVisited = new boolean[board.length][board[0].length];
                    
                    // check for liberties and delete the groupof pieces
                    if(!hasLiberties(board, tempVisited, i, j, pieceColor)) {
                        int capturedCount = removeGroup(board, i, j, pieceColor);
                        
                        // add captured pieces to opponents pile
                        if(pieceColor == "@") whiteCaptured += capturedCount;
                        else if(pieceColor =="o") blackCaptured += capturedCount;
                    }
                }
            }
        }
        System.out.println("Black capture points: " + blackCaptured + " | White capture points: " + whiteCaptured);
    }


    public static void main(String[] args) throws Exception {

        boolean playing = true;
        boolean turn = true;
        int x = 1;
        int y = 1;
        Scanner scn = new Scanner(System.in);

        while (playing) {
            printGoBoard(goBoard);

            if (turn == true) System.out.println("Blacks Turn");
            else if (turn == false) System.out.println("Whites Turn");

            System.out.print("Enter X coordinates:");
            x = scn.nextInt();
            System.out.print("Enter Y coordinates:");
            y = scn.nextInt();

            String temp = goBoard[y-1][x-1];

            if (turn == true) {
                if(suicideCheck(goBoard, x-1, y-1, "@") == true) System.out.println("Illegal Move. Try again.");
                else if(temp == null) {
                    goBoard[y-1][x-1] = "@";
                    turn = !turn;
                }
                else if("o" == temp || "@" == temp) {
                    System.out.println("INVALID PLACEMENT - Try Again");
                }
                captureCheck(goBoard);
            }

            else if (turn == false) {
                if(suicideCheck(goBoard, x-1, y-1, "o") == true) System.out.println("Illegal Move. Try again.");
                else if(temp == null) {
                    goBoard[y-1][x-1] = "o";
                    turn = !turn;
                }
                else if("o" == temp || "@" == temp) {
                    System.out.println("INVALID PLACEMENT - Try Again");
                }
                captureCheck(goBoard);
            }
        
        }
        scn.close();
    }
}
