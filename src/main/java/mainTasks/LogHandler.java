package mainTasks;

import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class LogHandler {
	private JScrollPane scroll;
	private JTextArea textArea;
	private BufferedWriter logWriter;

	public LogHandler(JScrollPane scroll, JTextArea textArea) {
		this.textArea = textArea;
		this.scroll = scroll;
		
		File dir = new File("logs");
		
		if(!dir.isDirectory()) {
			dir.mkdir();
		}
		
		LocalDateTime now = LocalDateTime.now();
		String fileName = "logs_"+now.getYear()+"-"+now.getMonthValue()+"-"+now.getDayOfMonth()+"_"+now.getHour()+"-"+now.getMinute()+"-"+now.getSecond();
		
		File logsFile = new File("logs/" + fileName + ".txt");
		
		try {
			logWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logsFile, true), "utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	public void append(String text) {
		append(text, true);
	}
	
	public void append(String text, boolean addTime) {
		LocalDateTime now = LocalDateTime.now();
		String time = now.getYear()+"-"+zerofy(now.getMonthValue())+"-"+zerofy(now.getDayOfMonth())+" "+zerofy(now.getHour())+":"+zerofy(now.getMinute())+":"+zerofy(now.getSecond());
		
		String toAppend = text;
		
		if(addTime) {
			toAppend = time + "  " + text;
		}
		
		append(scroll, textArea, toAppend);
		try {
			logWriter.append(toAppend + "\n");
			logWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String zerofy(int val) {
		return (val<10 ? "0":"") + val;
	}
	
	public void separator() {
		String sep = "";

		for (int i = 0; i < 50; i++) {
			sep += "-";
		}

		append(sep, false);
	}

	private static void append(JScrollPane scrollPane, JTextArea text, String toAppend) {
		text.append(toAppend + "\n");
		text.validate();

		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum() + 1);
	}
	
	public static LogHandler startLogHandler(Dimension screenSize) {
		JFrame logs = new JFrame();
		logs.setTitle("logs");
		logs.setSize(new Dimension((int) screenSize.getWidth(), (int) (screenSize.getHeight() / 3.0)));

		JTextArea logBuilder = new JTextArea();
		logBuilder.setFont(new Font("monospaced", Font.PLAIN, 14));
		logBuilder.setBorder(new EmptyBorder(10,10,10,10));
		logBuilder.setEditable(false);

		JScrollPane scroll = new JScrollPane(logBuilder, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		logs.setContentPane(scroll);

		LogHandler logHandler = new LogHandler(scroll, logBuilder);

		logs.setVisible(true);
		logs.setLocation(0,  (int) (screenSize.getHeight() * 2.0 / 3.0));
		
		return logHandler;
	}
}