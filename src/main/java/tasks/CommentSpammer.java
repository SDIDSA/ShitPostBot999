package tasks;

import java.awt.Dimension;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import mainTasks.Creds;
import mainTasks.LogHandler;
import profile.PostUtils;
import utils.DriverUtils;
import utils.SessionUtils;

public class CommentSpammer {
	public static void spamComments(Creds creds, String postLink, LogHandler logHandler,
			Dimension screenSize) {
		WebDriver driver = new FirefoxDriver();
		org.openqa.selenium.Dimension size = new org.openqa.selenium.Dimension((int) screenSize.getWidth(),
				(int) (screenSize.getHeight() / 2));
		Point location = new Point(0, 0);
		DriverUtils.resizeAndRelocate(driver, size, location);
		try {
			SessionUtils.login(driver, creds);

			int count = 404;
			
			PostUtils.openPostByLink(driver, "https://www.facebook.com/groups/712124039622475/permalink/736409857193893/");
			
			PostUtils.postComment(driver, ""+count++, true);

			while (true) {
				Thread.sleep(1000);
				try {
					PostUtils.postComment(driver, ""+count++, false);
				}catch(Exception x) {
					
				}	
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static String generateComment() {
		StringBuilder res = new StringBuilder();
		int len = (int) (Math.random() * 5) + 5;

		for (int i = 0; i < len; i++) {
			if (i != 0) {
				res.append(" ");
			}
			res.append(generateWord());
		}
		

		System.out.println("l = " + len + "\tc = " + res.toString());

		return res.toString();
	}

	public static String generateWord() {
		String symbs = "abcdefghijklmnopqrstuvwxyz";

		StringBuilder res = new StringBuilder();
		int len = (int) (Math.random() * 5) + 5;

		for (int i = 0; i < len; i++) {
			res.append(symbs.charAt((int) (Math.random() * symbs.length())));
		}
		System.out.println("l = " + len + "\tw = " + res.toString());

		return res.toString();
	}
}
