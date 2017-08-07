package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import application.AppState;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Expense;
import application.Main;

public class ExpensesOverview implements Initializable{

	@FXML private TableView<Expense> expensesTable;
	@FXML private TableColumn<Expense, String> nameColumn;
	@FXML private TableColumn<Expense, String> categoryColumn;
	@FXML private TableColumn<Expense, Double> priceColumn;
	@FXML private TableColumn<Expense, Integer> quantityColumn;
	@FXML private TableColumn<Expense, Boolean> paidByCreditCardColumn;
	@FXML private TableColumn<Expense, LocalDate> dateColumn;

	@FXML private ToggleButton filterName, filterCategory;
	@FXML private ComboBox<String> cbFilterOption;

	private Main main;

	public void setMain(Main main) {
		this.main = main;
		expensesTable.setItems(main.getExpenses());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameColumn.setCellValueFactory(new PropertyValueFactory<Expense, String>("name"));
		categoryColumn.setCellValueFactory(new PropertyValueFactory<Expense, String>("category"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<Expense, Double>("price"));
		quantityColumn.setCellValueFactory(new PropertyValueFactory<Expense, Integer>("quantity"));
		paidByCreditCardColumn.setCellValueFactory(new PropertyValueFactory<Expense, Boolean>("paidByCreditCard"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<Expense, LocalDate>("date"));
	}

	@FXML public void handleAddExpense() {
		Expense tmpExpense = new Expense();
		boolean isAccepted = main.showAddExpenseDialog(tmpExpense);
		if(isAccepted){
			main.getExpenses().add(tmpExpense);
			main.getDatabase().insertExpense(tmpExpense);
		}
	}

	@FXML public void handleEditExpense() {
		Expense selectedExpense = main.getSelectedExpense();
		if(selectedExpense != null){
			boolean isAccepted = main.showAddExpenseDialog(selectedExpense);
			if(isAccepted){
				main.getExpenses().set(expensesTable.getSelectionModel().getSelectedIndex(), selectedExpense);
				main.getDatabase().updateExpense(selectedExpense);
			}
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle(AppState.EXPENSE_NOT_SELECTED.get());
			alert.setHeaderText(null);
			alert.setContentText(AppState.SELECT_EXPENSE.get());
			alert.showAndWait();
		}
	}

	@FXML public void handleRemoveExpense() {
		Expense selectedExpense = expensesTable.getSelectionModel().getSelectedItem();
		if(selectedExpense != null){
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle(AppState.CONFIRM_DELETE_TITLE.get());
			alert.setHeaderText(null);
			alert.setContentText(AppState.CONFIRM_DELETE.get());
			
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				main.getExpenses().remove(expensesTable.getSelectionModel().getSelectedIndex());
				main.getDatabase().removeExpense(selectedExpense);
			}
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle(AppState.EXPENSE_NOT_SELECTED.get());
			alert.setHeaderText(null);
			alert.setContentText(AppState.SELECT_EXPENSE.get());
			alert.showAndWait();
		}
	}

	@FXML public void handleSelectedExpense() {
		main.setSelectedExpense(expensesTable.getSelectionModel().getSelectedItem());
	}


	@FXML public void handleFilterName() {
		if(filterName.isSelected()) {
			cbFilterOption.setDisable(false);
			cbFilterOption.setItems(main.getDatabase().getExpensesName());
		} else {
			expensesTable.setItems(main.getExpenses());
			cbFilterOption.setDisable(true);
		}

	}

	@FXML public void handleFilterCategory() {
		if (filterCategory.isSelected()) {
			cbFilterOption.setDisable(false);
			cbFilterOption.setItems(main.getDatabase().getExpensesCategory());
		} else {
			expensesTable.setItems(main.getExpenses());
			cbFilterOption.setDisable(true);
		}
	}

	@FXML public void handleFilterOption() {
		String selectedOption = cbFilterOption.getSelectionModel().getSelectedItem();
		expensesTable.setItems(main.getDatabase().getFilteredExpensesByNameOrCategory(selectedOption));
	}
}