package translation;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utils.DriverUtils;

public class TranslationUtils {
	
	public static TranslationResult translate(WebDriver driver, String input,String sl, String tl) throws InterruptedException {
		String toTranslate = input;
		
		if(input.indexOf("�") != -1) {
			toTranslate = input.substring(1, input.lastIndexOf("�"));
		}
		
		driver.get("https://translate.google.com/#view=home&op=translate&sl="+(sl == null ? "auto":sl)+"&tl="+(tl == null ? "en":tl)+"&text="+toTranslate);
		
		DriverUtils.waitForPageLoaded(driver);
		Thread.sleep(3000);
		String language = driver.findElement(By.cssSelector(".sl-sugg-button-container>.jfk-button-checked")).getText().split(" - ")[0];
		String result = driver.findElement(By.cssSelector(".translation")).getText();
		WebElement in = driver.findElement(By.cssSelector("#source"));
		for(int i = 0;i<input.length();i++) {
			in.sendKeys(Keys.BACK_SPACE);
		}
		in.sendKeys(language);
		Thread.sleep(8000);
		String actualLang = driver.findElement(By.cssSelector(".translation")).getText();
		String finalLang = Character.toUpperCase(actualLang.charAt(0)) + actualLang.substring(1).toLowerCase();
	
		return new TranslationResult(finalLang, input.replace(toTranslate, result));
	}
}
