package br.edu.ifce.engcomp.ppd.reversi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameTest {

  @Test
  public void shouldInstantiate(){
    Game game = new Game();
    Assertions.assertNotNull(game);
  }
}
