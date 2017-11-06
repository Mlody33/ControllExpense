package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Expense;

public class Database {

	private Connection connection;
	private Statement statement;

	private static final String ERROR_NAME = "Error in method";
	private Logger log = Logger.getLogger(this.getClass().getName());
	private final String URL_DATABASE = "jdbc:sqlite:"+this.getClass().getResource("/database/controlExpense.db");


	void connect(){
		try{
			connection = DriverManager.getConnection(URL_DATABASE);
			statement = connection.createStatement();
		}catch(SQLException e){
			log.warning(ERROR_NAME + Arrays.toString(Thread.currentThread().getStackTrace()));
		}
	}
	
	public void closeConnection(){
		try{
			statement.close();
			connection.close();
		}catch(SQLException e){
			log.warning(ERROR_NAME + Arrays.toString(Thread.currentThread().getStackTrace()));
		}
	}

	ObservableList<Expense> getExpenses(){
		ObservableList<Expense> expenseData = FXCollections.observableArrayList();
		try{
			ResultSet myRs = statement.executeQuery("SELECT * FROM expenses ORDER BY date DESC, id DESC");
			while(myRs.next()){
				expenseData.add(new Expense(myRs.getInt("id"), myRs.getString("name"), myRs.getString("category"), myRs.getDouble("price"), myRs.getInt("quantity"), myRs.getBoolean("isPaidByCreditCard"), LocalDate.parse(myRs.getString("date"))));
			}
		}catch(SQLException e){
			log.warning(ERROR_NAME + Arrays.toString(Thread.currentThread().getStackTrace()));
		}
		return expenseData;
	}

	public ObservableList<Expense> getFilteredExpensesByNameOrCategory(String value){
		ObservableList<Expense> expenseData = FXCollections.observableArrayList();
		try{
			ResultSet myRs = statement.executeQuery("SELECT * FROM expenses WHERE name = '"+value+"' OR category = '"+value+"' ");
			while(myRs.next()){
				expenseData.add(new Expense(myRs.getInt("id"), myRs.getString("name"), myRs.getString("category"), myRs.getDouble("price"), myRs.getInt("quantity"), myRs.getBoolean("isPaidByCreditCard"), LocalDate.parse(myRs.getString("date"))));
			}
		}catch(SQLException e){
			log.warning(ERROR_NAME + Arrays.toString(Thread.currentThread().getStackTrace()));
		}
		return expenseData;
	}
	
	public ObservableList<Expense> getExpensesInDay(String date){
		ObservableList<Expense> expenseData = FXCollections.observableArrayList();
		try{
			ResultSet myRs = statement.executeQuery("SELECT * FROM expenses WHERE date = '"+date+"'");
			while(myRs.next()){
				expenseData.add(new Expense(myRs.getInt("id"), myRs.getString("name"), myRs.getString("category"), myRs.getDouble("price"), myRs.getInt("quantity"), myRs.getBoolean("isPaidByCreditCard"), LocalDate.parse(myRs.getString("date"))));
			}
		}catch(SQLException e){
			log.warning(ERROR_NAME + Arrays.toString(Thread.currentThread().getStackTrace()));
		}
		return expenseData;
	}
	
	public double getExpensesSummaryInDay(String date){
		double sum = 0;
		try{
			ResultSet myRs = statement.executeQuery("SELECT COALESCE(ROUND(SUM(price),2),0) AS 'SUMA' FROM expenses WHERE date = '"+date+"'");
			if(myRs.next())
				sum = Double.parseDouble(myRs.getString(1));
		}catch(SQLException e){
			log.warning(ERROR_NAME + Arrays.toString(Thread.currentThread().getStackTrace()));
		}
		return sum;
	}
	
	public double getExpensesSummaryInMonth(LocalDate date){
		LocalDate startDate = date.minusDays(date.getDayOfMonth() - 1);
		LocalDate endDate = date.plusDays(date.getMonth().maxLength() - date.getDayOfMonth());
		double sum = 0;
		try{
			ResultSet myRs = statement.executeQuery("SELECT COALESCE(ROUND(SUM(price),2),0) AS 'SUMA' "
					+ "FROM expenses WHERE date BETWEEN '" + startDate + "' AND '" + endDate + "'");
			if(myRs.next())
				sum = Double.parseDouble(myRs.getString(1));
		}catch(SQLException e){
			log.warning(ERROR_NAME + Arrays.toString(Thread.currentThread().getStackTrace()));
		}
		return sum;
	}

	public ObservableList<String> getExpensesName(){
		ObservableList<String> names = FXCollections.observableArrayList();
		try{
			ResultSet myRs = statement.executeQuery("SELECT name FROM expenses GROUP BY name");
			while(myRs.next()){
				names.add(myRs.getString("name"));
			}
		}catch(SQLException e){
			log.warning(ERROR_NAME + Arrays.toString(Thread.currentThread().getStackTrace()));
		}
		return names;
	}

	public ObservableList<String> getExpensesCategory(){
		ObservableList<String> category = FXCollections.observableArrayList();
		try{
			ResultSet myRs = statement.executeQuery("SELECT category FROM expenses GROUP BY category");
			while(myRs.next()){
				category.add(myRs.getString("category"));
			}
		}catch(SQLException e){
			log.warning(ERROR_NAME + Arrays.toString(Thread.currentThread().getStackTrace()));
		}
		return category;
	}
	
	public void insertExpense(Expense expense){
		try{
			statement.executeUpdate("INSERT INTO expenses (name, category, price, quantity, isPaidByCreditCard, date) values ('"+expense.getName()+"', '"+expense.getCategory()+"', '"+expense.getPrice()+"', '"+expense.getQuantity()+"', '"+expense.isPaidByCreditCardToInt()+"', '"+expense.getDate()+"')");
		}catch(SQLException e){
			log.warning(ERROR_NAME + Arrays.toString(Thread.currentThread().getStackTrace()));
		}
	}

	public void updateExpense(Expense expense){
		try{
			statement.executeUpdate("UPDATE expenses SET name='"+expense.getName()+"', category='"+expense.getCategory()+"', price='"+expense.getPrice()+"', quantity='"+expense.getQuantity()+"', isPaidByCreditCard='"+expense.isPaidByCreditCardToInt()+"', date='"+expense.getDate()+"' WHERE id = '"+expense.getId()+"'");
		}catch(SQLException e){
			log.warning(ERROR_NAME + Arrays.toString(Thread.currentThread().getStackTrace()));
		}
	}

	public void updateCategory(String newName, String oldName) {
		try{
			statement.executeUpdate("UPDATE expenses SET category='"+newName+"' WHERE category='"+oldName+"' ");
		}catch(SQLException e){
			log.warning(ERROR_NAME + Arrays.toString(Thread.currentThread().getStackTrace()));

		}
	}
	
	public void removeExpense(Expense expense){
		try{
			statement.executeUpdate("DELETE FROM expenses WHERE id = '"+expense.getId()+"'");
		}catch(SQLException e){
			log.warning(ERROR_NAME + Arrays.toString(Thread.currentThread().getStackTrace()));
		}
	}

}