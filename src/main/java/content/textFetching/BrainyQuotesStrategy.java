package content.textFetching;

import java.util.Collections;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import mainTasks.LogHandler;

public class BrainyQuotesStrategy extends TextFetchingStrategy{
	public BrainyQuotesStrategy() {
		super("brainyquote.com");
	}

	@Override
	public String getText(LogHandler logHandler) {
		String path = "https://www.brainyquote.com/topics/knowledge-quotes_" + (((int) (Math.random() * 39)) + 1);
		
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
		
		Elements es = doc.getElementsByClass("clearfix");

		logHandler.append("\t\trandomly selecting a quote");
		Collections.shuffle(es);
		
		Element e = es.get(0);
		
		String quoteText = e.getElementsByAttributeValueContaining("title", "view quote").text();
		String by = e.getElementsByAttributeValueContaining("title", "view author").text();
		
		return (char) 8220 + quoteText + (char) 8221 + " " + (char) 8213 + " " + by;
	}
}
