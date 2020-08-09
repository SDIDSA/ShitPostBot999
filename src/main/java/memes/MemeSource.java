package memes;

import java.awt.image.BufferedImage;

public class MemeSource {
	private String name;
	private String link;
	private BufferedImage image;

	public MemeSource(String name, String link, BufferedImage image) {
		this.name = name;
		this.link = link;
		this.image = image;
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

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "MemeSource [\n\tname=" + name + "\n\tlink=" + link + "\n\timage=" + image + "\n]";
	}
}
