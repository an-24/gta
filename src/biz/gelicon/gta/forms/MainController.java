package biz.gelicon.gta.forms;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import biz.gelicon.gta.GTATray;
import biz.gelicon.gta.Main;
import biz.gelicon.gta.Monitor;
import biz.gelicon.gta.User;
import biz.gelicon.gta.data.Person;
import biz.gelicon.gta.data.Team;
import biz.gelicon.gta.net.ConnectionFactory;
import biz.gelicon.gta.net.NetService;
import biz.gelicon.gta.view.NodeView;

public class MainController {


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label lCountdown;

    @FXML
    private Button btnStart;

    @FXML
    private MenuItem miConnect;

    @FXML
    private TextField teamTextField;

    @FXML
    private MenuItem miDisconnect;

    @FXML
    private MenuItem miAbout;


    @FXML
    private MenuItem miSettings;

    @FXML
    private Label lUser;

    @FXML
    private TableView<Pair<String,String>> tableTeamTimes;

    @FXML
    private CheckBox onlineCheckBox;

    @FXML
    private Menu miFile;

    @FXML
    private MenuItem miExit;

    @FXML
    private Tab tabPerson;

    @FXML
    private VBox root;

    @FXML
    private Tab tabTeam;

    @FXML
    private TextField nicTextField;

    @FXML
    private CheckBox activeCheckBox;

    @FXML
    private TextField postTextField;

    @FXML
    private TabPane propertySheetTabPane;

    @FXML
    private TextField createDateTextField;

    @FXML
    void initialize() {
        assert lCountdown != null : "fx:id=\"lCountdown\" was not injected: check your FXML file 'Main.fxml'.";
        assert btnStart != null : "fx:id=\"btnStart\" was not injected: check your FXML file 'Main.fxml'.";
        assert miConnect != null : "fx:id=\"miConnect\" was not injected: check your FXML file 'Main.fxml'.";
        assert teamTextField != null : "fx:id=\"teamTextField\" was not injected: check your FXML file 'Main.fxml'.";
        assert miDisconnect != null : "fx:id=\"miDisconnect\" was not injected: check your FXML file 'Main.fxml'.";
        assert miAbout != null : "fx:id=\"miAbout\" was not injected: check your FXML file 'Main.fxml'.";
        assert tree != null : "fx:id=\"tree\" was not injected: check your FXML file 'Main.fxml'.";
        assert miSettings != null : "fx:id=\"miSettings\" was not injected: check your FXML file 'Main.fxml'.";
        assert lUser != null : "fx:id=\"lUser\" was not injected: check your FXML file 'Main.fxml'.";
        assert tableTeamTimes != null : "fx:id=\"tableTeamTimes\" was not injected: check your FXML file 'Main.fxml'.";
        assert onlineCheckBox != null : "fx:id=\"onlineCheckBox\" was not injected: check your FXML file 'Main.fxml'.";
        assert miFile != null : "fx:id=\"miFile\" was not injected: check your FXML file 'Main.fxml'.";
        assert miExit != null : "fx:id=\"miExit\" was not injected: check your FXML file 'Main.fxml'.";
        assert tabPerson != null : "fx:id=\"tabPerson\" was not injected: check your FXML file 'Main.fxml'.";
        assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'Main.fxml'.";
        assert tabTeam != null : "fx:id=\"tabTeam\" was not injected: check your FXML file 'Main.fxml'.";
        assert nicTextField != null : "fx:id=\"nicTextField\" was not injected: check your FXML file 'Main.fxml'.";
        assert activeCheckBox != null : "fx:id=\"activeCheckBox\" was not injected: check your FXML file 'Main.fxml'.";
        assert postTextField != null : "fx:id=\"postTextField\" was not injected: check your FXML file 'Main.fxml'.";
        assert propertySheetTabPane != null : "fx:id=\"propertySheetTabPane\" was not injected: check your FXML file 'Main.fxml'.";
        assert createDateTextField != null : "fx:id=\"createDateTextField\" was not injected: check your FXML file 'Main.fxml'.";
        
        init();
    }   

    @FXML
    private TreeTableView<NodeView> tree;
    
    private static final Logger log = Logger.getLogger("gta");
	private Team processTeam = null;
	private Monitor currentMonitor;
	private User currentUser;
	private NetService con;
	private int countDown = Monitor.MONITOR_PERIOD/1000/60; 
	private Timer timerCountDown;
	private Timer timerPing;
    
	private void init() {
		con = ConnectionFactory.getConnection(Main.getProperty("url"));

        tree.setRoot(new TreeItem<NodeView>(new NodeView() {
			@Override
			public String getText() {
				return resources.getString("teams");
			}

			@Override
			public boolean isActive() {
				return true;
			}
		}));
        
        TableColumn<Pair<String,String>, String> cKey = new TableColumn<>(resources.getString("param"));
        cKey.setCellValueFactory(new PropertyValueFactory<>("key"));
        cKey.setPrefWidth(180);
        

        TableColumn<Pair<String,String>, String> cValue = new TableColumn<>(resources.getString("value"));
        cValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        cValue.setPrefWidth(180);
        tableTeamTimes.getColumns().addAll(cKey,cValue);
        
        TreeTableColumn<NodeView, NodeView> c = new TreeTableColumn<NodeView, NodeView>();
        
        c.setCellValueFactory(new Callback<CellDataFeatures<NodeView, NodeView>,  ObservableValue<NodeView>>() {

			@Override
			public ObservableValue<NodeView> call(
					CellDataFeatures<NodeView, NodeView> wrap) {
				return new SimpleObjectProperty<NodeView>(wrap.getValue().getValue()) ;
			}
		});
        
        c.setSortable(false);
        c.setCellFactory(new Callback<TreeTableColumn<NodeView, NodeView>, TreeTableCell<NodeView, NodeView>>(){

			@Override
			public TreeTableCell<NodeView, NodeView> call(
					TreeTableColumn<NodeView, NodeView> param) {
				TreeTableCell<NodeView, NodeView> cell = new TreeTableCell<NodeView,NodeView>(){

					@Override
					public void updateItem(NodeView item, boolean empty) {  
	                    super.updateItem(item, empty);
	                    if(!empty) {
	                    	setText(item.getText());
                    		if(!item.isActive()) 
                    			setTextFill(Color.SILVER);else
                    				setTextFill(Color.BLACK);
                    			
	                    } else {
	                    	setText(null);
	                    }
	                }
					
				};
				return cell;
			}
        	
        });
        
        tree.getColumns().add(c);
        
        tree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<NodeView>>() {

			@Override
			public void changed(
					ObservableValue<? extends TreeItem<NodeView>> observable,
					TreeItem<NodeView> oldValue, TreeItem<NodeView> newValue) {
				if(newValue!=null) loadPropertySheet(newValue.getValue());else 
					loadPropertySheet(null);
				
			}


		});
        
        btnStart.setOnAction(e->{
			Team selteam = getSelectionTeam();
			if(selteam==null) return;
			if(selteam.isActive()) finishProcessTeam(selteam);else startProcessTeam(selteam);
		});
        
        miExit.setOnAction(e->{
        	Main.quit();
        });
        
        miConnect.setOnAction(e->{
			try {
				connect();
			} catch (Exception ex) {
				log.log(Level.SEVERE, ex.getMessage(), ex);
			}
        });
        
        miDisconnect.setOnAction(e->{
			disconnect();
        });
        
        miFile.setOnShowing(e->{
        	miConnect.setDisable(currentUser!=null);
        	miDisconnect.setDisable(currentUser==null);
        });
        
        miSettings.setOnAction(e->{
			//TODO
        });
        
        miAbout.setOnAction(e->{
			//TODO
        });
        
        Platform.runLater(()->{
        	miConnect.fire();
        });
    }

	private void updateTray() {
		Stage win = (Stage)root.getScene().getWindow();
		if(currentUser==null) GTATray.getInstance().updateState(GTATray.State.stInactive,win.getTitle()); else
			if(processTeam==null) GTATray.getInstance().updateState(GTATray.State.stReady,win.getTitle());else
				GTATray.getInstance().updateState(GTATray.State.stActive,win.getTitle());
	}


	private void updateStatus() {
		if(currentUser==null) lUser.setText(resources.getString("connect-needed")); else
			lUser.setText(currentUser.getNic());
	}

	private void startProcessTeam(Team team) {
		if(team == null) return;

		tree.getRoot().getChildren().forEach(t -> {
			NodeView curr = t.getValue();
			if(curr.isActive()) {
				finishProcessTeam((Team)curr);
			}
		});
		
		team.setActive(true);
		processTeam = team;

		currentMonitor = Monitor.startMonitor(currentUser, v->{
			Platform.runLater(()->{
				lCountdown.setText(resources.getString("massage-postdata-succ"));
			});
			countDown = Monitor.MONITOR_PERIOD/1000/60;
		});
		
		if(timerCountDown!=null) timerCountDown.cancel();
		timerCountDown = new Timer();
		timerCountDown.schedule(new TimerTask() {
			@Override
			public void run() {
				final int sCountDown = countDown;  
				Platform.runLater(()->{
					lCountdown.setText(String.format(resources.getString("message-countdown"), new Integer(sCountDown).toString()));
				});
				countDown--;
			}
		}, 0, Monitor.MONITOR_COUNTDOWN);
		
		updatePropertySheet();
		updateTree();
		updateCaption();
		updateTray();
	}

	private void finishProcessTeam(Team team) {
		if(team == null) return;
		team.setActive(false);
		
		if(currentMonitor!=null)
			currentMonitor.stop();
		
		timerCountDown.cancel();
		timerCountDown = null;
		lCountdown.setText("");
		
		processTeam = null;
		updatePropertySheet();
		updateTree(team);
		updateCaption();
		updateTray();
	}

	private void updateCaption() {
		Stage win = (Stage)root.getScene().getWindow();
		if(processTeam==null) win.setTitle(Main.GTA_APP_NAME+" (stopped)");else
			win.setTitle(Main.GTA_APP_NAME+" - active \""+processTeam.getName()+"\"");
	}

	
	private void updateTree(Team team) {
		tree.getRoot().getChildren().forEach(t -> {
			if(t.getValue()==team) {
				t.setValue(null);
				t.setValue(team);
			}
		});
	}

	private void updateTree() {
		tree.getRoot().getChildren().forEach(t -> {
			NodeView nv = t.getValue();
			t.setValue(null);
			t.setValue(nv);
		});
	}
	
	private Team getSelectionTeam() {
		TreeItem<NodeView> node = tree.getSelectionModel().selectedItemProperty().get();
		if(node==null) return null;
		NodeView value = node.getValue();
		if(value instanceof Team) return (Team) value;
		if(value instanceof Person) {
			node = node.getParent();
			return (Team) node.getValue();
		}
		return null;
	}
	
	private void updatePropertySheet() {
		loadPropertySheet(getSelectionTeam());
	}

	private void loadPropertySheet(NodeView value) {
		
		if(value instanceof Team) {
			Team team = (Team) value;
			teamTextField.setText(team.getName());
			activeCheckBox.selectedProperty().set(team.isActive());
			createDateTextField.setText(team.getCreateDateAsText());
			if(team.isActive()) btnStart.setText(resources.getString("finish")); else
				btnStart.setText(resources.getString("start"));
			tableTeamTimes.setItems(makeTimeItems(team));
			
			if(propertySheetTabPane.getTabs().indexOf(tabTeam)<0)
				propertySheetTabPane.getTabs().add(tabTeam);
			propertySheetTabPane.getTabs().remove(tabPerson);
		} else 
		if(value instanceof Person) {
			Person p = (Person) value;
			nicTextField.setText(p.getNic());
			postTextField.setText(p.getPost());
			onlineCheckBox.selectedProperty().set(p.isActive());
			
			if(propertySheetTabPane.getTabs().indexOf(tabPerson)<0)
				propertySheetTabPane.getTabs().add(tabPerson);
			propertySheetTabPane.getTabs().remove(tabTeam);
		} else {
			propertySheetTabPane.getTabs().remove(tabPerson);
			propertySheetTabPane.getTabs().remove(tabTeam);
		}
		
	}

	private ObservableList<Pair<String, String>> makeTimeItems(Team team) {
		List<Pair<String, String>> teamTimes = new ArrayList<>();
		teamTimes.add(new Pair<String,String>(resources.getString("limit"),String.valueOf(team.getLimit())));
		teamTimes.add(new Pair<String,String>(resources.getString("work-of-day"),String.valueOf(team.getWorkedOfDay())));
		teamTimes.add(new Pair<String,String>(resources.getString("work-of-week"),String.valueOf(team.getWorkedOfWeek())));
		teamTimes.add(new Pair<String,String>(resources.getString("work-of-month"),String.valueOf(team.getWorkedOfMonth())));
		teamTimes.add(new Pair<String,String>(resources.getString("work-of-begin-project"),String.valueOf(team.getWorkedOfBeginProject())));
		return FXCollections.observableList(teamTimes);
	}


	private void clearTreeTeams() {
		tree.getRoot().getChildren().clear();
		tree.getSelectionModel().clearSelection();
	};
	
	private void fillTreeTeams() {
		clearTreeTeams();
		tree.getRoot().getChildren().addAll(makeTreeItems(con.getTeams()));
	}

	private Collection<TreeItem<NodeView>> makeTreeItems(List<? extends NodeView> nodes) {
		List<TreeItem<NodeView>> items =  new LinkedList<>(); 
		for (NodeView nv : nodes) {
			TreeItem<NodeView> itm = new TreeItem<NodeView>(nv);
			items.add(itm);
			if(nv instanceof Team) {
				Team team = (Team) nv;
				itm.getChildren().addAll(makeTreeItems(team.getPersons()));
			}
		}
		return items;
	}

	private void connect() throws Exception {
		updatePropertySheet();
		LoginController.showModal(root.getScene().getWindow(), pair->{
			currentUser = con.connect(pair.getKey(), pair.getValue());
			if(currentUser==null) throw new Exception(Main.getResources().getString("err-invalid-user-or-password"));
			
			if(timerPing!=null) timerPing.cancel();
			timerPing = new Timer();
			timerPing.schedule(new TimerTask() {
				@Override
				public void run() {
					con.ping(list->{
						//TODO
					});
				}
			}, Monitor.MONITOR_PING, Monitor.MONITOR_PING);
			
		    fillTreeTeams();
		    updateCaption();
			updateStatus();
			updateTray();
		});
	}


	private void disconnect() {
		if(currentUser==null) return;
		if(processTeam!=null) finishProcessTeam(processTeam);
		
		if(timerPing!=null) timerPing.cancel();
		timerPing=null;
		
		currentUser=null;
		
		clearTreeTeams();
		updatePropertySheet();
		updateStatus();
		updateTray();
	}
}
