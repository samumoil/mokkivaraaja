package com.github.samumoil.mokkivaraaja.UI.duunarinakyma.controller;

import com.github.samumoil.mokkivaraaja.UI.duunarinakyma.model.ReservationModel;
import com.github.samumoil.mokkivaraaja.UI.duunarinakyma.interactor.ReservationInteractor;
import com.github.samumoil.mokkivaraaja.UI.duunarinakyma.view.ReservationViewBuilder;
import javafx.scene.layout.Region;
import javafx.util.Builder;

/**
 * The ReservationController class is responsible for managing the reservation-related
 * processes and handling interactions between the view, model, and interactor components.
 * <p>
 * This class initializes a reservation system by creating instances of a view builder,
 * an interactor, and connecting them with the reservation model. It facilitates the
 * creation of a user interface view through the builder pattern while maintaining a
 * separation of concerns between the data (model) and business logic (interactor).
 * <p>
 * Key responsibilities include:</p>
 * <ul>
 * <li>Creating and managing the reservation model.</li>
 * <li>Establishing the reservation interactor to handle business logic.</li>
 * <li>Building and providing the view for reservations through the view builder.</li>
 * </ul>
 */
public class ReservationController {

    Builder<Region> viewBuilder;
    ReservationModel model = new ReservationModel(); //Outer model
    private final ReservationInteractor interactor;

    public ReservationController() {

        interactor = new ReservationInteractor(model);

        viewBuilder = new ReservationViewBuilder(model);
    }

    private void handleAvailabilityLookUp() {
    }

    public Region getView() {
        return viewBuilder.build();
    }
}
