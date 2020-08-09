package content.textFetching;

import java.util.ArrayList;

import mainTasks.LogHandler;

public abstract class TextFetchingStrategy {
	private String index;
	
	private ArrayList<String> used;
	
	public TextFetchingStrategy(String index) {
		this.index = index;
		used = new ArrayList<String>();
	}
	
	public String getIndex() {
		return index;
	}
	
	public void setIndex(String index) {
		this.index = index;
	}
	
	public String fetch(LogHandler lh) {
		String res = getText(lh);
		
		while(used.indexOf(res) != -1) {
			res = getText(lh);
		}
		
		used.add(res);
		
		return res;
	}
	
	protected abstract String getText(LogHandler logHandler);
}
