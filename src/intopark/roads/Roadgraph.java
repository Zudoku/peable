/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.roads;

import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;

/**
 *
 * @author arska
 */
@Singleton
public class Roadgraph {
    //LOGGER
    private transient static final Logger logger = Logger.getLogger(Roadgraph.class.getName());
    //DEPENDENCIES
    //OWNS
    private DirectedGraph<Walkable,DefaultEdge> roadMap;
    private List<Road> roads;
    //VARIABLES
    
    public Roadgraph() {
        roadMap = new DefaultDirectedGraph<>(DefaultEdge.class);
        roads=new ArrayList<>();
    }
    public void addRoad(Road road){
        roadMap.addVertex(road);
        for(Road r:roads){
            /* If r and road can physically connect */
            if(r.canConnect(road)){
                /* If road is normal road */
                if(!road.getQueRoad()){
                    /* And r is normal road*/
                    if(!r.getQueRoad()){
                        /* Connect them */
                        connectWalkables(r, road);
                    }
                    /* And road is queRoad*/
                    else{
                        /* If r isn't connected to two vertixes already (2 edges between two vertixes. One for each direction)*/
                        if(!(roadMap.edgesOf(r).size()>4)){
                            /* Then connect them */
                            connectWalkables(r, road);
                        }
                    }
                }
                /* road is queRoad */
                else{
                    /* If road can connect since it's queRoad */
                    if(!(roadMap.edgesOf(road).size()>4)){
                        /* And r is normal road*/
                        if(!r.getQueRoad()){
                            /* Connect them */
                            connectWalkables(r, road);
                        }else{
                            /* If r isn't connected to two vertixes already (2 edges between two vertixes. One for each direction)*/
                            if(!(roadMap.edgesOf(r).size()>4)){
                                /* Then connect them */
                                connectWalkables(r, road);
                            }
                        } 
                    }else{
                        /* road already has 2 edges */
                        return;
                    }
                    
                }
            }else{
                //logger.log(Level.FINE, "");
            }
        }
        
    }
    private void connectWalkables(Walkable walk1,Walkable walk2){
        roadMap.addEdge(walk1, walk2);
        roadMap.addEdge(walk2, walk1);
    }
    public void deleteRoad(Road road){
        /* Look up all the edges connected to road. */
        Set<DefaultEdge>edgesDelete=roadMap.outgoingEdgesOf(road);
        edgesDelete.addAll(roadMap.incomingEdgesOf(road));
        /* Delete them */
        roadMap.removeAllEdges(edgesDelete);
        /* Delete also the vertex */
        roadMap.removeVertex(road);
        /* Remove it from the list */
        roads.remove(road);
    }
}
