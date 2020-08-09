package content.textFetching;

import java.util.Collections;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import mainTasks.LogHandler;

public class IceBreakerIdeas extends TextFetchingStrategy {

	public IceBreakerIdeas() {
		super("icebreakerideas.com");
	}

	@Override
	public String getText(LogHandler logHandler) {
		String path = "https://icebreakerideas.com/trick-questions/";

		Document doc = null;
		boolean success = false;
		logHandler.append("\t\tloading " + getIndex() + "...");
		while (!success) {
			try {
				doc = Jsoup.connect(path).get();
				success = true;
				break;
			} catch (Exception ex) {
				success = false;
				logHandler.append("Failed to get text due to " + ex.getMessage() + ", retrying...");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		Elements es = doc.select(".post-content > h2 + p + ol > li");

		logHandler.append("\t\trandomly selecting a quote");
		Collections.shuffle(es);

		Element e = es.get(0);
		return e.ownText();
	}

}
