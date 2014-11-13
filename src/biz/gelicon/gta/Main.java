package biz.gelicon.gta;
	
import java.io.FileInputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;
import org.jnativehook.GlobalScreen;

import biz.gelicon.gta.utils.UTF8Control;


public class Main extends Application {
	private static boolean debug = false;
	private static Properties settings;
	private static ResourceBundle lbundle;

	@Override
	public void start(Stage primaryStage) {
		try {
			settings = new Properties();
			settings.load(new FileInputStream("gta.properties"));
			
			GlobalScreen.registerNativeHook();

			// drop info log
			if(!debug) {
				Logger log = Logger.getLogger(GlobalScreen.class.getPackage().getName());
				log.getParent().removeHandler(log.getParent().getHandlers()[0]);
			}

			lbundle = ResourceBundle.getBundle("biz.gelicon.gta.bundles.strings", Locale.getDefault(), new UTF8Control());
			SplitPane root = (SplitPane)FXMLLoader.load(getClass().getResource("forms/Main.fxml"),lbundle);
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			
			primaryStage.setOnCloseRequest(event -> {
				GlobalScreen.unregisterNativeHook();
				System.runFinalization();
                System.exit(0);
			});
			
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
}
