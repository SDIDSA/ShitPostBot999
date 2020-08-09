package memes;

import java.awt.Rectangle;

public class Location {
	private int rgba;
	private Rectangle rectangle;

	public Location(int rgba, Rectangle rectangle) {
		this.rgba = rgba;
		this.rectangle = rectangle;
	}
	
	public int getRgba() {
		return rgba;
	}
	
	public int getStartX() {
		return (int) rectangle.getX();
	}
	
	public int getStartY() {
		return (int) rectangle.getY();
	}
	
	public int getEndX() {
		return (int) (rectangle.getX() + rectangle.getWidth());
	}
	
	public int getEndY() {
		return (int) (rectangle.getY() + rectangle.getHeight());
	}
	
	public void setStartX(int x) {
		int newWidth = (int) ((rectangle.getX() + rectangle.getWidth()) - x);
		rectangle.setLocation(x, (int) rectangle.getY());
		rectangle.setSize(newWidth, (int) rectangle.getHeight());
	}
	
	public void setStartY(int y) {
		int newHeight = (int) ((rectangle.getY() + rectangle.getHeight()) - y);
		rectangle.setLocation((int) rectangle.getX(), y);
		rectangle.setSize((int) rectangle.getWidth(), newHeight);
	}
	
	public void setEndX(int x) {
		rectangle.setSize((int) (x - rectangle.getX()), (int) rectangle.getHeight());
	}
	
	public void setEndY(int y) {
		rectangle.setSize((int) rectangle.getWidth(),(int) (y - rectangle.getY()));
	}
	
	public int getWidth() {
		return (int) rectangle.getWidth();
	}
	
	public int getHeight() {
		return (int) rectangle.getHeight();
	}

	public Rectangle intersection(Location loc2) {
		return rectangle.intersection(loc2.rectangle);
	}
	
	@Override
	public String toString() {
		return "Location [rgba=" + rgba + ", rectangle=" + rectangle + "]";
	}
}
