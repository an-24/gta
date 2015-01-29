package biz.gelicon.gta.forms;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.image.ImageView;
import biz.gelicon.gta.Main;

@SuppressWarnings("restriction")
public class AboutController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private VBox root;

	@FXML
	private ImageView ivLogo;

	@FXML
	private Button bOk;

	@FXML
	void initialize() {
		assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'About.fxml'.";
		assert ivLogo != null : "fx:id=\"ivLogo\" was not injected: check your FXML file 'About.fxml'.";
		assert bOk != null : "fx:id=\"bOk\" was not injected: check your FXML file 'About.fxml'.";

		init();
	}

	private void init() {
		bOk.setOnAction(e -> {
			root.getScene().getWindow().hide();
		});
		ivLogo.setImage(new Image(Main.class.getClassLoader().getResourceAsStream("about.png")));
	}

	public static void showModal(Window owner) {
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader(
				Main.class.getResource("forms/About.fxml"), Main.getResources());
		Parent rpane;
		try {
			rpane = loader.load();
		} catch (IOException e) {
			return;
		}
		Scene scene = new Scene(rpane);
		scene.getStylesheets().add(
				Main.class.getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle(Main.getResources().getString("frm-title-login"));
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(owner);
		stage.setResizable(false);
		stage.show();

	}
}
