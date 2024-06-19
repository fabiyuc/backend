package com.guardias.backend.enums;

public enum TipoGuardiaEnum {
    CONTRAFACTURA(1),
    EXTRA(2),
    CARGO(3),
    AGRUPACION(4),
    PASIVA(5);

    private final int valor;

    TipoGuardiaEnum(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}
