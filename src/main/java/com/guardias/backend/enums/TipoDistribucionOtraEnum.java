package com.guardias.backend.enums;

public enum TipoDistribucionOtraEnum {
    PASE_DE_SALA("Pase de sala"),
    ATENEO("Ateneo"),
    CONSULTORIO_EN_CAPS("Consultorio en CAPS"),
    OTROS("Otros");

    private final String displayName;

    TipoDistribucionOtraEnum(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
