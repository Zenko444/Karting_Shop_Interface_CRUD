package model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class Echipament extends Produs {
    private String marime; // S, M, L, XL
    private String tip; // Casca, Manusi, Costum

    public Echipament(String denumire, Double pret, Producator producator, String marime, String tip) {
        super(denumire, pret, producator);
        this.marime = marime;
        this.tip = tip;
    }
}