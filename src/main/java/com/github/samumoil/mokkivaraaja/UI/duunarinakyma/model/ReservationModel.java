package com.github.samumoil.mokkivaraaja.UI.duunarinakyma.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ReservationModel {


    private final BooleanProperty availabilityViewSelected = new SimpleBooleanProperty(true);

    private final BooleanProperty cabinViewSelected = new SimpleBooleanProperty(false);

    public boolean isPhaseOneSelected() {
        return phaseOneSelected.get();
    }

    public void setPhaseOneSelected(boolean phaseOneSelected) {
        this.phaseOneSelected.set(phaseOneSelected);
    }

    private final BooleanProperty phaseOneSelected = new SimpleBooleanProperty(false);

    public boolean isPhaseTwoSelected() {
        return phaseTwoSelected.get();
    }

    public void setPhaseTwoSelected(boolean phaseTwoSelected) {
        this.phaseTwoSelected.set(phaseTwoSelected);
    }

    private final BooleanProperty phaseTwoSelected = new SimpleBooleanProperty(false);


    public BooleanProperty availabilitySelectedProperty(){
        return availabilityViewSelected;
    }

    public BooleanProperty phaseOneSelectedProperty(){
        return phaseOneSelected;
    }
    public BooleanProperty phaseTwoSelectedProperty(){
        return phaseTwoSelected;
    }


}
