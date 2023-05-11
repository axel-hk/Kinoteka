package com.example.kinoteka.views;

import com.example.kinoteka.dto.entities.GenresEntity;
import com.example.kinoteka.dto.repositories.RepositoryGenres;
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



@Route(value="Genre", layout = MainLayout.class)
@PageTitle("Жанры | КиноТека")
@PermitAll
public class GenresView extends VerticalLayout {
    private final Grid<GenresEntity> grid = new Grid<>(GenresEntity.class);
    private ListDataProvider<GenresEntity> dataProvider;
    private final FormLayout editLayout = new FormLayout();
    private final ClearableTextField search = new ClearableTextField();
    private Button saveButton, deleteButton;
    private final Binder<GenresEntity> binder = new Binder<>();
    private final RepositoryGenres genres;

    private  GenresEntity genresEntity;

    private final SecurityService securityService;

    @Autowired
    public GenresView(RepositoryGenres genres, SecurityService securityService) {
        this.genres = genres;
        this.securityService = securityService;
    }

    @PostConstruct
    public void init(){
        setSizeFull();

        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.removeColumnByKey("moviesByGenreId");

        update();


        TextField nameField = new TextField("Жанр");

        binder.forField(nameField)
                .asRequired()
                .bind(GenresEntity::getName, GenresEntity::setName);

        HorizontalLayout editBarLayout = new HorizontalLayout();
        saveButton = new Button("Сохранить", this::save);
        deleteButton = new Button("Удалить", this::delete);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        editBarLayout.add(saveButton, deleteButton);

        editLayout.setEnabled(false);
        editLayout.add(nameField, editBarLayout);
        editLayout.setMaxWidth("15%");


        GridMultiSelectionModel<GenresEntity> multiSelectionModel = (GridMultiSelectionModel<GenresEntity>) grid.getSelectionModel();
        multiSelectionModel.addMultiSelectionListener(multiSelectionEvent -> {
            for (GenresEntity genresEntity: multiSelectionEvent.getAllSelectedItems()){
                binder.readBean(genresEntity);
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
        genresEntity = new GenresEntity();
        binder.readBean(genresEntity);
        editLayout.setEnabled(true);
        saveButton.setEnabled(true);
        deleteButton.setEnabled(false);
    }

    private void searchFilter(HasValue.ValueChangeEvent<? extends String> valueChangeEvent) {
        dataProvider.setFilter(GenresEntity::getName, item -> item.toLowerCase().contains(valueChangeEvent.getValue().toLowerCase()));
    }

    private void delete(ClickEvent<Button> buttonClickEvent) {
        genres.delete(genresEntity);
        update();
    }

    private void save(ClickEvent<Button> buttonClickEvent) {
        if(binder.isValid()){
            try{
                binder.writeBean(genresEntity);
                genresEntity = genres.save(genresEntity);
                if(dataProvider.getItems().contains( genresEntity)){
                    dataProvider.refreshItem(genresEntity);
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
        dataProvider = new ListDataProvider<>(genres.findAllByOrderByNameAsc());
        grid.setDataProvider(dataProvider);
    }

}
