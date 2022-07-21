package com.example.animation;

import com.brunomnsilva.smartgraph.graph.Vertex;
import com.example.algorithm.VertexRole;

import java.util.Map;

public class KingAnimationEngine extends AnimationEngine{
    @Override
    protected void highlightRoles(Map<Vertex<Integer>, VertexRole> roles) {
        for (Map.Entry<Vertex<Integer>, VertexRole> entry : roles.entrySet()){
            switch (entry.getValue()){
                case KING -> System.out.println("Vertex "+entry.getKey().element()+" is king");
                case LIEUTENANT -> System.out.println("Vertex "+entry.getKey().element()+" is lieutenant");
            }
        }
    }
}