package io.hoots.domain;

import java.util.UUID;

public class Point {

	private int time;
	private UUID id;

	public Point(UUID id, int time) {
		this.id = id;
		this.time = time;
	}

	public int getTime() {
		return time;
	}

	public UUID getId() { return id; }
}
