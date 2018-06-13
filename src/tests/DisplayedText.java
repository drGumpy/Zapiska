package tests;

public class DisplayedText {
	static String account = "C:\\Users\\"+System.getProperty("user.name")+"\\Desktop\\";
	
	static String probeDataPath = "P:\\Laboratorium\\Wyniki z wzorca\\";
	
	static String dataPath = account+"Laboratorium\\generacja\\";
	
	static String sheet = account+"Laboratorium.ods";
	
	static String emissivity= "Emisyjność źródła ε=%.2f.";
	static String distanceIR = "Odległość pirometru wzorcowanego od źródła w czasie wzorcowania wynosiła: %d mm";
	
	static String calibraionSheet = "Świadectwo wzorcowania";
	static String noteSheet = "Wyniki wzorcowania";
	
	static String calibrationDevice = "%s, model: %s, producent: %s, nr seryjny: %s";
	static String calibrationProbe1 = ", z %s, nr seryjny: %s.";
	static String calibrationProbe2 = ", z %s model %s, producent: %s, nr seryjny: %s.";
	static String enviromentT = "Temperatura: ";
	static String enviromentRh = "Wilgotność: ";
	static String[] notePath = {account+"Laboratorium\\Wyniki wzorcowań\\Zapiski\\",
			dataPath+"z_T.ods",
			dataPath+"z_T.ods",
			dataPath+"z_Rh.ods",
			dataPath+"z_T.ods"};
	static String[] certificatePath={account+"Laboratorium\\Wyniki wzorcowań\\Świadectwa wzorcowania\\",
			dataPath+"sw_T.ods",
			dataPath+"sw_Tx2.ods",
			dataPath+"sw_Rh.ods",
			dataPath+"sw_IR.ods"};
	
	
	static String channel = "Czujnik temperatury: %s (nazwa kanału: %s)";

			
}
