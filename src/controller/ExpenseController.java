package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import application.Main;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import model.Expense;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

public class ExpenseController implements Initializable{
	
	@FXML TableView<Expense> expensesTable;
	@FXML TableColumn<Expense, String> nameColumn;
	@FXML TableColumn<Expense, String> categoryColumn;
	@FXML TableColumn<Expense, Double> priceColumn;
	@FXML TableColumn<Expense, Integer> quantityColumn;
	@FXML TableColumn<Expense, Boolean> paidByCreditCardColumn;
	@FXML TableColumn<Expense, LocalDate> dateColumn;
	
	@FXML ComboBox<Integer> cbChoseDayToSort;
	@FXML ComboBox<String> cbChoseMonthToSort;
	
	@FXML Button newExpenseButton;
	@FXML Button editExpenseButton;
	@FXML Button removeExpenseButton;
	
	private Main main;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameColumn.setCellValueFactory(new PropertyValueFactory<Expense, String>("name"));
		categoryColumn.setCellValueFactory(new PropertyValueFactory<Expense, String>("category"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<Expense, Double>("price"));
		quantityColumn.setCellValueFactory(new PropertyValueFactory<Expense, Integer>("quantity"));
		paidByCreditCardColumn.setCellValueFactory(new PropertyValueFactory<Expense, Boolean>("paidByCreditCard"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<Expense, LocalDate>("date"));
	}
	
	public void setMain(Main main) {
		this.main = main;
		expensesTable.setItems(main.getExpensesData());
	}

	@FXML public void handleAddExpense() {
		Expense tmpExpense = new Expense();
		boolean isAccepted = main.showAddExpenseDialog(tmpExpense);
		if(isAccepted){
			main.getExpensesData().add(tmpExpense);
			main.getDatabase().insertExpense(tmpExpense);
		}
	}

	@FXML public void handleEditExpense() {
		Expense selectedExpense = main.getSelectedExpense();
		if(selectedExpense != null){
			boolean isAccepted = main.showAddExpenseDialog(selectedExpense);
			if(isAccepted){
				main.getExpensesData().set(expensesTable.getSelectionModel().getSelectedIndex(), selectedExpense);
				main.getDatabase().updateExpense(selectedExpense);
			}
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("Nie zaznaczono wydatku");
			alert.setHeaderText(null);
			alert.setContentText("Najpierw zaznacz istniejacy wydatek w tabeli!");
			alert.showAndWait();
		}
	}

	@FXML public void handleRemoveExpense() {
		Expense selectedExpense = expensesTable.getSelectionModel().getSelectedItem();
		if(selectedExpense != null){
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("Czy chcesz usunac?");
			alert.setHeaderText(null);
			alert.setContentText("Czy chcesz usunac zaznaczony wydatek?");
			
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				System.out.println("Database[ID]: "+selectedExpense.getId());
				System.out.println("Table[ID]: "+expensesTable.getSelectionModel().getSelectedIndex());
				
				main.getExpensesData().remove(expensesTable.getSelectionModel().getSelectedIndex());
				main.getDatabase().removeExpense(selectedExpense);
			}
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("Nie zaznaczono wydatku");
			alert.setHeaderText(null);
			alert.setContentText("Najpierw zaznacz istniejacy wydatek w tabeli!");
			alert.showAndWait();
		}
	}

	@FXML public void handleSelectedExpense() {
		main.setSelectedExpense(expensesTable.getSelectionModel().getSelectedItem());
	}
	

}