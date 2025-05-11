package com.github.samumoil.mokkivaraaja.UI.duunarinakyma.view;

import com.github.samumoil.mokkivaraaja.UI.duunarinakyma.model.ReservationModel;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Builder;

public class ReservationViewBuilder implements Builder<Region> {

    private final ReservationModel model;


   // private final Consumer<Runnable>


    public ReservationViewBuilder(ReservationModel model) {
        this.model = model;


    }

    @Override
    public Region build() {
        BorderPane result = new BorderPane();
        result.setTop(createTop());
        result.setBottom(createBot());
        return result;
    }

    public Region createTop(){
        return new HBox(new DatePicker(), new DatePicker(), new Button("TODO"));
    }


    public Region createBot(){
        return new StackPane(createPhaseOne(), createPhaseTwo());
    }

    public Node createPhaseOne(){

        Button back = new Button("Takaisin");
        back.setOnAction(actionEvent -> model.setPhaseOneSelected(true));
        Button forward = new Button("Varaa valittu mökki");
        HBox phaseOne = new HBox(back, forward);
        phaseOne.visibleProperty().bind(model.phaseOneSelectedProperty());
        return phaseOne;
    }

    public Node createPhaseTwo(){
        Button back = new Button("Takaisin");
        back.setOnAction(actionEvent -> model.setPhaseTwoSelected(true));
        Button forward = new Button("Vahvista varaus");
        HBox phaseTwo = new HBox(back, forward);
        phaseTwo.visibleProperty().bind(model.phaseTwoSelectedProperty());
        return phaseTwo;
    }




}
