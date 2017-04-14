package controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Expense;
import javafx.scene.control.Alert.AlertType;

public class AddExpenseController implements Initializable {

	@FXML TextField newName;
	@FXML TextField newCategory;
	@FXML ComboBox<String> newCategoryComboBox;
	@FXML TextField newPrice;
	@FXML TextField newQuantity;
	@FXML Spinner newQuantitySpinner;
	@FXML CheckBox isNewExpenseIsPaidByCreditCard;

	@FXML DatePicker newExpenseDate;
	@FXML Button addExpenseButton;

	@FXML Button cancelExpenseButton;

	private Stage dialogStage;
	private Main main;
	private Expense expense;
	private boolean isAccepted = false;
	
	private ObservableList<String> categoryList = FXCollections.observableArrayList();
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	@FXML public void handleAddExpense() {
		if(newName.getText() == null | newCategory.getText() == null | newPrice.getText().isEmpty() | newQuantity.getText().isEmpty() | newExpenseDate.getValue() == null){
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Brak danych");
			alert.setHeaderText(null);
			alert.setContentText("Wymagane sa wszystkie pola do utworzenia nowego wydatku!");
			alert.showAndWait();
		} else {
			expense.setName(newName.getText());
			expense.setCategory(newCategoryComboBox.getEditor().getText());
			expense.setPrice(Double.parseDouble(newPrice.getText()));
			expense.setQuantity(Integer.parseInt(newQuantity.getText()));
			expense.setPaidByCreditCard(isNewExpenseIsPaidByCreditCard.isSelected());
			expense.setDate(newExpenseDate.getValue());

			isAccepted = true;
			System.out.println(expense.toString());

			dialogStage.close();
		}
	}

	@FXML public void handleCancelExpense() {
		dialogStage.close();
	}
	
	public boolean isAcceptedNewExpense(){
		return isAccepted;
	}

	public void setExpense(Expense expense) {

		categoryList = main.getDatabase().selectExpensesCategory();
		newCategoryComboBox.setItems(categoryList);
		
		this.expense = expense;
		
		newName.setText(expense.getName());
		newCategory.setText(expense.getCategory());
		if(expense.getPrice() == 0)
			newPrice.clear();
		else
			newPrice.setText(Double.toString(expense.getPrice()));
		if(expense.getQuantity() == 0)
			newQuantity.clear();
		else
			newQuantity.setText(Integer.toString(expense.getQuantity()));
		isNewExpenseIsPaidByCreditCard.setSelected(expense.isPaidByCreditCard());
		
		LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		if(expense.getDate() == null)
			newExpenseDate.setValue(localDate);
		else
			newExpenseDate.setValue(expense.getDate());
	}

	public void setMain(Main main){
		this.main = main;
	}
	
}
