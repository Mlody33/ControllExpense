package application;

import java.io.IOException;
import java.util.logging.Logger;

import controller.*;
import model.Expense;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;

	private Logger log = Logger.getLogger("Main " + this.getClass().getName());
	private ObservableList<Expense> expenseData = FXCollections.observableArrayList();
	private Database db = new Database();
	private Expense selectedExpense;
	private ExpensesOverview expenseOverview;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		
		db.connect();
		expenseData.addAll(db.getExpenses());
		
		initMainView();
		showExpenseOverview();
	}
	
	private void initMainView() {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../view/Main.fxml"));
			rootLayout = loader.load();
			
			MainController controller = loader.getController();
			controller.setMain(this);

			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add(Main.class.getResource("../application/style.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Control your expense");
			primaryStage.show();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void showExpenseOverview() {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../view/ExpensesOverview.fxml"));
			AnchorPane expenseOverview = loader.load();
			
			rootLayout.setCenter(expenseOverview);
			
			this.expenseOverview = loader.getController();
			this.expenseOverview.setMain(this);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void showExpenseChart() {
		try{			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../view/ExpenseChart.fxml"));
			AnchorPane expenseChart = loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Expense Chart");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			
			Scene scene = new Scene(expenseChart);
			dialogStage.setScene(scene);
			
			ExpenseReportChart controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMain(this);
			
			dialogStage.showAndWait();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public boolean showAddExpenseDialog(Expense expense) {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../view/AddExpense.fxml"));
			AnchorPane addExpense = loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add new expense");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);

			Scene scene = new Scene(addExpense);
			dialogStage.setScene(scene);

			AddExpense controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMain(this);
			controller.setExpense(expense);

			dialogStage.showAndWait();

			return controller.isAcceptedNewExpense();
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}

	public void showEditCategoryNameDialog() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../view/EditCategories.fxml"));
			AnchorPane editCategory = loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit categories");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);

			Scene scene = new Scene(editCategory);
			dialogStage.setScene(scene);

			EditCategories controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMain(this);
			controller.setCategoryList(db.getExpensesCategory());

			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void refreshDataInTable(){
        expenseData.clear();
        expenseData.addAll(db.getExpenses());
        selectedExpense = null;
	}

	public ObservableList<Expense> getExpenses() {
	    log.info("[G]expenseData");
	    return expenseData;
	}
	
	public Database getDatabase() {
	    log.info("[G]database");
	    return db;
	}
	
	public Expense getSelectedExpense() {
	    log.info("[G]selectedExpense");
	    return selectedExpense;
	}
	
	public void setSelectedExpense(Expense expense) {
	    log.info("[S]selectedExpense");
	    this.selectedExpense = expense;
	}
	
	public Window getPrimaryStage() {
	    log.info("[G]expense");
	    return primaryStage;
	}
	
	public ExpensesOverview getExpenseOverview() {
	    log.info("[G]ExpensesOverview");
	    return expenseOverview;
	}
	
}