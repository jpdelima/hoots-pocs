package io.hoots.domain;

public class Point {

	private Chunk chunk;
	private Item item;

	public Point(Item item, int chunk) {
		this.item = item;
		this.chunk = new Chunk(chunk);
	}

	public Chunk getChunk() {
		return chunk;
	}

	public Item getItem() {
		return item;
	}
}
