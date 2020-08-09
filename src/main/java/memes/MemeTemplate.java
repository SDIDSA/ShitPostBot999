package memes;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.function.Predicate;

import mainTasks.LogHandler;

public class MemeTemplate {
	private String name;
	private String link;
	private BufferedImage template;
	private BufferedImage design;
	private BufferedImage overlay;

	private ArrayList<Location> locations;

	public MemeTemplate(String name, String link, BufferedImage template, BufferedImage design, BufferedImage overlay, LogHandler logHandler) {
		this.name = name;
		this.link = link;
		this.template = template;
		this.design = design;
		this.overlay = overlay;

		int width = template.getWidth();
		int height = template.getHeight();

		logHandler.append("\tresizing template images to be at the same size");
		
		if (design.getWidth() < width) {
			width = design.getWidth();
		}

		if (overlay != null && overlay.getWidth() < width) {
			width = overlay.getWidth();
		}

		if (design.getHeight() < height) {
			height = design.getHeight();
		}

		if (overlay != null && overlay.getHeight() < height) {
			height = overlay.getHeight();
		}

		template = resize(template, width, height);
		design = resize(design, width, height);
		if (overlay != null) {
			overlay = resize(overlay, width, height);
		}
		

		logHandler.append("\tanalysing template for possible image slots...");
		
		locations = new ArrayList<Location>();

		BufferedImage compareTo = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		compareTo.getGraphics().drawImage(template, 0, 0, null);
		if (overlay != null)
			compareTo.getGraphics().drawImage(overlay, 0, 0, null);
		int thresh = 5;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int argb1 = design.getRGB(x, y);
				int argb2 = compareTo.getRGB(x, y);
				if (!compare(argb1, argb2)) {
					boolean found = false;
					for (Location loc : locations) {
						if (compare(loc.getRgba(), argb1) && x >= loc.getStartX() - thresh && x <= loc.getEndX() + thresh
								&& y >= loc.getStartY() - thresh && y <= loc.getEndY() + thresh) {
							found = true;
							if (x < loc.getStartX()) {
								loc.setStartX(x);
							}
							if (x > loc.getEndX()) {
								loc.setEndX(x);
							}
							if (y < loc.getStartY()) {
								loc.setStartY(y);
							}

							if (y > loc.getEndY()) {
								loc.setEndY(y);
							}
						}
					}
					if (!found) {
						locations.add(new Location(argb1, new Rectangle(x, y, 0, 0)));
					}
				}
			}
		}
		locations.removeIf(new Predicate<Location>() {
			public boolean test(Location l) {
				return l.getHeight() <= 10 || l.getWidth() <= 10;
			}
		});
		
		ArrayList<Location> toRemove = new ArrayList<Location>();
		for(Location loc1: locations) {
			for(Location loc2: locations) {
				if(loc1 != loc2) {
					Rectangle inter = loc1.intersection(loc2);
					if(inter.getWidth() >= (loc1.getWidth() * .5) && inter.getHeight() >= (loc1.getHeight() * .5)) {
						if(!toRemove.contains(loc1) && !toRemove.contains(loc2)) {
							toRemove.add(loc1);
						}
					}
				}
			}
		}
		
		locations.removeAll(toRemove);
		
		logHandler.append("\tfound " + locations.size() + " slot" + (locations.size() != 1 ? "s":""));
	}

	private static boolean compare(int argb1, int argb2) {
		int r1 = (argb1 >> 16) & 0xFF;
		int g1 = (argb1 >> 8) & 0xFF;
		int b1 = (argb1) & 0xFF;

		int a2 = (argb2 >> 24) & 0xFF;
		int r2 = (argb2 >> 16) & 0xFF;
		int g2 = (argb2 >> 8) & 0xFF;
		int b2 = (argb2) & 0xFF;

		if ((a2 == 0 || (r2 < 20 && g2 < 20 && b2 < 20))
				&& ((r1 < 20 && g1 < 20 && b1 < 20) || (r1 > 230 && g1 > 230 && b1 > 230))) {
			return true;
		}

		int dr = Math.abs(r1 - r2);
		int dg = Math.abs(g1 - g2);
		int db = Math.abs(b1 - b2);
		int d = dr + dg + db;
		if (d > 15) {
			return false;
		} else {
			return true;
		}
	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public BufferedImage getTemplate() {
		return template;
	}

	public void setTemplate(BufferedImage template) {
		this.template = template;
	}

	public BufferedImage getDesign() {
		return design;
	}

	public void setDesign(BufferedImage design) {
		this.design = design;
	}

	public BufferedImage getOverlay() {
		return overlay;
	}

	public void setOverlay(BufferedImage overlay) {
		this.overlay = overlay;
	}

	public ArrayList<Location> getLocations() {
		return locations;
	}

	@Override
	public String toString() {
		return "MemeTemplate [\n\tname=" + name + "\n\tlink=" + link + "\n\ttemplate=" + template + "\n\tdesign="
				+ design + "\n\toverlay=" + overlay + "\n]";
	}
}
