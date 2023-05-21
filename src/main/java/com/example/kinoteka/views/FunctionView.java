package com.example.kinoteka.views;

import com.example.kinoteka.dao.entities.MoviesEntity;
import com.example.kinoteka.dao.entities.ParticipantsEntity;
import com.example.kinoteka.dao.repositories.RepositoryParticipants;
import com.example.kinoteka.dto.GenreStatistics;
import com.example.kinoteka.services.DtoSerivce;
import com.example.kinoteka.ui.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import java.util.stream.Collectors;

@Route(value="Func", layout = MainLayout.class)
@PageTitle("Демонстрация Функций | КиноТека")
@PermitAll
public class FunctionView extends VerticalLayout {
    private TextArea textArea = new TextArea("Вывод Результата");
    private ComboBox<Integer> id = new ComboBox<>("Id Актера");
    private IntegerField duration = new IntegerField("Длительность фильма");
    private TextField fullName = new TextField("ФИО");
    private DatePicker start = new DatePicker("Начало");
    private DatePicker end = new DatePicker("Конец");
    private final RepositoryParticipants participants;

    private final DtoSerivce dtoSerivce;

    public FunctionView(RepositoryParticipants participants, DtoSerivce dtoSerivce) {
        this.participants = participants;
        this.dtoSerivce = dtoSerivce;
    }

    @PostConstruct
    public void init(){
        dtoSerivce.createEntityManager();
        textArea.setWidthFull();
        id.setItems(participants.findAll().stream().map(ParticipantsEntity::getParticipantId).collect(Collectors.toSet()));
        duration.setMin(1);
        duration.setMax(600);

        Button getActorAge = new Button("Посчитать возраст Актера", this::getActorAge);
        Button getDuration = new Button("Посчитать длительность фильма", this::getDuration);
        Button getSername = new Button("Фамилия актера", this::getSername);
        Button getMoviesWithDirectorInLeadRole = new Button("Вывести фильмы с режиссерами", this::getMoviesWithDirectorInLeadRole);
        Button getCinemaRevenue = new Button("Посчитать выручку", this::getCinemaRevenue);
        Button getMoviesInCurrentYear = new Button("Вывести фильмы этог года в Финляндии", this::getMoviesInCurrentYear);
        Button getGenreStatistics = new Button("Вывести статистику", this::getGenreStatistics);

        VerticalLayout fields = new VerticalLayout();
        fields.add(id, duration, fullName, start, end);
        VerticalLayout buttons = new VerticalLayout();
        HorizontalLayout buttonsUp = new HorizontalLayout();
        HorizontalLayout buttonsDown = new HorizontalLayout();
        buttonsUp.add(getActorAge, getDuration, getSername, getMoviesWithDirectorInLeadRole);
        buttonsDown.add(getCinemaRevenue, getMoviesInCurrentYear, getGenreStatistics);
        buttons.add(buttonsUp, buttonsDown);
        HorizontalLayout edit = new HorizontalLayout();
        edit.add(textArea, fields);
        edit.setSizeFull();
        add(edit, buttons);

    }

    private void  getActorAge(ClickEvent<Button> buttonClickEvent){
        String age = "Возраст актера: " + dtoSerivce.getActorAge(id.getValue());
        textArea.setValue(age);
    }
    private void  getDuration(ClickEvent<Button> buttonClickEvent){
        textArea.setValue(dtoSerivce.getDuration(duration.getValue()));
    }
    private void  getSername(ClickEvent<Button> buttonClickEvent){
        textArea.setValue(dtoSerivce.getSername(fullName.getValue()));
    }
    private void  getMoviesWithDirectorInLeadRole(ClickEvent<Button> buttonClickEvent){
        StringBuilder builder = new StringBuilder();
        for(Object o: dtoSerivce.getMoviesWithDirectorInLeadRole()) {
            Object[] array = (Object[]) o;
            builder.append("Имя Режиссера: ").append(array[0]);
            builder.append(" Название: ").append(array[1]);
            builder.append(" Роль: ").append(array[2]);
            builder.append('\n');
        }
        textArea.setValue(String.valueOf(builder));
    }
    private void  getCinemaRevenue(ClickEvent<Button> buttonClickEvent){
        StringBuilder builder = new StringBuilder();
        for(Object o: dtoSerivce.getCinemaRevenue(start.getValue(), end.getValue())) {
            Object[] array = (Object[]) o;
            builder.append("Id: ").append(array[0]);
            builder.append(" Потенциальная прибыль: ").append(array[1]);
            builder.append(" Фактическая прибыль: ").append(array[2]);
            builder.append('\n');
        }
        textArea.setValue(String.valueOf(builder));
    }
    private void  getMoviesInCurrentYear(ClickEvent<Button> buttonClickEvent){
        StringBuilder builder = new StringBuilder();
        for(MoviesEntity moviesEntity: dtoSerivce.getMoviesInCurrentYear()) {
            builder.append("Название Фильма: ").append(moviesEntity.getTitle());
            builder.append('\n');
        }
        textArea.setValue(String.valueOf(builder));
    }
    private void  getGenreStatistics(ClickEvent<Button> buttonClickEvent){
        StringBuilder builder = new StringBuilder();
        for(GenreStatistics genreStatistics: dtoSerivce.getGenreStatistics()) {
            builder.append("Жанр: ").append(genreStatistics.getGenre());
            builder.append(" Количество в Стране: ").append(genreStatistics.getMoviesInCountry());
            builder.append(" Общее количество: ").append(genreStatistics.getMoviesOutCountry());
            builder.append('\n');
        }
        textArea.setValue(String.valueOf(builder));
    }

}
