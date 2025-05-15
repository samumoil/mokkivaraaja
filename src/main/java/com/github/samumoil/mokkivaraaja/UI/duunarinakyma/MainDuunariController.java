package com.github.samumoil.mokkivaraaja.UI.duunarinakyma;

import com.github.samumoil.mokkivaraaja.UI.duunarinakyma.controller.ReservationController;
import javafx.scene.layout.Region;
import javafx.util.Builder;

/**
 * Controller responsible for managing the main view of a user interface.
 * It integrates with various components such as the reservation system
 * and provides a structured view for the application.
 * <p>
 * This class employs a builder pattern to construct its associated view.
 * The view incorporates dynamic and modular components defined by
 * {@link MainDuunariViewBuilder} and includes a reservation view
 * provided by {@link ReservationController}.
 * <p>
 * The primary responsibilities of this class include:
 * - Initializing and configuring the main view's builder.
 * - Providing access to the constructed view for rendering within the application UI.
 */
public class MainDuunariController {

    private final Builder<Region> viewBuilder;

    public MainDuunariController() {
        viewBuilder = new MainDuunariViewBuilder(new ReservationController().getView());
    }

    public Region getView() {
        return viewBuilder.build();
    }
}
