package com.guardias.backend.dto;






import java.util.List;
import java.util.Set;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.guardias.backend.entity.Efector;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionDto {

    @NotBlank
    private String nombre;
    
    private List<Efector> efectores;
    private boolean activo;
}
