package com.github.samumoil.mokkivaraaja.UI.duunarinakyma.controller;

import com.github.samumoil.mokkivaraaja.UI.duunarinakyma.interactor.CottageInteractor;
import com.github.samumoil.mokkivaraaja.UI.duunarinakyma.model.CottageModel;
import com.github.samumoil.mokkivaraaja.UI.duunarinakyma.view.CottageViewBuilder;
import javafx.scene.layout.Region;
import javafx.util.Builder;

/**
 * The CottageController class acts as the primary controller for managing cottage-related
 * functionality in the application. It serves as the intermediary between the view, model,
 * and interactor components.
 * <p>
 * This class is responsible for initializing the necessary components required to display
 * and manage data for cottages. It ensures separation of concerns by delegating business
 * logic to the CottageInteractor and view construction to the CottageViewBuilder.
 * <p>
 * Key responsibilities include:</p>
 * <ul>
 * <li>Creating and managing an instance of the CottageInteractor to handle business logic.</li>
 * <li>Utilizing the CottageViewBuilder to construct and provide the user interface view.</li>
 * <li>Establishing communication between the data model and the view components.</li>
 * </ul>
 */
public class CottageController {
    private final Builder<Region> viewBuilder;
    private final CottageInteractor interactor;

    CottageController() {
        CottageModel model = new CottageModel();
        interactor = new CottageInteractor(model);
        viewBuilder = new CottageViewBuilder(model);
    }

    public Region getView() {
        return viewBuilder.build();
    }
}


