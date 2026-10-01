package com.guardias.backend.constants;

import java.util.List;

public final class GruposZonalesGuardia {

    // Resolución 3590-SyHF-2024, Art. 1 inciso b) — SOLO Extra/CF, +20%
    public static final List<String> ZONA_20_EXTRA_CF = List.of(
            "SAN MIGUEL DE YUTO",
            /* "NTRA. SRA. DEL PILAR", */
            "NUESTRA SEÑORA DEL VALLE",
            "NUESTRA SEÑORA DEL ROSARIO"/*
                                         * ,
                                         * "SANTA BÁRBARA"
                                         */);

    public static final String HOSPITAL_URO = "JORGE URO";
    public static final String HOSPITAL_SUSQUES = "SUSQUES";

    private GruposZonalesGuardia() {
    }

}
