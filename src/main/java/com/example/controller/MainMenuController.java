package com.example.controller;

import com.example.ApplicationState;
import com.example.draw.CreationHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/mainMenuView.fxml")
public class MainMenuController implements ChangeListener<ApplicationState> {
    // Toolbars
    @FXML
    GraphEditController graphToolsController;

    // Draw Menu
    @FXML
    DrawMenuController drawMenuController;

    // Simulation Menu
    @FXML
    SimulationMenuController simulationMenuController;

    private void bindToolbars(){
        graphToolsController.setDrawMenuController(drawMenuController);
        graphToolsController.setSimulationMenuController(simulationMenuController);
    }

    @FXML
    public void initialize(){
        bindToolbars();

    }

    public void setDrawingHelper(CreationHelper drawingHelper) {
        drawMenuController.setDrawHelper(drawingHelper);
    }

    @Override
    public void changed(ObservableValue<? extends ApplicationState> observable, ApplicationState oldValue, ApplicationState newValue) {
        switch (newValue){
            case SIMULATING -> {
                drawMenuController.setEnabled(false);
                graphToolsController.setEnabled(false, ApplicationState.DRAWING);
            }
            case DRAWING -> {
                drawMenuController.setEnabled(true);
                graphToolsController.setEnabled(true, ApplicationState.DRAWING);
            }
        }
    }
}