package com.example.algorithm;

import com.example.model.MyGraph;
import com.example.model.MyVertex;
import com.example.settings.AlgorithmSetting;
import com.example.settings.AlgorithmSettings;
import javafx.beans.property.SimpleBooleanProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LamportIterAlgorithmTest {
    private MyGraph<Integer, Integer> graph;
    private final Algorithm algorithm = new LamportIterAlgorithm();

    @BeforeEach
    public void resetGraph(){
        graph = new MyGraph<>();
    }

    @Test
    public void completeGraphWithFourVerticesAndOneTraitorTest(){
        // Given
        var v1 = (MyVertex<Integer>) graph.insertVertex(1);
        var v2 = (MyVertex<Integer>) graph.insertVertex(2);
        var v3 = (MyVertex<Integer>) graph.insertVertex(3);
        var v4 = (MyVertex<Integer>) graph.insertVertex(4);

        var e1 = graph.insertEdge(v1, v2, 1);
        var e2 = graph.insertEdge(v2, v3, 2);
        var e3 = graph.insertEdge(v3, v4, 3);
        var e4 = graph.insertEdge(v4, v1, 4);
        var e5 = graph.insertEdge(v1, v3, 5);
        var e6 = graph.insertEdge(v2, v4, 6);

        var settings = new AlgorithmSettings();

        // When
        v1.setIsTraitor(false);
        v2.setIsTraitor(true);
        v3.setIsTraitor(false);
        v4.setIsTraitor(false);

        v1.setIsSupporting(true);
        v2.setIsSupporting(true);
        v3.setIsSupporting(true);
        v4.setIsSupporting(false);

        settings.getSettings().put("depth", new AlgorithmSetting("depth", 1, Integer.class, object -> true));

        algorithm.loadEnvironment(graph, settings);
        while(!algorithm.isFinished()){
            algorithm.step();
        }

        // Then
        assertTrue(graph.checkConsensus());
    }
}
