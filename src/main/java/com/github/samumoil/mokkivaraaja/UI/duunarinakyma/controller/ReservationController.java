package com.github.samumoil.mokkivaraaja.UI.duunarinakyma.controller;

import com.github.samumoil.mokkivaraaja.UI.duunarinakyma.model.ReservationModel;
import com.github.samumoil.mokkivaraaja.UI.duunarinakyma.interactor.ReservationInteractor;
import com.github.samumoil.mokkivaraaja.UI.duunarinakyma.view.ReservationViewBuilder;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class ReservationController {

    Builder<Region> viewBuilder;
    ReservationModel model = new ReservationModel(); //Outer model
    private final ReservationInteractor interactor;

    public ReservationController(){

        interactor = new ReservationInteractor(model);

        viewBuilder = new ReservationViewBuilder(model);

    }

    private void handleAvailabilityLookUp(){

    }

    public Region getView(){
        return viewBuilder.build();
    }
}
