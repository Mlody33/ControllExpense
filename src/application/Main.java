package application;

import java.io.IOException;

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
	
	private ObservableList<Expense> expenseData = FXCollections.observableArrayList();
	private Database db = new Database();
	private Expense selectedExpense;
	private ExpenseController expenseController;//FIXME NOT SAFE???
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		
		db.connect();
		expenseData.addAll(db.selectExpensesData());
		
		initMainView();
		showExpenseOverview();
//		showAddExpenseDialog(new Expense());
	}
	
	public void initMainView() {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../view/Main.fxml"));
			rootLayout = (BorderPane)loader.load();
			
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
	
	public void showExpenseOverview() {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../view/ExpenseOverview.fxml"));
			AnchorPane expenseOverview = (AnchorPane)loader.load();
			
			rootLayout.setCenter(expenseOverview);
			
			//ExpenseController controller = loader.getController();
			expenseController = loader.getController();
			expenseController.setMain(this);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void showExpenseChart() {
		try{			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../view/ExpenseChart.fxml"));
			AnchorPane expenseChart = (AnchorPane)loader.load();
			
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
			AnchorPane addExpense = (AnchorPane)loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add new expense");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);

			Scene scene = new Scene(addExpense);
			dialogStage.setScene(scene);

			AddExpenseController controller = loader.getController();
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
			AnchorPane editCategory = (AnchorPane)loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit categories");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);

			Scene scene = new Scene(editCategory);
			dialogStage.setScene(scene);

			EditCategoriesController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMain(this);
			controller.setCategoryList(db.selectExpensesCategory());

			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ObservableList<Expense> getExpensesData() { System.out.println("[G]expenseData"); return expenseData; }
	
	public Database getDatabase(){ System.out.println("[G]db"); return db; }
	
	public Expense getSelectedExpense(){ System.out.println("[G]selectedExpense"); return selectedExpense; }
	
	public void setSelectedExpense(Expense expense){ System.out.println("[S]selectedExpense"); this.selectedExpense = expense; }
	
	public Window getPrimaryStage() { System.out.println("[G]expense"); return primaryStage; }
	
	public ExpenseController getExpenseController(){ System.out.println("[G]ExpenseController"); return expenseController; }
	
}
