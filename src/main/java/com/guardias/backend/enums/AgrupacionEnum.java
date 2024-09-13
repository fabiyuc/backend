package com.guardias.backend.enums;

import java.util.Arrays;
import java.util.List;

public enum AgrupacionEnum {
    ADMINISTRATIVO("Administrativo"),
    MANTENIMIENTO_Y_PRODUCCION("Mantenimiento y Producción"),
    SERVICIOS_GENERALES("Servicios Generales"),
    TECNICOS("Técnicos"),
    PROFESIONALES("Profesionales");

    private final String displayName;

    AgrupacionEnum(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static List<AgrupacionEnum> getAll() {
        return Arrays.asList(values());
    }
}
