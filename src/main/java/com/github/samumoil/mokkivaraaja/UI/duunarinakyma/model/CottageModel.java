package com.github.samumoil.mokkivaraaja.UI.duunarinakyma.model;

import com.github.samumoil.mokkivaraaja.domain.object.Cottage;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CottageModel {
    public ObservableList<Cottage> getCabinList() {
        return cabinList.get();
    }

    public ObjectProperty<ObservableList<Cottage>> cabinListProperty() {
        return cabinList;
    }

    public void setCabinList(ObservableList<Cottage> cabinList) {
        this.cabinList.set(cabinList);
    }

    private final ObjectProperty<ObservableList<Cottage>> cabinList = new SimpleObjectProperty<>(FXCollections.observableArrayList());


}
