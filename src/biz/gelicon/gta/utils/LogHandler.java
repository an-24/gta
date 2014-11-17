package biz.gelicon.gta.utils;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.jnativehook.GlobalScreen;

public class LogHandler extends ConsoleHandler {

	@Override
	public void publish(LogRecord record) {
		// skip org.jnativehook log
		if(record.getLoggerName().equals(GlobalScreen.class.getPackage().getName()) &&
				record.getLevel()==Level.INFO) return;
		super.publish(record);
	}

}
