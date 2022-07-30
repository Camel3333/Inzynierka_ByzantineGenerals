package com.example.algorithm.operations;

import com.brunomnsilva.smartgraph.graph.Vertex;
import javafx.beans.property.BooleanProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class SendOperation implements Operation{
    @Getter
    private Vertex<Integer> from;
    @Getter
    private Vertex<Integer> to;
    @Getter
    private BooleanProperty sentOpinion;

    @Override
    public OperationType getType() {
        return OperationType.SEND;
    }
}
