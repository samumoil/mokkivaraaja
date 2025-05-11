package com.github.samumoil.mokkivaraaja.UI.duunarinakyma;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class MainDuunariViewBuilder implements Builder<Region> {


    private final Region reservationView;

    public MainDuunariViewBuilder(Region reservationView){
        this.reservationView = reservationView;
    }

    @Override
    public Region build() {
        BorderPane result = new BorderPane();
        result.setCenter(reservationView);
        return result;
    }
}
