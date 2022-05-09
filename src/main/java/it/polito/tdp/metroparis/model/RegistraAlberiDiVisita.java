package it.polito.tdp.metroparis.model;

import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.graph.DefaultEdge;

public class RegistraAlberiDiVisita implements TraversalListener<RegistraAlberiDiVisita, DefaultEdge> {
	
	private Graph<Fermata, DefaultEdge> grafo;
	private Map<Fermata, Fermata> alberoInverso;
		
	public RegistraAlberiDiVisita(Map<Fermata, Fermata> alberoInverso) {
		this.alberoInverso = alberoInverso;
		this.grafo = alberoInverso;
	}

	public void edgeTraversed(EdgeTraverslEvent<DefaultEdge> e) {
//		System.out.println(e.getEdge());
		
		Fermata source = this.grafo.getEdgeSource(e.getEdge());
		Fermata target = this.grafo.getEdgeTarget(e.getEdge());
	//	System.out.println(source + "  --  " + target);
		
		if(!alberoInverso.containsKey(source)) {
			alberoInverso.put(target, source);
	//		System.out.println(target + " si raggiunge da " + source);
		} else if (!alberoInverso.containsKey(source)) {
			alberoInverso.put(source, target);
	//		System.out.println(source + " si raggiunge da " + target);
		}
	}

}
