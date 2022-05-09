package it.polito.tdp.metroparis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	private List<Fermata> fermate;
	private Graph<Fermata, DefaultEdge> grafo;
	Map<Integer, Fermata> fermateIdMap;
	
	public List<Fermata> getFermate(){
		if(this.fermate==null) {
		MetroDAO dao = new MetroDAO();
		this.fermate = dao.getAllFermate();
		
		this.fermateIdMap = new HashMap<Integer,Fermata>();
		for(Fermata f: fermate) {
			fermateIdMap.put(f.getIdFermata(), f);
		}
		}
		return this.fermate;
	}
	
	public List<Fermata> calcolaPercorso(Fermata partenza, Fermata arrivo){
		creaGrafo();
		Map<Fermata, Fermata> alberoInverso = visitaGrafo(partenza);
		
		Fermata corrente = arrivo;
		List<Fermata> percorso = new ArrayList<>();
	
		while(corrente != null) {
			percorso.add(0, corrente); // lo 0 è per stampare prima la stazione di partenza e poi la stazione di arrivo, 
									   // e non viceversa 
			corrente = alberoInverso.get(corrente); // passo al nodo precedente e aggiungo al nodo precedente 
		}
		// esco dal ciclo e il risultato è il percorso appena calcolato		
		return percorso;
	}
	
	public void creaGrafo() {
		this.grafo = new SimpleDirectedGraph<Fermata, DefaultEdge>(DefaultEdge.class);
		
		Graphs.addAllVertices(this.grafo, getFermate());
		
		MetroDAO  dao = new MetroDAO();
		
		// METODO 1 : itero su ogni coppia di vertici 
		/* for(Fermata partenza : fermate) {
			for(Fermata arrivo : fermate) {
				// se esiste ALMENO UNA (possono esisterne di più) connessione tra partenza e arrivo, 
				// se sì allora aggiungo l'arco con vertici partenza e arrivo
				if(dao.isFermateConnesse(partenza, arrivo)) {
					this.grafo.addEdge(partenza, arrivo);
				}
			}
		}
		*/
		
		// METODO 2 : dato ciascun vertice trova i vetici ad esso adiacenti
		// variante 2a: il DAO restituisce un elenco di ID numerici
		
		// nota: posso iterare su 'fermate' oppure su 'this.grafo.vertexSet()'
		/* for(Fermata partenza : fermate) {
			List<Integer> idConnesse = dao.getIdFermateConnesse(partenza);
			for(Integer id: idConnesse) {
				Fermata arrivo = null;      //(fermata che possiede questo "id"); // fermata che possiede quel determinato id
				
				for(Fermata f : fermate) { // NON SCRIVERE dao.getAllFermate() perchè creerebbe delle liste di ID numerici
					if(f.getIdFermata()==id) {
						arrivo = f;
						break;
					}
				}
				this.grafo.addEdge(partenza, arrivo);
			}
		}
		*/
		
		// METODO 2 : dato ciascun vertice trova i vetici ad esso adiacenti
		// variante 2b: il dao mi restituisce un elenco di oggetti fermata 
		/* for(Fermata partenza : fermate) {
			List<Fermata> arrivi = dao.getFermateConnesse(partenza);
			for(Fermata arrivo : arrivi) {
				this.grafo.addEdge(partenza, arrivo);
			}
		}
		*/
		
		// METODO 2 : dato ciascun vertice trova i vetici ad esso adiacenti
		// variante 2c: il dao mi restituisce un elenco di ID numerici che 
		// converto in oggetti tramite una Map <Integer, Fermata>
		/* for(Fermata partenza : fermate) {
			List<Integer> idConnesse = dao.getIdFermateConnesse(partenza);
			for(int id : idConnesse) {
				Fermata arrivo = fermateIdMap.get(id);
				this.grafo.addEdge(partenza, arrivo);
			}
		}
		*/
		
		// METODO 3 : fare una sola query che restituisce le coppire di fermate da collegare 
		// (preferisco 3c: usare Identity Map)
		List<CoppiaId> fermateDaCollegare = dao.getAllFermateConnesse();
		for(CoppiaId coppia: fermateDaCollegare) {
			this.grafo.addEdge(fermateIdMap.get(coppia.getIdPartenza()), fermateIdMap.get(coppia.getIdArrivo()));
		}
				
		
		// System.out.println(this.grafo);
		// System.out.println("Vertici = " + this.grafo.vertexSet().size()); // stampare SOLO il numero di vertici 
		// System.out.println("Archi = " + this.grafo.edgeSet().size());
		
	}
	
	public Map<Fermata, Fermata> visitaGrafo(Fermata partenza) {
		GraphIterator<Fermata, DefaultEdge> visita = new BreadthFirstIterator<>(this.grafo, partenza);
		Map<Fermata, Fermata> alberoInverso = new HashMap<>();
		alberoInverso.put(partenza, null);				
		
		visita.addTraversalListener(new RegistraAlberiDiVisita(alberoInverso, this.grafo));		
		while(visita.hasNext()) {
			Fermata f = visita.next();
	//		System.out.println(f);
		}	
		
		return alberoInverso;
	
	List<Fermata> percorso = new ArrayList<>();
	fermata = arrivo;
	while(fermata != null) {
		fermata = alberoInverso.get(fermata);
		percorso.add(fermata);
	}
	}

}
