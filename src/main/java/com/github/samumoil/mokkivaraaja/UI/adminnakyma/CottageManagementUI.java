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




    public CottageManagementUI() {
        list.setItems(CottageHandler.getCottageHandler().getCottageNames());

        tallennaMokki.setOnAction(actionEvent -> saveCottage());
        poistaMokki.setOnAction(actionEvent ->  deleteCottage());

        mokkiNakyma.setCenter(cottageDetails());
        mokkiNakyma.setRight(list);
    }
    public Region getView(){
        return mokkiNakyma;
    }

    private VBox cottageDetails() {
        return new VBox(8,
                new Label("Mökin osoite:"), mokinOsoite,
                new Label("Mökin ikä:"), mokinIka,
                new Label("Mökin koko:"), mokinKoko,
                new Label("Mökin numero:"), mokinNimi,
                tallennaMokki, luoMokki, poistaMokki
        );
    }
    public void searchAndFillCottageDetails(String id) {
        try {
            Cottage c = CottageHandler.getCottageHandler().getCottageById(Integer.parseInt(id));
            if (c != null) {
                mokinOsoite.setText(c.getAddress());
                mokinIka.setText(String.valueOf(c.getAge()));
                mokinKoko.setText(String.valueOf(c.getSize()));
                mokinNimi.setText(c.getNumber());
            } else showError("Mökkiä ei löytynyt: " + id);
        } catch (NumberFormatException ex) {
            showError("Virheellinen mökin ID: " + id);
        }
    }


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
        try {
            String rawId = mokinNimi.getText().trim();
            if (rawId.isEmpty()) {
                showError("Anna mökin numero ennen tyhjennystä.");
                return;
            }
            int id = Integer.parseInt(rawId);
            Cottage c = CottageHandler.getCottageHandler().getCottageById(id);
            if (c == null) {
                showError("Mökkiä ei löytynyt ID:llä " + id);
                return;
            }

            CottageHandler.getCottageHandler().deleteCottage(id);
            list.setItems(CottageHandler.getCottageHandler().getCottageNames());
            mokinOsoite.clear();
            mokinIka.clear();
            mokinKoko.clear();
            mokinNimi.clear();

            showInfo("Mökki ID " + id + " poistettu.");
        } catch (NumberFormatException ex) {
            showError("Virheellinen mökin numero: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Poisto epäonnistui: " + ex.getMessage());
        }
    }

    private void createNewCottage() {
        try {
            String address = mokinOsoite.getText().trim();
            String name = mokinNimi.getText().trim();
            String size = mokinKoko.getText().trim();
            String cottageNumberRaw = mokinNimi.getText().trim();

            if (address.isEmpty() || name.isEmpty() || size.isEmpty() || cottageNumberRaw.isEmpty()) {
                showError("Kaikki kentät täytyy täyttää.");
                return;
            }

            int cottageNumber = Integer.parseInt(cottageNumberRaw);
            Cottage newCottage = new Cottage();
            newCottage.setId(cottageNumber); // assuming there's a setter for 'id'
            newCottage.setName(name);
            newCottage.setLocation(address);
            newCottage.setDescription(size);
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
