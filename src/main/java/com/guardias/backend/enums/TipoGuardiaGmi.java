package com.guardias.backend.enums;

public enum TipoGuardiaGmi {
    
    CARGO(1),
    EXTRA(2),
    PASIVA(3);

    private final int valor;

    TipoGuardiaGmi(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}
