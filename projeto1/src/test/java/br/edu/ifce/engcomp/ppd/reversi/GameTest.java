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
        g1.host();
        g2.join("localhost");
        Thread.sleep(1000);
        g1.sendText("Hi!");
        Assertions.assertEquals(GameStatus.CONNECTED, g1.getStatus());
        Assertions.assertEquals(GameStatus.CONNECTED, g2.getStatus());
    }
}
