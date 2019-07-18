package project.oop.myapp.actions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.oop.myapp.model.MyClass;

/**Classe che, data una lista di oggetti di tipo MyClass e il nome di un campo di MyClass, filtra i dati distinguendo tra numeri e stringhe.
 * Restituisce una mappa contentente avg, min, max, dev std, sum, count, (nel caso di dati numerici), l'unicità delle stringhe la loro occorrenza.
 * 
 * @author Clini, Iannopollo
 *
 */







public class MyFilterAndStats {

	//FUNZIONE CHE CONTROLLA SE VENGONO SODDISFATTI I REQUISITI DEL FILTRO PER NUMERI E STRINGHE
	public static boolean Check(Object value, String operator, Object th) {
		
		//CONTROLLO SE I PARAMETRI DA CONFRONTARE SONO NUMERI
		if (th instanceof Number && value instanceof Number) {
			
			//CREO DUE VARIABILI CONVERTENDO IN DOUBLE IL VALORE DEI PARAMETRI
			Double thC = ((Number)th).doubleValue();
			Double valueC = ((Number)value).doubleValue();
			
			//SE L' OPERATORE INSERITO E' >
			if (operator.equals(">")) {
				return valueC > thC;
			//SE L' OPERATORE INSERITO E' <
			}else if (operator.equals("<")) {
				return valueC < thC;
			}
			
			//SE L' OPERATORE NON è NE > NE < TORNA false
			return false;
			
		//CONTROLLO SE I PARAMETRI DA CONFRONTARE SONO UN ARRAYLIST E UNA STRINGA 
		}else if(th instanceof ArrayList<?> && value instanceof String) {
			
			//CREO DUE VARIABILI CONVERTENDO IN UN ARRAYLIST DI STRINGHE E IN UNA STRINGA IL VALORE DEI PARAMETRI
			ArrayList<Object> thC = (ArrayList<Object>)th;
			String valueC = (String)value;
			
			//PER OGNI ELEMENTO DELL' ARRAYLIST DI STRINGHE
			for(Object s:thC) {
				
				if(s instanceof String) {
					
					String sC=(String)s;
					
					//SE L' OPERATORE E' in
					if (operator.equals("in")){
					
						//TORNA true SE valueC CONTIENE L' ELEMENTO DELL' ARRAYLIST
						if(valueC.contains(sC)) {
							return true;
						}
				
						//SE L' OPERATORE E' nin
					}else if(operator.equals("nin")){
					
						//TORNA false SE valueC CONTIENE L' ELEMENTO DELL' ARRAYLIST
						if(valueC.contains(sC)) {
							return false;
						}
					}
				}else {
					return false;
				}
			}
			
			//SE GLI IF PRECEDENTI NON HANNO FATTO TORNARE NESSUN VALORE, FACCIO TORNARE I VALORI NEGATIVI
			//PER I RISPETTIVI OPERATORI
			if (operator.equals("in")){
					return false;
			}else if(operator.equals("nin")){
					return true;
			}
		}
		
		//SE GLI IF PRECEDENTI NON HANNO FATTO TORNARE NESSUN VALORE, FACCIO TORNARE false
		return false;
	}
	
	//FUNZIONE CHE FA TORNARE UNA MAPPA CON CHIAVE STRINGA E VALORE NUMERICO CONTENENTE LE STATISTICHE
	//RELATIVE I DATI DELLA LISTA DI OGGETTI DI TIPO MyClass FILTRATI
	public static Map<String, Number> runFiltStat(List<MyClass> src, String fieldName, String operator, Object value) {
		
		//CREO LA MAPPA CHE CONTERRA' LE STATISTICHE
		Map<String, Number> results = new HashMap<String, Number>();
		//CREO UNA LISTA DI NUMERI CHE MI SERVIRA' PER IL CALCOLO DELLA DEVIAZIONE STANDARD
		List<Double> numsfordevStd = new ArrayList<Double>();
		
		//INIZIALIZZO LE STATISTICHE RELATIVE I DATI NUMERALI
		Double min=0.0;
		Double max=0.0;
		Double devStd=0.0;
		Double sum=0.0;
		Double count=0.0;
				
		//PER OGNI ELEMENTO DELLA LISTA PASSATA COME PARAMETRO
		for(MyClass item : src) {
			
			try {
				
				//CREO UN OGGETTO Method PER POTER USARE IL METODO get DEL CAMPO PASSATO COME PARAMETRO
				Method m = item.getClass().getMethod("get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1),null);
				
				try {
					
					//METTO DENTRO tmp IL RISULTATO DELLA CHIAMATA DEL METODO PRECEDENTEMENTE OTTENUTO
					Object tmp = m.invoke(item);
					
					//SE IL DATO SODDISFA I CRITERI DEL FILTRO
					if(Check(tmp, operator, value)) {
						
						//INCREMENTO IL COUNTER DEGLI ELEMENTI FILTRATI
						count++;
						
						//SE IL DATO E' UN NUMERO
						if(tmp instanceof Number) {
							
							//CONVERTO IL DATO IN DOUBLE
							Double tmpC=((Number)tmp).doubleValue();
							
							//AGGIUNGO IL DATO ALLA LISTA DEI NUMERI PER LA DEVIAZIONE STANDARD
							numsfordevStd.add(tmpC);
							
							//AGGIUNGO IL DATO ALLA SOMMA DEI DATI PRECEDENTI
							sum+=(tmpC);
							
							//SE IL DATO E' IL PRIMO NUMERO AGGIUNTO, LO IMPOSTO COME MINIMO E COME MASSIMO
							if(count==1) {
								max=tmpC;
								min=tmpC;
							}else {
								
								//ALTRIMENTI, SE E' MAGGIORE DI max DIVENTA max
								if(tmpC>max){
									max=tmpC;
								}
								
								//ALTRIMENTI, SE E' MINORE DI min DIVENTA min
								if(tmpC<min) {
									min=tmpC;
								}
							}
							
						//SE IL DATO E' UNA STRINGA
						}else if(tmp instanceof String) {
							
							//CONTROLLO SE E' GIA' PRESENTE NELLA MAPPA
							if(results.containsKey((String)tmp)) {
								
								//SE E' GIA' PRESENTE NELLA MAPPA, INCREMENTO LA SUA OCCORENZA DI 1
								results.replace((String)tmp, (int)results.get(tmp)+1);
								
							}else {
								
								//SE NON E' GIA' PRESENTE NELLA MAPPA, LO AGGIUNGO CON OCCORRENZA 1
								results.put((String)tmp, 1);
							}
							
						}
					}
					
				} catch (IllegalAccessException e) {
					System.out.println("ILLEGAL ACCESS ERROR");
				} catch (IllegalArgumentException e) {
					System.out.println("ILLEGAL ARGUMENT ERROR");
				} catch (InvocationTargetException e) {
					System.out.println("INVOCATION TARGET ERROR");
				}
				
			} catch (NoSuchMethodException e) {
				System.out.println("NO SUCH METHOD IN THE CLASS");
			} catch (SecurityException e) {
				System.out.println("SECURITY ERROR");
			}					
		}
		
		//USO LA SOMMA PER CONTROLLARE CHE LA STATISTICA RICHIESTA SIA RELATIVA A NUMERI
		if(sum!=0.0) {
			
			//CONTROLLO IL COUNTER PER EVITARE ERRORI MATEMATICI
			if(count!=0.0) {
				
				//INIZIO IL CALCOLO DELLA DEVIAZIONE STANDARD
				for(Double val:numsfordevStd) {
					devStd+= Math.pow(val - sum/count, 2);
				}
				
				//INSERISCO TUTTE LE STATISTICHE NELLA MAPPA
				results.put("minimo", min);
				results.put("massimo", max);
				results.put("somma", sum);
				results.put("media", sum/count);
				//FINISCO IL CALCOLO DELLA DEVIAZIONE STANDARD
				results.put("deviazioneStandard", Math.sqrt(devStd/count));
			}
		}
		
		//INSERISCO L' UNICA STATISTICA IN COMUNE TRA NUMERI E STRINGHE NELLA MAPPA
		results.put("numeroElementi", (Number)count.intValue());
		
		//FACCIO TORNARE LA MAPPA
		return results;
	}
	
}
