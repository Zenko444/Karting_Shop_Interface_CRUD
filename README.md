## Karting Shop Interface with CRUD - Romanian Language

The project was done using Gemini and OOP knowledge along with Vaadin plug-in and with a secured database in PostgreSQL. In persistence you can find the log-in area where you can connect your database using your profile and password.
By default the project is called KartingJPA, but you can change that. karting_db is the database, also can be changed depending on your preferences.

## Project Structure

```

Karting_Proiect/
├── frontend/
│   ├── generated/                 ← Cod generat automat de Vaadin (Ignorat in Git)
│   │   ├── flow/
│   │   └── jar-resources/
│   ├── index.html                 ← Punctul de intrare pentru interfața web
│   ├── vaadin.ts                  ← Configurare TypeScript pentru Vaadin
│   └── vaadin-featureflags.js     ← Flag-uri pentru funcționalități Vaadin
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── model/             ← Clasele tale de date (Client, Kart, Vanzare, etc.)
│   │       └── org.comenzi.model/ ← Interfața grafică și vizualizările (MainLayout, FormKartView, App, etc.)
│   └── test/                      ← (Opțional) Aici vor sta testele unitare
├── target/                        ← Codul compilat de Maven (Ignorat in Git)
├── .gitignore                     ← Lista fișierelor pe care Git nu trebuie să le urmărească
├── pom.xml                        ← Fișierul de configurare Maven (dependențe)
└── .idea/                         ← Setările specifice IntelliJ IDEA (Ignorat in Git)



```

