package com.example.kinoteka.views;

import com.example.kinoteka.dao.entities.*;
import com.example.kinoteka.dao.repositories.*;
import com.example.kinoteka.security.SecurityService;
import com.example.kinoteka.ui.ClearableTextField;
import com.example.kinoteka.ui.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
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

import java.util.stream.Collectors;

@Route(value="Participants", layout = MainLayout.class)
@PageTitle("Участники | КиноТека")
@PermitAll
public class ParticipantsView extends VerticalLayout {
    private final Grid<ParticipantsEntity> grid = new Grid<>();
    private ListDataProvider<ParticipantsEntity> dataProvider;
    private final FormLayout editLayout = new FormLayout();
    private final ClearableTextField search = new ClearableTextField();
    private Button saveButton, deleteButton;
    private final Binder<ParticipantsEntity> binder = new Binder<>();
    private final RepositoryMovies movies;

    private final RepositoryRole role;
    private final RepositoryParticipants participants;


    private  ParticipantsEntity participantsEntity;

    private final SecurityService securityService;

    @Autowired
    public ParticipantsView(RepositoryMovies movies, RepositoryRole role, RepositoryParticipants participants, SecurityService securityService) {
        this.movies = movies;
        this.role = role;
        this.participants = participants;
        this.securityService = securityService;
    }

    @PostConstruct
    public void init(){
        setSizeFull();

        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addColumn(ParticipantsEntity::getParticipantId).setHeader("ParticipantId").setSortable(true);
        grid.addColumn(ParticipantsEntity::getFullName).setHeader("FullName").setSortable(true);
        grid.addColumn(ParticipantsEntity::getBirthDate).setHeader("BirthDate").setSortable(true);
        grid.addColumn(ParticipantsEntity::getMovieId).setHeader("MovieId").setSortable(true);
        grid.addColumn(ParticipantsEntity::getRoleId).setHeader("RoleId").setSortable(true);

        update();

        TextField fullNameField = new TextField("Полное имя");

        DatePicker datePicker = new DatePicker("Дата рождения");

        ComboBox<Integer> moviesBox = new ComboBox<>("Фильмы");
        moviesBox.setItems(movies.findAll().stream().map(MoviesEntity::getMovieId).collect(Collectors.toSet()));


        ComboBox<Integer> roleBox = new ComboBox<>("Роль");
        roleBox.setItems(role.findAll().stream().map(RoleEntity::getRoleId).collect(Collectors.toSet()));


        binder.forField(fullNameField)
                .asRequired()
                .bind(ParticipantsEntity::getFullName, ParticipantsEntity::setFullName);

        binder.forField(datePicker)
                .asRequired()
                .bind(ParticipantsEntity::getBirthDate, ParticipantsEntity::setBirthDate);

        binder.forField(moviesBox)
                .asRequired()
                .bind(ParticipantsEntity::getMovieId, ParticipantsEntity::setMovieId);

        binder.forField(roleBox)
                .asRequired()
                .bind(ParticipantsEntity::getRoleId, ParticipantsEntity::setRoleId);



        HorizontalLayout editBarLayout = new HorizontalLayout();
        saveButton = new Button("Сохранить", this::save);
        deleteButton = new Button("Удалить", this::delete);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        editBarLayout.add(saveButton, deleteButton);

        editLayout.setEnabled(false);
        editLayout.setMaxWidth("15%");
        editLayout.add(fullNameField,
                datePicker,
                moviesBox,
                roleBox,
                editBarLayout);


        GridSingleSelectionModel<ParticipantsEntity> singleSelectionModel = (GridSingleSelectionModel<ParticipantsEntity>)  grid.getSelectionModel();
        singleSelectionModel.addSingleSelectionListener(event -> {
            participantsEntity = event.getSelectedItem().orElse(null);
            binder.readBean(participantsEntity);
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

        Button addNewButton = new Button("Новый Участник", VaadinIcon.PLUS.create());
        addNewButton.addClickListener(this::newItem);

        tBarLayout.add(search);
        tBarLayout.setFlexGrow(1, search);
        tBarLayout.add(addNewButton);
        return tBarLayout;
    }

    private void newItem(ClickEvent<Button> buttonClickEvent) {
        participantsEntity = new ParticipantsEntity();
        binder.readBean(participantsEntity);
        editLayout.setEnabled(true);
        saveButton.setEnabled(true);
        deleteButton.setEnabled(false);
    }

    private void searchFilter(HasValue.ValueChangeEvent<? extends String> valueChangeEvent) {
        dataProvider.setFilter(ParticipantsEntity::getFullName, item -> item.toLowerCase().contains(valueChangeEvent.getValue().toLowerCase()));
    }

    private void delete(ClickEvent<Button> buttonClickEvent) {
        participants.delete(participantsEntity);
        update();
    }

    private void save(ClickEvent<Button> buttonClickEvent) {
        if(binder.isValid()){
            try{
                binder.writeBean(participantsEntity);
                participantsEntity = participants.save(participantsEntity);
                if(dataProvider.getItems().contains(participantsEntity)){
                    dataProvider.refreshItem(participantsEntity);
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
        dataProvider = new ListDataProvider<>(participants.findAllByOrderByFullNameAsc());
        grid.setDataProvider(dataProvider);
    }

}
    

