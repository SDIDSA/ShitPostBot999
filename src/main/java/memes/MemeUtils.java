package memes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import mainTasks.LogHandler;

public class MemeUtils {
	public static GeneratedMeme generateMeme(LogHandler logHandler) throws IOException {
		boolean found = false;
		MemeTemplate temp = null;
		while (!found) {
			temp = ShitpostBotDatabase.getTemplate(logHandler);
			if (temp.getLocations().size() > 0 && temp.getLocations().size() < 8) {
				found = true;
			}
		}
		return generateMeme(temp, logHandler);
	}

	public static GeneratedMeme generateMeme(MemeTemplate temp, LogHandler logHandler) throws IOException {
		ColorUtils cut = new ColorUtils();
		ArrayList<String> sources = new ArrayList<String>();
		HashMap<String, MemeSource> sourcePerColor = new HashMap<String, MemeSource>();

		BufferedImage res = new BufferedImage(temp.getDesign().getWidth(), temp.getDesign().getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics gc = res.getGraphics();
		
		gc.drawImage(temp.getDesign(), 0, 0, null);
		gc.drawImage(temp.getTemplate(), 0, 0, null);

		if (temp.getLocations().size() != 0) {
			for (Location loc : temp.getLocations()) {
				String name = cut.getColorNameFromHex(loc.getRgba());
				MemeSource source = sourcePerColor.get(name);
				if (source == null) {
					logHandler.append("\tdownloading a source image");
					source = ShitpostBotDatabase.getSource(logHandler);
					sourcePerColor.put(name, source);
					sources.add(source.getLink());
				}
				gc.drawImage(resize(source.getImage(), loc.getWidth(), loc.getHeight()), loc.getStartX(),
						loc.getStartY(), null);
			}
		}

		if (temp.getOverlay() != null) {
			gc.drawImage(temp.getOverlay(), 0, 0, null);
		}
		logHandler.append("\toverlaying everything...");
		return new GeneratedMeme(temp.getLink(), res, sources);
	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}
}
