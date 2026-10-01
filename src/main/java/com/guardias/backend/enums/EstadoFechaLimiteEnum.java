package com.guardias.backend.enums;

public enum EstadoFechaLimiteEnum {
    VIGENTE,      // → fecha límite en uso para el mes/año/tipo de guardia
    REEMPLAZADA,  // → fue modificada, se creó una nueva versión
    ELIMINADA     // → fue dada de baja
}
