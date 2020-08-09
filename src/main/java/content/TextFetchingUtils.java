package content;

import java.io.IOException;

import content.textFetching.TextFetchingStrategy;
import mainTasks.LogHandler;
import utils.Entity;

public class TextFetchingUtils {
	public static String getText(TextFetchingStrategy tfs, LogHandler logHandler)
			throws IOException, InterruptedException {
		return tfs.fetch(logHandler);
	}

	public static TextImage getTextAsImage(Entity entity, TextFetchingStrategy tfs, LogHandler logHandler)
			throws IOException, InterruptedException {
		logHandler.append("generating a text image");

		logHandler.append("\t1/2 getting random text from the internet");

		boolean success = false;
		
		TextImage ti = null;
		
		while (!success) {
			try {
				String text = getText(tfs, logHandler);
				logHandler.append("\t\ttext selected : " + text);
				logHandler.append("\t2/2 creating an image with the text");

				ti = new TextImage(TextImageUtils.textToImage(text, entity, tfs, logHandler), text);
				success = true;
			} catch (IllegalArgumentException x) {
				success = false;
				logHandler.append("fetched text is either too short or too long");
			}
		}
		
		return ti;
	}
}
