package content;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class Line {
	private static final int UNSPECIFIED = 0, ARABIC = 1, LATIN = 2;

	private ArrayList<Part> parts;
	private int state = UNSPECIFIED;

	public Line(String input) {
		parts = new ArrayList<Part>();

		StringBuilder builder = new StringBuilder();

		for (char c : input.toCharArray()) {
			int code = (int) c;
			if (code > 1536 && code < 1791) {
				if (state == LATIN) {
					if (builder.length() != 0) {
						parts.add(new Part(builder.toString(), false));
					}
					builder = new StringBuilder();
				}
				state = ARABIC;
				builder.append(c);
			} else if(Character.isAlphabetic(c)){
				if (state == ARABIC) {
					if (builder.length() != 0) {
						parts.add(new Part(builder.toString(), true));
					}
					builder = new StringBuilder();
				}
				state = LATIN;
				builder.append(c);
			} else if(code == 8213) {
				parts.add(new Part(builder.toString(), state == ARABIC));
				parts.add(new Part((char) 8213 + "",false));
				builder = new StringBuilder();
				state = UNSPECIFIED;
			}else {
				builder.append(c);
			}
		}

		if (builder.length() != 0) {
			parts.add(new Part(builder.toString(), state == ARABIC));
		}
	}

	public ArrayList<Part> getParts() {
		return parts;
	}

	public int calculateWidth(Graphics2D g2d) {
		int res = 0;
		for (Part p : parts) {
			res += p.calculateWidth(g2d);
		}
		return res;
	}
	
	public void drawLine(Graphics2D g2d, int x, int y, int maxWidth) {
		boolean rtl = false;
		
		for(Part p:parts) {
			if(p.isArabic()) {
				rtl = true;
				x = maxWidth;
				break;
			}
		}
		
		for(int i = 0;i<parts.size();i++) {
			if(rtl) {
				x -= parts.get(i).calculateWidth(g2d);
			}
			parts.get(i).drawPart(g2d, x, y);
			if(!rtl) {
				x += parts.get(i).calculateWidth(g2d);
			}
		}
	}
}
