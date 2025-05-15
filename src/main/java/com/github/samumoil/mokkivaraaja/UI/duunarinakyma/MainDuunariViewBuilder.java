package com.github.samumoil.mokkivaraaja.UI.duunarinakyma;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Builder;

/**
 * MainDuunariViewBuilder is a builder implementation responsible for constructing
 * the primary view layout for an application interface, specifically for a feature
 * related to reservations.
 * <p>
 * This class leverages the Builder pattern to dynamically create a JavaFX Region
 * component. It utilizes a given reservation view to populate the center of
 * a constructed BorderPane layout.
 * <p>
 * Responsibilities of this builder include:
 * - Creating a BorderPane layout.
 * - Setting the provided reservation view in the center of the BorderPane.
 * - Returning the fully constructed Region object as the build result.
 * <p>
 * Example scenarios where this builder may be utilized include integrating it
 * into a controller where dynamic or modular views are required in the user interface.
 * <p>
 * The reservation view provided during instantiation is expected to be a previously
 * configured Region component intended to render reservation-related data or user
 * functionality.
 */
public class MainDuunariViewBuilder implements Builder<Region> {
    private final Region reservationView;

    public MainDuunariViewBuilder(Region reservationView) {
        this.reservationView = reservationView;
    }

    @Override
    public Region build() {
        BorderPane result = new BorderPane();
        result.setCenter(reservationView);
        return result;
    }
}
