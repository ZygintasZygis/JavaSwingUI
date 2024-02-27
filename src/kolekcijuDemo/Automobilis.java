/** 
 * Tai demonstracinė Automobilio klasė iš Lab2 darbo,
 *		importuojama iš paketo kolekcijuDemo.
 * Klasė supaprastinta (atskirta nuo paketo studijosKTU).
*/

package kolekcijuDemo;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Automobilis implements Comparable<Automobilis>, Serializable {
    
    // bendri duomenys visiems automobiliams (visai klasei)
    final static private int priimtinųMetųRiba = 1994;
    final static private int esamiMetai  = LocalDate.now().getYear();
    
    final static private double valKursas = esamiMetai <= 2014? 1: 1/3.4528;
    final static private double minKaina =     200.0;
    final static private double maxKaina = 120_000.0;

    // kiekvieno automobilio individualūs duomenys
    private String markė;
    private String modelis;
    private int gamMetai;  
    private int rida;
    private double kaina; 

    
    public Automobilis() {
    }
    public Automobilis(String markė, String modelis,
                        int gamMetai, int rida, double kaina){
        this.markė = markė;
        this.modelis = modelis;
        this.gamMetai = gamMetai;
        this.rida = rida;
        this.kaina = kaina;
    }
    public Automobilis(String dataString){
        this.parse(dataString);
    }

    public Automobilis create(String dataString) {
        Automobilis a = new Automobilis();
        a.parse(dataString);
        return a;
    }

    public final void parse(String dataString) {
        try {   // ed - tai elementarūs duomenys, atskirti tarpais
            Scanner ed = new Scanner(dataString);
            markė   = ed.next();
            modelis = ed.next();
            gamMetai= ed.nextInt();
            rida    = ed.nextInt();
            setKaina(ed.nextDouble());
        } catch (InputMismatchException  e) {
            System.err.println("Blogas duomenų formatas apie auto -> " + dataString);
        } catch (NoSuchElementException e) {
            System.err.println("Trūksta duomenų apie auto -> " + dataString);
        }
    }

    public String validate() {
        String klaidosTipas = "";
        if (gamMetai < priimtinųMetųRiba || gamMetai > esamiMetai)
           klaidosTipas = "Netinkami gamybos metai, turi būti [" +
                   priimtinųMetųRiba + ":" + esamiMetai + "]";
        if (kaina < minKaina || kaina > maxKaina )
            klaidosTipas += " Kaina už leistinų ribų [" + minKaina +
                            ":" + maxKaina  + "]";
        return klaidosTipas;
    }
    @Override
    public String toString(){  // surenkama visa reikalinga informacija
        return String.format("%-20s %-15s %8d %15d %8.1f %s",
               markė, modelis, gamMetai, rida, kaina, validate());
    };
    public String getMarkė() {
        return markė;
    }
    public String getModelis() {
        return modelis;
    }
    public int getGamMetai() {
        return gamMetai;
    }
    public int getRida() {
        return rida;
    }
    public double getKaina() {
        return kaina;
    }
    // keisti bus galima tik kainą - kiti parametrai pastovūs
    public void setKaina(double kaina) {
        this.kaina = kaina;
    }
    @Override
    public int compareTo(Automobilis a) {    //kam reikalingas sis metodas?
        // lyginame pagal svarbiausią požymį - kainą
        int metaiKiti = a.getGamMetai();
        if (gamMetai < metaiKiti) return -1;
        if (gamMetai > metaiKiti) return +1;
        return 0;
    }

	public final static Comparator pagalKainą = new Comparator() {
       // sarankiškai priderinkite prie generic interfeiso ir Lambda funkcijų
       @Override
       public int compare(Object o1, Object o2) {
          double k1 = ((Automobilis) o1).getKaina();
          double k2 = ((Automobilis) o2).getKaina();
          // didėjanti tvarka, pradedant nuo mažiausios
          if(k1<k2) return -1;
          if(k1>k2) return 1;
          return 0;
       }
    };
	
	/**
	 * Komparatorius Trečiojo JTable stulpelio "Rida" rikiavimui
	 */
    public static Comparator<Integer> pagalRida = (o1, o2) -> {
		return o1.compareTo(o2);
    };	
}
