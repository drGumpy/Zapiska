package tests;
 
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
 
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

public class CertificateData {
    
	static int calibration;
    static Types typ = new Types();
    private static File file= new File("C:\\Users\\Laboratorium\\Desktop\\Laboratorium kopia.ods");
    
    //Spis nie wystawionych certyfikatów wzorcowania
    private static ArrayList<Certificate> data = new ArrayList<Certificate>();
        
    //Spis danych o klientach
    private static HashMap<String, Client> clientsData =new HashMap<String, Client>();
    
    //Spis sond stosowanych przy wzrcowanych urządzeniach
    private static HashMap<String, Probe> probesData =new HashMap<String, Probe>();
    
    //Spis typów wzorcowanych urządzeń
    private static HashMap<String, Device> devicesData =new HashMap<String, Device>();
    
    //Wyszukiwanie nie wsytawionych świadectw - brak daty wzorcowania
    private static void _order() throws IOException{
        final Sheet sheet = SpreadSheet.createFromFile(file).getSheet("Zlecenia");
        int d=1;
        //wyszukiwanie pierwszego pola bez daty wzorcowania
        while(sheet.getValueAt(2,d)!="") d++;
        //wczytywanie zleceń do pierwszego braku urządzenia do wzorcowania
        while(sheet.getValueAt(5,d)!=""){
            if(sheet.getValueAt(2,d)==""){
            Certificate order = new Certificate();
            order.num = sheet.getValueAt(1,d).toString();
            order.declarant.name= sheet.getValueAt(3,d).toString();
            clientsData.put(order.declarant.name, order.declarant);
            order.user.name= sheet.getValueAt(4,d).toString();
            clientsData.put(order.user.name, order.user);
            order.device.model= sheet.getValueAt(5,d).toString();
            order.deviceSerial= sheet.getValueAt(6,d).toString();
            order.probe.model= sheet.getValueAt(7,d).toString();
            String probe =sheet.getValueAt(8,d).toString();
            order.probe_ser=probe;
            if(probe.equals(",")){
                String[] arr = {""};
                order.probeSerial = arr;
            }else{
                probe=probe.replaceAll("\\s+", "");
                order.probeSerial= probe.split(",");
            }
            order.calibrationCode=sheet.getValueAt(9,d).toString();
            order.calibrationDate=sheet.getValueAt(10,d).toString();
            devicesData.put(order.device.model, order.device);
            probesData.put(order.probe.model, order.probe);
            data.add(order);
            //System.out.println(order);
            }
            d++;
        }
    }
    
    private static int _check(String code){
    	try{
    		int number = Integer.parseInt(code);
    		return number;
    	}catch(NumberFormatException e){
    		return 0;
    	}
    }
    
    private static int _findCode(Certificate cal){
		String calibrationCode= cal.calibrationCode.toUpperCase();
		String points="";
		try{
			points = calibrationCode.substring
					(calibrationCode.indexOf("(")+1,calibrationCode.indexOf(")"));
			calibrationCode = calibrationCode.replace("-N("+points+")", "");
		}catch(StringIndexOutOfBoundsException e){}
		calibrationCode=calibrationCode.replaceAll("SW", "");
		String[] data = calibrationCode.split("x");
		int s;
		try{
			s=Integer.parseInt(data[0]);
			cal.point=StandardPoint.point(points,calibration);
			if(s==2)
				cal.channelNumber=2;
			else if(data.length==2){
				int channel = _check(data[1]);
				cal.channelNumber=channel;
				
			}else{
				cal.channelNumber=1;
			}
		}catch(NumberFormatException e){
			s=0;
		}
    	return s;
    }
    
    private static void _verificate(){
    	for(int i=0; i<data.size(); i++){
    		boolean q=true;
    		int s= _findCode(data.get(i));
    		if(s==calibration) q=false;
    		if(calibration==1 && s==2) q= false;
    		if(q){
    			data.remove(i);
    			i--;
    		}
    	}
    }
    
    //poszukiwanie klientów zlecających wzorcowanie/użytkowaników urządzenia
    private static void _findClientData() throws IOException{
        final Sheet sheet = SpreadSheet.createFromFile(file).getSheet("Klienci");
        int i=0;
        String name;
        while(sheet.getValueAt(0,i)!=""){
            name = sheet.getValueAt(0,i).toString();
            if(clientsData.containsKey(name)){
                Client andrzej = new Client();
                andrzej.name=name;
                andrzej.address= sheet.getValueAt(1,i).toString();
                andrzej.postalCode= sheet.getValueAt(2,i).toString();
                andrzej.place= sheet.getValueAt(3,i).toString();
                clientsData.put(andrzej.name, andrzej);
            }
            i++;
        }        
    }
    
    //wyszukiwanie danych o wzorcowanych urządzeniach
    private static void _findDeviceData() throws IOException{
        final Sheet sheet = SpreadSheet.createFromFile(file).getSheet("Urządzenia");
        int i=0;
        String model;
        while(sheet.getValueAt(0,i)!=""){
            model = sheet.getValueAt(0,i).toString();
            if(devicesData.containsKey(model)){
                Device nunczaku = new Device();
                nunczaku.model= model;
                nunczaku.type= sheet.getValueAt(1,i).toString();
                nunczaku.producent= sheet.getValueAt(2,i).toString();
                nunczaku.resolutionT= sheet.getValueAt(4,i).toString();
                nunczaku.resolutionRh= sheet.getValueAt(7,i).toString();
                String chanel = sheet.getValueAt(11,i).toString();
                if(chanel.equals(",")){
                    String[] arr = {""};
                    nunczaku.channel = arr;
                }
                else
                    nunczaku.channel = chanel.split(",");
                devicesData.put(nunczaku.model, nunczaku);
            }
            i++;
        }
    }
    
    //pobiera dane o zastosowanych sondach
    private static void _findProbeData() throws IOException{
        final Sheet sheet = SpreadSheet.createFromFile(file).getSheet("Sondy");
        int i=0;
        String model;
        while(sheet.getValueAt(0,i)!=""){
            model = sheet.getValueAt(0,i).toString();
            if(probesData.containsKey(model)){
                Probe nunczaku = new Probe();
                nunczaku.model=model;
                nunczaku.type= sheet.getValueAt(1,i).toString();
                nunczaku.producent= sheet.getValueAt(2,i).toString();
                probesData.put(nunczaku.model, nunczaku);
            }
            i++;
        }
    }
    
    //wprowadza uzyskane dane do klas certificate
    private static void _gather(){
        Certificate c= new Certificate();
        for (int i=0; i<data.size(); i++){
            c=data.get(i);
            c.declarant = clientsData.get(data.get(i).declarant.name);
            c.user = clientsData.get(data.get(i).user.name);
            c.device = devicesData.get(data.get(i).device.model);
            if(probesData.containsKey(data.get(i).probe.model)){
                c.probe = probesData.get(data.get(i).probe.model);
            }
            else
                c.probe=new Probe();
            data.set(i, c);
        }
    }
    
    //przekazanie danych o wzocowaniu 
    static ArrayList<Certificate> getData(){
            return data;
        }
 
    static void setFile(File _file){
        file=_file;
    }
    
    
    static void print(){
        for(int i=0; i<data.size(); i++){
            System.out.println("nr"+data.get(i).num+"\nzgłaszający:");
            System.out.println(data.get(i).declarant +"\nużytkownik:");
            System.out.println(data.get(i).user +"\nurządzenie:");
            System.out.println(data.get(i).device + "\tnr. "+data.get(i).deviceSerial+"\nsonda:");
            System.out.println(data.get(i).probe + "\tnr. "+data.get(i).probeSerial[0]+"\nkod:");
            System.out.println(data.get(i).calibrationCode +"\ndata:");
            System.out.println(data.get(i).calibrationDate +"\n");
        }
    }
    
    //otrzymanie danych o wzorcowniu
    static void run() throws IOException{
        _order();
        _verificate();
        _findClientData();
        _findDeviceData();
        _findProbeData();
        _gather();
    }
}
