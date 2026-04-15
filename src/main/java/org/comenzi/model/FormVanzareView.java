package org.comenzi.model;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.ArticolVanzare;
import model.Vanzare;

import java.time.LocalDate;

@Route(value = "vanzare-detalii", layout = MainLayout.class)
public class FormVanzareView extends VerticalLayout implements HasUrlParameter<Long> {

    private Long vanzareId;
    private TextField clientField = new TextField("Client");
    private TextField dataField = new TextField("Data (YYYY-MM-DD)");
    private TextField totalField = new TextField("Total (RON)");
    private Grid<ArticolVanzare> articoleGrid = new Grid<>(ArticolVanzare.class);

    private Button btnSave = new Button("Salvează Data");
    private Button btnDelete = new Button("Șterge Vânzarea");

    public FormVanzareView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("background", "transparent");

        // --- Container Chenar ---
        VerticalLayout container = new VerticalLayout();
        container.setMaxWidth("800px"); // Mai lat pentru tabel
        container.getStyle().set("background-color", "rgba(0, 0, 0, 0.75)");
        container.getStyle().set("border-radius", "15px");
        container.getStyle().set("padding", "40px");
        container.setAlignItems(Alignment.CENTER);

        H2 titlu = new H2("Detalii și Editare Vânzare");
        titlu.getStyle().set("color", "white");

        FormLayout form = new FormLayout();
        form.setWidthFull();

        clientField.setReadOnly(true);
        totalField.setReadOnly(true);

        form.add(clientField, dataField, totalField);

        // Stilizare Albă
        styleField(clientField);
        styleField(dataField);
        styleField(totalField);

        // Grid Articole
        articoleGrid.removeAllColumns();
        articoleGrid.addColumn(a -> a.getProdus().getDenumire()).setHeader("Produs");
        articoleGrid.addColumn(ArticolVanzare::getCantitate).setHeader("Cantitate");
        articoleGrid.addColumn(ArticolVanzare::getValoare).setHeader("Valoare");
        articoleGrid.setHeight("250px"); // Înălțime fixă pentru gridul mic
        // Opțional: stilizare grid (colțuri)
        articoleGrid.getStyle().set("border-radius", "10px");

        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(e -> salveaza());

        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(e -> sterge());

        RouterLink backLink = new RouterLink("Înapoi", NavigableGridVanzariView.class);
        backLink.getStyle().set("color", "#cccccc");
        backLink.getStyle().set("font-weight", "bold");

        HorizontalLayout actions = new HorizontalLayout(btnSave, btnDelete, backLink);
        actions.setAlignItems(Alignment.CENTER);
        actions.getStyle().set("margin-top", "20px");

        container.add(titlu, form, articoleGrid, actions);
        add(container);
    }

    private void styleField(Component field) {
        field.getElement().getStyle().set("--vaadin-input-field-label-color", "#ffffff");
        field.getElement().getStyle().set("--vaadin-input-field-value-color", "#ffffff");
        field.getElement().getStyle().set("--vaadin-text-field-background", "rgba(255, 255, 255, 0.1)");
    }

    @Override
    public void setParameter(BeforeEvent event, Long id) {
        this.vanzareId = id;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KartingJPA");
        EntityManager em = emf.createEntityManager();

        try {
            Vanzare vanzare = em.find(Vanzare.class, id);

            if (vanzare != null) {
                if (vanzare.getClient() != null) {
                    clientField.setValue(vanzare.getClient().getNume());
                } else {
                    clientField.setValue("-");
                }
                dataField.setValue(vanzare.getData().toString());
                totalField.setValue(String.valueOf(vanzare.getTotal()));
                articoleGrid.setItems(vanzare.getArticole());
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
            Vanzare v = em.find(Vanzare.class, vanzareId);

            if (v != null) {
                try {
                    LocalDate dataNoua = LocalDate.parse(dataField.getValue());
                    v.setData(dataNoua);

                    em.merge(v);
                    em.getTransaction().commit();
                    Notification.show("Data vânzării actualizată!");
                } catch (Exception ex) {
                    Notification.show("Formatul datei este incorect! Folosește YYYY-MM-DD");
                }
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            Notification.show("Eroare la salvare: " + e.getMessage());
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
            Vanzare v = em.find(Vanzare.class, vanzareId);
            if (v != null) {
                em.remove(v);
                em.getTransaction().commit();
                Notification.show("Vânzare ștearsă!");
                getUI().ifPresent(ui -> ui.navigate(NavigableGridVanzariView.class));
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            Notification.show("Eroare la ștergere.");
        } finally {
            em.close();
            emf.close();
        }
    }
}