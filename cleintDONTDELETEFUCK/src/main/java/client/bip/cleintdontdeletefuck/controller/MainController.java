package client.bip.cleintdontdeletefuck.controller;

import client.bip.cleintdontdeletefuck.entity.FinalLoadEntity;
import client.bip.cleintdontdeletefuck.entity.LoadEntity;
import client.bip.cleintdontdeletefuck.utils.HTTPUtils;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class MainController {

        @FXML
        private ComboBox<String> SubjectComboBox;

        @FXML
        private ComboBox<String> comboBoxSelectTable;

        @FXML
        private TableColumn<LoadEntity, Integer> consultationsColumn;

        @FXML
        private TableColumn<LoadEntity, Integer> courseworkColumn;

        @FXML
        private TableColumn<LoadEntity, Integer> diplomaColumn;

        @FXML
        private TableColumn<LoadEntity, Integer> examColumn;

        @FXML
        private TableColumn<LoadEntity, FinalLoadEntity> finalLoadColumn;

        @FXML
        private AnchorPane finalLoadInsides;

        @FXML
        private TableView<?> finalLoadTable;

        @FXML
        private TableColumn<?, ?> groupColumn;

        @FXML
        private ComboBox<?> groupComboBox;

        @FXML
        private AnchorPane insides;

        @FXML
        private TableColumn<?, ?> semesterFirstColumn;

        @FXML
        private TableColumn<?, ?> semesterSecondColumn;

        @FXML
        private TableColumn<?, ?> subjectColumn;

        @FXML
        private GridPane tablesGridPane;

        @FXML
        private TableColumn<?, ?> teacherColumn;

        @FXML
        private ComboBox<?> teacherComboBox;

        @FXML
        private CheckBox withinOrWithout;

        @FXML
        private CheckBox withoutConsultations;

        @FXML
        private CheckBox withoutCoursework;

        @FXML
        private CheckBox withoutDiploma;

        @FXML
        private CheckBox withoutExam;

        @FXML
        private CheckBox withoutFirstSemester;

        @FXML
        private CheckBox withoutSecondSemester;

        @FXML
        void addButton(ActionEvent event) {

    }

        @FXML
        void changeFinalEdit(ActionEvent event) {

    }

        @FXML
        void deleteButton(ActionEvent event) {

    }

        @FXML
        void editButton(ActionEvent event) {

    }

        @FXML
        void finalLoadOpenButton(ActionEvent event) {

    }

        @FXML
        void fromExcel(ActionEvent event) {

    }

        @FXML
        void groupsOpenButton(ActionEvent event) {

    }

        @FXML
        void loadOpenButton(ActionEvent event) {

    }

        @FXML
        void subjectsOpenButton(ActionEvent event) {

    }

        @FXML
        void tablesOpenButton(ActionEvent event) {

    }

        @FXML
        void teachersOpernButton(ActionEvent event) {

    }

        @FXML
        void toExcel(ActionEvent event) {

    }

    }
