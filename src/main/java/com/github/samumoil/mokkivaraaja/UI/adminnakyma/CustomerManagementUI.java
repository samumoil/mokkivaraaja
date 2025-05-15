package com.github.samumoil.mokkivaraaja.UI.adminnakyma;

import com.github.samumoil.mokkivaraaja.domain.handler.CustomerHandler;
import com.github.samumoil.mokkivaraaja.domain.object.Customer;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class CustomerManagementUI {
    private TextField AsiakasNimi = new TextField();
    private TextField AsiakasEmail = new TextField();
    private TextField AsiakasPuhelin = new TextField();
    private TextField AsiakasOsoite = new TextField();
    private TextField AsiakkaanMokki = new TextField();
    private TextField AsiakkaanUserId = new TextField();
    private final Button tallennaAsiakas = new Button("Tallenna");
    private final Button poistaAsiakas = new Button("Poista asiakas?");
    private final ListView<String> list = new ListView<>();
    private final BorderPane asiakasNakyma = new BorderPane();

    /**
     * Constructs an instance of the CustomerManagementUI class. This constructor initializes
     * and sets up the customer management user interface by configuring the layout, event
     * handlers for button actions, and populating the customer list.
     * <p>
     * The customer list is populated using the `CustomerHandler.getCustomerHandler().getCustomerNames()`
     * method, which fetches observable customer name entries from the back-end handler.
     * <p>
     * The "Save Customer" button is set to trigger the `saveCustomer()` method, allowing users
     * to save customer details entered into the interface to the database.
     * <p>
     * The "Delete Customer" button is set to trigger the `deleteCustomer()` method, enabling
     * removal of a customer identified by the entered customer ID.
     * <p>
     * The layout of the UI is defined such that the customer details input form is placed
     * centrally within the `asiakasNakyma` BorderPane element, while the customer list is displayed
     * on the right-hand side.
     */
    public CustomerManagementUI() {
        list.setItems(CustomerHandler.getCustomerHandler().getCustomerNames());

        tallennaAsiakas.setOnAction(actionEvent -> saveCustomer());
        poistaAsiakas.setOnAction(actionEvent -> deleteCustomer());

        asiakasNakyma.setCenter(customerDetails());
        asiakasNakyma.setRight(list);
    }

    public Region getView() {
        return asiakasNakyma;
    }

    private VBox customerDetails() {
        return new VBox(8,
                 new Label("Asiakkaan ID:"), AsiakkaanUserId,
                 new Label("Nimi:"), AsiakasNimi,
                 new Label("Sähköposti:"), AsiakasEmail,
                 new Label("Puhelin:"), AsiakasPuhelin,
                 new Label("Osoite:"), AsiakasOsoite,
                 new Label("Mökin numero:"), AsiakkaanMokki,
                 tallennaAsiakas, poistaAsiakas
        );
    }

    /**
     * Searches for a customer in the database using either an ID or a wildcard pattern
     * derived from the input string and updates the corresponding customer details
     * in the user interface if a match is found.
     * <p>
     * If the search by ID fails, the method attempts to find a customer by a wildcard
     * search based on the input string. If a customer is found, the method populates
     * their details (name, email, phone number, address, and ID) into the UI fields.
     * If no customer is found, it displays an error message in the UI.
     *
     * @param raw the input string that may contain a customer ID or partial customer information
     *            used to search for the customer. Leading and trailing whitespaces are ignored,
     *            and the first number found is treated as an ID.
     */
    public void searchAndFillCustomerDetails(String raw) {
        Customer cust = null;
        try {
            cust = CustomerHandler.getCustomerHandler().getCustomerById(Integer.parseInt(raw.replaceAll("^\\s*(\\d+).*$", "$1")));
        } catch (Exception ignored) {
        }
        if (cust == null)
            cust = CustomerHandler.getCustomerHandler().getCustomerByWildCardStuff("%" + raw + "%");
        if (cust != null) {
            AsiakasNimi.setText(cust.getName());
            AsiakasEmail.setText(cust.getEmail());
            AsiakasPuhelin.setText(cust.getPhoneNumber());
            AsiakasOsoite.setText(cust.getAddress());
            AsiakkaanMokki.setText(String.valueOf(cust.getId()));
        } else showError("Asiakasta ei löytynyt: " + raw);
    }

    public void saveCustomer() {
        try {
            Customer c = new Customer();
            c.setName(AsiakasNimi.getText().trim());
            c.setEmail(AsiakasEmail.getText().trim());
            c.setPhoneNumber(AsiakasPuhelin.getText().trim());
            c.setAddress(AsiakasOsoite.getText().trim());
            c.setUserId(Integer.parseInt(AsiakkaanUserId.getText().trim()));
            c.setCottageId(Integer.parseInt(AsiakkaanMokki.getText().trim()));
            CustomerHandler.getCustomerHandler().createOrUpdate(c);
            list.setItems(CustomerHandler.getCustomerHandler().getCustomerNames());

            showInfo("Asiakas tallennettu onnistuneesti.");
        } catch (Exception ex) {
            showError("Asiakkaan tallennus epäonnistui: " + ex.getMessage());
        }
    }

    private void deleteCustomer() {
        try {
            String rawId = AsiakkaanUserId.getText().trim();
            if (rawId.isEmpty()) {
                showError("Anna Asiakkaan ID ennen tyhjennystä.");
                return;
            }
            int id = Integer.parseInt(rawId);

            Customer c = CustomerHandler.getCustomerHandler().getCustomerById(id);
            if (c == null) {
                showError("Asiakasta ei löytynyt ID:llä " + id);
                return;
            }

            CustomerHandler.getCustomerHandler().deleteCustomer(id);
            list.setItems(CustomerHandler.getCustomerHandler().getCustomerNames());
            AsiakkaanUserId.clear();
            AsiakasNimi.clear();
            AsiakasEmail.clear();
            AsiakasPuhelin.clear();
            AsiakasOsoite.clear();
            AsiakkaanMokki.clear();

            showInfo("Asiakas ID " + id + " poistettu.");
        } catch (NumberFormatException ex) {
            showError("Virheellinen Asiakkaan ID: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Asiakkaan poisto epäonnistui: " + ex.getMessage());
        }
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Virhe");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Valmis");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
