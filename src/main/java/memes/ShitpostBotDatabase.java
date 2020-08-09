package memes;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import mainTasks.LogHandler;

public class ShitpostBotDatabase {
	private static final String PREFIX = "https://www.shitpostbot.com";

	public static MemeTemplate getTemplate(LogHandler logHandler) throws IOException {
		logHandler.append("\trandomly selecting a template from https://www.shitpostbot.com");
		Connection con = Jsoup.connect(
				PREFIX + "/gallery/templates?review_state=accepted&query=&order=last_reviewed_at&direction=DESC&page="
						+ ((int) (Math.random() * 98) + 1));
		Document doc = null;
		doc = con.get();
		List<Element> elements = doc.getElementsByClass("caption");
		Collections.shuffle(elements);
		Element a = elements.get(0).getElementsByTag("a").get(1);
		String name = a.text();
		logHandler.append("\tselected template : " + name);
		String link = PREFIX + a.attr("href");
		con = Jsoup.connect(link);
		doc = con.get();
		String tempLink = PREFIX + doc.getElementsByClass("template").get(0)
				.getElementsByAttributeValue("data-title", "Base Image").get(0).attr("href");
		String designLink = PREFIX + doc.getElementsByClass("design").get(0)
				.getElementsByAttributeValue("data-title", "Base Image").get(0).attr("href");
		String overlayLink = null;
		
		try {
			overlayLink = PREFIX + doc.getElementsByClass("overlay").get(0)
					.getElementsByAttributeValue("data-title", "Overlay Image").get(0).attr("href");
		}catch(Exception x) {
			
		}
		
		
		logHandler.append("\tdownloading template...");
		return new MemeTemplate(name, link, downloadImage(tempLink), downloadImage(designLink),
				downloadImage(overlayLink), logHandler);
	}

	public static MemeSource getSource(LogHandler logHandler) throws IOException {
		logHandler.append("\t\trandomly selecting a source image from https://www.shitpostbot.com");
		Connection con = Jsoup.connect(
				PREFIX + "/gallery/sourceimages?review_state=accepted&query=&order=total_rating&direction=DESC&page="
						+ ((int) (Math.random() * 400) + 1));
		Document doc = null;
		doc = con.get();
		List<Element> elements = doc.getElementsByClass("caption");
		Collections.shuffle(elements);
		Element a = elements.get(0).getElementsByTag("a").get(0);
		String name = a.text();
		logHandler.append("\t\tselected source image : " + name);
		String link = PREFIX + a.attr("href");
		con = Jsoup.connect(link);
		doc = con.get();

		String imageLink = PREFIX + doc.getElementsByClass("sourceimage").get(0)
				.getElementsByAttributeValue("data-title", "Base Image").get(0).attr("href");

		logHandler.append("\t\tdownloading source image...");
		return new MemeSource(name, link, downloadImage(imageLink));
	}

	private static BufferedImage downloadImage(String link) {
		if (link == null) {
			return null;
		}
		BufferedImage image = null;
		try {
			URL url = new URL(link);
			image = ImageIO.read(url);
			return image;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
