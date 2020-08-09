package content.image;

import java.awt.Color;

public class Pixel {
	private Color color;

	public Pixel(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}

	public int getRed() {
		return color.getRed();
	}

	public int getGreen() {
		return color.getGreen();
	}

	public int getBlue() {
		return color.getBlue();
	}

	public int getAlpha() {
		return color.getAlpha();
	}

	public void setRed(int red) {
		color = new Color(red, color.getGreen(), color.getBlue(), color.getAlpha());
	}

	public void setGreen(int green) {
		color = new Color(color.getRed(), green, color.getBlue(), color.getAlpha());
	}

	public void setBlue(int blue) {
		color = new Color(color.getRed(), color.getGreen(), blue, color.getAlpha());
	}

	public void setAlpha(int alpha) {
		color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}
	
	public int getRgb() {
		return color.getRGB();
	}
	
	public void setColor(Color c) {
		color = c;
	}
}
