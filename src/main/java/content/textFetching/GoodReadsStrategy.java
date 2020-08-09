package content.textFetching;

import java.util.Collections;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import mainTasks.LogHandler;

public class GoodReadsStrategy extends TextFetchingStrategy{

	public GoodReadsStrategy() {
		super("goodreads.com");
	}

	@Override
	public String getText(LogHandler logHandler) {
		String path = "https://www.goodreads.com/quotes?page=" + (((int) (Math.random() * 100)) + 1);
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
				logHandler.append("Failed to get quote due to " + ex.getMessage() + ", retrying...");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		Elements es = doc.getElementsByClass("quoteText");

		logHandler.append("\t\trandomly selecting a quote");
		Collections.shuffle(es);

		return es.get(0).text();
	}

}
