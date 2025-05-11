package com.github.samumoil.mokkivaraaja.UI.duunarinakyma.controller;

import com.github.samumoil.mokkivaraaja.UI.duunarinakyma.interactor.CottageInteractor;
import com.github.samumoil.mokkivaraaja.UI.duunarinakyma.model.CottageModel;
import com.github.samumoil.mokkivaraaja.UI.duunarinakyma.view.CottageViewBuilder;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class CottageController {
    private final Builder<Region> viewBuilder;
    private final CottageInteractor interactor;

    CottageController(){
        CottageModel model = new CottageModel();
        interactor = new CottageInteractor(model);
        viewBuilder = new CottageViewBuilder(model);
        }

    public Region getView() {
        return viewBuilder.build();
    }


    }


