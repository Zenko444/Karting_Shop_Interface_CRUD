package org.comenzi.model;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Kart;

@Route(value = "kart", layout = MainLayout.class)
public class FormKartView extends VerticalLayout implements HasUrlParameter<Integer> {

    private Integer kartId;
    private TextField producatorField = new TextField("Producător (Info)");
    private TextField denumireField = new TextField("Model / Denumire");
    private NumberField pretField = new NumberField("Preț (RON)");
    private TextField motorField = new TextField("Tip Motor");
    private TextField categorieField = new TextField("Categorie");

    private Button btnSave = new Button("Salvează");
    private Button btnDelete = new Button("Șterge");

    public FormKartView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("background", "transparent");

        // --- 1. CONFIGURARE CONTAINER (CHENARUL) ---
        VerticalLayout container = new VerticalLayout();
        container.setMaxWidth("600px");
        container.getStyle().set("background-color", "rgba(0, 0, 0, 0.75)"); // Negru transparent
        container.getStyle().set("border-radius", "15px");
        container.getStyle().set("padding", "40px");
        container.setAlignItems(Alignment.CENTER);

        H2 titlu = new H2("Editare Kart");
        titlu.getStyle().set("color", "white");

        FormLayout form = new FormLayout();
        form.setWidthFull();

        producatorField.setReadOnly(true); // Doar informativ
        form.add(producatorField, denumireField, pretField, motorField, categorieField);

        // Stil field-uri pentru vizibilitate (Text Alb)
        styleField(producatorField); styleField(denumireField);
        styleField(pretField); styleField(motorField); styleField(categorieField);

        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(e -> salveaza());

        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(e -> sterge());

        RouterLink backLink = new RouterLink("Înapoi", NavigableGridKartView.class);
        backLink.getStyle().set("color", "#cccccc");
        backLink.getStyle().set("font-weight", "bold");

        HorizontalLayout actions = new HorizontalLayout(btnSave, btnDelete, backLink);
        actions.setAlignItems(Alignment.CENTER);
        actions.getStyle().set("margin-top", "20px");

        container.add(titlu, form, actions);
        add(container);
    }

    private void styleField(Component field) {
        field.getElement().getStyle().set("--vaadin-input-field-label-color", "#ffffff");
        field.getElement().getStyle().set("--vaadin-input-field-value-color", "#ffffff");
        field.getElement().getStyle().set("--vaadin-text-field-background", "rgba(255, 255, 255, 0.1)");
    }

    @Override
    public void setParameter(BeforeEvent event, Integer id) {
        this.kartId = id;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KartingJPA");
        EntityManager em = emf.createEntityManager();
        try {
            Kart k = em.find(Kart.class, id);
            if (k != null) {
                producatorField.setValue(k.getProducator() != null ? k.getProducator().getNume() : "Nespecificat");
                denumireField.setValue(k.getDenumire());
                pretField.setValue(k.getPret());
                motorField.setValue(k.getTipMotor());
                categorieField.setValue(k.getCategorie());
            }
        } finally {
            em.close();
            emf.close();
        }
    }

    private void salveaza() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KartingJPA");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Kart k = em.find(Kart.class, kartId);
            if (k != null) {
                k.setDenumire(denumireField.getValue());
                k.setPret(pretField.getValue());
                k.setTipMotor(motorField.getValue());
                k.setCategorie(categorieField.getValue());

                em.merge(k);
                em.getTransaction().commit();
                Notification.show("Salvat!");
            }
        } finally {
            em.close();
            emf.close();
        }
    }

    private void sterge() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KartingJPA");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Kart k = em.find(Kart.class, kartId);
            if (k != null) {
                em.remove(k);
                em.getTransaction().commit();
                Notification.show("Kart șters!");
                getUI().ifPresent(ui -> ui.navigate(NavigableGridKartView.class));
            }
        } catch (Exception e) {
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            Notification.show("Eroare la ștergere.");
        } finally {
            em.close();
            emf.close();
        }
    }
}