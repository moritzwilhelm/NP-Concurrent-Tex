package com.pseuco.np19.project.rocket.monitors;

public class SynchronizedBoolean {

	private boolean value = false;

	public SynchronizedBoolean(boolean value) {
		this.value = value;
	}

	public synchronized boolean getValue() {
		return value;
	}

	public synchronized void setValue(boolean value) {
		this.value = value;
	}

}
