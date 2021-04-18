package application;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import application.CategoryEnum.CategoryBoxEnum;
import application.CategoryEnum.SubcategoryBoxEnum;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BudgetController implements Initializable {

	private final Task currentTask = new Task();
	private ObservableList<Task> tasks = FXCollections.observableArrayList();
	private final HashMap<Integer, Task> tasksMap = new HashMap<>();
	int lastId = 0;

	//SQL
	private Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	String query = null;
	Task taskdata = null;

	@FXML
	private TableView<Task> budgetTable;

	@FXML
	private TableColumn<Task, LocalDate> dateColumn;

	@FXML
	private TableColumn<Task, CategoryEnum.CategoryBoxEnum> categoryColumn;

	@FXML
	private TableColumn<Task, CategoryEnum.SubcategoryBoxEnum> subcategoryColumn;

	@FXML
	private TableColumn<Task, String> furtherSubcategoryColumn;

	@FXML
	private TableColumn<Task, String> priceColumn;

	@FXML
	private TableColumn<Task, String> detailsColumn;

	@FXML
	private ComboBox<TableNameEnum> tableNameBox;

	@FXML
	private DatePicker datePicker;

	@FXML
	private ComboBox<CategoryEnum.CategoryBoxEnum> categoryBox;

	@FXML
	private ComboBox<SubcategoryBoxEnum> subcategoryBox;

	@FXML
	private ComboBox<String> furtherSubcategoryBox;

	@FXML
	private TextField currencyText;

	@FXML
	private TextField detailText;

	@FXML
	void changeButton(ActionEvent event) {
		if (currentTask.getId() == null) {
			Task t = new Task(++lastId, currentTask.getDateUsed(), currentTask.getCategorySelected(),
					currentTask.getSubcategorySelected(), currentTask.getFurtherSubcategorySelected(),
					currentTask.getPriceEntered(), currentTask.getDetailsEntered());
			tasks.add(t);
			tasksMap.put(lastId, t);
		} else {
			Task t = tasksMap.get(currentTask.getId());
			t.setDateUsed(currentTask.getDateUsed());
			t.setCategorySelected(currentTask.getCategorySelected());
			t.setSubcategorySelected(currentTask.getSubcategorySelected());
			t.setFurtherSubcategorySelected(currentTask.getFurtherSubcategorySelected());
			t.setPriceEntered(currentTask.getPriceEntered());
			t.setDetailsEntered(currentTask.getDetailsEntered());
		}
		add_data();
	}

	@FXML
	void deleteButtonClicked(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Deleting Upadate");
		alert.setHeaderText("Are you sure you want to delete?");
		alert.getButtonTypes().remove(0, 2);
		alert.getButtonTypes().add(0, ButtonType.YES);
		alert.getButtonTypes().add(1, ButtonType.NO);
		Optional<ButtonType> confirmationResponse = alert.showAndWait();
		if (confirmationResponse.get() == ButtonType.YES) {
			setCurrentTask(null);
			int selectedId = budgetTable.getSelectionModel().getSelectedIndex();
			if (selectedId >= 0) {
				budgetTable.getItems().remove(selectedId);
			} else {
				Alert warning = new Alert(AlertType.WARNING);
				warning.setTitle("No Selection");
				warning.setHeaderText("None of Row Selected");
				warning.setContentText("Please select a Row in the table");
				warning.showAndWait();
			}
			budgetTable.getSelectionModel().clearSelection();
			delete_data();
		}
	}

	@FXML
	void loadDataFromDatabase(ActionEvent event) {
		try {
			Connection conn = MySqlConnect.Connect();
			ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM main_table");
			while (rs.next()) {
				tasks.add(new Task(++lastId, LocalDate.parse(rs.getString("Date")),
						CategoryEnum.CategoryBoxEnum.valueOf(rs.getString("Category")),
						CategoryEnum.SubcategoryBoxEnum.valueOf(rs.getString("Subcategory")), 
						rs.getString("FurtherSubcategory"), rs.getString("Price"), rs.getString("Details")));

				dateColumn.setCellValueFactory(new PropertyValueFactory<Task, LocalDate>("dateUsed"));
				categoryColumn.setCellValueFactory(new PropertyValueFactory<Task, CategoryEnum.CategoryBoxEnum>("categorySelected"));
				subcategoryColumn.setCellValueFactory(new PropertyValueFactory<Task, CategoryEnum.SubcategoryBoxEnum>("subcategorySelected"));
				furtherSubcategoryColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("furtherSubcategorySelected"));
				priceColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("priceEntered"));
				detailsColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("detailsEntered"));

				budgetTable.setItems(null);
				budgetTable.setItems(tasks);
			}
		} catch (SQLException e) {
			System.out.println("Error" + e);
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TableNameEnum (Take "name" item from constructor in TableNameEnum)
		Callback<ListView<TableNameEnum>, ListCell<TableNameEnum>> cellFactory = (
				ListView<TableNameEnum> param) -> new ListCell<TableNameEnum>() {
					@Override
					protected void updateItem(TableNameEnum item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null && !empty) {
							setText(item.getName());
						}
					}
				};
				ObservableList<TableNameEnum> tableNameNum = FXCollections.observableArrayList(TableNameEnum.values());
				tableNameBox.getItems().addAll(tableNameNum);
				tableNameBox.setButtonCell(cellFactory.call(null));
				tableNameBox.setCellFactory(cellFactory);

				// CategoryBoxEnum
				ObservableList<CategoryEnum.CategoryBoxEnum> categoryBoxNum = FXCollections
						.observableArrayList(CategoryEnum.CategoryBoxEnum.values());
				categoryBox.getItems().addAll(categoryBoxNum);
				// SubcategoryBoxEnum
				ObservableList<SubcategoryBoxEnum> subBoxEnum = FXCollections.observableArrayList(SubcategoryBoxEnum.values());
				categoryBox.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends CategoryEnum.CategoryBoxEnum> observable,
						CategoryEnum.CategoryBoxEnum oldValue, CategoryEnum.CategoryBoxEnum newValue) -> {
							if (Objects.nonNull(newValue)) {
								subcategoryBox.getItems().clear();
								switch (newValue) {
								case Income:
									subcategoryBox.getItems().addAll(subBoxEnum.subList(0, 4));
									break;
								case Expense:
									subcategoryBox.getItems().addAll(subBoxEnum.subList(5, 21));
									break;
								case Saving:
									subcategoryBox.getItems().addAll(subBoxEnum.subList(22, 23));
									break;
								default:
									System.out.println("Category hasn't been selected");
								}
							}
						});
				// FurtherSubcategoryBox
				subcategoryBox.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends CategoryEnum.SubcategoryBoxEnum> observable,
						CategoryEnum.SubcategoryBoxEnum oldValue, CategoryEnum.SubcategoryBoxEnum newValue) -> {
							if (Objects.nonNull(newValue)) {
								furtherSubcategoryBox.getItems().clear();
								switch (newValue) {
								case Paycheck:
									furtherSubcategoryBox.getItems().addAll("Eliza", "Ryo");
									break;
								case EbayFB:
									furtherSubcategoryBox.getItems().addAll("-");
									break;
								case Misc:
									furtherSubcategoryBox.getItems().addAll("Tax return", "Purchase refund", "Present",
											"Lottery/gambling");
									break;
								case Investment:
									furtherSubcategoryBox.getItems().addAll("Dividends");
									break;
								case WorkBonus:
									furtherSubcategoryBox.getItems().addAll("Eliza", "Ryo");
									break;
								case Food:
									furtherSubcategoryBox.getItems().addAll("Groceries", "Restaurant/Cafe", "Fast Food/TA",
											"Food Delivery", "Servo/Snack");
									break;
								case Shopping:
									furtherSubcategoryBox.getItems().addAll("Clothes", "Shoes", "Bag", "Jewellery", "Homewares",
											"Tech", "Toiletries/Makeup", "Others");
									break;
								case Housing:
									furtherSubcategoryBox.getItems().addAll("Mortgage", "Rates", "Body Corp",
											"Household Repairs");
									break;
								case Transportation:
									furtherSubcategoryBox.getItems().addAll("Petrol", "Parking fees",
											"Maintenance & oil change", "Rego", "Taxi");
									break;
								case Travel:
									furtherSubcategoryBox.getItems().addAll("Flights", "Visa", "Passport", "Accomodation",
											"Cash", "Transport");
									break;
								case Utilities:
									furtherSubcategoryBox.getItems().addAll("Electricity", "Water", "Phones", "Internet");
									break;
								case Others:
									furtherSubcategoryBox.getItems().addAll("Postage", "Ebay Fee", "Others");
									break;
								case Personal:
									furtherSubcategoryBox.getItems().addAll("Haircuts", "Massage");
									break;
								case Entertainment:
									furtherSubcategoryBox.getItems().addAll("Alcohol/Bars", "Games", "Movies", "Concerts",
											"Subscriptions");
									break;
								case Medical:
									furtherSubcategoryBox.getItems().addAll("GP", "Dentist", "Specialist", "Urgent care",
											"Medications");
									break;
								case Household:
									furtherSubcategoryBox.getItems().addAll("Furniture", "Cleaning supplies/tools");
									break;
								case Gym:
									furtherSubcategoryBox.getItems().addAll("Membership", "Protein");
									break;
								case Insurance:
									furtherSubcategoryBox.getItems().addAll("Health Insurance", "Contents Insurance",
											"Car Insurance", "Travel Insurance");
									break;
								case Debt:
									furtherSubcategoryBox.getItems().addAll("Personal loans", "Credit cards");
									break;
								case Education:
									furtherSubcategoryBox.getItems().addAll("Books", "Classes");
									break;
								case Gift:
									furtherSubcategoryBox.getItems().addAll("Birthday", "Anniversary", "Wedding", "Christmas",
											"Special occasion", "Charities", "Monks");
									break;
								case Investments:
									furtherSubcategoryBox.getItems().addAll("Shares", "Dividends", "Cryptocurrency");
									break;
								case MonthlySavings:
									furtherSubcategoryBox.getItems().addAll("Eliza Savings", "Ryo Savings", "Joint Savings");
									break;
								default:
									System.out.println("Category hasn't been selected");
								}
							}
						});

				// Bind Properties and controllers
				datePicker.valueProperty().bindBidirectional(currentTask.dateProperty());
				categoryBox.valueProperty().bindBidirectional(currentTask.categoryProperty());
				subcategoryBox.valueProperty().bindBidirectional(currentTask.subcategoryProperty());
				furtherSubcategoryBox.valueProperty().bindBidirectional(currentTask.furtherSubcategoryProperty());
				currencyText.textProperty().bindBidirectional(currentTask.priceProperty());
				detailText.textProperty().bindBidirectional(currentTask.detailsProperty());

				// set property value into each column
				budgetTable.setItems(tasks);
				dateColumn.setCellValueFactory(rowData -> rowData.getValue().dateProperty());
				categoryColumn.setCellValueFactory(rowData -> rowData.getValue().categoryProperty());
				subcategoryColumn.setCellValueFactory(rowData -> rowData.getValue().subcategoryProperty());
				furtherSubcategoryColumn.setCellValueFactory(rowData -> rowData.getValue().furtherSubcategoryProperty());
				priceColumn.setCellValueFactory(rowData -> Bindings.concat("$", rowData.getValue().priceProperty()));
				detailsColumn.setCellValueFactory(rowData -> rowData.getValue().detailsProperty());

				/*
				 * When a row selected on a tableView, get each item using getter from Task
				 * class and set them using setter. each property binds with each controller
				 * then selected row data will display on each controller
				 */
				budgetTable.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends Task> observable, Task oldValue, Task newValue) -> {
					setCurrentTask(newValue);
				});
	}

	private void setCurrentTask(Task selectedTask) {
		if (selectedTask != null) {
			currentTask.setId(selectedTask.getId());
			currentTask.setDateUsed(selectedTask.getDateUsed());
			currentTask.setCategorySelected(selectedTask.getCategorySelected());
			currentTask.setSubcategorySelected(selectedTask.getSubcategorySelected());
			currentTask.setFurtherSubcategorySelected(selectedTask.getFurtherSubcategorySelected());
			currentTask.setPriceEntered(selectedTask.getPriceEntered());
			currentTask.setDetailsEntered(selectedTask.getDetailsEntered());
		} else {
			currentTask.setId(null);
			currentTask.setDateUsed(null);
			currentTask.setCategorySelected(null);
			currentTask.setSubcategorySelected(null);
			currentTask.setFurtherSubcategorySelected("");
			currentTask.setPriceEntered("");
			currentTask.setDetailsEntered("");
		}
	}

	void setTasksMap(HashMap<Integer, Task> initialTasksMap) {
		tasksMap.clear();
		tasks.clear();
		tasksMap.putAll(initialTasksMap);
		tasks.addAll(initialTasksMap.values());
		lastId = tasksMap.keySet().stream().max(Integer::compare).get();
	}

	//Adding data from TableView (add_data + insert + getQuery)
	public void add_data() {
		conn = MySqlConnect.Connect();
		String Date = String.valueOf(datePicker.getValue());
		String Category = String.valueOf(categoryBox.getValue());
		String Subcategory = String.valueOf(subcategoryBox.getValue());
		String FurtherSubcategory = String.valueOf(furtherSubcategoryBox.getValue());
		String Price = currencyText.getText();
		String Details = detailText.getText();
		if (Date.isEmpty()|| Category.isEmpty() || Subcategory.isEmpty() || 
				FurtherSubcategory.isEmpty() || Price.isEmpty() ) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Please Fill All DATA");
			alert.showAndWait();
		} else {
			getQuery();
			insert();

		}
	}

	private void insert() {
		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, String.valueOf(datePicker.getValue()));
			pst.setString(2, String.valueOf(categoryBox.getValue()));
			pst.setString(3, String.valueOf(subcategoryBox.getValue()));
			pst.setString(4, String.valueOf(furtherSubcategoryBox.getValue()));
			pst.setString(5, currencyText.getText());
			pst.setString(6, detailText.getText());
			pst.execute();
		} catch (SQLException e) {
			System.out.println("Error" + e);
		}
	}

	private void getQuery() {
		query = "INSERT INTO main_table (Date, Category, Subcategory, FurtherSubcategory, Price, Details) VALUES(?,?,?,?,?,?)";
	}
  
	// Delete Data from TableView (delete_data + refreshTable)
	public void delete_data() {
		try {
			Connection conn = MySqlConnect.Connect();
			taskdata = budgetTable.getSelectionModel().getSelectedItem();
			query = "DELETE FROM main_table WHERE Id" + taskdata.getId();
			pst = conn.prepareStatement(query);
			pst.execute();
			refreshTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void refreshTable() {
		try {
			tasks.clear();
			query = "SELECT * FROM main_table";
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				tasks.add(new Task(Integer.parseInt(rs.getString("Id")), LocalDate.parse(rs.getString("Date")),
						CategoryEnum.CategoryBoxEnum.valueOf(rs.getString("Category")),
						CategoryEnum.SubcategoryBoxEnum.valueOf(rs.getString("Subcategory")), 
						rs.getString("FurtherSubcategory"), rs.getString("Price"), rs.getString("Details")));
				budgetTable.setItems(tasks);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
