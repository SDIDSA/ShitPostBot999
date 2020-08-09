package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import mainTasks.Creds;

public class SessionUtils {

	public static boolean login(WebDriver driver, Creds creds) throws InterruptedException {
		Set<Cookie> cks = getSavedSession();
		driver.get("http://facebook.com");
		DriverUtils.waitForPageLoaded(driver);
		if(cks == null) {
			driver.findElement(By.id("email")).sendKeys(creds.getEmail());
			driver.findElement(By.id("pass")).sendKeys(creds.getPassword());
			driver.findElement(By.cssSelector("input[type='submit']")).click();
			Thread.sleep(1000);
			DriverUtils.waitForPageLoaded(driver);
			saveSession(driver.manage().getCookies());
		}else {
			for(Cookie ck : cks) {
				driver.manage().addCookie(ck);
			}
		}

		if (driver.getTitle().indexOf("Facebook") != -1) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public static Set<Cookie> getSavedSession() {
		Set<Cookie> res = new HashSet<Cookie>();
		File file = new File("logs/cookies.data");
		if(!file.exists()) {
			return null;
		}
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader Buffreader = new BufferedReader(fileReader);
			String strline;
			while ((strline = Buffreader.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(strline, ";");
				while (token.hasMoreTokens()) {
					String name = token.nextToken();
					String value = token.nextToken();
					String domain = token.nextToken();
					String path = token.nextToken();
					Date expiry = null;
					String val;
					if (!(val = token.nextToken()).equals("null")) {
						String[] vals = val.split("-");
						
						int day = Integer.parseInt(vals[0]);
						int month = Integer.parseInt(vals[1]);
						int year = Integer.parseInt(vals[2]);
						
						expiry = new Date(year, month, day);
					}
					boolean isSecure = Boolean.parseBoolean(token.nextToken());
					res.add(new Cookie(name, value, domain, path, expiry, isSecure));
				}
			}
			Buffreader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	public static void saveSession(Set<Cookie> cookies) {
		File file = new File("logs/cookies.data");
		try {
			file.delete();
			file.createNewFile();
			FileWriter fileWrite = new FileWriter(file);
			BufferedWriter Bwrite = new BufferedWriter(fileWrite);

			for (Cookie ck : cookies) {
				@SuppressWarnings("deprecation")
				String date = ck.getExpiry() == null ? "null" : (ck.getExpiry().getDate() + "-" + ck.getExpiry().getMonth() + "-" + ck.getExpiry().getYear());
				
				Bwrite.write((ck.getName() + ";" + ck.getValue() + ";" + ck.getDomain() + ";" + ck.getPath() + ";"
						+ date + ";" + ck.isSecure()));
				Bwrite.newLine();
			}
			Bwrite.close();
			fileWrite.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
