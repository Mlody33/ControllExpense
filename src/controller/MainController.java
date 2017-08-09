package controller;

import java.net.URL;
import java.util.ResourceBundle;
import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.application.Platform;

public class MainController implements Initializable{
	
	private Main main;

	public void setMain(Main main) {
		this.main = main;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	@FXML public void handleRefreshData() {
	    main.refreshDataInTable();
	}

	@FXML public void handleExit() {
        main.getDatabase().closeConnection();
        Platform.exit();
	}

	@FXML public void handleChangeCategories() {
		main.showEditCategoryNameDialog();
	}

	@FXML public void handleAddExpense() {
		main.getExpenseOverview().handleAddExpense();
	}

	@FXML public void handleEditExpense() {
		main.getExpenseOverview().handleEditExpense();
	}

	@FXML public void handleDeleteExpense() {
		main.getExpenseOverview().handleRemoveExpense();
	}

	@FXML public void handleShowSheetReport() {}

	@FXML public void handleShowCharReport() {
		main.showExpenseChart();
	}

}