package br.edu.ifce.engcomp.ppd.reversi;

import java.util.HashMap;
import java.util.Map;

public class Board {

    public Square getSquaresFor(Player player) {
        if (player.equals(whites)) return Square.WHITE;
        else return Square.BLACK;
    }

    public interface ExecIt {
        void doIt(String key);
    }

    private Player turn = Player.PLAYER1;
    private Player whites = Player.PLAYER1;
    private Player blacks = Player.PLAYER2;

    private Map<String, Square> board = new HashMap<>();

    public Board() {
        scanBoard(k -> board.put(k, Square.EMPTY));
        board.put("d4", Square.WHITE);
        board.put("e4", Square.BLACK);
        board.put("d5", Square.BLACK);
        board.put("e5", Square.WHITE);
//        System.out.println(board);
    }

    private void scanBoard(ExecIt it) {
        char letters[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        int i = 9;
        while (i-- > 1) {
            int j = 8;
            while (j-- > 0) {
                String key = letters[j] + "" + i;
                it.doIt(key);
            }
        }
    }

    public Player getTurn() {
        return turn;
    }

    public void setTurn(Player turn) {
        this.turn = turn;
    }

    public Player getWhites() {
        return whites;
    }

    public void setWhites(Player whites) {
        this.whites = whites;
    }

    public Player getBlacks() {
        return blacks;
    }

    public void setBlacks(Player blacks) {
        this.blacks = blacks;
    }

    private int getWhiteScore() {
        final int scoreHolder[] = new int[]{0};
        scanBoard(k -> {
            if (board.get(k).equals(Square.WHITE)) scoreHolder[0]++;
        });
        return scoreHolder[0];
    }

    private int getBlackScore() {
        final int scoreHolder[] = new int[]{0};
        scanBoard(k -> {
            if (board.get(k).equals(Square.BLACK)) scoreHolder[0]++;
        });
        return scoreHolder[0];
    }

}
