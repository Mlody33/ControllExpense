package controller;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Expense;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class EditCategoriesController implements Initializable {

	@FXML TextField newCategoryName;
	@FXML TextField chosenCategoryName;
	@FXML ComboBox cbCategoryList;

	@FXML Button acceptChangeButton, cancelChangeButton;

	private Stage dialogStage;
	private Main main;

	private ObservableList<String> categoryList = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
	public void setDialogStage(Stage dialogStage) {	this.dialogStage = dialogStage;	}
	public void setMain(Main main){ this.main = main; }

	public void setCategoryList(ObservableList<String> categoryList) {
		this.categoryList = categoryList;
		cbCategoryList.setItems(categoryList);
	}

	@FXML public void handleChosenCategory() {
		chosenCategoryName.setText(cbCategoryList.getSelectionModel().getSelectedItem().toString());
		newCategoryName.requestFocus();
	}

	@FXML public void handleAcceptChange() {
		String newCategoryName = this.newCategoryName.getText();
		String oldCategoryName = cbCategoryList.getSelectionModel().getSelectedItem().toString();

		main.getDatabase().updateCategory(newCategoryName, oldCategoryName);
		dialogStage.close();

		main.getExpensesData().clear();
		main.getExpensesData().addAll(main.getDatabase().selectExpensesData());
	}

	@FXML public void handleCancelChange() {
		dialogStage.close();
	}
}
