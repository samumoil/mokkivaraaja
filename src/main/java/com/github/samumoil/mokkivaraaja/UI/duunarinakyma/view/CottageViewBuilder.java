package com.github.samumoil.mokkivaraaja.UI.duunarinakyma.view;

import com.github.samumoil.mokkivaraaja.domain.object.Cottage;
import com.github.samumoil.mokkivaraaja.UI.duunarinakyma.model.CottageModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class CottageViewBuilder implements Builder<Region> {

    private final CottageModel cottageModel;

    TableView<Cottage> cottageTableView = new TableView<>();

    public CottageViewBuilder(CottageModel cottageModel){
        this.cottageModel = cottageModel;
    }

    @Override
    public Region build() {
        return null;
    }

    private Region populateTableView(){
        TableColumn<Cottage, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Cottage, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Cottage, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Cottage, String> capacityColumn = new TableColumn<>("Capacity");
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        TableColumn<Cottage, String> pricePerNightColumn = new TableColumn<>("Price per night");
        pricePerNightColumn.setCellValueFactory(new PropertyValueFactory<>("price_per_night"));

        return cottageTableView;
    }

}
