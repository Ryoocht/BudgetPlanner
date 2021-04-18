package application;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Task {
	private final ObjectProperty<Integer> id = new SimpleObjectProperty<>(null);
	private final ObjectProperty<LocalDate> dateUsed = new SimpleObjectProperty<>();
	private final ObjectProperty<CategoryEnum.CategoryBoxEnum> categorySelected = new SimpleObjectProperty<>();
	private final ObjectProperty<CategoryEnum.SubcategoryBoxEnum> subcategorySelected = new SimpleObjectProperty<>();
	private final StringProperty furtherSubcategorySelected = new SimpleStringProperty();
	private final StringProperty priceEntered = new SimpleStringProperty();
	private final StringProperty detailsEntered = new SimpleStringProperty();
	
	/*
		Create two constructors
		one with no argument to access for binding or many things
		one with full arguments to gather item values to set
	*/
	public Task() {
	}
	
	public Task(Integer id, LocalDate dateUsed,CategoryEnum.CategoryBoxEnum categorySelected,
			CategoryEnum.SubcategoryBoxEnum subcategorySelected,String furtherSubcategorySelected,
			String priceEntered,String detailsEntered) {
		this.id.set(id);
		this.dateUsed.set(dateUsed);
		this.categorySelected.set(categorySelected);
		this.subcategorySelected.set(subcategorySelected);
		this.furtherSubcategorySelected.set(furtherSubcategorySelected);
		this.priceEntered.set(priceEntered);
		this.detailsEntered.set(detailsEntered);
	}
	
	/*
	 	Prepare each getter and setter in order to set values
	 	when a row is selected on tableView
	 	Prepare each property in order to bind data with controllers
	*/
	
	//ID Property (getter, setter, property)
	public Integer getId() {
		return this.id.get();
	}

	public void setId(Integer id) {
		this.id.set(id);;
	}
	public ObjectProperty<Integer> idProperty(){
		return id;
	}
	
	// DatePicker Property (getter, setter, property)
	public LocalDate getDateUsed() {
		return dateUsed.get();
	}
	public void setDateUsed(LocalDate dateUsed) {
		this.dateUsed.set(dateUsed);
	}
	public ObjectProperty<LocalDate> dateProperty() {
		return dateUsed;
	}
	
	// Category Property (getter, setter, property)
	public CategoryEnum.CategoryBoxEnum getCategorySelected() {
		return categorySelected.get();
	}
	public void setCategorySelected(CategoryEnum.CategoryBoxEnum categorySelected) {
		this.categorySelected.set(categorySelected);
	}
	public ObjectProperty<CategoryEnum.CategoryBoxEnum> categoryProperty() {
		return categorySelected;
	}
	
	// Sub-category Property (getter, setter, property)
	public CategoryEnum.SubcategoryBoxEnum getSubcategorySelected() {
		return subcategorySelected.get();
	}
	public void setSubcategorySelected(CategoryEnum.SubcategoryBoxEnum subcategorySelected) {
		this.subcategorySelected.set(subcategorySelected);
	}
	public ObjectProperty<CategoryEnum.SubcategoryBoxEnum> subcategoryProperty() {
		return subcategorySelected;
	}
	
	// furtherSubcategory Property (getter, setter, property)
	public String getFurtherSubcategorySelected() {
		return furtherSubcategorySelected.get();
	}
	public void setFurtherSubcategorySelected(String furtherSubcategorySelected) {
		this.furtherSubcategorySelected.set(furtherSubcategorySelected);
	}
	public StringProperty furtherSubcategoryProperty() {
		return furtherSubcategorySelected;
	}
	
	// Price Property (getter, setter, property)
	public String getPriceEntered() {
		return priceEntered.get();
	}
	public void setPriceEntered(String priceEntered) {
		this.priceEntered.set(priceEntered);
	}
	public StringProperty priceProperty() {
		return priceEntered;
	}
	
	// Details Property (getter, setter, property)
	public String getDetailsEntered() {
		return detailsEntered.get();
	}
	public void setDetailsEntered(String detailsEntered) {
		this.detailsEntered.set(detailsEntered);
	}
	public StringProperty detailsProperty() {
		return detailsEntered;
	}
}
