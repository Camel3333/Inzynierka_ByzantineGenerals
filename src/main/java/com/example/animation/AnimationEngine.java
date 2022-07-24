package com.example.animation;

import com.brunomnsilva.smartgraph.graph.Vertex;
import com.example.algorithm.VertexRole;
import com.example.algorithm.operations.ChooseOperation;
import com.example.algorithm.operations.Operation;
import com.example.algorithm.operations.SendOperation;
import com.example.algorithm.report.StepReport;
import com.example.controller.GraphController;
import javafx.application.Platform;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public abstract class AnimationEngine{
    public void setGraphController(GraphController graphController) {
        this.graphController = graphController;
    }

    GraphController graphController;

    public void animate(StepReport report){
//        try {
//            System.out.println("Staring animation...");
//            Thread.sleep(100);
//            System.out.println("Animate!");
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
        highlightRoles(report.getRoles());
        for (Operation operation : report.getOperations()){
            switch (operation.getType()){
                case SEND -> animateSend((SendOperation) operation);
                case CHOOSE -> animateOpinionChange((ChooseOperation) operation);
            }
        }
    }

    protected abstract void highlightRoles(Map<Vertex<Integer>, VertexRole> roles);

    public void animateSend(SendOperation operation){
        // unpack send operation and animate
        System.out.println("Animating send between "+operation.getFromId()+" and "+operation.getToId()+" message: "+operation.getSentOpinion().getValue());
        System.out.println(graphController);
        Platform.runLater(() -> graphController.sendMessage(operation.getFromId(), operation.getToId()));

    }

    public void animateOpinionChange(ChooseOperation operation){
        // unpack opinion change operation and animate
        System.out.println("Animating opinion change for "+operation.getId()+" to "+operation.getChosenOpinion().getValue());
    }
}
