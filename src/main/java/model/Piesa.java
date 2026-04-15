package model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class Piesa extends Produs {
    public  String compatibilitate; // ex: "Toate motoarele Rotax"

    public Piesa(String denumire, Double pret, Producator producator, String compatibilitate) {
        super(denumire, pret, producator);
        this.compatibilitate = compatibilitate;
    }
}