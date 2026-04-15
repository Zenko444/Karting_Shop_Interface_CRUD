package org.comenzi.model;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Competitie;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "competitii", layout = MainLayout.class)
public class NavigableGridCompetitiiView extends VerticalLayout {

    private Grid<Competitie> grid = new Grid<>(Competitie.class);
    private TextField searchField = new TextField();
    private List<Competitie> allItems;

    public NavigableGridCompetitiiView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        getStyle().set("background", "transparent");

        Button btnAdd = new Button("Adaugă Competiție");
        btnAdd.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnAdd.addClickListener(e -> creeazaSiNavigheaza());

        searchField.setPlaceholder("Caută ID sau Nume...");
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(e -> filtreazaGrid());

        searchField.getStyle().set("--vaadin-input-field-value-color", "#ffffff");
        searchField.getStyle().set("--vaadin-input-field-placeholder-color", "#cccccc");
        searchField.getStyle().set("--vaadin-text-field-background", "rgba(255, 255, 255, 0.1)");
        searchField.getStyle().set("color", "white");

        HorizontalLayout toolbar = new HorizontalLayout(searchField, btnAdd);
        toolbar.setWidthFull();

        configureGrid();
        fetchData();
        grid.getStyle().set("border-radius", "10px");

        VerticalLayout contentBox = new VerticalLayout(toolbar, grid);
        contentBox.setMaxWidth("1200px");
        contentBox.setHeight("90%");
        contentBox.getStyle().set("background-color", "rgba(0, 0, 0, 0.6)");
        contentBox.getStyle().set("border-radius", "15px");
        contentBox.getStyle().set("padding", "20px");
        contentBox.getStyle().set("box-shadow", "0 4px 15px rgba(0,0,0,0.5)");

        add(contentBox);
    }

    private void configureGrid() {
        grid.removeAllColumns();
        grid.addColumn(Competitie::getId).setHeader("ID").setWidth("80px").setFlexGrow(0);
        grid.addColumn(Competitie::getNume).setHeader("Eveniment");
        grid.addColumn(Competitie::getCircuit).setHeader("Circuit");
        grid.addColumn(Competitie::getDataStart).setHeader("Data");

        grid.addComponentColumn(item -> {
            Button btnEdit = new Button("Edit");
            btnEdit.addThemeVariants(ButtonVariant.LUMO_SMALL);
            btnEdit.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(FormCompetitiiView.class, item.getId())));
            Button btnDelete = new Button("Șterge");
            btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
            btnDelete.addClickListener(e -> stergeItem(item));
            return new HorizontalLayout(btnEdit, btnDelete);
        }).setHeader("Acțiuni");
    }

    private void stergeItem(Competitie item) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KartingJPA");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Competitie c = em.find(Competitie.class, item.getId());
            if (c != null) {
                em.remove(c);
                em.getTransaction().commit();
                Notification.show("Șters!");
                fetchData();
            }
        } catch (Exception e) {
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            Notification.show("Eroare la ștergere.");
        } finally {
            em.close();
            emf.close();
        }
    }

    private void fetchData() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KartingJPA");
        EntityManager em = emf.createEntityManager();
        try {
            allItems = em.createQuery("SELECT c FROM Competitie c", Competitie.class).getResultList();
            filtreazaGrid();
        } finally {
            em.close();
            emf.close();
        }
    }

    private void filtreazaGrid() {
        String val = searchField.getValue();
        if (allItems != null) {
            if (val == null || val.isEmpty()) {
                grid.setItems(allItems);
            } else {
                List<Competitie> filtered = allItems.stream()
                        .filter(c -> String.valueOf(c.getId()).contains(val) || c.getNume().toLowerCase().contains(val.toLowerCase()))
                        .collect(Collectors.toList());
                grid.setItems(filtered);
            }
        }
    }

    private void creeazaSiNavigheaza() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KartingJPA");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Competitie c = new Competitie("Competiție Nouă", "-", LocalDate.now(), 0, "-");
            em.persist(c);
            em.getTransaction().commit();
            getUI().ifPresent(ui -> ui.navigate(FormCompetitiiView.class, c.getId()));
        } finally {
            em.close();
            emf.close();
        }
    }
}