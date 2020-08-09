package content.image;

import java.awt.Color;

public class BoxBlur extends Filter {
	private int radius;

	public BoxBlur(int radius) {
		this.radius = radius;
	}

	@Override
	public Image apply(Image image) {
		Pixel[][] res = new Pixel[image.getHeight()][image.getWidth()];
		for (int y = 0; y < image.getHeight(); y++) {
			res[y] = new Pixel[image.getWidth()];
			for (int x = 0; x < image.getWidth(); x++) {
				long count = 0;
				long redSum = 0;
				long greenSum = 0;
				long blueSum = 0;

				for (int yy = 0; yy < radius * 2; yy++) {
					for (int xx = 0; xx < radius * 2; xx++) {
						int fx = (x + xx) - radius;
						int fy = (y + yy) - radius;
						
						if(fx >= 0 && fx <image.getWidth() && fy >= 0 && fy < image.getHeight()) {
							Pixel px = image.getPixel((x + xx) - radius, (y + yy) - radius);
							
							redSum += px.getRed();
							greenSum += px.getGreen();
							blueSum += px.getBlue();
							count++;
						}
					}
				}

				int red = Math.min(255, (int) (redSum / count));
				int green = Math.min(255, (int) (greenSum / count));
				int blue = Math.min(255, (int) (blueSum / count));

				res[y][x] = new Pixel(new Color(red, green, blue));
			}
		}
		return new Image(res);
	}
}
