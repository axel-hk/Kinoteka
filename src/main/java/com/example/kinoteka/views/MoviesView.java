package com.example.kinoteka.views;

import com.example.kinoteka.dao.entities.GenresEntity;
import com.example.kinoteka.dao.entities.MoviesEntity;
import com.example.kinoteka.dao.entities.StudiosEntity;
import com.example.kinoteka.dao.repositories.RepositoryGenres;
import com.example.kinoteka.dao.repositories.RepositoryMovies;
import com.example.kinoteka.dao.repositories.RepositoryStudios;
import com.example.kinoteka.security.SecurityService;
import com.example.kinoteka.ui.ClearableTextField;
import com.example.kinoteka.ui.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Route(value="Movie", layout = MainLayout.class)
@PageTitle("Фильмы | КиноТека")
@PermitAll
public class MoviesView extends VerticalLayout {
    private final Grid<MoviesEntity> grid = new Grid<>();
    private ListDataProvider<MoviesEntity> dataProvider;
    private final FormLayout editLayout = new FormLayout();
    private final ClearableTextField search = new ClearableTextField();
    private Button saveButton, deleteButton;
    private final Binder<MoviesEntity> binder = new Binder<>();
    private final RepositoryMovies movies;

    private final RepositoryStudios studios;

    private final RepositoryGenres genres;


    private  MoviesEntity moviesEntity;

    private final SecurityService securityService;

    @Autowired
    public MoviesView(RepositoryMovies movies, RepositoryStudios studios, RepositoryGenres genres, SecurityService securityService) {
        this.movies = movies;
        this.studios = studios;
        this.genres = genres;
        this.securityService = securityService;
    }

    @PostConstruct
    public void init(){
        setSizeFull();

        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addColumn(MoviesEntity::getMovieId).setHeader("MovieId").setSortable(true);
        grid.addColumn(MoviesEntity::getTitle).setHeader("Title").setSortable(true);
        grid.addColumn(MoviesEntity::getStudioId).setHeader("StudioId").setSortable(true);
        grid.addColumn(MoviesEntity::getYear).setHeader("Year").setSortable(true);
        grid.addColumn(MoviesEntity::getCountry).setHeader("Country").setSortable(true);
        grid.addColumn(MoviesEntity::getDuration).setHeader("Duration").setSortable(true);
        grid.addColumn(MoviesEntity::getGenreId).setHeader("GenreId").setSortable(true);
        grid.addColumn(MoviesEntity::getRating).setHeader("Rating").setSortable(true);


        update();


        TextField titleField = new TextField("Название");

        ComboBox<Integer> studioBox = new ComboBox<>("Студия");
        studioBox.setItems(studios.findAll().stream().map(StudiosEntity::getStudioId).collect(Collectors.toSet()));

        IntegerField year = new IntegerField("Год выпуска");
        year.setMin(1930);
        year.setMax(LocalDate.now().getYear());

        TextField countryField = new TextField("Страна");

        IntegerField duration = new IntegerField("Длительность");
        duration.setMin(1);
        duration.setMax(300);

        ComboBox<Integer> genreBox = new ComboBox<>("Жанры");
        genreBox.setItems(genres.findAll().stream().map(GenresEntity::getGenreId).collect(Collectors.toSet()));

        IntegerField rating = new IntegerField("Рейтинг");
        rating.setMin(0);
        rating.setMax(10);

        binder.forField(titleField)
                .asRequired()
                .bind(MoviesEntity::getTitle, MoviesEntity::setTitle);

        binder.forField(studioBox)
                .asRequired()
                .bind(MoviesEntity::getStudioId, MoviesEntity::setStudioId);

        binder.forField(year)
                .asRequired()
                .bind(MoviesEntity::getYear, MoviesEntity::setYear);

        binder.forField(countryField)
                .asRequired()
                .bind(MoviesEntity::getCountry, MoviesEntity::setCountry);

        binder.forField(duration)
                .asRequired()
                .bind(MoviesEntity::getDuration, MoviesEntity::setDuration);

        binder.forField(genreBox)
                .asRequired()
                .bind(MoviesEntity::getGenreId, MoviesEntity::setGenreId);

        binder.forField(rating)
                .asRequired()
                .bind(MoviesEntity::getRating, MoviesEntity::setRating);

        HorizontalLayout editBarLayout = new HorizontalLayout();
        saveButton = new Button("Сохранить", this::save);
        deleteButton = new Button("Удалить", this::delete);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        editBarLayout.add(saveButton, deleteButton);

        editLayout.setEnabled(false);
        editLayout.setMaxWidth("15%");
        editLayout.add(titleField,
                studioBox,
                year,
                countryField,
                duration,
                genreBox,
                rating,
                editBarLayout);


        GridSingleSelectionModel<MoviesEntity> singleSelectionModel = (GridSingleSelectionModel<MoviesEntity>)  grid.getSelectionModel();
        singleSelectionModel.addSingleSelectionListener(event -> {
            moviesEntity = event.getSelectedItem().orElse(null);
            binder.readBean(moviesEntity);
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

        Button addNewButton = new Button("Новый Фильм", VaadinIcon.PLUS.create());
        addNewButton.addClickListener(this::newItem);

        tBarLayout.add(search);
        tBarLayout.setFlexGrow(1, search);
        tBarLayout.add(addNewButton);
        return tBarLayout;
    }

    private void newItem(ClickEvent<Button> buttonClickEvent) {
        moviesEntity = new MoviesEntity();
        binder.readBean(moviesEntity);
        editLayout.setEnabled(true);
        saveButton.setEnabled(true);
        deleteButton.setEnabled(false);
    }

    private void searchFilter(HasValue.ValueChangeEvent<? extends String> valueChangeEvent) {
        dataProvider.setFilter(MoviesEntity::getTitle, item -> item.toLowerCase().contains(valueChangeEvent.getValue().toLowerCase()));
    }

    private void delete(ClickEvent<Button> buttonClickEvent) {
        movies.delete(moviesEntity);
        update();
    }

    private void save(ClickEvent<Button> buttonClickEvent) {
        if(binder.isValid()){
            try{
                binder.writeBean(moviesEntity);
                moviesEntity = movies.save(moviesEntity);
                if(dataProvider.getItems().contains(moviesEntity)){
                    dataProvider.refreshItem(moviesEntity);
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
        dataProvider = new ListDataProvider<>(movies.findAllByOrderByTitleAsc());
        grid.setDataProvider(dataProvider);
    }

}