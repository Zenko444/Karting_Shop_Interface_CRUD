package model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String nume;
    private String email;

    public Client(String nume, String email) {
        this.nume = nume;
        this.email = email;
    }
}