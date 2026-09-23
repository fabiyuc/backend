package com.guardias.backend.constants;

import java.util.List;

public final class GruposZonalesGuardia {
    
    // Resolución 3590-SyHF-2024, Art. 1 inciso b) — SOLO Extra/CF, +20%
    public static final List<String> ZONA_20_EXTRA_CF = List.of(
            "SAN MIGUEL",
            "NTRA. SRA. DEL PILAR",
            "NTRA. SRA. DEL VALLE",
            "NTRA. SRA. DEL ROSARIO",
            "SANTA BÁRBARA"
    );

    public static final String HOSPITAL_URO = "DR. JORGE URO";
    public static final String HOSPITAL_SUSQUES = "SUSQUES";

    private GruposZonalesGuardia() {}
    
}
