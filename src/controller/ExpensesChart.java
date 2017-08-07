package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import application.AppState;
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
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;

public class ExpensesChart implements Initializable {

	@FXML private AreaChart<String, Number> reportChart;
	@FXML private ComboBox<AppState> chartTypeComboBox;
	
	@FXML private VBox dailyChartTypeBox;
	@FXML private VBox weeklyChartTypeBox;
	@FXML private VBox monthlyChartTypeBox;
	@FXML private VBox yearlyChartTypeBox;
	
	@FXML private DatePicker chosenDayForDailyChart;

	private static final int countOfLastWeekDays = 7;

	private Main main;
	private Stage dialogStage;
		
	private final LocalDate today = LocalDate.now();
	private LocalDate currentlySetDate = today;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<AppState> chartTypes = FXCollections.observableArrayList(AppState.DAILY_CHART, AppState.WEEKLY_CHART, AppState.MONTHLY_CHART, AppState.YEARLY_CHART);
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

	@FXML public void handleDayChangedForDailyChart() {
		currentlySetDate = chosenDayForDailyChart.getValue();
		dailyChartType(currentlySetDate);
	}

	@FXML public void handleSetDayToToday() {
		chosenDayForDailyChart.setValue(today);
	}

	@FXML public void handleSetNextDay() {
		currentlySetDate = chosenDayForDailyChart.getValue().plusDays(1);
		chosenDayForDailyChart.setValue(currentlySetDate);
	}

	@FXML public void handleSetPreviousDay() {
		currentlySetDate = chosenDayForDailyChart.getValue().minusDays(1);
		chosenDayForDailyChart.setValue(currentlySetDate);
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

	private void dailyChartType(LocalDate date){
		chosenDayForDailyChart.setValue(date);

		XYChart.Series<String, Number> seriesChartData = new XYChart.Series<String, Number>();
		reportChart.getData().clear();

		for(Expense data: main.getDatabase().getExpensesInDay(date.toString())){
			seriesChartData.getData().add(new XYChart.Data<String, Number>(data.getName()+"\n( " + data.getQuantity() + AppState.SPACE.get() + AppState.PIECE_FOR.get() + AppState.SPACE.get() + data.getPrice() + AppState.CURRENCY.get() + " )", data.getPrice()*data.getQuantity()));
		}
		if(!main.getDatabase().getExpensesInDay(date.toString()).isEmpty()) {
			seriesChartData.setName(date.toString() + AppState.SEPARATION.get() + date.getDayOfWeek().name().toLowerCase());
			reportChart.setTitle(AppState.WEEKLY_CHART.get() + AppState.SPACE.get() + AppState.SUM.get() + AppState.SPACE.get() + main.getDatabase().getExpensesSummaryInDay(date.toString()) + AppState.CURRENCY.get());
			reportChart.getData().add(seriesChartData);
		}else
			reportChart.setTitle(AppState.NO_DATA.get());
	}

	private void weeklyChartType(LocalDate startDate) {
		XYChart.Series<String, Number> seriesChartData = new XYChart.Series<String, Number>();

		currentlySetDate = startDate;

		for(int i = 0; i < countOfLastWeekDays; i++){
			seriesChartData.getData().add(new XYChart.Data<String, Number>(currentlySetDate.getDayOfWeek().name(), main.getDatabase().getExpensesSummaryInDay(currentlySetDate.toString())));
			currentlySetDate = currentlySetDate.minusDays(1);
		}

		seriesChartData.setName(startDate + " - " + currentlySetDate.plusDays(1));
		reportChart.setTitle(AppState.WEEKLY_CHART.get());
		reportChart.getData().add(seriesChartData);

	}

	private void monthlyChartType(LocalDate startDate) {
		XYChart.Series<String, Number> seriesChartData = new XYChart.Series<String, Number>();

		currentlySetDate = startDate.minusDays(startDate.getDayOfMonth() - 1);

		for(int i = 1; i <= startDate.getMonth().maxLength(); i++){
			seriesChartData.getData().add(new XYChart.Data<String, Number>(currentlySetDate.getDayOfMonth() + AppState.SEPARATION.get(), main.getDatabase().getExpensesSummaryInDay(currentlySetDate.toString())));
			currentlySetDate = currentlySetDate.plusDays(1);
		}
		seriesChartData.setName(startDate.getMonth().name());
		reportChart.setTitle(AppState.MONTHLY_CHART.get());
		reportChart.getData().add(seriesChartData);

	}

	private void yearlyChartType(LocalDate startDate) {
		XYChart.Series<String, Number> seriesChartData = new XYChart.Series<String, Number>();

		currentlySetDate = startDate.minusDays(startDate.getDayOfYear() - 1);

		for(int i = 1; i <= 12; i++){
			seriesChartData.getData().add(new XYChart.Data<String, Number>(currentlySetDate.getMonth().toString(),
				main.getDatabase().getExpensesSummaryInMonth(currentlySetDate)));
			currentlySetDate = currentlySetDate.plusMonths(1);
		}

		seriesChartData.setName(startDate.getYear()+"");
		reportChart.setTitle(AppState.YEARLY_CHART.get());
		reportChart.getData().add(seriesChartData);

	}

	@FXML public void changeChartType() {
		reportChart.getData().clear();
		dailyChartTypeBox.setVisible(false);
		weeklyChartTypeBox.setVisible(false);
		monthlyChartTypeBox.setVisible(false);
		yearlyChartTypeBox.setVisible(false);

		switch(chartTypeComboBox.getSelectionModel().getSelectedItem()){
			case DAILY_CHART:
				dailyChartTypeBox.setVisible(true);
				dailyChartType(today);
				break;
			case WEEKLY_CHART:
				weeklyChartTypeBox.setVisible(true);
				weeklyChartType(today);
				break;
			case MONTHLY_CHART:
				monthlyChartTypeBox.setVisible(true);
				monthlyChartType(today);
				break;
			case YEARLY_CHART:
				yearlyChartTypeBox.setVisible(true);
				yearlyChartType(today);
				break;
		}

	}

}