package com.guardias.backend.enums;

public enum MesesEnum {
    ENERO(1),
    FEBRERO(2),
    MARZO(3),
    ABRIL(4),
    MAYO(5),
    JUNIO(6),
    JULIO(7),
    AGOSTO(8),
    SEPTIEMBRE(9),
    OCTUBRE(10),
    NOVIEMBRE(11),
    DICIEMBRE(12);

    private final int numeroMes;

    MesesEnum(int numeroMes) {
        this.numeroMes = numeroMes;
    }

    public int getNumeroMes() {
        return numeroMes;
    }

    public static MesesEnum fromNumeroMes(int numeroMes) {
        for (MesesEnum mes : values()) {
            if (mes.getNumeroMes() == numeroMes) {
                return mes;
            }
        }
        throw new IllegalArgumentException("Número de mes inválido: " + numeroMes);
    }
}
