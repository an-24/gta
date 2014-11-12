package biz.gelicon.gta;
	
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;

import biz.gelicon.gta.utils.UTF8Control;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application {
	private static boolean debug = false;

	@Override
	public void start(Stage primaryStage) {
		try {
			GlobalScreen.registerNativeHook();

			// убиваем вывод в консоль
			if(!debug) {
				Logger log = Logger.getLogger(GlobalScreen.class.getPackage().getName());
				log.getParent().removeHandler(log.getParent().getHandlers()[0]);
			}

			FXMLLoader loader = new FXMLLoader();
			ResourceBundle lbundle = ResourceBundle.getBundle("biz.gelicon.gta.bundles.strings", Locale.getDefault(), new UTF8Control());
			SplitPane root = (SplitPane)FXMLLoader.load(getClass().getResource("Main.fxml"),lbundle);
			Scene scene = new Scene(root,400,400);
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
}
