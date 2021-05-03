package br.edu.ifce.engcomp.ppd.reversi;

import java.util.ArrayList;
import java.util.List;

public class MessageProcessor {

    private Game game;
    private Player player;
    private List<String> messageHistory = new ArrayList<>();

    public MessageProcessor(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<String> getMessageHistory() {
        return messageHistory;
    }

    public String makePingMessage() {
        return "messageType:" + MessageType.PING + ",message:none,player:" + player + "\n";
    }

    public String makeTextMessage(String message) {
        return "messageType:" + MessageType.TEXT + ",message:" + message + ",player:" + player + "\n";
    }

    public String makePieceSet(Square piece) {
        return "messageType:" + MessageType.PIECE_SET + ",message:" + piece + ",player:" + player + "\n";
    }

    public void processMessage(String message) {
        addHistory(message);
        MessageType type = extractType(message);
        switch (type) {
            case PIECE_SET:
                Square piece = extractPiece(message);
                game.playAs(piece);
            break;
        }
    }

    private Square extractPiece(String message) {
        String piece = message.split(",")[1].split(":")[1];
        return Square.valueOf(piece);
    }

    private MessageType extractType(String message) {
        String type = message.split(",")[0].split(":")[1];
        return MessageType.valueOf(type);
    }

    public void addHistory(String message) {
        System.out.println(message);
        messageHistory.add(message);
        if (messageHistory.size() > 1000)
            messageHistory.remove(0);
    }
}
