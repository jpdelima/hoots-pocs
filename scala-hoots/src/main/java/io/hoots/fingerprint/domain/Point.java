package io.hoots.fingerprint.domain;

import io.hoots.input.domain.Item;

public class Point {

	private Chunk chunk;
	private Item item;

	public Point(Item item, Chunk chunk) {
		this.item = item;
		this.chunk = chunk;
	}

	public Chunk getChunk() {
		return chunk;
	}

	public Item getItem() {
		return item;
	}
}
