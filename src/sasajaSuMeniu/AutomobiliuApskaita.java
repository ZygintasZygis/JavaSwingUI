package sasajaSuMeniu;

/**
 * @author Aleksas
 */
import javax.swing.*;
import javax.swing.table.*;
import java.io.*;
import java.util.*;

import kolekcijuDemo.Automobilis;
/**
 * Keletas metodų su Automobilis klasės objektais meniu komandų demostracijai.
 * Nuskaitti iš pasirinkto dialoge failo duomenys saugomi List kolekcijoje.
 * Naudojama interfeiso List realizacija - klasė ArrayList.
 */
public class AutomobiliuApskaita {
	
	// Čia automobilių saugykla -  kolekcija ArrayList. L2 darbe tai būtų sąrašas (ListKTUx).

    private final List<Automobilis> mašinos = new ArrayList<>(); 

	/**
	 * Atrenka automobilius pagal nurodytą markę (išvedimas į JTextArea elementą)
	 * @param markė String klasės objektas
	 * @param ta JTextArea klasės objektas
	 */
	public void atrinktiPagalMarkę(String markė, JTextArea ta) {
		for (Automobilis a: mašinos){
			if (a.getMarkė().startsWith(markė)) {
				ta.append(a.toString() + "\n");
			}
		}
	}
	
	/**
	 * Atrenka automobilius pagal nurodytą markę (išvedimas į JTable elementą)
	 * @param lentelėsModelis modelis
	 * @param markė String klasės objektas
	 * @return true, jei rasta
	 */
	public boolean atrinktiPagalMarkęLentele(DefaultTableModel lentelėsModelis, String markė) {
		for (Automobilis a: mašinos){
			if (a.getMarkė().startsWith(markė)) {
				lentelėsModelis.addRow(new Object[] {a.getModelis(), a.getGamMetai(), a.getRida(), a.getKaina()});
			}
		}
		return lentelėsModelis.getRowCount() > 0 ? true : false;
	}

	/**
	 * Surikiuoja pradinį sąrašą pagal kainos mažėjimą 
	 *	(panaudotas Automobilis klasės komparatorius pagalKainą).
	 */
	public void rikiuojaPagalKainą() {
		Collections.sort(mašinos, Automobilis.pagalKainą);		
	}
	
	/**
	 * Failo skaitymas ir jo turinio išvedimas į JTextArea
	 * @param fName File klasės objektas
	 * @param ta JTextArea klasės objektas
	 */
	public String loadAndPrint(File fName, JTextArea ta) {
		String klaidosKodas = null;
		try {
			mašinos.clear();
			BufferedReader fReader =  new BufferedReader(new FileReader(fName));
			String line;
			
			ta.append("     D u o m e n y s iš failo <" + fName.getName() + ">\n\n");

			while ((line = fReader.readLine()) != null) {
				mašinos.add(new Automobilis(line));
				ta.append(line + "\n"); // į sąsajos elementą JTextArea
			}
			fReader.close();
		} catch (IOException e) {
			klaidosKodas = "Failo " + fName.getName() + " skaitymo klaida";
		}
		return klaidosKodas;
	}
	
	/**
	 * Užkloja ArrayList kolekcijos metodą toString
	 * @return mašinos objektą išvedimui
	 */
	@Override
    public String toString() {
		String kolekcija = "";
        for (Automobilis a: mašinos) {
	        kolekcija += a.toString() + "\n";
		}
		return kolekcija;
    };
	
} // Klasės AutomobiliuApskaita pabaiga

