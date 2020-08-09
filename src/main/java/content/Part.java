package content;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public class Part {
	private String content;
	private boolean arabic;

	static {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,
					new File(Part.class.getResource("/THARWATEMARARUQAA.ttf").getFile())));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	private static Font arabicFont = new Font("THARWATEMARARUQAA", Font.PLAIN, 72);
	private static Font latinFont = new Font("Trebuchet MS", Font.PLAIN, 64);

	private Font font;

	public Part(String content, boolean arabic) {
		this.content = content;
		this.arabic = arabic;

		if (arabic) {
			this.content = this.content.replace((char) 8220, (char) 8222);
			this.content = this.content.replace((char) 8221, (char) 8220);
			this.content = this.content.replace((char) 8222, (char) 8221);
			this.content = this.content.replace((char) 44, (char) 1548);
		}

		font = arabic ? arabicFont : latinFont;
	}

	public int calculateWidth(Graphics2D g2d) {
		FontMetrics fm = g2d.getFontMetrics(font);
		return fm.stringWidth(content);
	}

	public boolean isArabic() {
		return arabic;
	}

	public String getContent() {
		return content;
	}

	public void drawPart(Graphics2D g2d, int x, int y) {
		g2d.setFont(font);
		g2d.drawString(content, x, y);
	}
}