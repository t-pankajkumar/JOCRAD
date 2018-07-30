package com.ocrad;

public class Pixmap {
	private String data;
	private int height;
	private int width;
	private int mode;
	
	public Pixmap(String data, int height, int width, int mode) {
		this.data = data;
		this.height = height;
		this.width = width;
		this.mode = mode;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

}
