package biz.gelicon.gta;
	
import java.awt.SystemTray;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.controlsfx.dialog.Dialogs;
import org.jnativehook.GlobalScreen;

import biz.gelicon.gta.forms.MainController;
import biz.gelicon.gta.utils.LogHandler;
import biz.gelicon.gta.utils.UTF8Control;

import com.apple.eawt.AppEvent.AppForegroundEvent;
import com.apple.eawt.AppEvent.AppReOpenedEvent;


public class Main extends Application {

	public static final String GTA_APP_NAME = "Gelicon Team App";
	private static Properties settings;
	private static ResourceBundle lbundle;
    private static final Logger log = Logger.getLogger("gta");
	public static final String POOL_PATH = "pool";
    private static Stage primaryStage;
	private static MainController maincontroller;

	@Override
	public void start(Stage primaryStage) {
		try {
			Main.primaryStage = primaryStage;
			
			settings = new Properties();
			settings.load(new FileInputStream("gta.properties"));
			
			GlobalScreen.registerNativeHook();
			
			// skip org.jnativehook log
			Logger log = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			log.setUseParentHandlers(false);
			log.getParent().removeHandler(log.getParent().getHandlers()[0]);
			log.getParent().addHandler(new LogHandler());
			
			if(System.getProperty("os.name").startsWith("Mac")) {
				macOSExt();
			}

			lbundle = ResourceBundle.getBundle("biz.gelicon.gta.bundles.strings", Locale.getDefault(), new UTF8Control());
			FXMLLoader loader = new FXMLLoader(getClass().getResource("forms/Main.fxml"),lbundle);
			//Parent root = (Parent)FXMLLoader.load(getClass().getResource("forms/Main.fxml"),lbundle);
			Parent root = (Parent)loader.load();
			maincontroller = (MainController)loader.getController();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			
			Platform.setImplicitExit(false);
			primaryStage.setOnCloseRequest(event -> {
				if(!SystemTray.isSupported()) Main.quit(); 
										else primaryStage.hide();
			});
			
			if (SystemTray.isSupported()) {
				GTATray.installTray();
			};
			primaryStage.show();
		} catch(Exception e) {
		
			e.printStackTrace();
		}
	}

	@SuppressWarnings("restriction")
	private void macOSExt() throws IOException {
		BufferedImage aboutImage = ImageIO.read(Main.class.getResourceAsStream("resources/about.png"));
		com.apple.eawt.Application app = com.apple.eawt.Application.getApplication();
		app.setDockIconImage(aboutImage);
		app.addAppEventListener(new com.apple.eawt.AppForegroundListener(){

			@Override
			public void appMovedToBackground(AppForegroundEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void appRaisedToForeground(AppForegroundEvent e) {
				Platform.runLater(()->{
					primaryStage.show();
				});
			}
			
		});
		app.addAppEventListener(new com.apple.eawt.AppReOpenedListener() {

			@Override
			public void appReOpened(AppReOpenedEvent e) {
				Platform.runLater(()->{
					primaryStage.show();
				});
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static String getProperty(String key) {
		return settings.getProperty(key);
	}

	public static void setProperty(String key, String value) {
		settings.setProperty(key,value);
		try {
			settings.store(new FileOutputStream("gta.properties"),"Gelicon Team App properties");
		} catch (Exception e) {
			log.log(Level.SEVERE,e.getMessage(),e);
		}
	}

	public static ResourceBundle getResources() {
		return lbundle;
	}

	@SuppressWarnings("deprecation")
	public static void showErrorBox(String m) {
		Dialogs.create()
	      .title(lbundle.getString("frm-title-errorbox"))
	      .message(m)
	      .showError();
	}

	@SuppressWarnings("deprecation")
	public static void showErrorBox(Exception ex) {
		Dialogs.create()
	      .title(lbundle.getString("frm-title-errorbox"))
	      .message(ex.getMessage())
	      .showException(ex);
	}

	@SuppressWarnings("deprecation")
	public static void showErrorBox(String m, Exception ex) {
		Dialogs.create()
	      .title(lbundle.getString("frm-title-errorbox"))
	      .message(m)
	      .showException(ex);
	}
	
	public static void quit() {
		GlobalScreen.unregisterNativeHook();
		maincontroller.disconnect();
    	Platform.exit();
    	System.exit(0);
	}

	public static Stage getPrimaryStage() {
		return primaryStage;
	}

}
