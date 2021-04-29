package br.edu.ifce.engcomp.ppd.reversi;

public class MessageProcessor {

    private Player player;

    public void setPlayer(Player player){
        this.player=player;
    }

    public String makePingMessage() {
        return "messageType:" + MessageType.PING + ",message:none,player:" + player + "\n";
    }

    public String makeTextMessage(String message) {
        return "messageType:" + MessageType.TEXT + ",message:" + message + ",player:" + player + "\n";
    }
}
