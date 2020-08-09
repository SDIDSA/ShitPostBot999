package tasks;

import java.awt.Dimension;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import mainTasks.Creds;
import mainTasks.LogHandler;
import utils.DriverUtils;
import utils.SessionUtils;

public class PostAutoTranslator {
	static WebDriver translateDriver;
	static WebDriver pd;
	
	public static void translatePostById(WebDriver postingDriver, Creds creds, String postId, LogHandler logHandler, Dimension screenSize) throws InterruptedException {
		pd = postingDriver;
		
		Thread pdt = null;
		
		if(pd == null) {
			pdt = new Thread() {
				public void run() {
					pd = new FirefoxDriver();
					org.openqa.selenium.Dimension size = new org.openqa.selenium.Dimension((int) screenSize.getWidth() / 2,
							 (int) (screenSize.getHeight() / 2));
					Point location = new Point(0, 0);
					DriverUtils.resizeAndRelocate(pd, size, location);
					try {
						SessionUtils.login(pd, creds);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			
			pdt.start();
		}
		
		
		translateDriver = new FirefoxDriver();
		org.openqa.selenium.Dimension size = new org.openqa.selenium.Dimension((int) screenSize.getWidth() / 2,
				 (int) (screenSize.getHeight() / 2));
		Point location = new Point((int) screenSize.getWidth() / 2, 0);
		DriverUtils.resizeAndRelocate(translateDriver, size, location);
		
		if(pdt != null) {
			pdt.join();
		}
		
		try {
			AutomatedTasks.translatePostById(pd, translateDriver, postId, logHandler);
		}catch(Exception x) {
			System.out.println(x.getMessage());
		}
		
		Thread.sleep(2000);
		
		translateDriver.close();
	}
	
	public static void translatePostByLink(WebDriver postingDriver, Creds creds, String link, String text, LogHandler logHandler, Dimension screenSize) throws InterruptedException {
		pd = postingDriver;
		
		Thread pdt = null;
		
		if(pd == null) {
			logHandler.append("Loading facebook.com and logging in");
			logHandler.separator();
			pdt = new Thread() {
				public void run() {
					pd = new FirefoxDriver();
					org.openqa.selenium.Dimension size = new org.openqa.selenium.Dimension((int) screenSize.getWidth() / 2,
							 (int) (screenSize.getHeight() / 2));
					Point location = new Point(0, 0);
					DriverUtils.resizeAndRelocate(pd, size, location);
					try {
						SessionUtils.login(pd, creds);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			
			pdt.start();
		}
		
		translateDriver = new FirefoxDriver();
		org.openqa.selenium.Dimension size = new org.openqa.selenium.Dimension((int) screenSize.getWidth() / 2,
				 (int) (screenSize.getHeight() * 2.0 / 3.0));
		Point location = new Point(0, 0);
		DriverUtils.resizeAndRelocate(translateDriver, size, location);
		
		
		if(pdt != null) {
			pdt.join();
		}
		
		try {
			AutomatedTasks.translatePostByLink(pd, translateDriver, link, text, logHandler);
		}catch(Exception x) {
			System.out.println(x.getMessage());
		}
		
		Thread.sleep(2000);
		
		translateDriver.close();
	}
}
