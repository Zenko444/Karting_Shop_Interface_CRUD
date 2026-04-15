package model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter @Setter @NoArgsConstructor
public class Kart extends Produs {
    private String tipMotor; // ex: 125cc, 4 timpi
    private String categorie; // ex: Senior, Junior

    public Kart(String denumire, Double pret, Producator producator, String tipMotor, String categorie) {
        super(denumire, pret, producator);
        this.tipMotor = tipMotor;
        this.categorie = categorie;
    }
}
