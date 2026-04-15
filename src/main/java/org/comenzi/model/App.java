package org.comenzi.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.time.LocalDate;
import model.*;

public class App {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KartingJPA");
        EntityManager em = emf.createEntityManager();

        try {
            // ==========================================
            // 1. CURĂȚARE BAZĂ DE DATE (DELETE)
            // ==========================================
            System.out.println("--- 1. Curățare Bază de Date ---");
            em.getTransaction().begin();

            // Ștergem în ordinea dependențelor (Copii -> Părinți)
            em.createQuery("DELETE FROM ArticolVanzare").executeUpdate();
            em.createQuery("DELETE FROM Vanzare").executeUpdate();
            em.createQuery("DELETE FROM MagazinFizic").executeUpdate();

            // Ștergem produsele specifice și apoi generic
            em.createQuery("DELETE FROM AccesoriiKarting").executeUpdate();
            em.createQuery("DELETE FROM Kart").executeUpdate();
            em.createQuery("DELETE FROM Piesa").executeUpdate();
            em.createQuery("DELETE FROM Echipament").executeUpdate();
            em.createQuery("DELETE FROM Produs").executeUpdate();

            em.createQuery("DELETE FROM Producator").executeUpdate();
            em.createQuery("DELETE FROM Client").executeUpdate();
            em.createQuery("DELETE FROM Competitie").executeUpdate();

            em.getTransaction().commit();
            System.out.println("--- Baza de date a fost ștearsă complet! ---");


            // ==========================================
            // 2. POPULARE CU 20+ ÎNREGISTRĂRI/CATEGORIE
            // ==========================================
            System.out.println("--- 2. Începe popularea masivă ---");
            em.getTransaction().begin();

            // --- A. PRODUCĂTORI (20) ---
            Producator p1 = new Producator("Tony Kart", "Italia");
            Producator p2 = new Producator("Rotax", "Austria");
            Producator p3 = new Producator("Sparco", "Italia");
            Producator p4 = new Producator("Bell Racing", "SUA");
            Producator p5 = new Producator("Alpinestars", "Italia");
            Producator p6 = new Producator("Birel ART", "Italia");
            Producator p7 = new Producator("CRG", "Italia");
            Producator p8 = new Producator("IAME", "Italia");
            Producator p9 = new Producator("OMP", "Italia");
            Producator p10 = new Producator("Vega Tires", "Franța");
            Producator p11 = new Producator("TM Racing", "Italia");
            Producator p12 = new Producator("Sodi Kart", "Franța");
            Producator p13 = new Producator("Praga", "Cehia");
            Producator p14 = new Producator("Kosmic", "Italia");
            Producator p15 = new Producator("Vortex", "Italia");
            Producator p16 = new Producator("LeCont", "Italia");
            Producator p17 = new Producator("Tillett", "Marea Britanie");
            Producator p18 = new Producator("Alfano", "Belgia");
            Producator p19 = new Producator("Arai", "Japonia");
            Producator p20 = new Producator("Bridgestone", "Japonia");

            em.persist(p1); em.persist(p2); em.persist(p3); em.persist(p4); em.persist(p5);
            em.persist(p6); em.persist(p7); em.persist(p8); em.persist(p9); em.persist(p10);
            em.persist(p11); em.persist(p12); em.persist(p13); em.persist(p14); em.persist(p15);
            em.persist(p16); em.persist(p17); em.persist(p18); em.persist(p19); em.persist(p20);

            // --- B. KARTURI (20) ---
            Kart k1 = new Kart("Racer 401R", 5200.0, p1, "125cc OK", "Senior");
            Kart k2 = new Kart("Krypton 801", 5100.0, p1, "125cc KZ", "Shifter");
            Kart k3 = new Kart("Rookie EV", 2800.0, p1, "60cc Mini", "Mini");
            Kart k4 = new Kart("RY30 S14", 4950.0, p6, "Rotax Max", "Senior");
            Kart k5 = new Kart("RY32 S10", 5050.0, p6, "KZ2", "Shifter");
            Kart k6 = new Kart("C28 S9", 2700.0, p6, "60cc", "Mini");
            Kart k7 = new Kart("Road Rebel", 5300.0, p7, "KZ", "Shifter");
            Kart k8 = new Kart("KT2", 4800.0, p7, "OKJ", "Junior");
            Kart k9 = new Kart("Heron", 4900.0, p7, "X30", "Senior");
            Kart k10 = new Kart("Sigma KZ", 5400.0, p12, "KZ TM", "Shifter");
            Kart k11 = new Kart("Sigma RS3", 4700.0, p12, "Rotax", "Senior");
            Kart k12 = new Kart("Delta", 2600.0, p12, "Micro Max", "Micro");
            Kart k13 = new Kart("Dragon Evo", 4850.0, p13, "OK", "Senior");
            Kart k14 = new Kart("Dark Rider", 4950.0, p13, "KZ", "Shifter");
            Kart k15 = new Kart("Mercury R", 5250.0, p14, "Vortex OK", "Senior");
            Kart k16 = new Kart("Lynx", 5150.0, p14, "Vortex KZ", "Shifter");
            Kart k17 = new Kart("Exprit Noesis", 5000.0, p1, "IAME X30", "Senior");
            Kart k18 = new Kart("Redspeed RX", 5000.0, p1, "Rotax", "Senior");
            Kart k19 = new Kart("Ricciardo Kart", 4900.0, p6, "IAME", "Junior");
            Kart k20 = new Kart("Charles Leclerc", 4950.0, p6, "KZ", "Shifter");

            em.persist(k1); em.persist(k2); em.persist(k3); em.persist(k4); em.persist(k5);
            em.persist(k6); em.persist(k7); em.persist(k8); em.persist(k9); em.persist(k10);
            em.persist(k11); em.persist(k12); em.persist(k13); em.persist(k14); em.persist(k15);
            em.persist(k16); em.persist(k17); em.persist(k18); em.persist(k19); em.persist(k20);

            // --- C. PIESE (20) ---
            Piesa pi1 = new Piesa("Set Cauciucuri Slick", 180.0, p10, "Universal");
            Piesa pi2 = new Piesa("Set Cauciucuri Ploaie", 200.0, p10, "Universal");
            Piesa pi3 = new Piesa("Bujie Iridium", 25.0, p2, "Rotax/IAME");
            Piesa pi4 = new Piesa("Lanț 114 Zale", 45.0, p11, "KZ/OK");
            Piesa pi5 = new Piesa("Pinion Spate 78T", 30.0, p11, "Universal");
            Piesa pi6 = new Piesa("Ax Spate 50mm Mediu", 150.0, p1, "OTK");
            Piesa pi7 = new Piesa("Ax Spate 50mm Dur", 150.0, p1, "OTK");
            Piesa pi8 = new Piesa("Butuc Volan", 40.0, p1, "OTK Inclinat");
            Piesa pi9 = new Piesa("Plăcuțe Frână Față", 55.0, p7, "CRG Ven11");
            Piesa pi10 = new Piesa("Plăcuțe Frână Spate", 45.0, p7, "CRG Ven11");
            Piesa pi11 = new Piesa("Piston 53.98", 85.0, p15, "Vortex Rok");
            Piesa pi12 = new Piesa("Bielă Motor", 120.0, p11, "TM KZ10");
            Piesa pi13 = new Piesa("Carburator Dellorto", 250.0, p2, "Rotax VHSB34");
            Piesa pi14 = new Piesa("Membrană Carburator", 20.0, p8, "IAME Tillotson");
            Piesa pi15 = new Piesa("Filtru Aer", 60.0, p2, "Rotax Evo");
            Piesa pi16 = new Piesa("Radiator Mare", 300.0, p11, "New Line");
            Piesa pi17 = new Piesa("Pompă Apă", 50.0, p11, "Aluminiu");
            Piesa pi18 = new Piesa("Scaun Transparent", 90.0, p17, "T11 Soft");
            Piesa pi19 = new Piesa("Rezervor 8.5L", 40.0, p7, "CRG");
            Piesa pi20 = new Piesa("Volan 320mm", 130.0, p3, "Sparco Alcantara");

            em.persist(pi1); em.persist(pi2); em.persist(pi3); em.persist(pi4); em.persist(pi5);
            em.persist(pi6); em.persist(pi7); em.persist(pi8); em.persist(pi9); em.persist(pi10);
            em.persist(pi11); em.persist(pi12); em.persist(pi13); em.persist(pi14); em.persist(pi15);
            em.persist(pi16); em.persist(pi17); em.persist(pi18); em.persist(pi19); em.persist(pi20);

            // --- D. ECHIPAMENT (20) ---
            Echipament e1 = new Echipament("Costum KS-1", 400.0, p9, "M", "Costum");
            Echipament e2 = new Echipament("Costum Thunder", 250.0, p3, "L", "Costum");
            Echipament e3 = new Echipament("Costum KMX-9", 300.0, p5, "S", "Costum");
            Echipament e4 = new Echipament("Ghete KB-Gam", 120.0, p3, "42", "Ghete");
            Echipament e5 = new Echipament("Ghete Tech-1 KX", 150.0, p5, "43", "Ghete");
            Echipament e6 = new Echipament("Ghete KS-3", 100.0, p9, "40", "Ghete");
            Echipament e7 = new Echipament("Costum Ploaie Transparent", 50.0, p3, "XL", "Ploaie");
            Echipament e8 = new Echipament("Costum Ploaie Negru", 60.0, p5, "M", "Ploaie");
            Echipament e9 = new Echipament("Protectie Coaste Carbon", 180.0, p17, "M", "Protectie");
            Echipament e10 = new Echipament("Protectie Coaste Bumper", 120.0, p2, "L", "Protectie");
            Echipament e11 = new Echipament("Guler Suport", 40.0, p3, "Universal", "Protectie");
            Echipament e12 = new Echipament("Costum Mecanic", 80.0, p3, "XXL", "Mecanic");
            Echipament e13 = new Echipament("Lenjerie Ignifugă Top", 70.0, p5, "M", "Lenjerie");
            Echipament e14 = new Echipament("Lenjerie Ignifugă Pantaloni", 60.0, p5, "M", "Lenjerie");
            Echipament e15 = new Echipament("Ciorapi Coolmax", 15.0, p3, "40-43", "Accesorii");
            Echipament e16 = new Echipament("Costum Copii Rookie", 200.0, p9, "140cm", "Costum");
            Echipament e17 = new Echipament("Ghete Copii", 90.0, p5, "36", "Ghete");
            Echipament e18 = new Echipament("Protectie Genunchi", 30.0, p3, "Universal", "Protectie");
            Echipament e19 = new Echipament("Protectie Cot", 25.0, p3, "Universal", "Protectie");
            Echipament e20 = new Echipament("Geanta Echipament", 80.0, p3, "Mare", "Accesorii");

            em.persist(e1); em.persist(e2); em.persist(e3); em.persist(e4); em.persist(e5);
            em.persist(e6); em.persist(e7); em.persist(e8); em.persist(e9); em.persist(e10);
            em.persist(e11); em.persist(e12); em.persist(e13); em.persist(e14); em.persist(e15);
            em.persist(e16); em.persist(e17); em.persist(e18); em.persist(e19); em.persist(e20);

            // --- E. ACCESORII KARTING (20) --- (Cu Stoc)
            AccesoriiKarting ac1 = new AccesoriiKarting("Manusi Tech-1 K", 80.0, p5, "Manusi", "M", "Textil");
            AccesoriiKarting ac2 = new AccesoriiKarting("Manusi Tech-1 ZX", 120.0, p5, "Manusi", "L", "Textil Premium");
            AccesoriiKarting ac3 = new AccesoriiKarting("Manusi Tide K", 140.0, p3, "Manusi", "S", "Silicon");
            AccesoriiKarting ac4 = new AccesoriiKarting("Manusi Rush", 50.0, p3, "Manusi", "XL", "Textil");
            AccesoriiKarting ac5 = new AccesoriiKarting("Manusi KS-2", 60.0, p9, "Manusi", "M", "Textil");
            AccesoriiKarting ac6 = new AccesoriiKarting("Casca KC7-CMR", 600.0, p4, "Casca", "57", "Kevlar");
            AccesoriiKarting ac7 = new AccesoriiKarting("Casca KC7-CMR Venom", 700.0, p4, "Casca", "59", "Kevlar");
            AccesoriiKarting ac8 = new AccesoriiKarting("Casca GP3 Carbon", 950.0, p4, "Casca", "60", "Carbon");
            AccesoriiKarting ac9 = new AccesoriiKarting("Casca SK-6", 550.0, p19, "Casca", "M", "Fibra");
            AccesoriiKarting ac10 = new AccesoriiKarting("Casca CK-6", 450.0, p19, "Casca", "S", "Fibra Copii");
            AccesoriiKarting ac11 = new AccesoriiKarting("Viziera Fumurie KC7", 90.0, p4, "Viziera", "Universal", "Policarbonat");
            AccesoriiKarting ac12 = new AccesoriiKarting("Viziera Albastra SK6", 100.0, p19, "Viziera", "Universal", "Iridium");
            AccesoriiKarting ac13 = new AccesoriiKarting("Viziera Aurie GP3", 110.0, p4, "Viziera", "Universal", "Gold");
            AccesoriiKarting ac14 = new AccesoriiKarting("Set Tear-offs", 20.0, p4, "Consumabil", "Universal", "Plastic");
            AccesoriiKarting ac15 = new AccesoriiKarting("Spoiler Spate Casca", 50.0, p4, "Aero", "Universal", "Plastic");
            AccesoriiKarting ac16 = new AccesoriiKarting("Balaclava Coolmax", 25.0, p3, "Sub-Casca", "OneSize", "Textil");
            AccesoriiKarting ac17 = new AccesoriiKarting("Balaclava Nomex", 40.0, p5, "Sub-Casca", "OneSize", "Ignifug");
            AccesoriiKarting ac18 = new AccesoriiKarting("Kit Suruburi Viziera", 15.0, p4, "Piese", "Universal", "Metal");
            AccesoriiKarting ac19 = new AccesoriiKarting("Spray Curatare Casca", 10.0, p3, "Intretinere", "200ml", "Lichid");
            AccesoriiKarting ac20 = new AccesoriiKarting("Sac Casca", 30.0, p4, "Transport", "Universal", "Textil");

            // Setăm stocurile
            ac1.setStoc(50); ac2.setStoc(20); ac3.setStoc(15); ac4.setStoc(100); ac5.setStoc(40);
            ac6.setStoc(10); ac7.setStoc(5); ac8.setStoc(3); ac9.setStoc(8); ac10.setStoc(12);
            ac11.setStoc(25); ac12.setStoc(20); ac13.setStoc(15); ac14.setStoc(200); ac15.setStoc(30);
            ac16.setStoc(60); ac17.setStoc(40); ac18.setStoc(50); ac19.setStoc(100); ac20.setStoc(45);

            em.persist(ac1); em.persist(ac2); em.persist(ac3); em.persist(ac4); em.persist(ac5);
            em.persist(ac6); em.persist(ac7); em.persist(ac8); em.persist(ac9); em.persist(ac10);
            em.persist(ac11); em.persist(ac12); em.persist(ac13); em.persist(ac14); em.persist(ac15);
            em.persist(ac16); em.persist(ac17); em.persist(ac18); em.persist(ac19); em.persist(ac20);

            // --- F. MAGAZINE (20) ---
            MagazinFizic m1 = new MagazinFizic("Karting Shop Bucharest", "Romania", "București", "Pipera", "0722000111", "09-18", k1);
            MagazinFizic m2 = new MagazinFizic("Transilvania Racing", "Romania", "Cluj-Napoca", "Turzii", "0744000222", "10-18", ac1);
            MagazinFizic m3 = new MagazinFizic("Speed Hub TM", "Romania", "Timișoara", "Aradului", "0755000333", "09-17", pi3);
            MagazinFizic m4 = new MagazinFizic("Prejmer Store", "Romania", "Brașov", "Prejmer", "0733000444", "08-17", k3);
            MagazinFizic m5 = new MagazinFizic("Mondo Kart", "Italia", "Milano", "Via Monza", "+3902123", "09-19", k2);
            MagazinFizic m6 = new MagazinFizic("South Garda Shop", "Italia", "Lonato", "Pista", "+3903099", "08-20", pi1);
            MagazinFizic m7 = new MagazinFizic("Superkart It", "Italia", "Roma", "Via Appia", "+3906555", "10-18", ac6);
            MagazinFizic m8 = new MagazinFizic("London Kart Centre", "UK", "Londra", "Silverstone", "+4420123", "09-18", k17);
            MagazinFizic m9 = new MagazinFizic("Demon Tweeks", "UK", "Wrexham", "Industrial Est", "+441978", "08-17", e1);
            MagazinFizic m10 = new MagazinFizic("Kartodrom Paris", "Franța", "Paris", "Carole", "+331456", "10-19", k12);
            MagazinFizic m11 = new MagazinFizic("Sodi Factory Store", "Franța", "Nantes", "Coueron", "+332403", "09-17", k11);
            MagazinFizic m12 = new MagazinFizic("Kart Shop DE", "Germania", "Berlin", "Ring", "+4930111", "10-18", pi13);
            MagazinFizic m13 = new MagazinFizic("Kerpen Kart", "Germania", "Kerpen", "Schumacher Str", "+492273", "09-18", ac8);
            MagazinFizic m14 = new MagazinFizic("Racing Store ES", "Spania", "Madrid", "Jarama", "+349155", "10-20", e4);
            MagazinFizic m15 = new MagazinFizic("Zuera Shop", "Spania", "Zaragoza", "Zuera", "+349766", "09-18", pi20);
            MagazinFizic m16 = new MagazinFizic("Dubai Autodrome", "UAE", "Dubai", "Motor City", "+971436", "10-22", k20);
            MagazinFizic m17 = new MagazinFizic("USA Karting", "SUA", "New York", "Broadway", "+1212555", "09-17", ac2);
            MagazinFizic m18 = new MagazinFizic("GoPro Motorplex", "SUA", "Charlotte", "Mooresville", "+1704696", "08-20", k1);
            MagazinFizic m19 = new MagazinFizic("Suzuka Store", "Japonia", "Suzuka", "Circuit", "+815937", "09-18", ac9);
            MagazinFizic m20 = new MagazinFizic("Genk Shop", "Belgia", "Genk", "Home of Champions", "+328965", "09-18", k13);

            em.persist(m1); em.persist(m2); em.persist(m3); em.persist(m4); em.persist(m5);
            em.persist(m6); em.persist(m7); em.persist(m8); em.persist(m9); em.persist(m10);
            em.persist(m11); em.persist(m12); em.persist(m13); em.persist(m14); em.persist(m15);
            em.persist(m16); em.persist(m17); em.persist(m18); em.persist(m19); em.persist(m20);

            // --- G. COMPETIȚII (20) ---
            Competitie c1 = new Competitie("Cupa României Etapa 1", "Amckart", LocalDate.of(2024, 4, 15), 35, "Mini 60");
            Competitie c2 = new Competitie("Cupa României Etapa 2", "Skarta", LocalDate.of(2024, 5, 10), 40, "Junior");
            Competitie c3 = new Competitie("Campionat Național 1", "Prejmer", LocalDate.of(2024, 6, 20), 60, "KZ2");
            Competitie c4 = new Competitie("Campionat Național 2", "Bacau", LocalDate.of(2024, 7, 15), 50, "OKJ");
            Competitie c5 = new Competitie("WSK Champions Cup", "Lonato", LocalDate.of(2024, 1, 28), 200, "Toate");
            Competitie c6 = new Competitie("WSK Super Master 1", "Lonato", LocalDate.of(2024, 2, 11), 250, "Toate");
            Competitie c7 = new Competitie("WSK Super Master 2", "Cremona", LocalDate.of(2024, 2, 25), 230, "Toate");
            Competitie c8 = new Competitie("WSK Super Master 3", "Franciacorta", LocalDate.of(2024, 3, 5), 240, "Toate");
            Competitie c9 = new Competitie("Winter Cup", "South Garda", LocalDate.of(2024, 2, 18), 80, "KZ");
            Competitie c10 = new Competitie("Rotax Euro Trophy 1", "Genk", LocalDate.of(2024, 4, 12), 150, "Rotax");
            Competitie c11 = new Competitie("Rotax Euro Trophy 2", "Wackersdorf", LocalDate.of(2024, 6, 7), 140, "Rotax");
            Competitie c12 = new Competitie("FIA European KZ", "Zuera", LocalDate.of(2024, 5, 15), 90, "KZ");
            Competitie c13 = new Competitie("FIA European OK", "Valencia", LocalDate.of(2024, 6, 1), 80, "OK");
            Competitie c14 = new Competitie("FIA World Championship", "Portimao", LocalDate.of(2024, 11, 10), 180, "OK");
            Competitie c15 = new Competitie("Rok Cup Superfinal", "Lonato", LocalDate.of(2024, 10, 15), 300, "Rok");
            Competitie c16 = new Competitie("IAME International Final", "Le Mans", LocalDate.of(2024, 10, 20), 350, "X30");
            Competitie c17 = new Competitie("Rotax Grand Finals", "Bahrain", LocalDate.of(2024, 12, 1), 400, "Rotax Max");
            Competitie c18 = new Competitie("SKUSA Supernationals", "Las Vegas", LocalDate.of(2024, 11, 20), 500, "Toate");
            Competitie c19 = new Competitie("Trofeo Margutti", "Lonato", LocalDate.of(2024, 3, 17), 120, "Toate");
            Competitie c20 = new Competitie("Trofeo Industrie", "Lonato", LocalDate.of(2024, 10, 29), 130, "Toate");

            em.persist(c1); em.persist(c2); em.persist(c3); em.persist(c4); em.persist(c5);
            em.persist(c6); em.persist(c7); em.persist(c8); em.persist(c9); em.persist(c10);
            em.persist(c11); em.persist(c12); em.persist(c13); em.persist(c14); em.persist(c15);
            em.persist(c16); em.persist(c17); em.persist(c18); em.persist(c19); em.persist(c20);

            // --- H. CLIENȚI (20) ---
            Client cl1 = new Client("George Pilotul", "george@racing.ro");
            Client cl2 = new Client("Ion Popescu", "ion@mail.com");
            Client cl3 = new Client("Maria Ionescu", "maria@kart.ro");
            Client cl4 = new Client("Andrei Radu", "andrei@speed.com");
            Client cl5 = new Client("Elena Dumitru", "elena@racing.com");
            Client cl6 = new Client("Max Verstappen", "max@f1.com");
            Client cl7 = new Client("Lewis Hamilton", "lewis@f1.com");
            Client cl8 = new Client("Charles Leclerc", "charles@ferrari.com");
            Client cl9 = new Client("Lando Norris", "lando@mclaren.com");
            Client cl10 = new Client("Oscar Piastri", "oscar@mclaren.com");
            Client cl11 = new Client("Fernando Alonso", "fernando@am.com");
            Client cl12 = new Client("Carlos Sainz", "carlos@ferrari.com");
            Client cl13 = new Client("George Russell", "george@merc.com");
            Client cl14 = new Client("Alex Albon", "alex@williams.com");
            Client cl15 = new Client("Kimi Antonelli", "kimi@merc.com");
            Client cl16 = new Client("Sebastian Vettel", "seb@green.com");
            Client cl17 = new Client("Michael Schumacher", "msc@keepfighting.com");
            Client cl18 = new Client("Ayrton Senna", "senna@brasil.com");
            Client cl19 = new Client("Alain Prost", "prost@prof.com");
            Client cl20 = new Client("Niki Lauda", "niki@legend.com");

            em.persist(cl1); em.persist(cl2); em.persist(cl3); em.persist(cl4); em.persist(cl5);
            em.persist(cl6); em.persist(cl7); em.persist(cl8); em.persist(cl9); em.persist(cl10);
            em.persist(cl11); em.persist(cl12); em.persist(cl13); em.persist(cl14); em.persist(cl15);
            em.persist(cl16); em.persist(cl17); em.persist(cl18); em.persist(cl19); em.persist(cl20);

            // --- I. VÂNZĂRI (20) ---
            Vanzare v1 = new Vanzare(cl1); v1.setData(LocalDate.now().minusDays(30)); v1.adaugaProdus(k1, 1);
            Vanzare v2 = new Vanzare(cl2); v2.setData(LocalDate.now().minusDays(25)); v2.adaugaProdus(pi1, 4); v2.adaugaProdus(pi3, 2);
            Vanzare v3 = new Vanzare(cl3); v3.setData(LocalDate.now().minusDays(20)); v3.adaugaProdus(ac6, 1);
            Vanzare v4 = new Vanzare(cl4); v4.setData(LocalDate.now().minusDays(18)); v4.adaugaProdus(e1, 1); v4.adaugaProdus(e4, 1);
            Vanzare v5 = new Vanzare(cl5); v5.setData(LocalDate.now().minusDays(15)); v5.adaugaProdus(pi13, 1);
            Vanzare v6 = new Vanzare(cl6); v6.setData(LocalDate.now().minusDays(12)); v6.adaugaProdus(k7, 2); // 2 KZ-uri pt Max
            Vanzare v7 = new Vanzare(cl7); v7.setData(LocalDate.now().minusDays(10)); v7.adaugaProdus(ac8, 1); v7.adaugaProdus(ac13, 1);
            Vanzare v8 = new Vanzare(cl8); v8.setData(LocalDate.now().minusDays(8)); v8.adaugaProdus(k20, 1); // Charles Kart
            Vanzare v9 = new Vanzare(cl9); v9.setData(LocalDate.now().minusDays(7)); v9.adaugaProdus(pi6, 2); v9.adaugaProdus(pi1, 2);
            Vanzare v10 = new Vanzare(cl10); v10.setData(LocalDate.now().minusDays(6)); v10.adaugaProdus(e3, 1);
            Vanzare v11 = new Vanzare(cl11); v11.setData(LocalDate.now().minusDays(5)); v11.adaugaProdus(k13, 1);
            Vanzare v12 = new Vanzare(cl12); v12.setData(LocalDate.now().minusDays(4)); v12.adaugaProdus(pi20, 1); v12.adaugaProdus(ac1, 1);
            Vanzare v13 = new Vanzare(cl13); v13.setData(LocalDate.now().minusDays(3)); v13.adaugaProdus(pi16, 1); v13.adaugaProdus(pi17, 1);
            Vanzare v14 = new Vanzare(cl14); v14.setData(LocalDate.now().minusDays(2)); v14.adaugaProdus(e7, 1); v14.adaugaProdus(pi2, 4);
            Vanzare v15 = new Vanzare(cl15); v15.setData(LocalDate.now().minusDays(1)); v15.adaugaProdus(k15, 1);
            Vanzare v16 = new Vanzare(cl16); v16.setData(LocalDate.now()); v16.adaugaProdus(ac16, 5); // Balaclave pt toata echipa
            Vanzare v17 = new Vanzare(cl17); v17.setData(LocalDate.of(2023, 12, 1)); v17.adaugaProdus(k1, 3);
            Vanzare v18 = new Vanzare(cl18); v18.setData(LocalDate.of(2023, 11, 15)); v18.adaugaProdus(ac7, 1);
            Vanzare v19 = new Vanzare(cl19); v19.setData(LocalDate.of(2023, 10, 10)); v19.adaugaProdus(pi11, 4);
            Vanzare v20 = new Vanzare(cl20); v20.setData(LocalDate.of(2023, 9, 5)); v20.adaugaProdus(m5.getProdusVedeta(), 1);

            em.persist(v1); em.persist(v2); em.persist(v3); em.persist(v4); em.persist(v5);
            em.persist(v6); em.persist(v7); em.persist(v8); em.persist(v9); em.persist(v10);
            em.persist(v11); em.persist(v12); em.persist(v13); em.persist(v14); em.persist(v15);
            em.persist(v16); em.persist(v17); em.persist(v18); em.persist(v19); em.persist(v20);

            em.getTransaction().commit();
            System.out.println("--- Datele au fost introduse cu succes! ---");

        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
            emf.close();
        }
    }
}