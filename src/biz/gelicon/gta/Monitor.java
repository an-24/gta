package biz.gelicon.gta;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;
import org.jnativehook.mouse.NativeMouseMotionListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

import biz.gelicon.gta.data.Message;
import biz.gelicon.gta.data.Team;
import biz.gelicon.gta.net.ConnectionFactory;
import biz.gelicon.gta.net.NetService;
import biz.gelicon.gta.utils.Handler;

public class Monitor implements NativeKeyListener, NativeMouseListener, NativeMouseMotionListener, NativeMouseWheelListener {
	public static final int MONITOR_PERIOD = 1000*60*1;
	public static final int MONITOR_PING = 1000*5;
	public static final int MONITOR_COUNTDOWN = 1000*60;
	
	private static Logger log = Logger.getLogger("gta");
	
	private Timer timer = null;
	private Timer timerScreenshot;
	
	private User currentUser;
	private Team currentTeam;
	private int key = 0;
	private int mouse = 0;
	private int mouseMove = 0;
	private BufferedImage capture;
	private Handler<Void> afterPost;
	private Date dtBegin;
	private Date dtFinish;

	
	Monitor(User currentUser, Team team, Handler<Void> afterPost) {
		this.currentUser = currentUser;
		this.currentTeam = team;
		this.afterPost = afterPost;
	}


	public static Monitor startMonitor(User currentUser, Team team,  Handler<Void> afterPost) throws Exception {
		Monitor m = new Monitor(currentUser, team, afterPost);
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
			try {
				if(capture==null) screenshot();
				dtFinish = new Date();
				postData();
			} catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}	
	}

	public void screenshot() throws Exception {
		Rectangle2D r = Screen.getPrimary().getBounds();
		Rectangle screenRect = new Rectangle((int)r.getMinX(),(int)r.getMinY(),
				(int)r.getWidth(),(int)r.getHeight());
		capture = new Robot().createScreenCapture(screenRect);
	}
	
	private void startTimer() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					postData();
					dtBegin = new Date();
					dtFinish = new Date(dtBegin.getTime()+MONITOR_PERIOD);  
				} catch (Exception e) {
					log.log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}, MONITOR_PERIOD, MONITOR_PERIOD);
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		dtBegin = cal.getTime();
		cal.add(Calendar.MILLISECOND, MONITOR_PERIOD);
		dtFinish = cal.getTime();  
		
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
	

	private void postData() throws Exception {
		File poolpath = new File(Main.POOL_PATH);
		if(!poolpath.exists()) poolpath.mkdirs();
		// save image
		File imgfile = File.createTempFile("img", ".png", poolpath);
		ImageIO.write(capture, "png", imgfile);
		// save message
		JAXBContext context = initJaxb(Message.class);
		Marshaller jaxb = context.createMarshaller();
		jaxb.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		
		Message message = new Message(dtBegin, dtFinish,key, mouse, mouseMove,imgfile.getName());
		message.setTeamName(currentTeam.getName());
		jaxb.marshal(message, File.createTempFile("mess", ".tmp", poolpath));
		if(afterPost!=null) afterPost.handle(null);
		key=0;
		mouse=0;
		mouseMove=0;
		capture = null;
	}
	
	synchronized public static void immediatelyPostData(File file, boolean clean) throws Exception {
		if(!file.exists()) return;
		// read message
		JAXBContext context = initJaxb(Message.class);
		Unmarshaller jaxb = context.createUnmarshaller();
		Message message = (Message) jaxb.unmarshal(file);
		// read image
		File imgfile = new File(Main.POOL_PATH,message.getCaptureFileName());
		//post
		NetService con = ConnectionFactory.getConnection();
		con.postData(message,imgfile);
		// delete file
		if(clean) {
			file.delete();
			imgfile.delete();
		}
	}


	private static JAXBContext initJaxb(Class cls) throws JAXBException {
		 JAXBContext context = JAXBContext.newInstance(cls);
		 return context;
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
