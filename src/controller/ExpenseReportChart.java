package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import model.Expense;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ExpenseReportChart implements Initializable {

	@FXML private AreaChart<String, Number> reportChart;
	@FXML private ComboBox<String> chartTypeComboBox;
	
	@FXML private VBox dailyChartTypeBox;
	@FXML private VBox weeklyChartTypeBox;
	@FXML private VBox monthlyChartTypeBox;
	@FXML private VBox yearlyChartTypeBox;
	
	@FXML private DatePicker choosenDayForDailyChart;
	@FXML private Button showToday;
	@FXML private Button showNextDay;
	@FXML private Button showPreviusDay;
	
	@FXML private Button showNextWeekButton;
	@FXML private Button showOnlyCurrentWeekButton;
	
	@FXML private Button showPreviusMonthButton;
	@FXML private Button showOnlyCurrentMonth;
	
	private static final int countOfLastWeekDays = 7;
	
	private Main main;
	private Stage dialogStage;
		
	private final LocalDate today = LocalDate.now();
	private LocalDate currentlySetDate = today;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<String> chartTypes = FXCollections.observableArrayList("Dzienny", "Tygodniowy", "Miesieczny", "Roczny");
		chartTypeComboBox.setItems(chartTypes);
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	public void setMain(Main main) {
		this.main = main;
		
		dailyChartType(today);
		dailyChartTypeBox.setVisible(true);
		chartTypeComboBox.getSelectionModel().selectFirst();
	}

	private void dailyChartType(LocalDate date){
		choosenDayForDailyChart.setValue(date);//FIXME IT CALL dailyChartType TWICE WHEN IT CALL FIRST TIME

		XYChart.Series<String, Number> seriesChartData = new XYChart.Series<String, Number>();
		reportChart.getData().clear();

		for(Expense data: main.getDatabase().selectExpensesByDate(date.toString())){
			seriesChartData.getData().add(new XYChart.Data<String, Number>(data.getName()+"\n( "+data.getQuantity()+" szt. po " + data.getPrice() + " zl. )", data.getPrice()*data.getQuantity()));
		}
		if(!main.getDatabase().selectExpensesByDate(date.toString()).isEmpty()) {
			seriesChartData.setName(date.toString() + ", " + date.getDayOfWeek().name().toLowerCase());
			reportChart.setTitle("Dzienny, Suma " + main.getDatabase().selectExpensesSumByDate(date.toString()) + " zl. ");
			reportChart.getData().add(seriesChartData);
		}else
			reportChart.setTitle("Brak danych w wybranym dniu");
	}

	private void weeklyChartType(LocalDate startDate) {

		XYChart.Series<String, Number> seriesChartData = new XYChart.Series<String, Number>();

		currentlySetDate = startDate;

		for(int i = 0; i < countOfLastWeekDays; i++){
			seriesChartData.getData().add(new XYChart.Data<String, Number>(currentlySetDate.getDayOfWeek().name(), main.getDatabase().selectExpensesSumByDate(currentlySetDate.toString())));
			currentlySetDate = currentlySetDate.minusDays(1);
		}

		seriesChartData.setName(startDate + " - " + currentlySetDate.plusDays(1));
		reportChart.setTitle("Tygodniowy");
		reportChart.getData().add(seriesChartData);

	}

	private void monthlyChartType(LocalDate startDate) {

		XYChart.Series<String, Number> seriesChartData = new XYChart.Series<String, Number>();

		currentlySetDate = startDate.minusDays(startDate.getDayOfMonth() - 1);

		for(int i = 1; i <= startDate.getMonth().maxLength(); i++){
			seriesChartData.getData().add(new XYChart.Data<String, Number>(currentlySetDate.getDayOfMonth() + ", ", main.getDatabase().selectExpensesSumByDate(currentlySetDate.toString())));
			currentlySetDate = currentlySetDate.plusDays(1);
		}
		seriesChartData.setName(startDate.getMonth().name());
		reportChart.setTitle("Miesieczny");
		reportChart.getData().add(seriesChartData);

	}

	private void yearlyChartType(LocalDate startDate) {
		XYChart.Series<String, Number> seriesChartData = new XYChart.Series<String, Number>();

		currentlySetDate = startDate.minusDays(startDate.getDayOfYear() - 1);

		for(int i = 1; i <= 12; i++){
			seriesChartData.getData().add(new XYChart.Data<String, Number>(currentlySetDate.getMonth().toString(),
				main.getDatabase().selectExpensesSumByMonth(currentlySetDate)));
			currentlySetDate = currentlySetDate.plusMonths(1);
		}

		seriesChartData.setName(startDate.getYear()+"");
		reportChart.setTitle("Roczny");
		reportChart.getData().add(seriesChartData);

	}

	/*
	 * HANDLE ACTION OF BUTTON
	*/

	@FXML public void changeChartType() {

		System.out.println("Changed chart type: "+chartTypeComboBox.getSelectionModel().getSelectedItem());

		reportChart.getData().clear();

		dailyChartTypeBox.setVisible(false);
		weeklyChartTypeBox.setVisible(false);
		monthlyChartTypeBox.setVisible(false);
		yearlyChartTypeBox.setVisible(false);

		switch(chartTypeComboBox.getSelectionModel().getSelectedIndex()){
			case 0:
				dailyChartTypeBox.setVisible(true);
				dailyChartType(today);
				break;
			case 1:
				weeklyChartTypeBox.setVisible(true);
				weeklyChartType(today);
				break;
			case 2:
				monthlyChartTypeBox.setVisible(true);
				monthlyChartType(today);
				break;
			case 3:
				yearlyChartTypeBox.setVisible(true);
				yearlyChartType(today);
				break;
		}

	}

	@FXML public void handleDayChangedForDailyChart() {
		currentlySetDate = choosenDayForDailyChart.getValue();
		dailyChartType(currentlySetDate);
	}

	@FXML public void handleSetDayToToday() {
		choosenDayForDailyChart.setValue(today);
	}
	
	@FXML public void handleSetNextDay() {
		currentlySetDate = choosenDayForDailyChart.getValue().plusDays(1);
		choosenDayForDailyChart.setValue(currentlySetDate);
	}
	
	@FXML public void handleSetPreviusDay() {
		currentlySetDate = choosenDayForDailyChart.getValue().minusDays(1);
		choosenDayForDailyChart.setValue(currentlySetDate);
	}
		
	@FXML public void handleShowNextWeek() {
		weeklyChartType(currentlySetDate);
	}
	
	@FXML public void handleShowCurrentWeek() {
		reportChart.getData().clear();
		weeklyChartType(today);
	}

	@FXML public void handleShowPreviusMonth() {
		currentlySetDate = currentlySetDate.minusMonths(2);
		monthlyChartType(currentlySetDate);
	}

	@FXML public void handleShowCurrentMonth() {
		reportChart.getData().clear();
		monthlyChartType(today);
	}

	@FXML public void handleShowLastYear() {
		currentlySetDate = currentlySetDate.minusYears(2);
		yearlyChartType(currentlySetDate);
	}

	@FXML public void handleShowCurrentYear() {
		reportChart.getData().clear();
		yearlyChartType(today);
	}
}
