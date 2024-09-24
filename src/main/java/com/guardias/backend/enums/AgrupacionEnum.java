package com.guardias.backend.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name();
    }

    public static List<AgrupacionEnumDto> getAllAgrupaciones() {
        return Arrays.stream(values())
                .map(agrupacion -> new AgrupacionEnumDto(agrupacion.name(), agrupacion.getDisplayName()))
                .collect(Collectors.toList());
    }

    public static class AgrupacionEnumDto {
        private final String name;
        private final String displayName;

        public AgrupacionEnumDto(String name, String displayName) {
            this.name = name;
            this.displayName = displayName;
        }

        public String getName() {
            return name;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public static AgrupacionEnum fromDisplayName(String displayName) {
        // Normaliza el nombre para hacer la comparación
        String normalizedDisplayName = displayName.replace("_", " ").trim();

        for (AgrupacionEnum agrupacion : AgrupacionEnum.values()) {
            // Compara tanto el displayName normalizado como el nombre del enum
            if (agrupacion.getDisplayName().equalsIgnoreCase(normalizedDisplayName) ||
                    agrupacion.name().equalsIgnoreCase(displayName)) {
                return agrupacion;
            }
        }
        return null; // Retorna null si no se encuentra
    }

}