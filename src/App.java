import java.util.Scanner;

public class App {

    //static String[][] goBoard = new String[9][9];
    static String[][] goBoard = {
                                    {null,null,"@","@",null,null,null,null,null},
                                    {null,"@","o","o","@",null,null,null,null},
                                    {null,"@","o",null,"o","@",null,null,null},
                                    {null,"@","o","o","o","@",null,null,null},
                                    {null,"@","o",null,"o","@",null,null,null},
                                    {null,null,"@","o","o","@",null,null,null},
                                    {null,null,null,"@","@",null,null,null,null},
                                    {null,null,null,null,null,null,null,null,null},
                                    {null,null,null,null,null,null,null,null,null}
                                };

    static boolean[][] visited = new boolean[9][9];
    static int blackCaptured = 0;
    static int whiteCaptured = 0;

    static void printGoBoard(String[][] goBoard) {
        for(int i = 0; i < goBoard.length; i++){
            for(int j = 0; j < goBoard.length; j++){
                if (goBoard[i][j] == null){
                    System.out.print(" + ");
                }
                else if (goBoard[i][j] == "o"){
                    System.out.print(" o ");
                }
                else if (goBoard[i][j] == "@"){
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

        if(x < 0 || x >= board.length || y < 0 || y >= board.length) {
            return false;
        }

        String cell = board[x][y];

        if(cell == null) return true;

        //if cell is already visited or is not the original piece color
        if(!cell.equals(pieceColor) || visited[x][y]) return false;

        visited[x][y] = true;

        //adjacency check
        boolean hasLiberty = hasLiberties(board, visited, x+1, y, pieceColor) ||
                            hasLiberties(board, visited, x-1, y, pieceColor) ||
                            hasLiberties(board, visited, x, y+1, pieceColor) ||
                            hasLiberties(board, visited, x, y-1, pieceColor);

        return hasLiberty;
    }


    static int removeGroup(String[][] board, int x, int y, String pieceColor) {
        // Boundary check
        if(x < 0 || x >= board.length || y < 0 || y >= board.length) {
            return 0;
        }

        String cell = board[x][y];

        // If cell doesn't match the piece color or is empty, stop
        if(cell == null || !cell.equals(pieceColor)) {
            return 0;
        }

        // Remove the piece
        board[x][y] = null;
        int count = 1;

        // Recursively remove all connected pieces of the same color
        count += removeGroup(board, x+1, y, pieceColor);
        count += removeGroup(board, x-1, y, pieceColor);
        count += removeGroup(board, x, y+1, pieceColor);
        count += removeGroup(board, x, y-1, pieceColor);

        return count;
    }


    static void captureCheck(String[][] board) {
        //check all positions on the board
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j] != null) {
                    String pieceColor = board[i][j];
                    
                    //reset visited
                    boolean[][] tempVisited = new boolean[board.length][board[0].length];
                    
                    //check for liberties and delete the groupof selected pieces
                    if(!hasLiberties(board, tempVisited, i, j, pieceColor)) {
                        //remove it
                        int capturedCount = removeGroup(board, i, j, pieceColor);
                        
                        //add captured pieces to opponents pile
                        if(pieceColor.equals("@")) {
                            whiteCaptured += capturedCount;
                            System.out.println("Black group captured! White captures " + capturedCount + " piece(s).");
                        } else if(pieceColor.equals("o")) {
                            blackCaptured += capturedCount;
                            System.out.println("White group captured! Black captures " + capturedCount + " piece(s).");
                        }
                    }
                }
            }
        }
        System.out.println("Black captured: " + blackCaptured + " | White captured: " + whiteCaptured);
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
                switch (temp) { 
                case null:
                    goBoard[y-1][x-1] = "@";
                    turn = !turn;
                    break;
                case "o":
                    System.out.println("INVALID PLACEMENT - Try Again");
                    break;
                case "@":
                    System.out.println("INVALID PLACEMENT - Try Again");
                    break;
                default:
                    break;
                }
                captureCheck(goBoard);
            }
            else if (turn == false) {
                switch (temp) { 
                case null:
                    goBoard[y-1][x-1] = "o";
                    turn = !turn;
                    break;
                case "o":
                    System.out.println("INVALID PLACEMENT - Try Again");
                    break;
                case "@":
                    System.out.println("INVALID PLACEMENT - Try Again");
                    break;
                default:
                    break;
                }
                captureCheck(goBoard);
            }
        
        }
        scn.close();
    }
}
