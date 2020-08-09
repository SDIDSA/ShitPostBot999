package content;

import java.io.File;

public class TextImage {
	private File file;
	private String text;

	public TextImage(File file, String text) {
		this.file = file;
		this.text = text;
	}

	public File getFile() {
		return file;
	}

	public String getText() {
		return text;
	}
}
