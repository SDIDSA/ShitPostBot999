package profile;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.DriverUtils;

public class ProfileUtils {
	public static void openProfile(WebDriver driver) {
		driver.get("https://facebook.com/profile.php");
		DriverUtils.waitForPageLoaded(driver);
	}
	
	public static String getLastPostId(WebDriver driver) {
		String[] parts = driver.findElement(By.cssSelector("div[data-testid='story-subtitle'] a")).getAttribute("href").split("/");
		return parts[parts.length - 1];
	}
	
	public static String getLastPostLink(WebDriver driver) {
		String link = driver.findElement(By.cssSelector("div[data-testid='story-subtitle'] a")).getAttribute("href").replace("&theater", "");
		return link;
	}
}
