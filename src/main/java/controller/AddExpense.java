package controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import application.AppState;
import application.Main;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Expense;
import javafx.scene.control.Alert.AlertType;

public class AddExpense implements Initializable {

	@FXML private TextField newName;
	@FXML private ComboBox<String> newCategory;
	@FXML private TextField newPrice;
	@FXML private Spinner newQuantity;
	@FXML private CheckBox isNewExpenseIsPaidByCreditCard;
	@FXML private Tooltip categoryToolTip;
	@FXML private DatePicker newExpenseDate;

	private Stage dialogStage;
	private Main main;
	private Expense expense;
	private boolean isAccepted = false;
	
	private ObservableList<String> categoryList = FXCollections.observableArrayList();
	private ObservableList<String> matchedCategory = FXCollections.observableArrayList();
	private int indexOfMatchedCategory;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	categoryToolTip.setAutoHide(true); }
	public void setDialogStage(Stage dialogStage) {	this.dialogStage = dialogStage;	}
	public void setMain(Main main){ this.main = main; }
	public boolean isAcceptedNewExpense(){
		return isAccepted;
	}


	public void setExpense(Expense expense) {
		categoryList = main.getDatabase().getExpensesCategory();
		newCategory.setItems(categoryList);

		this.expense = expense;

		newName.setText(expense.getName());
		newCategory.getEditor().setText(expense.getCategory());
		if(expense.getPrice() == 0)
			newPrice.clear();
		else
			newPrice.setText(Double.toString(expense.getPrice()));
		if(expense.getQuantity() == 0)
			newQuantity.getEditor().setText(AppState.DEFAULT_QUANTITY.get());
		else
			newQuantity.getEditor().setText(Integer.toString(expense.getQuantity()));
		isNewExpenseIsPaidByCreditCard.setSelected(expense.isPaidByCreditCard());

		LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		if(expense.getDate() == null)
			newExpenseDate.setValue(localDate);
		else
			newExpenseDate.setValue(expense.getDate());
	}

	public void handleKeyReleased(KeyEvent keyEvent) {
		switch(keyEvent.getCode().toString()){
			case "ENTER":
				newCategory.getEditor().setText(categoryToolTip.getText());
				categoryToolTip.hide();
				break;
			case "RIGHT":
			case "DOWN":
				if(indexOfMatchedCategory == matchedCategory.size()-1) {
					indexOfMatchedCategory = -1;
				}
				categoryToolTip.setText(matchedCategory.get(++indexOfMatchedCategory));
				break;
			case "LEFT":
			case "UP":
				if(indexOfMatchedCategory == 0)
					indexOfMatchedCategory = matchedCategory.size();
				categoryToolTip.setText(matchedCategory.get(--indexOfMatchedCategory));
				break;
			default:
				matchedCategory.clear();
				findInCategory(newCategory.getEditor().getText());
				if (!matchedCategory.isEmpty()) {
					categoryToolTip.setText(matchedCategory.get(indexOfMatchedCategory));
					categoryToolTip.show(dialogStage);
					categoryToolTip.setX(dialogStage.getX() + newCategory.getLayoutX());
					categoryToolTip.setY(dialogStage.getY() + newCategory.getLayoutY() + newCategory.getHeight() + 30);
				} else
					categoryToolTip.hide();
				break;
		}
	}

	private void findInCategory(String value){
		if(value.isEmpty())
			matchedCategory.clear();
		for(String category: categoryList){
			if(category.toLowerCase().contains(value.toLowerCase())){
				matchedCategory.add(category);
			}
		}
		indexOfMatchedCategory = 0;
	}

	@FXML public void handleAddExpense() {
		if(newName.getText() == null | newCategory.getEditor().getText() == null | newPrice.getText().isEmpty() | newQuantity.getEditor().getText().isEmpty() | newExpenseDate.getValue() == null){
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle(AppState.NO_DATA_TITLE.get());
			alert.setHeaderText(null);
			alert.setContentText(AppState.TYPE_ALL_DATA_ADD.get());
			alert.showAndWait();
		} else {
			expense.setName(newName.getText());
			expense.setCategory(newCategory.getEditor().getText());
			expense.setPrice(Double.parseDouble(newPrice.getText()));
			expense.setQuantity(Integer.parseInt(newQuantity.getEditor().getText()));
			expense.setPaidByCreditCard(isNewExpenseIsPaidByCreditCard.isSelected());
			expense.setDate(newExpenseDate.getValue());
			isAccepted = true;
			dialogStage.close();
		}
	}

	@FXML public void handleCancelExpense() {
		dialogStage.close();
	}
}
