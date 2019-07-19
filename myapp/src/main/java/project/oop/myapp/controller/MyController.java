package project.oop.myapp.controller;

import java.util.ArrayList;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import project.oop.myapp.actions.MyDownloadAndParse;
import project.oop.myapp.actions.MyFilterAndStats;
import project.oop.myapp.model.MyClass;

/**Classe che si occupa della gestione delle richieste mandate dall'utente.
 * Nel caso in cui vengano richiesti dei filtri, si controlla che i parametri non contengano errori quali campo non conforme, operatore non valido.
 * Utilizza MyFilterAndStats per effettuare il filtro e successivamente restituisce una mappa all'utente contenente i risultati.
 * 
 * 
 * 
 * 
 * @author Ceskie
 *
 */

@RestController
public class MyController {
	
	//VARIABILE CHE CONTERRA' LA LISTA DI OGGETTI DI TIPO MyClass OTTENUTA DAL PARSING DEL FILE CSV
	private List<MyClass> results= new ArrayList<MyClass>();
	
	//EFFETTUO IL DOWNLOAD E IL PARSING DEI DATI ALL' AVVIO DELL' APPLICAZIONE
	@EventListener(ApplicationReadyEvent.class)
	public void downloadAfterStartup() {
	    results=MyDownloadAndParse.go();
	}
	
	//FACCIO RITORNARE TUTTI I DATI OTTENUTI ALL' AVVIO DELL' APPLICAZIONE
	@GetMapping("/data")
	public List<MyClass> getData() {
		return results;
	}
	
	//FACCIO RITORNARE I METADATI DELLA CLASSE MyClass
	@GetMapping("/metadata")
	public Map<String,String> getMetadata() {
		return MyClass.getMetadata();
	}
	
	//FACCIO RITORNARE UNA MAPPA CONTENENTE LE STATISTICHE SUI DATI FILTRATI,
	//IL FILTRO E' RICHIESTO TRAMITE POST CON PARAMETRO JSON NELLA FORMA:
	//{"attributo": {"condizione": valore}} PER I NUMERI
	//{"attributo" : {"condizione" : [valore1, valore2, ...]}} PER LE STRINGHE
	@PostMapping("/filter")
	public Map<String,Number> callFilter(@RequestBody Map<String,Map> body) {
		
		//CREO UNA MAPPA DI ERRORE DA FAR TORNARE IN CASO NON VENGANO RISPETTATI I PARAMETRI DEL FILTRO
		Map<String,Number> e=new HashMap<String,Number>();
		
		//IL NOME DEL CAMPO SU CUI EFFETTUARE IL FILTRO VIENE MESSO DENTRO field
		String field = body.keySet().toString();
		
		//VENGONO TOLTE LE [] A INIZIO E FINE DEL VALORE
		field=field.substring(1,field.length()-1);
		
		//LA CONDIZIONE CON CUI EFFETTUARE IL FILTRO VIENE MESSA DENTRO condition
		String condition = body.get(field).keySet().toString();
		
		//VENGONO TOLTE LE [] A INIZIO E FINE DEL VALORE
		condition=condition.substring(1,condition.length()-1);
		
		//IL VALORE DI RIFERIMENTO PER IL FILTRO VIENE MESSO DENTRO value
		Object value =body.get(field).get(condition);
		
		//CONTROLLO SE field E' VERAMENTE UN ATTRIBUTO DI MyClass
		if(MyClass.getMetadata().keySet().contains(field)) {
			
			//CONTROLLO SE value E' UN NUMERO
			if(value instanceof Number) {
				
				//IN CASO AFFERMATIVO CONTROLLO SE condition E' "greater than" O "lower than"
				//E NEI DUE CASI FACCIO TORNARE IL RISULTATO DEL RELATIVO FILTRO
				if(condition.equals("$gt")) {
					
					return MyFilterAndStats.runFiltStat(results, field, ">", value);
				
				}else if(condition.equals("$lt")) {
					
					return MyFilterAndStats.runFiltStat(results, field, "<", value);
					
				}else {
					
					//SE condition NON E' "greater than" O "lower than" TORNA ERRORE
					e.put("ERROR: NUMBER CONDITION IS NOT $gt OR $lt", null);
					return e;
				}
			
			//CONTROLLO SE value E' UN ARRAYLIST
			}else if(value instanceof ArrayList<?>){
				
				//IN CASO AFFERMATIVO CONTROLLO SE condition E' "in" O "not in"
				//E NEI DUE CASI FACCIO TORNARE IL RISULTATO DEL RELATIVO FILTRO
				if(condition.equals("$in")) {
					
					return MyFilterAndStats.runFiltStat(results, field, "in", value);
				
				}else if(condition.equals("$nin")) {
				
					return MyFilterAndStats.runFiltStat(results, field, "nin", value);
					
				}else {
					
					//SE condition NON E' "in" O "not in" TORNA ERRORE
					e.put("ERROR: STRING[] CONDITION IS NOT $in OR $nin", null);
					return e;
				}
				
			}else {
				
				//SE value NON E' UN NUMERO O UNA ARRAYLIST TORNA ERRORE
				e.put("ERROR: PARAMETER IS NOT NUMBER OR ARRAYLIST", null);
				return e;
			}
			
		}else {
			
			//SE field NON E' UN ATTRIBUTO DI MyClass TORNA ERRORE
			e.put("ERROR: FIELD DOES NOT EXIST", null);
			return e;
		}
	}
}
