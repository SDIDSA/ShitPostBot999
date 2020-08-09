package content.image;

import java.awt.Color;

public class BlackAndWhite extends Filter {

	@Override
	public Image apply(Image image) {
		Image copy = image.copy();

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int r = copy.getPixel(x, y).getRed();
				int g = copy.getPixel(x, y).getGreen();
				int b = copy.getPixel(x, y).getBlue();
				
				int gray = (r + g + b) / 3;
				
				copy.getPixel(x, y).setColor(new Color(gray, gray, gray));
			}
		}

		return copy;
	}

}
