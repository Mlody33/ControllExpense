package application;

public enum AppState {
    APP_TITLE("Controll your expenses"),
    ADD_EXPENSE_TITLE("Add new expense"),
    EDIT_EXPENSE_TITLE("Edit selected expense"),
    EXPENSE_CHART_TITLE("Expenses chart"),
    EDIT_CATEGORY_NAME("Edit category name"),
    EXPENSE_NOT_SELECTED("Expenses not selected"),
    SELECT_EXPENSE("First select expense in table!"),
    CONFIRM_DELETE_TITLE("Delete expense"),
    CONFIRM_DELETE("Do you want delete selected expense?"),
    DAILY_CHART("Daily"),
    WEEKLY_CHART("Weekly"),
    MONTHLY_CHART("Monthly"),
    YEARLY_CHART("Yearly"),
    NO_DATA("No data in selected day"),
    NO_DATA_TITLE("No data"),
    TYPE_ALL_DATA_EDIT("First insert data to all fields"),
    TYPE_ALL_DATA_ADD("First insert data to all fields if you want to add new expense"),
    DEFAULT_QUANTITY("1"),
    CURRENCY(" zl."),
    SUM("Sum"),
    PIECE_FOR("piece for"),
    SPACE(" "),
    SEPARATION(", ");

    private String value;

    AppState(String value){
        this.value = value;
    }

    public String get(){
        return value;
    }
}