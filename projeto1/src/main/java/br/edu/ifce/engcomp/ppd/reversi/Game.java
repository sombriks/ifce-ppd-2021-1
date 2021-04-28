package br.edu.ifce.engcomp.ppd.reversi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Game {

    private GameStatus status = GameStatus.OPEN;
    private Player player = Player.PLAYER1;
    private Board board = new Board();

    public void host() throws IOException {
        new Thread(() -> {
            try (ServerSocketChannel server = ServerSocketChannel.open()) {
                server.bind(new InetSocketAddress(8765));
                try {
                    SocketChannel channel = server.accept();
                    status = GameStatus.STARTED;
                    Scanner s = new Scanner(channel);
                    while (status != GameStatus.FINISHED) {
                        String ping = s.nextLine();
                        System.out.println("player 1 got " + ping);
                        String pong = "pong\n";
                        channel.write(ByteBuffer.wrap(pong.getBytes()));
                        Thread.sleep(5000);
                    }
                    System.out.println("yay");
                } catch (Exception e) {
                    e.printStackTrace();
                    status = GameStatus.FINISHED;
                }
            } catch (IOException e) {
                e.printStackTrace();
                status = GameStatus.FINISHED;
            }
        }).start();
    }

    public void join(String address) throws IOException {
        player = Player.PLAYER2;
        new Thread(() -> {
            try {
                SocketChannel channel = SocketChannel.open(new InetSocketAddress(address, 8765));
                status = GameStatus.STARTED;
                Scanner s = new Scanner(channel);
                while (status != GameStatus.FINISHED) {
                    String ping = "ping\n";
                    channel.write(ByteBuffer.wrap(ping.getBytes()));
                    String pong = s.nextLine();
                    System.out.println("player 2 got " + pong);
                    Thread.sleep(5000);
                }
            } catch (Exception e) {
                e.printStackTrace();
                status = GameStatus.FINISHED;
            }
        }).start();
    }

    public GameStatus getStatus() {
        return status;
    }

}
