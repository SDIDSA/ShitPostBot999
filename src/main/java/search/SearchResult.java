package search;

public class SearchResult {
	private String posterName;
	private String fullContent;
	private String timePosted;
	
	public SearchResult(String posterName, String fullContent, String timePosted) {
		this.posterName = posterName;
		this.fullContent = fullContent;
		this.timePosted = timePosted;
	}

	@Override
	public String toString() {
		return "SearchResult [posterName=" + posterName + ",\n\tfullContent=" + fullContent + ",\n\ttimePosted=" + timePosted
				+ "]";
	}

	public String getPosterName() {
		return posterName;
	}

	public void setPosterName(String posterName) {
		this.posterName = posterName;
	}

	public String getFullContent() {
		return fullContent;
	}

	public void setFullContent(String fullContent) {
		this.fullContent = fullContent;
	}

	public String getTimePosted() {
		return timePosted;
	}

	public void setTimePosted(String timePosted) {
		this.timePosted = timePosted;
	}
}
