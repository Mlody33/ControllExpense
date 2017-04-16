package controller;

import application.Main;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditCategoriesController implements Initializable {

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
			alert.setTitle("Brak danych");
			alert.setHeaderText(null);
			alert.setContentText("Wymagane jest uzupelnienie wszystkich danych!");
			alert.showAndWait();
		} else {
			String oldCategoryName = cbCategoryList.getSelectionModel().getSelectedItem();
			main.getDatabase().updateCategory(newCategoryName.getText(), oldCategoryName);
			dialogStage.close();
			main.refreshDataTable();
		}
	}

	@FXML public void handleCancelChange() {
		dialogStage.close();
	}
}
