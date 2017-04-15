package controller;

import java.net.URL;
import java.util.ResourceBundle;
import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.application.Platform;

public class MainController implements Initializable{
	
	
	@FXML MenuItem refreshDataMenuItem;
	@FXML MenuItem closeAppMenuItem;
	
	@FXML MenuItem addExpenseMenuItem;
	@FXML MenuItem editExpenseMenuItem;
	@FXML MenuItem deleteExpenseMenuItem;
	
	@FXML MenuItem showSheetReportMenuItem;
	@FXML MenuItem showChartReportMenuItem;
	
	private Main main;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

	public void setMain(Main main) {
		this.main = main;
	}
	
	@FXML public void menuHandleRefreshData() {
		main.getExpensesData().clear();
		main.getExpensesData().addAll(main.getDatabase().selectExpensesData());
	}

	@FXML public void menuHandleExit() {
		Platform.exit();
	}

	@FXML public void menuHandleChangeCategories() { main.showEditCategoryNameDialog(); }

	@FXML public void menuHandleAddExpense() {
		main.getExpenseController().handleAddExpense();
	}

	@FXML public void menuHandleEditExpense() {
		main.getExpenseController().handleEditExpense();
	}

	@FXML public void menuHandleRemoveExpense() {
		main.getExpenseController().handleRemoveExpense();
	}

	@FXML public void menuHandleShowSheetReport() {}

	@FXML public void menuHandleShowCharReport() {
		main.showExpenseChart();
	}
	
	

}
