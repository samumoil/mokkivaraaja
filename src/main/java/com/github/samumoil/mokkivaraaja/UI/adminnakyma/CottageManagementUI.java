package com.github.samumoil.mokkivaraaja.UI.adminnakyma;

import com.github.samumoil.mokkivaraaja.domain.handler.CottageHandler;
import com.github.samumoil.mokkivaraaja.domain.object.Cottage;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;

public class CottageManagementUI {

    private final TextField mokinOsoite = new TextField();
    private final TextField mokinIka = new TextField();
    private final TextField mokinKoko = new TextField();
    private final TextField mokinNimi = new TextField();
    private final Button tallennaMokki = new Button("Tallenna mökki");
    private final Button luoMokki = new Button("Luo uusi");
    private final Button poistaMokki = new Button("Poista mökki");
    private final ListView<String> list = new ListView<>();
    private final BorderPane mokkiNakyma = new BorderPane();
    private Cottage valittuMokki = null;

    /**
     * Constructs and initializes the CottageManagementUI. This class is responsible
     * for managing the UI components and interactions related to cottage management.
     * <p>
     * The constructor performs the following actions:
     * - Populates the cottage list UI with names of cottages obtained from CottageHandler.
     * - Sets up action handlers for buttons used for saving and deleting cottages.
     * - Configures the layout by setting the cottage details form in the center and
     * the cottage list on the right side of the interface.
     */
    public CottageManagementUI() {
        list.setItems(CottageHandler.getCottageHandler().getCottageNames());

        tallennaMokki.setOnAction(actionEvent -> saveCottage());
        poistaMokki.setOnAction(actionEvent -> deleteCottage());
        luoMokki.setOnAction(actionEvent -> createNewCottage());

        mokkiNakyma.setCenter(cottageDetails());
        mokkiNakyma.setRight(list);
    }

    public Region getView() {
        return mokkiNakyma;
    }

    private VBox cottageDetails() {
        return new VBox(8,
                 new Label("Mökin nimi:"), mokinNimi,
                 new Label("Mökin osoite:"), mokinOsoite,
                 new Label("Mökin ikä:"), mokinIka,
                 new Label("Mökin koko:"), mokinKoko,
                 tallennaMokki, luoMokki, poistaMokki
        );
    }

    /**
     * Searches for a cottage by its identifier and fills the UI components with the details of the found cottage.
     * If the cottage is not found, or if the provided identifier is invalid, an error message is displayed.
     *
     * @param id the identifier of the cottage as a string. The string will be parsed into an integer
     *           to search for the corresponding cottage in the system.
     */
    public void searchAndFillCottageDetails(String id) {
        try {
            this.valittuMokki = CottageHandler.getCottageHandler().getCottageById(Integer.parseInt(id));
            if (valittuMokki != null) {
                mokinOsoite.setText(valittuMokki.getAddress());
                mokinIka.setText(String.valueOf(valittuMokki.getAge()));
                mokinKoko.setText(String.valueOf(valittuMokki.getSize()));
                mokinNimi.setText(valittuMokki.getName());
            } else showError("Mökkiä ei löytynyt: " + id);
        } catch (NumberFormatException ex) {
            showError("Virheellinen mökin ID: " + id);
        }
    }

    /**
     * Saves a new cottage to the system by collecting data from the user input fields,
     * validating the input, and storing the cottage in the backend storage.
     * <p>
     * The method performs the following steps:
     * 1. Retrieves and trims text input from user fields for address, name, size, and cottage number.
     * 2. Validates that none of the required fields are empty.
     * 3. Parses numeric fields (size and cottage number) and handles invalid formats.
     * 4. Constructs a new Cottage object and populates its fields with the provided data.
     * 5. Calls the appropriate handler to add the new cottage to the backend storage.
     * 6. Updates the UI's cottage list to reflect the added cottage.
     * 7. Clears the input fields for new data entry.
     * 8. Displays success messages or error messages depending on the outcome.
     * <p>
     * Input validation:
     * <ul>
     * <li>Ensures address, size, and cottage number fields are not empty.</li>
     * <li>Ensures size and cottage number are valid integers.</li>
     * </ul>
     * <p>
     * Error handling:
     * <ul>
     * <li>Catches and displays errors for invalid numeric input or failed storage operations.</li>
     * <li>Displays appropriate messages in response to exceptions or validation failures.</li>
     * </ul>
     * <p>
     * Preconditions:
     * <ul>
     * <li>The CottageHandler instance should be properly initialized.</li>
     * <li>The input fields on the UI should be accessible and filled with valid data.</li>
     * </ul>
     * <p>
     * Postconditions:
     * <ul>
     * <li>A valid new cottage is stored in the backend storage and reflected in the UI.</li>
     * <li>Input fields are cleared, and appropriate feedback is provided to the user.</li>
     * </ul>
     */
    private void saveCottage() {
        try {
            String address = mokinOsoite.getText().trim();
            String name = mokinIka.getText().trim();
            String sizeRaw = mokinKoko.getText().trim();
            String cottageNumberRaw = mokinNimi.getText().trim();

            if (address.isEmpty() || sizeRaw.isEmpty() || cottageNumberRaw.isEmpty()) {
                showError("Kaikki kentät täytyy täyttää.");
                return;
            }

            int cottageNumber = Integer.parseInt(cottageNumberRaw);
            int size = Integer.parseInt(sizeRaw);

            Cottage newCottage = new Cottage();
            newCottage.setId(cottageNumber);
            newCottage.setName(name);
            newCottage.setLocation(address);
            newCottage.setDescription("N/A");
            newCottage.setOwnerId(1);
            newCottage.setPricePerNight(100.0f);
            newCottage.setCreatedAt(LocalDateTime.now());
            newCottage.setSize(size);
            CottageHandler.getCottageHandler().addCottage(newCottage);

            list.setItems(CottageHandler.getCottageHandler().getCottageNames());
            mokinOsoite.clear();
            mokinKoko.clear();
            mokinNimi.clear();

            showInfo("Mökki luotu onnistuneesti.");
        } catch (NumberFormatException ex) {
            showError("Virheellinen mökin numero tai koko: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Mökin luominen epäonnistui: " + ex.getMessage());
        }
    }

    private void deleteCottage() {
        if (this.valittuMokki == null) {
            showError("Valitse poistettava mökki");
            return;
        }

        try {
            int id = this.valittuMokki.getId();
            CottageHandler.getCottageHandler().deleteCottage(id);
            list.setItems(CottageHandler.getCottageHandler().getCottageNames());
            mokinOsoite.clear();
            mokinIka.clear();
            mokinKoko.clear();
            mokinNimi.clear();

            showInfo("Mökki ID " + id + " poistettu.");
        } catch (Exception ex) {
            showError("Poisto epäonnistui: " + ex.getMessage());
        }
    }

    private void createNewCottage() {
        try {
            if (mokinOsoite.getText().isEmpty() || mokinNimi.getText().isEmpty() || mokinKoko.getText().isEmpty()) {
                showError("Kaikki kentät täytyy täyttää.");
                return;
            }

            String address = mokinOsoite.getText().trim();
            String name = mokinNimi.getText().trim();
            int size = Integer.parseInt(mokinKoko.getText().trim());
            int age = Integer.parseInt(mokinIka.getText().trim());


            Cottage newCottage = new Cottage();
            newCottage.setName(name);
            newCottage.setLocation(address);
            newCottage.setAge(age);
            newCottage.setSize(size);
            newCottage.setCreatedAt(LocalDateTime.now());
            CottageHandler.getCottageHandler().addCottage(newCottage);
            list.setItems(CottageHandler.getCottageHandler().getCottageNames());
            mokinOsoite.clear();
            mokinIka.clear();
            mokinKoko.clear();
            mokinNimi.clear();

            showInfo("Mökki luotu onnistuneesti.");
        } catch (NumberFormatException ex) {
            showError("Virheellinen mökin numero: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Mökin luominen epäonnistui: " + ex.getMessage());
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
