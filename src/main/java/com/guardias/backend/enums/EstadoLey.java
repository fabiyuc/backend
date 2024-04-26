package com.guardias.backend.enums;

public enum EstadoLey {
    VETADO("Vetado"),
    AGREGADO("Agregado"),
    MODIFICADO("Modificado"),
    VIGENTE("Vigente"),
    INCORPORADO("Incorporado"),
    DEROGADO("Derogado");

    private final String displayName;

    EstadoLey(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

}
