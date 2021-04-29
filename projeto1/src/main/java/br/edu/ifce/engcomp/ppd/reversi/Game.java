package br.edu.ifce.engcomp.ppd.reversi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {

    private GameStatus status = GameStatus.OPEN;
    private Player player = Player.PLAYER1;
    private Board board = new Board();
    private MessageProcessor processor = new MessageProcessor();
    private List<String> messageHistory = new ArrayList<>();
    private List<String> messageBuffer = new ArrayList<>();

    public void host() throws IOException {
        new Thread(() -> {
            try (ServerSocketChannel server = ServerSocketChannel.open()) {
                server.bind(new InetSocketAddress(8765));
                try {
                    SocketChannel channel = server.accept();
                    status = GameStatus.CONNECTED;
                    Scanner s = new Scanner(channel);
                    while (status != GameStatus.FINISHED) {
                        String message = s.nextLine();
                        messageHistory.add(message);
                        System.out.println(message);
                        // TODO process message

                        String reply = null;
                        if (messageBuffer.size() > 0)
                            reply = messageBuffer.remove(0);
                        else
                            reply = processor.makePingMessage();
                        channel.write(ByteBuffer.wrap(reply.getBytes()));
                        messageHistory.add(reply);
                        // TODO process message
                        Thread.sleep(5000);
                    }
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
                status = GameStatus.CONNECTED;
                Scanner s = new Scanner(channel);
                while (status != GameStatus.FINISHED) {
                    String message = null;
                    if (messageBuffer.size() > 0)
                        message = messageBuffer.remove(0);
                    else
                        message = processor.makePingMessage();
                    channel.write(ByteBuffer.wrap(message.getBytes()));
                    messageHistory.add(message);
                    // TODO process message

                    String reply = s.nextLine();
                    messageHistory.add(reply);
                    System.out.println(reply);
                    // TODO process message
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


    public void sendText(String message) {
        messageBuffer.add(processor.makeTextMessage(message));
    }
}
