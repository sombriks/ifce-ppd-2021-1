package br.edu.ifce.engcomp.ppd.reversi;

/**
 * Hello world!
 *
 */
public class Game {

    private GameStatus status = GameStatus.OPEN;

    public void host(){}
    
    public void join(String address){}

    public GameStatus getStatus() {
        return status;
    }
    
}
