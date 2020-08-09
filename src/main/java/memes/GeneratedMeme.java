package memes;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GeneratedMeme {
	private String tempLink;
	private ArrayList<String> sources;
	private BufferedImage meme;
	public GeneratedMeme(String tempLink, BufferedImage meme, ArrayList<String> sources) {
		this.tempLink = tempLink;
		this.meme = meme;
		this.sources = sources;
	}
	public String getTempLink() {
		return tempLink;
	}
	public void setTempLink(String tempLink) {
		this.tempLink = tempLink;
	}
	public BufferedImage getMeme() {
		return meme;
	}
	public void setMeme(BufferedImage meme) {
		this.meme = meme;
	}
	
	public ArrayList<String> getSources(){
		return sources;
	}
}
