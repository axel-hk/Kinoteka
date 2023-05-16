package com.example.kinoteka.views;

import com.example.kinoteka.dao.entities.StudiosEntity;
import com.example.kinoteka.dao.repositories.RepositoryStudios;
import com.example.kinoteka.security.SecurityService;
import com.example.kinoteka.ui.ClearableTextField;
import com.example.kinoteka.ui.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value="studios", layout = MainLayout.class)
@PageTitle("Студия | КиноТека")
@PermitAll
public class StudioView extends VerticalLayout {
    private final Grid<StudiosEntity> grid = new Grid<>();
    private ListDataProvider<StudiosEntity> dataProvider;
    private final FormLayout editLayout = new FormLayout();
    private final ClearableTextField search = new ClearableTextField();
    private Button saveButton, deleteButton;
    private final Binder<StudiosEntity> binder = new Binder<>();

    private  StudiosEntity studiosEntity;

    private final RepositoryStudios studios;

    private final SecurityService securityService;

    @Autowired
    public StudioView(SecurityService securityService, RepositoryStudios studios) {
        this.studios = studios;
        this.securityService = securityService;
    }

    @PostConstruct
    public void init(){
        setSizeFull();

        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(StudiosEntity::getStudioId).setHeader("StudioId").setSortable(true);
        grid.addColumn(StudiosEntity::getName).setHeader("Name").setSortable(true);
        grid.addColumn(StudiosEntity::getCountry).setHeader("Country").setSortable(true);

        update();


        TextField nameField = new TextField("Студия");
        TextField countryField = new TextField("Страна");

        binder.forField(nameField)
                .asRequired()
                .bind(StudiosEntity::getName, StudiosEntity::setName);

        binder.forField(countryField)
                .asRequired()
                .bind(StudiosEntity::getCountry, StudiosEntity::setCountry);

        HorizontalLayout editBarLayout = new HorizontalLayout();
        saveButton = new Button("Сохранить", this::save);
        deleteButton = new Button("Удалить", this::delete);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        editBarLayout.add(saveButton, deleteButton);

        editLayout.setEnabled(false);
        editLayout.add(nameField, countryField, editBarLayout);
        editLayout.setMaxWidth("15%");


        GridMultiSelectionModel<StudiosEntity> multiSelectionModel = (GridMultiSelectionModel<StudiosEntity>) grid.getSelectionModel();
        multiSelectionModel.addMultiSelectionListener(multiSelectionEvent -> {
            for (StudiosEntity StudiosEntity: multiSelectionEvent.getAllSelectedItems()){
                binder.readBean(StudiosEntity);
            }
            editLayout.setEnabled(true);
            saveButton.setEnabled(false);
            deleteButton.setEnabled(true);
        });

        binder.addValueChangeListener(event -> saveButton.setEnabled(binder.isValid()));

        Div gridLayout = new Div();
        gridLayout.setSizeFull();
        gridLayout.add(grid);

        HorizontalLayout contentLayout = new HorizontalLayout();
        contentLayout.setSizeFull();
        contentLayout.add(gridLayout);
        if(securityService.getAllUserRoles().contains("admin"))  contentLayout.add(editLayout);

        add(getTBarLayout(), contentLayout);
        setFlexGrow(1, contentLayout);
    }

    private HorizontalLayout getTBarLayout() {
        HorizontalLayout tBarLayout = new HorizontalLayout();
        tBarLayout.setWidth("100%");
        search.getTextField().setId("rcr-user-search");
        search.setPlaceholder("Поиск");
        search.addValueChangeListener(this::searchFilter);

        Button addNewButton = new Button("Новый Жанр", VaadinIcon.PLUS.create());
        addNewButton.addClickListener(this::newItem);

        tBarLayout.add(search);
        tBarLayout.setFlexGrow(1, search);
        tBarLayout.add(addNewButton);
        return tBarLayout;
    }

    private void newItem(ClickEvent<Button> buttonClickEvent) {
        studiosEntity = new StudiosEntity();
        binder.readBean(studiosEntity);
        editLayout.setEnabled(true);
        saveButton.setEnabled(true);
        deleteButton.setEnabled(false);
    }

    private void searchFilter(HasValue.ValueChangeEvent<? extends String> valueChangeEvent) {
        dataProvider.setFilter(StudiosEntity::getName, item -> item.toLowerCase().contains(valueChangeEvent.getValue().toLowerCase()));
    }

    private void delete(ClickEvent<Button> buttonClickEvent) {
        studios.delete(studiosEntity);
        update();
    }

    private void save(ClickEvent<Button> buttonClickEvent) {
        if(binder.isValid()){
            try{
                binder.writeBean(studiosEntity);
                studiosEntity = studios.save(studiosEntity);
                if(dataProvider.getItems().contains( studiosEntity)){
                    dataProvider.refreshItem(studiosEntity);
                } else {
                    update();
                    search.clear();
                }
            }
            catch (ValidationException e){
                e.printStackTrace();
            }
        }

    }

    private void update(){
        dataProvider = new ListDataProvider<>(studios.findAllByOrderByNameAsc());
        grid.setDataProvider(dataProvider);
    }
}
