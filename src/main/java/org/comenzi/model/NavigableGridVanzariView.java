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
import com.vaadin.flow.router.RouteAlias;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Vanzare;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "vanzari", layout = MainLayout.class)
public class NavigableGridVanzariView extends VerticalLayout {

    private Grid<Vanzare> grid = new Grid<>(Vanzare.class);
    private TextField searchField = new TextField();
    private List<Vanzare> allItems;

    public NavigableGridVanzariView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        getStyle().set("background", "transparent");

        Button btnAdd = new Button("Vânzare Nouă");
        btnAdd.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnAdd.addClickListener(e -> creeazaSiNavigheaza());

        searchField.setPlaceholder("Caută ID Vânzare...");
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
        grid.addColumn(Vanzare::getId).setHeader("ID").setWidth("80px").setFlexGrow(0);
        grid.addColumn(v -> v.getData().toString()).setHeader("Data");
        grid.addColumn(v -> v.getClient() != null ? v.getClient().getNume() : "Fără Client").setHeader("Client");
        grid.addColumn(Vanzare::getTotal).setHeader("Total (RON)");

        grid.addComponentColumn(item -> {
            Button btnEdit = new Button("Edit");
            btnEdit.addThemeVariants(ButtonVariant.LUMO_SMALL);
            btnEdit.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(FormVanzareView.class, item.getId())));
            Button btnDelete = new Button("Șterge");
            btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
            btnDelete.addClickListener(e -> stergeItem(item));
            return new HorizontalLayout(btnEdit, btnDelete);
        }).setHeader("Acțiuni");
    }

    private void stergeItem(Vanzare item) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KartingJPA");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Vanzare v = em.find(Vanzare.class, item.getId());
            if (v != null) {
                em.remove(v);
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
            allItems = em.createQuery("SELECT v FROM Vanzare v", Vanzare.class).getResultList();
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
                List<Vanzare> filtered = allItems.stream()
                        .filter(v -> String.valueOf(v.getId()).contains(val))
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
            Vanzare v = new Vanzare();
            v.setData(LocalDate.now());
            em.persist(v);
            em.getTransaction().commit();
            getUI().ifPresent(ui -> ui.navigate(FormVanzareView.class, v.getId()));
            Notification.show("Vânzare inițiată!");
        } catch (Exception e) {
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            Notification.show("Eroare: " + e.getMessage());
        } finally {
            em.close();
            emf.close();
        }
    }
}