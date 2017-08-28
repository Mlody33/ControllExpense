package model;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Expense {
	
	private int id;
	private SimpleStringProperty name;
	private SimpleStringProperty category;
	private SimpleDoubleProperty price;
	private SimpleIntegerProperty quantity;
	private SimpleBooleanProperty paidByCreditCard;
	private ObjectProperty<LocalDate> date;

	public Expense() {
		this.name = new SimpleStringProperty(null);
		this.category = new SimpleStringProperty(null);
		this.price = new SimpleDoubleProperty(0);
		this.quantity = new SimpleIntegerProperty(0);
		this.paidByCreditCard = new SimpleBooleanProperty(false);
		this.date = new SimpleObjectProperty<LocalDate>(null);
	}
	
	public Expense(int id, String name, String category, double price, int quantity, boolean payByCreditCard, LocalDate date) {
		this.id = id;
		this.name = new SimpleStringProperty(name);
		this.category = new SimpleStringProperty(category);
		this.price = new SimpleDoubleProperty(price);
		this.quantity = new SimpleIntegerProperty(quantity);
		this.paidByCreditCard = new SimpleBooleanProperty(payByCreditCard);
		this.date = new SimpleObjectProperty<LocalDate>(date);
	}

	public int getId(){
		return id;
	}
	
	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name = new SimpleStringProperty(name);
	}
	
	public String getCategory() {
		return category.get();
	}

	public void setCategory(String category) {
		this.category = new SimpleStringProperty(category);
	}
	
	public double getPrice() {
		return price.get();
	}

	public void setPrice(double price) {
		this.price = new SimpleDoubleProperty(price);
	}
	
	public int getQuantity() {
		return quantity.get();
	}

	public void setQuantity(int quantity) {
		this.quantity = new SimpleIntegerProperty(quantity);
	}
	
	public int isPaidByCreditCardToInt(){
		if(paidByCreditCard.get())
			return 1;
		else
			return 0;
	}

	public boolean isPaidByCreditCard() {
		return paidByCreditCard.get();
	}

	public void setPaidByCreditCard (
			boolean paidByCreditCard) { this.paidByCreditCard = new SimpleBooleanProperty(paidByCreditCard);
	}
	
	public LocalDate getDate() {
		return date.get();
	}

	public void setDate(LocalDate date) {
		this.date = new SimpleObjectProperty<LocalDate>(date);
	}

	@Override
	public String toString() {
		return "Expense{" +
				"id=" + id +
				", name=" + name +
				", category=" + category +
				", price=" + price +
				", quantity=" + quantity +
				", paidByCreditCard=" + paidByCreditCard +
				", date=" + date +
				'}';
	}

}