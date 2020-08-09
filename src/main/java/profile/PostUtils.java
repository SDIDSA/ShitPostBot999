package profile;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utils.DriverUtils;

public class PostUtils {

	public static void addImage(WebDriver driver, String name) {
		driver.findElement(By.cssSelector("input[aria-label='Add Photo or Video']")).sendKeys(name);
	}
	
	public static void openPostById(WebDriver driver, String id) {
		String prefix = driver.findElement(By.cssSelector("div[data-click='profile_icon']>a")).getAttribute("href");
		driver.get(prefix + "/posts/" + id);	
		DriverUtils.waitForPageLoaded(driver);
	}
	
	public static void openPostByLink(WebDriver driver, String link) throws InterruptedException {
		driver.get(link);
		DriverUtils.waitForPageLoaded(driver);
		
		Thread.sleep(3000);
	}
	
	public static String getPostText(WebDriver driver) {
		return driver.findElement(By.cssSelector("div[data-testid='post_message']")).getText();
	}
	
	public static String getPostTime(WebDriver driver) {
		return driver.findElement(By.cssSelector("div[data-testid='story-subtitle'] abbr")).getAttribute("title");
	}

	public static void postComment(WebDriver driver, String text, boolean init) throws InterruptedException {
		if(init) {
			WebElement hold = driver.findElement(By.cssSelector(".commentable_item form div[contenteditable='true']+div"));
			hold.click();
		}
		
		Thread.sleep(1000);
		
		WebElement in = driver.findElement(By.cssSelector("div[role='textbox']"));
		
		System.out.println(in.getText());
		for(char c:text.toCharArray()) {
			in.sendKeys(c + "");
		}
		in.sendKeys(Keys.ENTER);
	}
	
	public static void postCommentInTheater(WebDriver driver, String text) {
		List<WebElement> els = driver.findElements(By.cssSelector("a[title='Leave a comment']"));
		els.get(els.size() - 1).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		List<WebElement> fls = driver.findElements(By.cssSelector("div[aria-label='Write a comment...']"));
		WebElement in = fls.get(fls.size() - 1);
		System.out.println(in.getText());
		for(char c:text.toCharArray()) {
			in.sendKeys(c + "");
		}
		in.sendKeys(Keys.ENTER);
	}
	
	public static void createPost(WebDriver driver, String text) throws InterruptedException {
		WebElement datadiv = driver.findElement(By.cssSelector("div[role='combobox']"));
		datadiv.click();
		Thread.sleep(1000);
		WebElement input = driver.findElement(By.cssSelector("div[role='combobox']"));
		for(char c:text.toCharArray()) {
			input.sendKeys(c+"");
		}
		Thread.sleep(1000);
	}

	public static void publish(WebDriver driver) throws InterruptedException {
		WebElement button = driver.findElement(By.cssSelector("button[type='submit']>span"));
		button.click();
		Thread.sleep(1000);
	}
	
	public static void publishInGroup(WebDriver driver) throws InterruptedException {
		WebElement button = driver.findElements(By.cssSelector("button[type='submit']>span")).get(1);
		button.click();
		Thread.sleep(1000);
	}
	
	public static void tagFriend(WebDriver driver, String name) throws InterruptedException {
		driver.findElement(By.xpath("//div[text()='Tag Friends']")).click();
		WebElement tag = driver.findElement(By.cssSelector("input[aria-label='Who are you with?']"));
		tag.sendKeys(name);
		Thread.sleep(4000);
		tag.sendKeys(Keys.ENTER);
	}

	public static void makePublic(WebDriver driver) throws InterruptedException {
		changeAudience(driver, Audience.PUBLIC);
	}

	public static void changeAudience(WebDriver driver, Audience audience) throws InterruptedException {
		WebElement button = driver.findElement(By.cssSelector("div[aria-label='Create a post'] a[rel='toggle']"));
		String id = button.getAttribute("id");
		button.click();
		Thread.sleep(500);
		WebElement layer = driver
				.findElement(By.xpath("//div[@data-ownerid='" + id + "']//span[text()='" + audience.getName() + "']"));
		layer.click();
		Thread.sleep(1000);
	}

	public static class Audience {
		public static final Audience PUBLIC = new Audience("Public");
		public static final Audience FRIENDS = new Audience("Friends");
		public static final Audience ONLY_ME = new Audience("Only me");
		private String name;
		private Audience(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
	}
}
