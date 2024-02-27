 package sasajaSuMeniu;

/**
 * @author Aleksas
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.io.*;
import java.util.*;
import kolekcijuDemo.Automobilis;

/**
 * Tai nedidelis grafinės vartotojo sąsajos pavyzdys su valdymo komandomis iš
 * meniu. Vartotojo sąsaja suprogramuota, t.y. NetBeans dizaineriu nesinaudota.
 * <p>
 * Duomenų ir rezultatų išvedimui naudojami sąsajoe elementai JTextArea
 * ir JTable, papildomos informacijos įvedimui/išvedimui - klasės JOptionPane
 * dialogai.
 * <p>
 * IŠSIAIŠKINKITE elementaraus menių sudarymo principus ir jo veikimą (įvykių
 * apdorojimą). 
 * <p>
 * Įvykiu apdorojime panaudoti įvairūs variantai: vidinė klasė,
 * anoniminė klasė, lambda išraiška , nuoroda į metodą.
 * Naudokite tuos variantus, kurie jums patinka (arba tai, ką pasiūlo NetBeans dizaineris).
 * <p>
 * SVARBU: Šioje klasėje atliekamas tik metodų iš jūsų individualaus paketo iškvietimas.
 */
public class SasajaSuMeniu extends JFrame {

	private final JMenuBar meniuBaras = new JMenuBar();
	private Container sasajosLangoTurinys;
	private final JTextArea taInformacija = new JTextArea(20, 50);	// Informacijos išvedimo langas
	private final JScrollPane zona = new JScrollPane(taInformacija);
	private final JLabel taAntraste = new JLabel("Rezultatai");
	private final JPanel paInfo = new JPanel();	// Duomenų ir rezultatų panelis. Paleidus programą jis nėra rodomas - 
												//  rodomas tik nuskaičius duomenų failą. 
	private JMenuItem miMarkė;
	private JMenuItem miKaina;
	private JLabel perspejimas = new JLabel("Tai tik mažas GUI PAVYZDYS - aklai nekopijuoti !!!", SwingConstants.CENTER);

	private AutomobiliuApskaita apskaita = new AutomobiliuApskaita(); // metodų klasės objektas

	/**
	 * Klasės konstruktorius - nustatymai ir meniu įdiegimas.
	 */
	public SasajaSuMeniu() {

		// Nustatymai:
		Locale.setDefault(Locale.US); // suvienodiname skaičių formatus
		Font f = new Font("Courier New", Font.PLAIN, 12); // Suvienodinam simbolių pločius (lygiavimui JTextArea elemente)
		taInformacija.setFont(f);

		meniuIdiegimas();

		miMarkė.setEnabled(false);	// neaktyvi, nes nėra duomenų (failas nenuskaitytas)
		miKaina.setEnabled(false);	// neaktyvi
	}

	/**
	 * Suformuoja meniu, priskiria įvykius ir įdiegia jų veiksmus. 
	 *	Sudėtingesni meniu komandos veiksmai realizuoti atskirais metodais ar atskira klase.
	 */
	private void meniuIdiegimas() {
		setJMenuBar(meniuBaras);
		JMenu mFailai = new JMenu("Failai");
		meniuBaras.add(mFailai);
		JMenu mAuto = new JMenu("Automobilių apskaita");
		mAuto.setMnemonic('a'); // Alt + a
		meniuBaras.add(mAuto);
		JMenu mPagalba = new JMenu("Pagalba");
		meniuBaras.add(mPagalba);

		//  Grupė  "Failai" :
		JMenuItem miSkaityti = new JMenuItem("Skaityti iš failo...");
		mFailai.add(miSkaityti);
		miSkaityti.addActionListener(this::veiksmaiSkaitantFailą);  // įdiegimas nuoroda į metodą
		//miSkaityti.addActionListener((e) -> veiksmaiSkaitantFailą(e));  // galima metodą kviesti ir lambda išraiškomis
		//miSkaityti.addActionListener((e) -> veiksmaiSkaitantFailą());  // jei metodo antraštė būtų be parametro, tai kviestume taip
		JMenuItem miBaigti = new JMenuItem("Pabaiga");
		miBaigti.setMnemonic('b'); // Alt + b
		mFailai.add(miBaigti);
		// kadangi išėjimo iš programos metodas trumpas, jo turinį parašome vietoje
		miBaigti.addActionListener((ActionEvent e) -> System.exit(0));

		//	Grupė "Automobiliu apskaita"
		miMarkė = new JMenuItem("Atranka pagal markę…");
		mAuto.add(miMarkė);
		miMarkė.addActionListener(this::atrankaPagalMarke);

		miKaina = new JMenuItem("Surikiuoja pagal kainą"); 
		mAuto.add(miKaina);
		miKaina.addActionListener(new ActionListenerImpl());

		//    Grupė  "Pagalba" :
		JMenuItem miDokumentacija = new JMenuItem("Paketo Dokumentacija");
		mPagalba.add(miDokumentacija);
		miDokumentacija.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,
				ActionEvent.ALT_MASK));
		miDokumentacija.addActionListener(new VeiksmaiDokumentacija()); // įdiegimas vidine klase
		JMenuItem miGreitojiPagalba = new JMenuItem("Greitoji Pagalba :-)");
		mPagalba.add(miGreitojiPagalba);
		miGreitojiPagalba.addActionListener((e) -> veiksmaiGreitojiPagalba());

		JMenuItem miApie = new JMenuItem("Apie programą ...");
		mPagalba.add(miApie);
		miApie.addActionListener((ActionEvent event)
				-> JOptionPane.showMessageDialog(SasajaSuMeniu.this,
						"Demo programa - sąsaja su meniu\nVersija 2.3\n2016 spalis",
						"Apie...", JOptionPane.INFORMATION_MESSAGE));

		// Sukuriamas JPanel elementas informacijai išvesti ir padedamas į JFrame langą.
		//  ** Rodomas tik nuskaičius duomenų failą (metode veiksmaiSkaitantFailą) **
		paInfo.setLayout(new BorderLayout());
		paInfo.add(taAntraste, BorderLayout.NORTH);
		paInfo.add(zona, BorderLayout.CENTER);
		perspejimas.setFont(new Font("Arial", Font.PLAIN, 18));
		getContentPane().add(perspejimas); // po failo skaitymo žymė perspejimas bus pašalinta


		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // kad veiktų lango uždarymas ("kryžiukas")

	} // Metodo meniuIdiegimas pabaiga

	/**
	 * Metodas yra kviečiamas vykdant meniu punktą "Skaityti iš failo..."
	 *
	 * @param e	klasės ActionEvent objektas. Jis būtinas metodo pagal nuorodą (::) iškvietimui 
	 *		(kviečiant šį metodą su lambda, jo antraštė gali būti ir be šio parametro).
	 */
	public void veiksmaiSkaitantFailą(ActionEvent e) {
		JFileChooser fc = new JFileChooser(".");  // "." tam, kad rodytų projekto katalogą
		fc.setDialogTitle("Atidaryti failą skaitymui");
		fc.setApproveButtonText("Atidaryti");
		int rez = fc.showOpenDialog(SasajaSuMeniu.this);
		if (rez == JFileChooser.APPROVE_OPTION) {
			// veiksmai, kai pasirenkamas atsakymas “Open"
			if (!paInfo.isShowing()) {
				// Jei JPanel objektas paInfo dar neįdėtas į JFrame
				sasajosLangoTurinys = getContentPane();
				sasajosLangoTurinys.remove(perspejimas);
				sasajosLangoTurinys.setLayout(new FlowLayout());
				sasajosLangoTurinys.add(paInfo);
				validate();
			}

			File f1 = fc.getSelectedFile();
			String klaidosKodas = apskaita.loadAndPrint(f1, taInformacija);
			
			if (klaidosKodas != null) {
				JOptionPane.showMessageDialog(SasajaSuMeniu.this, klaidosKodas,
					"Skaitymas - rašymas", JOptionPane.INFORMATION_MESSAGE);
				
			}

			miMarkė.setEnabled(true);	// aktyvi - duomenys nuskaityti
			miKaina.setEnabled(true);
		} else if (rez == JFileChooser.CANCEL_OPTION) {
			JOptionPane.showMessageDialog(SasajaSuMeniu.this, // kad rodyti sąsajos lango centre (null rodytų ekrano centre)
					"Skaitymo atsisakyta (paspaustas ESC arba Cancel)",
					"Skaitymas - rašymas", JOptionPane.INFORMATION_MESSAGE);
		}
	} // metodo veiksmaiSkaitantFailą pabaiga

	/**
	 * Atrenka automobilius pagal pasirinktą jų markę. Markė įvedama įvedimo lange (JOptionPane.showInputDialog)
	 *
	 * @param e klasės ActionEvent objektas.
	 */
	public void atrankaPagalMarke(ActionEvent e) {
		String markė = JOptionPane.showInputDialog(SasajaSuMeniu.this, "Įveskite markę", // vietoje SasajaSuMeniu.this parašius null, įvedimo langas nebus sąsajos lange
				"Tekstas", JOptionPane.WARNING_MESSAGE);
		if (markė == null) // Kai pasirinkta Cancel arba Esc
		{
			return;
		}

		// PIRMAS variantas: Atrinktieji įrašai išvedami į tą patį JTextArea elementą:
		taInformacija.append("\n  Atrinkti " + markė + " markės automobiliai :" + System.lineSeparator());
		apskaita.atrinktiPagalMarkę(markė, taInformacija);

		// ANTRAS variantas: Atrinktieji įrašai išvedami į atskitą JFrame objektą lentele JTable
		//   (lentelė gali būti dedama ir į pagrindinį sąsajos langą):
		JFrame fr = new JFrame();
		JPanel pa = new JPanel();
		pa.setLayout(new BorderLayout());
		JTable lentelė = new JTable();
		String stulpeliuVardai[] = {"Modelis", "Metai", "Rida", "Kaina"};
		DefaultTableModel lentelėsModelis = (DefaultTableModel) lentelė.getModel();
		//lentelė.setModel(lentelėsModelis);
		JScrollPane juosta = new JScrollPane(lentelė);
		pa.add(new JLabel("Atrinkti " + markė + " markės automobiliai:", SwingConstants.CENTER), BorderLayout.NORTH);
		pa.add(juosta, BorderLayout.CENTER);

		// Stulpelių vardai ir informacija:
		lentelėsModelis.setColumnIdentifiers(stulpeliuVardai);
		
		// Lentelės užpildymas:
		boolean b = apskaita.atrinktiPagalMarkęLentele(lentelėsModelis, markė);

		// Rikiavimui pagal stulpelius su pele
		TableRowSorter rikiuoklis = new TableRowSorter(lentelėsModelis);
		// Trečiam stulpeliui Rida panaudosim komparatoriu pagalRida (jis klaseje Automobilis, bet gali būti bet kur)
		rikiuoklis.setComparator(2, Automobilis.pagalRida);
		lentelė.setRowSorter(rikiuoklis);

		if (b) {
			// Jei įrašų atrinkta
			fr.add(pa);
			fr.setSize(300, 350);

			// Dėsim JTable lentelę į sąsajos lango centrą (kitaip lentelę padės į ekrano centrą):
			Dimension dydis = SasajaSuMeniu.this.getSize();	// sąsajos lango gydis
			Point vieta = SasajaSuMeniu.this.getLocation();	// sąsajos lango vieta (kairys-viršutinis kampas)
			fr.setLocation((vieta.x + dydis.width / 2) - fr.getSize().width / 2,
					(vieta.y + dydis.height / 2) - fr.getSize().height / 2);
			fr.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(SasajaSuMeniu.this,
					"<" + markė + "> markės automobilių nerasta.", "Atranka", JOptionPane.WARNING_MESSAGE);

		}
	} // Metodo atrankaPagalMarke pabaiga

	/**
	 * Išorinės programos (C:\Windows\System32\calc.exe) iškvietimo pavyzdys.
	 */
	public void veiksmaiGreitojiPagalba() {
		// Galimos programos vardo (exe failo) suformavimo alternatyvos:
		// Pirma:
		//String programa = System.getenv("windir") + File.separatorChar +
		//					 "system32" + File.separatorChar + "calc.exe";

		// Kita:
		String programa = "C:\\WINDOWS\\system32\\calc.exe";
		try {
			Process p = Runtime.getRuntime().exec(programa);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(SasajaSuMeniu.this,
					"Vykdomasis failas <" + programa + "> nerastas",
					"Klaida", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Įvykių apdorojimas atskira klase. Tai gali būti tiek vidinė (kaip šiuo atveju), 
	 *	tiek ir išorinė (ne private) klasė. Tai ypač patogiau tada, kai
	 *	įvykis turi daug veiksmų (daug kodo), kuriuos norime patalpinti kitame javos faile.
	 */
	private class VeiksmaiDokumentacija implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			File f = null;
			try {
				f = new File("dist\\javadoc\\index.html").getAbsoluteFile();
				Desktop.getDesktop().open(f);
				JOptionPane.showMessageDialog(SasajaSuMeniu.this,
						"Dokumentacija sėkmingai atidaryta naršyklės lange",
						"Informacija", JOptionPane.INFORMATION_MESSAGE);
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(SasajaSuMeniu.this,
						"Dokumentacijos failas <" + f.toString()
						+ "> nerastas(arba dokumentacija dar nesugeneruota)\n", "Klaida", JOptionPane.ERROR_MESSAGE);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(SasajaSuMeniu.this,
						"Dokumentacijos failo <" + f.toString()
						+ "> atidaryti nepavyko", "Klaida", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Programos paleidimo taškas: sukuriamas JFrame klasės vaiko (SasajaSuMeniu) objektas, 
	 *	nustatomas lango dydis ir vieta ir langas parodomas ekrane (metodas setVisible).
	 *
	 * @param args argumentų masyvas.
	 */
	public static void main(String[] args) {
		SasajaSuMeniu langas = new SasajaSuMeniu();
		langas.setSize(500, 400);
		langas.setLocation(200, 200);
		langas.setTitle("Vartotojo sąsajos su meniu  p a v y z d y s");
		langas.setVisible(true);
	}

    private class ActionListenerImpl implements ActionListener {

        public ActionListenerImpl() {
        }

        // įdiegimas anonimine klase
        @Override
        public void actionPerformed(ActionEvent e) {
            apskaita.rikiuojaPagalKainą(); // Klasės AutomobiliuApskaita metodas
            taInformacija.append("\n       SURIKIUOTA (pradinis sąrašas):\n");
            taInformacija.append(apskaita.toString());
        }
    }

}	// Klasės SasajaSuMeniu pabaiga
