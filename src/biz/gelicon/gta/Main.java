package biz.gelicon.gta;
	
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;
import org.jnativehook.GlobalScreen;

import biz.gelicon.gta.utils.LogHandler;
import biz.gelicon.gta.utils.UTF8Control;


public class Main extends Application {

	public static final String GTA_APP_NAME = "Gelicon Team App";
	private static Properties settings;
	private static ResourceBundle lbundle;
    private static final Logger log = Logger.getLogger("gta");
	public static final String POOL_PATH = "pool";
    private static Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			
			settings = new Properties();
			settings.load(new FileInputStream("gta.properties"));
			
			GlobalScreen.registerNativeHook();

			// skip org.jnativehook log
			Logger log = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			log.getParent().removeHandler(log.getParent().getHandlers()[0]);
			log.getParent().addHandler(new LogHandler());

			lbundle = ResourceBundle.getBundle("biz.gelicon.gta.bundles.strings", Locale.getDefault(), new UTF8Control());
			Parent root = (Parent)FXMLLoader.load(getClass().getResource("forms/Main.fxml"),lbundle);
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

	public static void main(String[] args) {
		launch(args);
	}

	public static String getProperty(String key) {
		return settings.getProperty(key);
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
		System.runFinalization();
    	Platform.exit();
    	System.exit(0);
	}

	public static Stage getPrimaryStage() {
		return primaryStage;
	}
}
