package biz.gelicon.gta.forms;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import biz.gelicon.gta.Main;
import biz.gelicon.gta.utils.Handler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;

public class LoginController {

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private VBox root;
    @FXML
    private TextField tfName;
    @FXML
    private Button bCancel;
    @FXML
    private ImageView ivKeys;
    @FXML
    private Button bOk;
    @FXML
    void initialize() {
        assert tfPassword != null : "fx:id=\"tfPassword\" was not injected: check your FXML file 'Login.fxml'.";
        assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'Login.fxml'.";
        assert tfName != null : "fx:id=\"tfName\" was not injected: check your FXML file 'Login.fxml'.";
        assert bCancel != null : "fx:id=\"bCancel\" was not injected: check your FXML file 'Login.fxml'.";
        assert ivKeys != null : "fx:id=\"ivKeys\" was not injected: check your FXML file 'Login.fxml'.";
        assert bOk != null : "fx:id=\"bOk\" was not injected: check your FXML file 'Login.fxml'.";
        
        try {
            init();
        } catch (Exception e) {
        	log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

	private Handler<Pair<String,String>> handler;
    private static final Logger log = Logger.getLogger("gta");
    
	private void init() {
		bOk.setOnAction(e->{
			if(tfName.getText().isEmpty()) {
				Main.showErrorBox(Main.getResources().getString("err-empty-user"));
				return;
			}
			try {
				handler.handle(new Pair<String,String>(tfName.getText(),tfPassword.getText()));
			} catch (Exception ex) {
				Main.showErrorBox(ex.getMessage());
				return;
			}
			root.getScene().getWindow().hide();
		});
		bCancel.setOnAction(e->{
			root.getScene().getWindow().hide();
		});
		tfName.requestFocus();
		
		ivKeys.setImage(new Image(Main.class.getResourceAsStream("resources/keys.png")));
	}

	private void setHandler(Handler<Pair<String,String>> handler) {
		this.handler = handler; 
	}
	
	public static void showModal(Window owner,Handler<Pair<String,String>> handler) throws IOException {
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("forms/Login.fxml"), Main.getResources());
		Parent rpane =  loader.load();
		((LoginController)loader.getController()).setHandler(handler);
		Scene scene = new Scene(rpane);
		scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());
		stage.setScene(scene);
	    stage.setTitle(Main.getResources().getString("frm-title-login"));
	    stage.initModality(Modality.WINDOW_MODAL);
	    stage.initOwner(owner);
		stage.show();
	}

}
