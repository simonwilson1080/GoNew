import java.util.Scanner;

public class App {

    static String[][] goBoard = new String[9][9];

    //checker
    static boolean[][] lives = new boolean[9][9];
    static boolean[][] territory = new boolean[9][9];
    static boolean[][] beenChecked = new boolean[9][9];

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
                    System.out.print(" + ");
                }
            }
            System.out.println();
        }
    }

    static void scoreCheck(String[][] goBoard) {

    }

    static void captureCheck(int[][] board, boolean[][] visited, int r, int c, int t, int n) {

        if(r < 0 || r >= board.length ||  c < 0 || c >= board[0].length || board[r][c] != t || board[r][c]==n) {
            return;
        }
        if(visited == null || visited.length != board.length || visited[0].length != board[0].length) {
            return;
        }
        if(visited[r][c] || board[r][c] != n) return;

        visited[r][c] = true;
        board[r][c] = n;

        captureCheck (board, visited, r+1, c, t, n);
        captureCheck (board, visited, r-1, c, t, n);
        captureCheck (board, visited, r, c+1, t, n);
        captureCheck (board, visited, r, c-1, t, n);
    }

    public static void main(String[] args) throws Exception {
        boolean playing = true;
        boolean turn = true;
        int x = 1;
        int y = 1;
        Scanner scn = new Scanner(System.in);


        while (playing) {
            printGoBoard(goBoard);

            if (turn == true) System.out.println("Whites Turn");
            else if (turn == false) System.out.println("Blacks Turn");

            System.out.print("Enter X coordinates:");
            x = scn.nextInt();
            System.out.print("Enter Y coordinates:");
            y = scn.nextInt();

            String temp = goBoard[y-1][x-1];
            //ⵔ⬤⚪⚫

            if (turn == true) {
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
            }
            else if (turn == false) {
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
            
            }
        
        }
        scn.close();
    }
}
