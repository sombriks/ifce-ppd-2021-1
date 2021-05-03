package br.edu.ifce.engcomp.ppd.reversi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameTest {

    @Test
    public void shouldConnect() throws Exception {
        Game g1 = new Game();
        Assertions.assertEquals(GameStatus.OPEN, g1.getStatus());
        Thread.sleep(1000); // wait resource allocation
        Game g2 = new Game();
        g1.setDelay(100);
        g2.setDelay(100);
        g1.host();
        g2.join("localhost");
        Thread.sleep(1000);
        Assertions.assertEquals(GameStatus.PIECE_SELECT, g1.getStatus());
        Assertions.assertEquals(GameStatus.PIECE_SELECT, g2.getStatus());
        g1.sendText("Hi!");
        g2.sendText("Hello!");
        g1.playAs(Square.BLACK);
        Thread.sleep(1000);
        Assertions.assertEquals(GameStatus.CONNECTED, g1.getStatus());
        Assertions.assertEquals(GameStatus.CONNECTED, g2.getStatus());
        Assertions.assertEquals(Square.WHITE, g2.getMySquares());
        // illegal move at this point of the game: already taken
        g1.take("d4");
        // illegal move at this point of the game: not my turn
        g1.take("c4");
        // illegal move: gibberish
        g1.take("f117");
    }
}
