package content.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Image {
	private int width;
	private int height;
	private Pixel[][] pixels;

	public Image(Pixel[][] pixels) {
		this.pixels = pixels;
		height = pixels.length;
		width = pixels[0].length;
	}

	public Image(BufferedImage bm) {
		width = bm.getWidth();
		height = bm.getHeight();
		pixels = new Pixel[height][width];
		for (int y = 0; y < height; y++) {
			pixels[y] = new Pixel[width];
			for (int x = 0; x < width; x++) {
				pixels[y][x] = new Pixel(new Color(bm.getRGB(x, y)));
			}
		}
	}

	public Pixel getPixel(int x, int y) {
		return pixels[y][x];
	}

	public void draw(Graphics2D g2d, int offX, int offY) {
		BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (pixels[y][x] != null)
					buffer.setRGB(x, y, pixels[y][x].getColor().getRGB());
			}
		}
		g2d.drawImage(buffer, offX, offY, null);
		buffer.flush();
	}

	public void setOpacity(double opacity) {
		int alpha = (int) (opacity * 255.0);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (pixels[y][x] != null)
					pixels[y][x].setAlpha(alpha);
			}
		}
	}

	public Image copy() {
		Pixel[][] res = new Pixel[height][width];

		for (int y = 0; y < height; y++) {
			res[y] = new Pixel[width];

			for (int x = 0; x < width; x++) {
				res[y][x] = new Pixel(new Color(pixels[y][x].getRgb()));
			}
		}

		return new Image(res);
	}

	public Image applyFilter(Filter filter) {
		return filter.apply(this);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
