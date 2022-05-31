package com.example.controller;

import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import com.example.draw.CreationHelper;
import com.example.draw.DrawMode;
import com.example.draw.MySmartGraphPanel;
import com.example.model.MyVertex;
import com.example.util.DrawMouseEventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lombok.Getter;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.controlsfx.control.PopOver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/graphView.fxml")
public class GraphController {

    @FXML
    private Pane graphRoot;

    @Autowired
    private FxWeaver fxWeaver;

    private int counter = 0;
    private SmartGraphDemoContainer container;
    private MySmartGraphPanel<Integer, Integer> graphView;
    @Getter
    private Graph<Integer, Integer> graph;
    private CreationHelper drawingHelper;

    public void setModelGraph(Graph<Integer, Integer> graph){
        this.graph = graph;
        //remove old graph
        graphRoot.getChildren().remove(container);
        init();
        initGraphView();
    }

    public void setDrawingHelper(CreationHelper drawingHelper) {
        this.drawingHelper = drawingHelper;
    }

    public Graph<Integer,Integer> getModelGraph(){
        return graph;
    }

    private void buildGraphContainers() {
        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        // TODO: Load properties from file
        SmartGraphProperties properties = new SmartGraphProperties("edge.arrow = false");
        graphView = new MySmartGraphPanel<>(graph, properties, strategy);
        setGraphViewBindings();
        container = new SmartGraphDemoContainer(graphView);
    }

    private void setGraphViewBindings(){
        graphView.setVertexSingleClickAction(graphVertex -> {
            if(drawingHelper != null) {
                drawingHelper.selectVertex(graphVertex.getUnderlyingVertex());
            }
        });

        graphView.setEdgeSingleClickAction(graphEdge -> {
            if(drawingHelper != null) {
                drawingHelper.selectEdge(graphEdge.getUnderlyingEdge());
            }
        });

        graphView.setVertexDoubleClickAction(graphVertex -> {
            // load popUp view
            FxControllerAndView<VertexSettingsController, Node> controllerAndView = fxWeaver.load(VertexSettingsController.class);
            // bind controller with selected vertex
            controllerAndView.getController().bindVertex((MyVertex<Integer>)graphVertex.getUnderlyingVertex());
            // configure and show popUp
            PopOver vertexSettingsWindow = new PopOver(controllerAndView.getView().get());
            vertexSettingsWindow.show((Node)graphVertex);

            // bind vertex traitor property with vertex color
            MyVertex<Integer> vertex = (MyVertex<Integer>)graphVertex.getUnderlyingVertex();
            vertex.getIsTraitor().addListener(changed -> {
                if (vertex.getIsTraitor().get()) {
                    graphView.getStylableVertex(vertex).setStyleClass("traitor");
                } else {
                    graphView.getStylableVertex(vertex).setStyleClass("vertex");
                }
            });
        });

        //adding vertex
        DrawMouseEventHandler drawMouseEventHandler = new DrawMouseEventHandler();
        drawMouseEventHandler.setOnClickedEventHandler((mouseEvent) -> {
            if (drawingHelper.getCurrentDrawMode() == DrawMode.VERTEX){
                // add vertex at clicked position
                System.out.println("X = "+mouseEvent.getX());
                System.out.println("Y = "+mouseEvent.getY());
                var x = mouseEvent.getX();
                var y = mouseEvent.getY();
                var vertex = graph.insertVertex(counter++);
                graphView.updateAndWait();
                graphView.setVertexPosition(vertex, x, y);
            }
        });
        graphView.addEventHandler(MouseEvent.ANY, drawMouseEventHandler);
    }

    public void initGraphView() {
        if (graphView.getAbleToInit().get()) {
            // GraphView is ready to be initialized
            graphView.init();
        }
        else {
            // Listen while GraphView won't be ready
            graphView.getAbleToInit().addListener((o, oldVal, newVal) -> {
                if (newVal && !oldVal) {
                    graphView.init();
                }
            });
        }
    }

    // TODO: implement update as listener to graph changes
    public void update(){
        graphView.updateAndWait();
    }

    private void init() {
        buildGraphContainers();
        container.prefWidthProperty().bind(graphRoot.widthProperty());
        container.prefHeightProperty().bind(graphRoot.heightProperty());
        graphRoot.getChildren().add(container);
    }
}
