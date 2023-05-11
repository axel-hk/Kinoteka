package com.example.kinoteka.views;

import com.example.kinoteka.dto.entities.MoviesEntity;
import com.example.kinoteka.dto.entities.SessionsEntity;
import com.example.kinoteka.dto.repositories.RepositoryMovies;
import com.example.kinoteka.dto.repositories.RepositorySessions;
import com.example.kinoteka.security.SecurityService;
import com.example.kinoteka.ui.ClearableTextField;
import com.example.kinoteka.ui.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@Route(value="Sessions", layout = MainLayout.class)
@PageTitle("Сеанс | КиноТека")
@PermitAll
public class SessionView extends VerticalLayout {
    private final Grid<SessionsEntity> grid = new Grid<>();
    private ListDataProvider<SessionsEntity> dataProvider;
    private final FormLayout editLayout = new FormLayout();
    private final ClearableTextField search = new ClearableTextField();
    private Button saveButton, deleteButton;
    private final Binder<SessionsEntity> binder = new Binder<>();
    private final RepositoryMovies movies;
    
    private final RepositorySessions sessions;


    private  SessionsEntity sessionsEntity;

    private final SecurityService securityService;


    @Autowired
    public SessionView(RepositoryMovies movies, RepositorySessions sessions, SecurityService securityService) {
        this.movies = movies;
        this.sessions = sessions;
        this.securityService = securityService;
    }

    @PostConstruct
    public void init(){
        setSizeFull();

        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(SessionsEntity::getSessionId).setHeader("SessionId").setSortable(true);
        grid.addColumn(SessionsEntity::getMovieId).setHeader("MovieId").setSortable(true);
        grid.addColumn(SessionsEntity::getStartTime).setHeader("StartTime").setSortable(true);
        grid.addColumn(SessionsEntity::getSeatsTotal).setHeader("SeatsTotal").setSortable(true);
        grid.addColumn(SessionsEntity::getSeatsAvailable).setHeader("SeatsAvailable").setSortable(true);

        update();


        ComboBox<Integer> moviesBox = new ComboBox<>("Фильмы");
        moviesBox.setItems(movies.findAll().stream().map(MoviesEntity::getMovieId).collect(Collectors.toSet()));

        DateTimePicker datePicker = new DateTimePicker("Дата и время");


        IntegerField total = new IntegerField("Общее количество мест");
        IntegerField available = new IntegerField("Свободное количество мест");

        available.addInputListener(inputEvent -> available.setMax(total.getValue()));



        binder.forField(datePicker)
                .asRequired()
                .bind(SessionsEntity::getStartTime, SessionsEntity::setStartTime);

        binder.forField(moviesBox)
                .asRequired()
                .bind(SessionsEntity::getMovieId, SessionsEntity::setMovieId);

        binder.forField(total)
                .asRequired()
                .bind(SessionsEntity::getSeatsTotal, SessionsEntity::setSeatsTotal);

        binder.forField(available)
                .asRequired()
                .bind(SessionsEntity::getSeatsAvailable, SessionsEntity::setSeatsAvailable);



        HorizontalLayout editBarLayout = new HorizontalLayout();
        saveButton = new Button("Сохранить", this::save);
        deleteButton = new Button("Удалить", this::delete);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        editBarLayout.add(saveButton, deleteButton);

        editLayout.setEnabled(false);
        editLayout.setMaxWidth("15%");
        editLayout.add(
                moviesBox,
                datePicker,
                total,
                available,
                editBarLayout);


        GridMultiSelectionModel<SessionsEntity> multiSelectionModel = (GridMultiSelectionModel<SessionsEntity>) grid.getSelectionModel();
        multiSelectionModel.addMultiSelectionListener(multiSelectionEvent -> {
            for (SessionsEntity SessionsEntity: multiSelectionEvent.getAllSelectedItems()){
                binder.readBean(SessionsEntity);
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

        Button addNewButton = new Button("Новая Сессия", VaadinIcon.PLUS.create());
        addNewButton.addClickListener(this::newItem);

        tBarLayout.add(search);
        tBarLayout.setFlexGrow(1, search);
        tBarLayout.add(addNewButton);
        return tBarLayout;
    }

    private void newItem(ClickEvent<Button> buttonClickEvent) {
        sessionsEntity = new SessionsEntity();
        binder.readBean(sessionsEntity);
        editLayout.setEnabled(true);
        saveButton.setEnabled(true);
        deleteButton.setEnabled(false);
    }

    private void searchFilter(HasValue.ValueChangeEvent<? extends String> valueChangeEvent) {
        dataProvider.setFilter(SessionsEntity::getMovieId,
                item -> item.equals( Integer.parseInt(valueChangeEvent.getValue())));
    }

    private void delete(ClickEvent<Button> buttonClickEvent) {
        sessions.delete(sessionsEntity);
        update();
    }

    private void save(ClickEvent<Button> buttonClickEvent) {
        if(binder.isValid()){
            try{
                binder.writeBean(sessionsEntity);
                sessionsEntity = sessions.save(sessionsEntity);
                if(dataProvider.getItems().contains(sessionsEntity)){
                    dataProvider.refreshItem(sessionsEntity);
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
        dataProvider = new ListDataProvider<>(sessions.findAllByOrderBySessionIdAsc());
        grid.setDataProvider(dataProvider);
    }

}