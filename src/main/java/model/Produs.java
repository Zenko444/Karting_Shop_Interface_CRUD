package model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter @NoArgsConstructor
public abstract class Produs {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public   Integer id;

    public String denumire;
    public  Double pret;

    @ManyToOne
    @JoinColumn(name = "producator_id")
    public Producator producator;

    public Produs(String denumire, Double pret, Producator producator) {
        this.denumire = denumire;
        this.pret = pret;
        this.producator = producator;
    }

    // toString simplu
    @Override
    public String toString() { return denumire + " " + pret + " RON"; }
}