package biz.gelicon.gta;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import biz.gelicon.gta.data.Person;
import biz.gelicon.gta.data.Team;
import biz.gelicon.gta.view.NodeView;

public class MainController {

    private static final String GTA_APP_NAME = "Gelicon Team App";

	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private Control root;

    @FXML
    private TreeTableView<NodeView> tree;
    @FXML
    private TextField teamTextField;
    @FXML
    private TextField nicTextField;
    @FXML
    private CheckBox activeCheckBox;
    @FXML
    private TextField postTextField;
    @FXML
    private TextField createDateTextField;
    @FXML
    private TabPane propertySheetTabPane;
    @FXML
    private CheckBox onlineCheckBox;
    @FXML
    private Tab tabPerson;
    @FXML
    private Tab tabTeam;
    @FXML
    private Button btnStart;

	private Team processTeam = null;
	private Monitor currentMonitor;

	private User currentUser;
    
    @FXML
	public void initialize() {
        assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'Main.fxml'.";
        assert teamTextField != null : "fx:id=\"teamTextField\" was not injected: check your FXML file 'Main.fxml'.";
        assert tree != null : "fx:id=\"tree\" was not injected: check your FXML file 'Main.fxml'.";
        assert nicTextField != null : "fx:id=\"nicTextField\" was not injected: check your FXML file 'Main.fxml'.";
        assert activeCheckBox != null : "fx:id=\"activeCheckBox\" was not injected: check your FXML file 'Main.fxml'.";
        assert postTextField != null : "fx:id=\"postTextField\" was not injected: check your FXML file 'Main.fxml'.";
        assert propertySheetTabPane != null : "fx:id=\"propertySheetTabPane\" was not injected: check your FXML file 'Main.fxml'.";
        assert createDateTextField != null : "fx:id=\"createDateTextField\" was not injected: check your FXML file 'Main.fxml'.";
        assert onlineCheckBox != null : "fx:id=\"onlineCheckBox\" was not injected: check your FXML file 'Main.fxml'.";
        assert btnStart != null : "fx:id=\"btnStart\" was not injected: check your FXML file 'Main.fxml'.";

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
        
        btnStart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Team selteam = getSelectionTeam();
				if(selteam==null) return;
				if(selteam.isActive()) finishProcessTeam(selteam);else startProcessTeam(selteam);
			}
		});
        
        fillTreeTeams();
        
        
        Platform.runLater(new Runnable() {
			@Override
			public void run() {
		        updateCaption();
			}
		});
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

		currentMonitor = Monitor.startMonitor(currentUser);
		
		updatePropertySheet();
		updateTree();
		updateCaption();
		
	}

	private void finishProcessTeam(Team team) {
		if(team == null) return;
		team.setActive(false);
		
		if(currentMonitor!=null)
			currentMonitor.stop();
		
		processTeam = null;
		updatePropertySheet();
		updateTree(team);
		updateCaption();
	}

	private void updateCaption() {
		Stage win = (Stage)root.getScene().getWindow();
		if(processTeam==null) win.setTitle(GTA_APP_NAME+" (stopped)");else
			win.setTitle(GTA_APP_NAME+" - active \""+processTeam.getName()+"\"");
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

	private void fillTreeTeams() {
		tree.getRoot().getChildren().clear();
		//FIXME demo
		tree.getRoot().getChildren().addAll(makeTreeItems(Team.getDemoTeams()));
		
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

}
