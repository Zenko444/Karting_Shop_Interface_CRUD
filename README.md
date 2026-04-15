## Karting Shop Interface with CRUD - Romanian Language

The project was done using Gemini and OOP knowledge along with Vaadin plug-in and with a secured database in PostgreSQL. In persistence you can find the log-in area where you can connect your database using your profile and password.
By default the project is called KartingJPA, but you can change that. karting_db is the database, also can be changed depending on your preferences.

## Project Structure

```

Karting_Proiect/
├── frontend/
│   ├── generated/                 ← Generated code by Vaadin automatically
│   │   ├── flow/
│   │   └── jar-resources/
│   ├── index.html                 ← Entry point for the web interface
│   ├── vaadin.ts                  ← Configuration of TypeScript for Vaadin
│   └── vaadin-featureflags.js     ← Flags for Vaadin functionalities
├── src/
│   ├── main/
│       └── java/
│           ├── model/             ← Data classes (Client, Kart, Vanzare, etc.)
│           └── org.comenzi.model/ ← Interface and elements (MainLayout, FormKartView, App, etc.)
├── target/                        ← Compiled code by Maven
├── .gitignore                     ← List of files ignored by Git
├── pom.xml                        ← File for Maven configurations (dependencies)
└── .idea/                         ← Specific settings for IntelliJ IDEA which are ignored by Git



```

