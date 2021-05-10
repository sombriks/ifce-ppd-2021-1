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
    private Board board = new Board();
    private MessageProcessor processor = new MessageProcessor(this);
    private List<String> messageBuffer = new ArrayList<>();
    private List<String> chatHistory = new ArrayList<>();
    private long delay = 3000;

    public void host() throws IOException {
        processor.setPlayer(Player.PLAYER1);
        new Thread(() -> {
            try (ServerSocketChannel server = ServerSocketChannel.open()) {
                server.bind(new InetSocketAddress(8765));
                try {
                    SocketChannel channel = server.accept();
                    status = GameStatus.PIECE_SELECT;
                    Scanner s = new Scanner(channel);
                    while (status != GameStatus.FINISHED) {
                        readMessage(s);
                        writeMessage(channel);
                        Thread.sleep(delay);
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
        processor.setPlayer(Player.PLAYER2);
        new Thread(() -> {
            try {
                SocketChannel channel = SocketChannel.open(new InetSocketAddress(address, 8765));
                status = GameStatus.PIECE_SELECT;
                Scanner s = new Scanner(channel);
                while (status != GameStatus.FINISHED) {
                    writeMessage(channel);
                    readMessage(s);
                    Thread.sleep(delay);
                }
            } catch (Exception e) {
                e.printStackTrace();
                status = GameStatus.FINISHED;
            }
        }).start();
    }

    private void readMessage(Scanner s) {
        String message = s.nextLine();
        processor.processMessage(message);
    }

    private void writeMessage(SocketChannel channel) throws IOException {
        String message = null;
        if (messageBuffer.size() > 0)
            message = messageBuffer.remove(0);
        else
            message = processor.makePingMessage();
        channel.write(ByteBuffer.wrap(message.getBytes()));
        processor.addHistory(message);
    }

    public GameStatus getStatus() {
        return status;
    }

    public List<String> getChatHistory() {
        return chatHistory;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void sendText(String message) {
        message = message.replaceAll(",:\\n", "");// protocol reserved chars
        messageBuffer.add(processor.makeTextMessage(message));
    }

    public void playAs(Square piece) {
        if (status.equals(GameStatus.PIECE_SELECT)) {
            if (Square.BLACK.equals(piece)) {
                board.setBlacks(processor.getPlayer());
                board.setWhites(processor.getOtherPlayer());
                board.setTurn(processor.getOtherPlayer());
                if (processor.getPlayer().equals(Player.PLAYER1))
                    messageBuffer.add(processor.makePieceSet(Square.WHITE));
                status = GameStatus.CONNECTED;
            } else if (Square.WHITE.equals(piece)) {
                board.setWhites(processor.getPlayer());
                board.setBlacks(processor.getOtherPlayer());
                board.setTurn(processor.getPlayer());
                if (processor.getPlayer().equals(Player.PLAYER1))
                    messageBuffer.add(processor.makePieceSet(Square.BLACK));
                status = GameStatus.CONNECTED;
            }
        }
    }

    public Square getMySquares() {
        return board.getSquaresFor(processor.getPlayer());
    }

    public void tryTake(String move) {
    	if(board.isMyTurn(processor.getPlayer()))
    		messageBuffer.add(processor.makeMoveMessage(move));
    	else System.out.println("Not my turn");
    }

	public void take(Move square) {
		// TODO Auto-generated method stub
		System.out.println(square);
		
	}
}
