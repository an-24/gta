package biz.gelicon.gta;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;
import org.jnativehook.mouse.NativeMouseMotionListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

public class Monitor implements NativeKeyListener, NativeMouseListener, NativeMouseMotionListener, NativeMouseWheelListener {
	public static final int MONITOR_PERIOD = 1000*60*10;
	public static final int MONITOR_PING = 1000*5;
	public static final int MONITOR_COUNTDOWN = 1000*60;
	
	private static Logger log = Logger.getLogger("gta");
	
	private Timer timer = null;
	private Timer timerScreenshot;
	
	private User currentUser;
	private int key = 0;
	private int mouse = 0;
	private int mouseMove = 0;
	private BufferedImage capture;

	
	Monitor(User currentUser) {
		this.currentUser = currentUser;
	}


	public static Monitor startMonitor(User currentUser) {
		Monitor m = new Monitor(currentUser);
		m.startTimer();
		GlobalScreen.getInstance().addNativeKeyListener(m);
		GlobalScreen.getInstance().addNativeMouseListener(m);
		GlobalScreen.getInstance().addNativeMouseMotionListener(m);
		GlobalScreen.getInstance().addNativeMouseWheelListener(m);

		return m;
	}


	public void stop() {
		if(timer!=null) {
			timerScreenshot.cancel();
			timerScreenshot = null;
			timer.cancel();
			timer = null;
			GlobalScreen.getInstance().removeNativeKeyListener(this);
			GlobalScreen.getInstance().removeNativeMouseListener(this);
			GlobalScreen.getInstance().removeNativeMouseMotionListener(this);
			GlobalScreen.getInstance().removeNativeMouseWheelListener(this);
			postData();
		}	
	}

	public void screenshot() throws Exception {
		Rectangle2D r = Screen.getPrimary().getBounds();
		Rectangle screenRect = new Rectangle((int)r.getMinX(),(int)r.getMinY(),
				(int)r.getWidth(),(int)r.getHeight());
		capture = new Robot().createScreenCapture(screenRect);
		//ImageIO.write(capture, "png", new File("screenshot.png"));
	}
	
	private void startTimer() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				postData();
			}
		}, MONITOR_PERIOD, MONITOR_PERIOD);
		
		timerScreenshot = new Timer();
		doTimerScreenshot(0);
	}


	private void doTimerScreenshot(int offs) {
		int period = new Random().nextInt(MONITOR_PERIOD);
		timerScreenshot.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					screenshot();
					doTimerScreenshot(MONITOR_PERIOD-period);
				} catch (Exception e) {
					log.severe("error in screenshot(): "+e.getMessage());
				}
			}
		}, offs+period);
	}
	

	private void postData() {
		// TODO Auto-generated method stub
		log.info("Data posted in server");
	}


	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		key++;
	}


	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
	}


	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
	}


	@Override
	public void nativeMouseClicked(NativeMouseEvent e) {
		mouse++;
	}


	@Override
	public void nativeMousePressed(NativeMouseEvent e) {
	}


	@Override
	public void nativeMouseReleased(NativeMouseEvent e) {
	}


	@Override
	public void nativeMouseWheelMoved(NativeMouseWheelEvent e) {
		mouseMove++;
	}


	@Override
	public void nativeMouseMoved(NativeMouseEvent e) {
		mouseMove++;
	}


	@Override
	public void nativeMouseDragged(NativeMouseEvent e) {
	}
}
