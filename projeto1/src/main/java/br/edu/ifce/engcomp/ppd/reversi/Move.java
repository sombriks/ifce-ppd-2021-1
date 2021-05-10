package br.edu.ifce.engcomp.ppd.reversi;

public class Move {

	private String move;
	private Player player;

	public Move(String move, Player player) {
		this.player = player;
		this.move = move;
	}

	@Override
	public String toString() {
		return player + " - " + move;
	}
}
