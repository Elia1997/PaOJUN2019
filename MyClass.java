package project.oop.myapp.model;

import java.util.HashMap;
import java.util.Map;
/**
 * Classe che modella i dati presenti nel file CSV.
 * Contiene il metodo getMetadata() che restituisce una mappa contente il nome del campo e il tipo di dato.  
 * 
 * @author Clini, Iannopollo
 *
 */

public class MyClass {
	
	private String memberState;
	private int thcode;
	private String theme;
	private long decidedOps;
	private double nationalSFCFpercent;
	
	public MyClass(String memberState,int thcode,String theme,long decidedOps,double nationalSFCFpercent) {
		this.memberState=memberState;
		this.thcode=thcode;
		this.theme=theme;
		this.decidedOps=decidedOps;
		this.nationalSFCFpercent=nationalSFCFpercent;
	}

	public String getMemberState() {
		return memberState;
	}

	public void setMemberState(String memberState) {
		this.memberState = memberState;
	}

	public int getThcode() {
		return thcode;
	}

	public void setThcode(int thcode) {
		this.thcode = thcode;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public long getDecidedOps() {
		return decidedOps;
	}

	public void setDecidedOps(long decidedOps) {
		this.decidedOps = decidedOps;
	}

	public double getNationalSFCFpercent() {
		return nationalSFCFpercent;
	}

	public void setNationalSFCFpercent(float nationalSFCFpercent) {
		this.nationalSFCFpercent = nationalSFCFpercent;
	}
	
	public String toString() {
		return memberState+" - "+thcode+" - "+theme+" - "+decidedOps+" - "+nationalSFCFpercent+" .";
	}
	
	//METODO PER FAR TORNARE I NOMI DEGLI ATTRIBUTI CON IL RELATIVO TIPO
	public static Map<String, String> getMetadata() {
		Map<String, String> metaD = new HashMap<String, String>();
		metaD.put("memberState", "String");
		metaD.put("thcode", "int");
		metaD.put("theme", "String");
		metaD.put("decidedOps", "long");
		metaD.put("nationalSFCFpercent", "double");
		return metaD;
	}
}