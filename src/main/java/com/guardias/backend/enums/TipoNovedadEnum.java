package com.guardias.backend.enums;

public enum TipoNovedadEnum {
    Compensatorio,
    LAO,
    Maternidad,
    Parte_de_enfermo,
    Familiar_enfermo,
    Falta_sin_aviso;

    @Override
    public String toString() {
        switch (this) {
            case Compensatorio:
                return "Compensatorio";
            case LAO:
                return "L.A.O";
            case Maternidad:
                return "Maternidad";
            case Parte_de_enfermo:
                return "Parte de Enfermo";
            case Familiar_enfermo:
                return "Familiar enfermo";
            case Falta_sin_aviso:
                return "Falta sin aviso";
            default:
                return "";
        }
    }

}
