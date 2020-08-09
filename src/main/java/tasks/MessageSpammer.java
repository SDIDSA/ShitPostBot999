package tasks;

import java.awt.Dimension;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import mainTasks.Creds;
import mainTasks.MainLauncher;
import mainTasks.LogHandler;
import messaging.MessagingUtils;
import utils.DriverUtils;
import utils.SessionUtils;

public class MessageSpammer {
	static WebDriver driver;
	
	public static void startMessageSpamming(Creds creds, String chatId) throws InterruptedException {
		LogHandler logHandler = MainLauncher.logHandler;
		Dimension screenSize = MainLauncher.screenSize;
		
		logHandler.append("Loading facebook.com and logging in...");
		
		driver = new FirefoxDriver();
		org.openqa.selenium.Dimension size = new org.openqa.selenium.Dimension(
				(int) (screenSize.getWidth() * 3.0 / 4.0), (int) (screenSize.getHeight() / 2));
		Point location = new Point((int) (screenSize.getWidth() / 4), 0);
		DriverUtils.resizeAndRelocate(driver, size, location);
		
		boolean in = SessionUtils.login(driver, creds);
		
		boolean open = false;
		
		if(in) {
			
			int counter = -1;
			
			while(true) {
				counter++;
				
				if(counter % 100 == 0) {
					if(!open) {
						MessagingUtils.openConversation(driver, chatId);
						open = true;
					}else {
						driver.navigate().refresh();
						
						DriverUtils.waitForPageLoaded(driver);
					}
	
					MessagingUtils.openStickerPopup(driver);
					MessagingUtils.selectStickerCategory(driver, "Meep");
				}

				MessagingUtils.sendSticker(driver, "Meep Frustrated face sticker");
				
				logHandler.append(counter + " stickers were sent...");
				
				Thread.sleep(500);
			}
		}
	}
}
