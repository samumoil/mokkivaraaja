package com.github.samumoil.mokkivaraaja.UI.duunarinakyma.interactor;

import com.github.samumoil.mokkivaraaja.UI.duunarinakyma.model.CottageModel;

/**
 * The CottageInteractor class serves as a bridge between the CottageModel and higher-level
 * controllers such as CottageController. It encapsulates the business logic related to
 * cottages and manages operations concerning the CottageModel.
 * <p>
 * This class is responsible for loading, processing, and manipulating cottage-related data
 * which are stored within the CottageModel. The CottageInteractor promotes separation of
 * concerns by isolating data handling and business logic from controller and view components.
 * <p>
 * Key responsibilities include:
 * <ul>
 * <li>Managing the lifecycle of cottage data through the CottageModel.</li>
 * <li>Performing operations related to cottages, such as retrieving or updating cottage information.</li>
 * </ul>
 */
public class CottageInteractor {

    private final CottageModel model;

    public CottageInteractor(CottageModel model) {
        this.model = model;
    }

    public void loadCottages() {

    }


}
