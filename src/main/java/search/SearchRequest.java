package search;

public class SearchRequest {
	private String text;
	private SearchResult res;
	private boolean ready;
	
	public SearchRequest(String text) {
		this.text = text;
		res = null;
		ready = false;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public SearchResult getRes() {
		return res;
	}

	public void setRes(SearchResult res) {
		this.res = res;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}
}
