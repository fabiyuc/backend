package com.guardias.backend.enums;

public enum CondicionDdjjEnum {
    
    OFICIAL,          // Todos los registros válidos
    PARCIAL,          // Algunos registros excluidos (facturas incompletas)
    FUERA_DE_TERMINO  // Para casos especiales fuera del plazo
    
}
