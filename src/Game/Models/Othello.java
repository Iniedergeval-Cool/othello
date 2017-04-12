package Game.Models;

/**
 * Created by Ruben on 10-Apr-17.
 */
public class Othello {

    private String[][] board;

    public Othello() {
        this.board = new String[8][8];

        this.initFirstState();
    }

    private void initFirstState() {
        this.board[4][4] = "x";
        this.board[5][5] = "x";

        this.board[4][5] = "o";
        this.board[5][4] = "o";
    }
}