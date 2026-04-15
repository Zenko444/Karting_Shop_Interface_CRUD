package model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class Producator {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String nume;
    private String taraOrigine;

    public Producator(String nume, String taraOrigine) {
        this.nume = nume;
        this.taraOrigine = taraOrigine;
    }

    @Override
    public String toString() { return nume + " (" + taraOrigine + ")"; }
}