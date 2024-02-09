package com.guardias.backend.enums;

public enum AgrupacionEnum {
    ADMINISTRATIVO("Administrativo"),
    MANTENIMIENTO_Y_PRODUCCION("Mantenimiento y Producción"),
    SERVICIOS_GENERALES("servicios Generales"),
    TECNICOS("Tecnicos"),
    PROFESIONALES("Profesionales");

    private final String displayName;

    AgrupacionEnum(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
