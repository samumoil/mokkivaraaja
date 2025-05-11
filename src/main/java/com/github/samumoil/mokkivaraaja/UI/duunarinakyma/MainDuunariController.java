package com.github.samumoil.mokkivaraaja.UI.duunarinakyma;

import com.github.samumoil.mokkivaraaja.UI.duunarinakyma.controller.ReservationController;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class MainDuunariController {

    private final Builder<Region> viewBuilder;

    public MainDuunariController(){
        viewBuilder = new MainDuunariViewBuilder(new ReservationController().getView());
    }

    public Region getView(){
        return viewBuilder.build();
    }
}
