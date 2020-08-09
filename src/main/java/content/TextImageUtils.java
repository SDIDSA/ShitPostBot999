package content;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.json.JSONException;
import org.json.JSONObject;

import content.image.BlackAndWhite;
import content.image.BoxBlur;
import content.image.Image;
import content.textFetching.TextFetchingStrategy;
import mainTasks.LogHandler;
import tasks.CommentSpammer;
import utils.Entity;

public class TextImageUtils {
	public static File textToImage(String text, Entity entity, TextFetchingStrategy tfs, LogHandler logHandler)
			throws IOException, IllegalArgumentException {

		logHandler.append("\t\tcalculating the size of the image");

		LinedText lq = new LinedText(text);

		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		Font font = new Font("Trebuchet MS", Font.PLAIN, 80);
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
		int width = 0, height = fm.getHeight() * lq.size();

		for (Line line : lq) {
			int wi = line.calculateWidth(g2d);
			if (wi > width) {
				width = wi;
			}
		}

		width += 20;
		height += 30;

		int padding = 300;
		width += padding;
		height += padding;

		logHandler.append("\t\tsize : " + width + " x " + height);
		logHandler.append("\t\tcreating an empty image");

		g2d.dispose();

		img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		g2d = img.createGraphics();

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, height);

		logHandler.append("\t\tdownloading a background image from unsplash.com");
		BufferedImage back = getBackgroundImage();

		logHandler.append("\t\tscaling the background for the target size");
		double targetFactor = (double) width / (double) height;
		double sourceFactor = (double) back.getWidth() / (double) back.getHeight();

		BufferedImage targetBack = null;
		int b_x = 0;
		int b_y = 0;

		if (targetFactor < sourceFactor) {
			targetBack = toBufferedImage(back.getScaledInstance(-1, height, 0));
			b_x = -(targetBack.getWidth(null) - width) / 2;
		} else {
			targetBack = toBufferedImage(back.getScaledInstance(width, -1, 0));
			b_y = -(targetBack.getHeight(null) - height) / 2;
		}

		back.flush();

		Image final_back = new Image(targetBack);
		logHandler.append("\t\tapplying filters");
		Image blured = final_back.applyFilter(new BoxBlur(5)).applyFilter(new BlackAndWhite());
		blured.setOpacity(.4);
		logHandler.append("\t\tadding the background");
		blured.draw(g2d, b_x, b_y);

		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		logHandler.append("\t\tadding the quote text");
		g2d.setColor(Color.BLACK);
		for (int i = 0; i < lq.size(); i++) {
			int dx = 10 + padding / 2;
			int dy = fm.getHeight() * (i + 1) + padding / 2;
			lq.get(i).drawLine(g2d, dx, dy, width - padding / 2);
		}

		font = new Font("Tw Cen MT", Font.PLAIN, 42);
		g2d.setFont(font);
		fm = g2d.getFontMetrics();

		logHandler.append("\t\tadding credits");
		int gn_width = fm.stringWidth(entity.getName());
		int top_width = fm.stringWidth("From " + tfs.getIndex());
		g2d.drawString(entity.getName(), (width - gn_width) / 2, height - 20);
		g2d.drawString("From " + tfs.getIndex(), (width - top_width) / 2, fm.getHeight());

		g2d.dispose();
		File res = new File(System.getProperty("java.io.tmpdir") + CommentSpammer.generateWord() + ".jpg");

		try {
			ImageIO.write(img, "jpg", res);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		logHandler.append("\ttext image is ready : " + res.getAbsolutePath());

		return res;
	}

	private static BufferedImage toBufferedImage(java.awt.Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		img.flush();
		return bimage;
	}

	private static BufferedImage getBackgroundImage() throws JSONException, IOException {
		JSONObject obj = JsonReader.readJsonFromUrl(
				"https://api.unsplash.com/photos/random?client_id=raIcDmGoI4piwUVIp_tGeCM7Bs27eSi4eAnjWZZU1VQ");
		URL url = new URL(obj.getJSONObject("urls").getString("full").toString());
		BufferedImage res = ImageIO.read(url);
		return res;
	}
}
