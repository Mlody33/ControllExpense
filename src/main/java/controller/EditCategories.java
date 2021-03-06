package controller;

import application.AppState;
import application.Main;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditCategories implements Initializable {

	@FXML private TextField newCategoryName, chosenCategoryName;
	@FXML private ComboBox<String> cbCategoryList;

	private Stage dialogStage;
	private Main main;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
	public void setDialogStage(Stage dialogStage) {	this.dialogStage = dialogStage;	}
	public void setMain(Main main){ this.main = main; }

	public void setCategoryList(ObservableList<String> categoryList) {
		cbCategoryList.setItems(categoryList);
	}

	@FXML public void handleChosenCategory() {
		chosenCategoryName.setText(cbCategoryList.getSelectionModel().getSelectedItem());
		newCategoryName.requestFocus();
	}

	@FXML public void handleAcceptChange() {
		if(newCategoryName.getText().isEmpty() || chosenCategoryName.getText().isEmpty()){
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle(AppState.NO_DATA_TITLE.get());
			alert.setHeaderText(null);
			alert.setContentText(AppState.TYPE_ALL_DATA_EDIT.get());
			alert.showAndWait();
		} else {
			String oldCategoryName = cbCategoryList.getSelectionModel().getSelectedItem();
			main.getDatabase().updateCategory(newCategoryName.getText(), oldCategoryName);
			dialogStage.close();
			main.refreshDataInTable();
		}
	}

	@FXML public void handleCancelChange() {
		dialogStage.close();
	}
}
