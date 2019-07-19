package project.oop.myapp.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.NoRouteToHostException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import project.oop.myapp.model.MyClass;

/**
 * Classe che si occupa di controllare l'esistenza del file CSV e in caso contrario lo scarica tramite URL.
 * Successivamente tramite il metodo parseFile() effettua restituisce una lista di oggetti di tipo MyClass.
 * 
 * @author Clini, Iannopollo
 *
 */





public class MyDownloadAndParse {

	//FUNZIONE CHE EFFETTUA IL DOWNLOAD NEL CASO IL FILE NON SIA GIA' PRESENTE
	//E POI RESTITUISCE UNA LISTA DI OGGETTI DI TIPO MyClass
	public static List<MyClass> go() {

		//CONTROLLO SE IL FILE ESISTE GIA'
		File checkF=new File("FundsBreakdown.csv");
		
		//SE NON ESISTE
		if(!checkF.exists()) {
			
			//INSERISCO IL PRIMO URL DENTRO url
			String url = "http://data.europa.eu/euodp/data/api/3/action/package_show?id=breakdown-available-funds-by-theme-2007-2013";
		
			try {
			
				//CREO UN OGGETTO URLConnection PER COLLEGARMI AL PRIMO URL
				URLConnection openConnection = new URL(url).openConnection();
			
				//CREO UN OGGETTO InputStream E CI METTO IL CONTENUTO DELLA PAGINA INDICATA DAL PRIMO URL
				InputStream in = openConnection.getInputStream();
			
				String data = "";
				String line = "";
			 
				//CREO UN OGGETTO InputStreamReader PER CONVERTIRE I BYTE DI InputStream IN CHAR
				InputStreamReader inR = new InputStreamReader(in);
			   
				//CREO UN OGGETTO BufferedReader PER MIGLIORARE L' EFFICIENZA DI LETTURA DI InputStreamReader
				BufferedReader buf = new BufferedReader(inR);

				//METTO OGNI RIGA DI BufferedReader DENTRO LA STRINGA data 
				while ((line = buf.readLine()) != null ) {
					data+=line;
				}
				
				//CHIUDO LA CONNESSIONE
				in.close();
	
				//CREO UN OGGETTO JSONObject EFFETTUANDO IL PARSING DI data
				JSONObject obj = (JSONObject) JSONValue.parseWithException(data); 
		
				//METTO DENTRO objI IL CONTENUTO DEL CAMPO result
				JSONObject objI = (JSONObject) (obj.get("result"));
			
				//METTO DENTRO objA IL CONTENUTO DEL CAMPO resources
				JSONArray objA = (JSONArray) (objI.get("resources"));
			
				//PER OGNI ELEMENTO DI objA
				for(Object o: objA){
				
					//SE L' ELEMENTO E' DI TIPO JSONObject
					if ( o instanceof JSONObject ) {
			    	
						//COPIO IL CONTENUTO DELL' ELEMENTO IN o1
						JSONObject o1 = (JSONObject)o; 
			        
						//METTO NELLA STRINGA format IL VALORE DEL CAMPO format DI o1
						String format = (String)o1.get("format");
			        
						//METTO NELLA STRINGA urlD IL VALORE DEL CAMPO url DI o1
						String urlD = (String)o1.get("url");
			        
						//SE LA STRINGA format CONTIENE "CSV"
						if(format.contains("CSV")) {
			        		
							//SCARICO IL FILE CON URL = urID E LO SALVO COME "FundsBreakdown.csv"
							download(urlD, "FundsBreakdown.csv");
							System.out.println("FILE DOWNLOADED");
						}
					}
				}
				
			} catch (IOException e) {
				System.out.println("I/O ERROR");
			}catch(ParseException e) {
				System.out.println("JSON PARSING ERROR");
			} catch (Exception e) {
				System.out.println("GENERIC ERROR");
			}
		}else {
			System.out.println("FILE ALREADY EXISTS, NO NEED TO DOWNLOAD");
		}
		
		//FACCIO RITORNARE IL RISULTATO DEL PARSING DEL FILE CSV
	    return parseFile();
	}
	
	//FUNZIONE PER SCARICARE IL FILE MEDIANTE url E SALVARLO come fileName 
	public static void download(String url, String fileName) throws Exception {
	    try (InputStream in = URI.create(url).toURL().openStream()) {
	        Files.copy(in, Paths.get(fileName));
	    }
	}
	
	//FUNZIONE CHE DAL FILE CSV RICAVA UNA LISTA DI OGGETTI DI TIPO MyClass
	public static List<MyClass> parseFile() {

		//CREO UN OGGETTO Pattern CHE MI SERVIRA' PER RICAVARE I DATI DA UNA STRINGA CONTENENTE VIRGOLE
		Pattern pattern = Pattern.compile(",");
		
		//CREO LA LISTA CHE CONTERRA' GLI OGGETTI DI TIPO MyClass
		List<MyClass> myList = new ArrayList<MyClass>();
				
		try{
			
			//APRO IL FILE CSV
			BufferedReader bufCSV = new BufferedReader(new FileReader("FundsBreakdown.csv"));
			
			//LEGGO LA PRIMA RIGA DEL FILE, CHE CONTIENE I NOMI DEGLI ATTRIBUTI, PER NON CAUSARE ERRORI DI CONVERSIONE
			String line = bufCSV.readLine();
			
			//PER OGNI RIGA DEL FILE, DALLA SECONDA ALL' ULTIMA
			while ((line = bufCSV.readLine()) != null ) {
				
				//CONTROLLO SE NELLA RIGA E' PRESENTE IL CARATTERE " CHE ESCLUDEREBBE A LIVELLO LOGICO
				//EVENTUALI VIRGOLE DAL PARSING FINO AL " SUCCESSIVO
				if(line.contains("\"")) {
					
					//CONTROLLO DA UN " ALL' ALTRO SE CI SONO VIRGOLE
					for(int i=line.indexOf("\"");i<line.lastIndexOf("\"");i++) {
						
						//SE TROVO UNA VIRGOLA, LA ELIMINO
						if (line.charAt(i)==',') {
							line = line.substring(0, i)+""+line.substring(i + 1); 
						}
					}
				}
				
				//OGNI DATO SEPARATO DALLA VIRGOLA VIENE MESSO DENTRO UN ARRAY DI STRINGHE
				String[] bufline= pattern.split(line);				
				
				//CONTROLLO SE CI SONO DATI MANCANTI, IN QUESTO CASO LA LUNGHEZZA DELLA STRINGA E' MINORE DI 5
				if(bufline.length<5) {
					//IN CASO MANCHINO DEI DATI, CREO UN ARRAY DI DEFAULT CON LUNGHEZZA 5
					String[] buftemp= {"","0","","0","0"};
					//E LO RIEMPIO COI DATI DISPONIBILI
					for(int i=0;i<bufline.length;i++) {
						buftemp[i]=bufline[i];
					}
					//POI AGGIUNGO ALLA LISTA DEGLI OGGETTI DI TIPO MyClass UN NUOVO OGGETTO CREATO COI DATI
					//PRESENTI NELL' ARRAY DI STRINGHE
					myList.add(new MyClass(buftemp[0],Integer.parseInt(buftemp[1]),buftemp[2],Long.parseLong(buftemp[3]),Float.parseFloat(buftemp[4])));
				}else {
					//ALTRIMENTI AGGIUNGO ALLA LISTA DEGLI OGGETTI DI TIPO MyClass UN NUOVO OGGETTO CREATO COI DATI
					//PRESENTI NELL' ARRAY DI STRINGHE
					myList.add(new MyClass(bufline[0],Integer.parseInt(bufline[1]),bufline[2],Long.parseLong(bufline[3]),Float.parseFloat(bufline[4])));
				}
			}
			
			//CHIUDO IL FILE CSV
			bufCSV.close();
			
		}catch(FileNotFoundException e) {
			System.out.println("FILE DOESN'T EXISTS");
		}catch(IOException e) {
			System.out.println("I/O ERROR");
		}catch(NumberFormatException e){	
			System.out.println("NUMERIC CONVERTION ERROR");
		}catch(Exception e){	
			System.out.println("GENERIC ERROR");
		}
		
		//FACCIO RITORNARE LA LISTA DEGLI OGGETTI DI TIPO MyClass
		return myList;
	}
	
}
