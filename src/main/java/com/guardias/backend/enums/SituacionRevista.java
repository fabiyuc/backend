package com.guardias.backend.enums;

public enum SituacionRevista {
    CONTRATO_DE_OBRA,
    CONTRATO_DE_SERVICIO,
    INTERINATO,
    PASANTE,
    PLANTA_PERMANENTE,
    RESIDENTE,
    SIN_RELACIÓN_DE_DEPENDENCIA,
    OTRO;

    @Override
    public String toString() {
        switch (this) {
            case CONTRATO_DE_OBRA:
                return "CONTRATO DE OBRA";
            case CONTRATO_DE_SERVICIO:
                return "CONTRATO DE SERVICIO";
            case INTERINATO:
                return "INTERINATO";
            case PASANTE:
                return "PASANTE";
            case PLANTA_PERMANENTE:
                return "PLANTA PERMANENTE";
            case RESIDENTE:
                return "RESIDENTE";
            case SIN_RELACIÓN_DE_DEPENDENCIA:
                return "SIN RELACIÓN DE DEPENDENCIA";
            case OTRO:
                return "OTRO";
            default:
                return "";
        }
    }
    
}
