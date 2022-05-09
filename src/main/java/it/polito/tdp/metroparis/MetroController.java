package it.polito.tdp.metroparis;

import java.util.List;

import it.polito.tdp.metroparis.model.Fermata;
import it.polito.tdp.metroparis.model.Model;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

public class MetroController {
	
	private Model model;
	
	private ComboBox<Fermata> boxArrivo;
	
	private ComboBox<Fermata> boxPartenza;
	
	private TextArea txtResult;
	
	private TableView<Fermata> tblPercorso; // Fermata è l'oggetto di ciascuna riga
	
	private TableColumn<Fermata, String> colFermata; // ogni colonna ha il tipo della tabella e il tipo del dato visualizzato nella cella
	
	void handleCerca(ActionEvent event) {
		Fermata partenza = boxPartenza.getValue();
		Fermata arrivo = boxArrivo.getValue();
		
		if(partenza!= null && arrivo!=null && !partenza.equals(arrivo)) {
			List<Fermata> percorso = model.calcolaPercorso(partenza, arrivo);
			
			tblPercorso.setItems(FXCollections.observableArrayList(percorso)); // costruisco un array list observable composta da tutti 
																			   // gli elementi della lista percorso			
			txtResult.setText("Percorso trovato con " + percorso.size() + " stazioni\n");
			
		} else {
			txtResult.setText("Devi selezionare due stazioni, diverse tra loro\n");
		}
	}
	
	public void setModel(Model m) {
		this.model = m;
		List<Fermata> fermate = this.model.getFermate();
		boxPartenza.getItems().addAll(fermate);
		boxArrivo.getItems().addAll(fermate);		
		
	}
	
	void inizialize() {
		// istruire la colonna su come riceve l'informazione dall'oggetto e ne estrae la stringa da visualizzare
		colFermata.setCellValueFactory(new PropertyValueFactory<Fermata, String>("nome"));
	}
	
	
	
}
