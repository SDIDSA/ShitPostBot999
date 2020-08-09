package translation;

public class TranslationResult {
	private String sourceLang;
	private String translation;

	public TranslationResult(String sourceLang, String translation) {
		this.sourceLang = sourceLang;
		this.translation = translation;
	}

	public String getSourceLang() {
		return sourceLang;
	}

	public void setSourceLang(String sourceLang) {
		this.sourceLang = sourceLang;
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}
	
	public String toString() {
		return "translated from : " + sourceLang + " \n" + translation + " ";
	}

}
