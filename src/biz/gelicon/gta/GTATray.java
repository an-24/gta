package biz.gelicon.gta;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;


import javafx.application.Platform;

import javafx.stage.Window;

import javax.imageio.ImageIO;

public class GTATray {
	public static enum State {stInactive, stReady,stActive, stNetNotAvaible};
	
    private static final Logger log = Logger.getLogger("gta");
	private static GTATray instance;
	private BufferedImage imgInactive;
	private BufferedImage imgReady;
	private BufferedImage imgActive;
	private BufferedImage imgNetNotAvaible;
	private TrayIcon trayIcon;

	GTATray() {
		try {
			imgInactive = ImageIO.read(Main.class.getClassLoader().getResourceAsStream("inactive.png"));
			imgReady = ImageIO.read(Main.class.getClassLoader().getResourceAsStream("ready.png"));
			imgActive = ImageIO.read(Main.class.getClassLoader().getResourceAsStream("active.png"));
			imgNetNotAvaible = ImageIO.read(Main.class.getClassLoader().getResourceAsStream("netnotavaible.png"));

			if (SystemTray.isSupported()) {
				PopupMenu popup = new PopupMenu();
				MenuItem item;
		        
				item = new MenuItem(Main.getResources().getString("mnu_open"));
		        item.addActionListener(e->{
		        	Platform.runLater(()->{
		        		if(Main.getPrimaryStage().isIconified()) Main.getPrimaryStage().setIconified(false);else {
		        			Main.getPrimaryStage().show();
		        		}
		        			
		        	});
		        });
		        popup.add(item);
		        
		        item = new MenuItem("-");
		        popup.add(item);
		        
		        item = new MenuItem(Main.getResources().getString("mnu_exit"));
		        item.addActionListener(e->{
		        	Main.quit();
		        });
		        popup.add(item);
		        
				trayIcon = new TrayIcon(imgInactive, Main.GTA_APP_NAME, popup);
				trayIcon.addActionListener(e->{
		        	Platform.runLater(()->{
		        		if(Main.getPrimaryStage().isIconified()) Main.getPrimaryStage().setIconified(false);else
		        			Main.getPrimaryStage().show();
		        	});
				});
				
				SystemTray.getSystemTray().add(trayIcon);
			}
			
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		
	}
	
	public static void installTray() {
		instance = new GTATray();
	}
	
	public static GTATray getInstance() {
		if(instance==null) installTray();
		return instance;
	}
	
	public void updateState(State state) {
		if (!SystemTray.isSupported()) return;
		switch (state) {
		case stInactive:
			trayIcon.setImage(imgInactive);
			break;
		case stActive:
			trayIcon.setImage(imgActive);
			break;
		case stReady:
			trayIcon.setImage(imgReady);
			break;
		case stNetNotAvaible:
			trayIcon.setImage(imgNetNotAvaible);
			break;
		default:
			break;
		}
	}

	public void updateState(State state, String tooltip) {
		if (!SystemTray.isSupported()) return;
		updateState(state);
		trayIcon.setToolTip(tooltip);
	}
}
