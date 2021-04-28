package br.edu.ifce.engcomp.ppd.reversi.main;

import br.edu.ifce.engcomp.ppd.reversi.Game;

public class MainClient {

    public static void main(String[] args) throws Exception {
        String addr = "localhost";
        if (args.length > 0) addr = args[0];
        Game p2 = new Game();
        p2.join(addr);
    }
}
