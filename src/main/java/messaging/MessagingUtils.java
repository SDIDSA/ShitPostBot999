package messaging;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utils.DriverUtils;

public class MessagingUtils {
	public static void openConversation(WebDriver driver, String chatId) throws InterruptedException {
		driver.get("https://www.facebook.com/messages/t/" + chatId);
		DriverUtils.waitForPageLoaded(driver);

		Thread.sleep(5000);
	}

	public static void openStickerPopup(WebDriver driver) throws InterruptedException {
		driver.findElement(By.cssSelector("a[aria-label='Choose a sticker']")).click();

		Thread.sleep(2000);
	}

	public static void selectStickerCategory(WebDriver driver, String category) throws InterruptedException {
		driver.findElement(By.cssSelector("a[aria-label='" + category + "']")).click();

		Thread.sleep(2000);
	}

	public static void sendSticker(WebDriver driver, String sticker) {
		driver.findElement(By.cssSelector("div[aria-label='" + sticker + "']")).click();
	}

	static String lastMsg = "";
	public static String readLastMessage(WebDriver driver) { 
		List<WebElement> vals = driver.findElements(By.cssSelector(".clearfix.direction_ltr.text_align_ltr > div > div > span"));
		String val = vals.get(vals.size() - 1).getText();
		
		if(!val.equals(lastMsg)) {
			lastMsg = val;
			return val;
		}
		
		return null;
	}
	
	public static void sendText(WebDriver driver, String text) {
		WebElement e = driver.findElement(By.cssSelector(".navigationFocus[role='presentation'] div div[role='combobox']"));

		lastMsg = text;
		e.sendKeys(text);
		
		e.sendKeys("\n");
	}
}
