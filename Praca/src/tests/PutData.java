package tests;
    
import java.io.BufferedReader;
import java.io.File;
 
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
    
    
public class PutData {
        
    //parametry wzorcowania i plik docelowy
    static boolean Rh = true;                            //wzorcowanie wilgotności
    static int points = 5;                                 //liczba punktów wzorcowania
    static File file= new File("C:/Users/Laboratorium/Desktop/Laboratorium.ods");
    
    //zmienne z danymi o wzorcowaniu
    static Types typ = new Types();                        //dane o wzocoeania
    static ArrayList<Data> logger = new ArrayList<Data>();     //wzorcowanie rejejestraotory
    static ArrayList<Data> punkt = new ArrayList<Data>();     //informacje o punktach wzorcowania
        
    //sortowanie punktów na podstawie daty i godzinny
    private static void _sort(Data[] punkty){
        double[] arr = new double[points];
        for(int i=0; i<points;i++){
            String[] date = punkty[i].date.split("\\.");
            String[] time = punkty[i].time.split(":");
            if(date.length>=3 && time.length>=2){
                arr[i]=(Integer.parseInt(date[2])-2010)*400+Integer.parseInt(date[1])*31+Integer.parseInt(date[0]);
                arr[i]=arr[i]*1440+Integer.parseInt(time[0])*60+Integer.parseInt(time[1]);
                arr[i]+=(double)punkty[i].num/100;
            }
        }
        Arrays.sort(arr);
        for(int i=0; i<points;i++){
            int d =(int)Math.round((arr[i]*100)%100);
            punkt.add(i,punkty[d]);
        }
    }
        
    //wyszczególnianie danych z wzorca.        
    private static Data _divide(String line){
        StringTokenizer st = new StringTokenizer(line);
        String[] num = {st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken()};
        Data d =new Data(Rh);
        d.temp = num[3];
        d.hum = num[2];
        String[] time = num[1].split(":");
        d.time = time[0]+":"+time[1];
        return d;
    }
        
    //wprowadzanie danych z wzorca        
    private static void _putProbeData(){
            //numer seryjny sondy
            String typeProbe = typ.probe;
            Pattern probe = Pattern.compile(typeProbe, Pattern.CASE_INSENSITIVE);
            //punkt pomiarowy
            int num=0;          
            //folder z plikami
            File path = new File("C:/Users/Laboratorium/Desktop/Wyniki z wzorca/");
            String[] list;
            //lista pilków z wynikiami wzorca + sortowanie po nazwie
            list=path.list(); //
            Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
            try{
                final Sheet sheet = SpreadSheet.createFromFile(typ.file).getSheet(typ.Sheetname);
                for(; num<points; num++){
                    //data wzorcowania punktów
                    String[] date = punkt.get(num).date.split("\\.");
                    String findDate = date[2]+date[1]+date[0];
                    Pattern patternDate = Pattern.compile(findDate, Pattern.CASE_INSENSITIVE);
                    int fileNum=2;
                    boolean correctName=true;
                    //szukanie pliku z danymi - sprawdzanie data+ nr. sondy
                    do{
                        fileNum++;
                        if(fileNum==list.length) break;
                        Matcher matcherDate = patternDate.matcher(list[fileNum].toString());
                        Matcher matcherProbe = probe.matcher(list[fileNum].toString());
                        if(matcherDate.find() && matcherProbe.find()) correctName=false;
                    }while(correctName);
                    String line;
                    if(!correctName){
                    System.out.println("Otwieranie pliku: "+list[fileNum]);
                    BufferedReader in;
                    in = new BufferedReader(new FileReader("C:/Users/Laboratorium/Desktop/Wyniki z wzorca/"+list[fileNum]));
                    //wyrzucenie pustych lini
                    for(int i=0; i<47; i++){
                       line = in.readLine();
                    }      
                    line = in.readLine();
                    while(line != null){
                        //podział danych na: godzinę, dzień, wskazania wzorca
                        Data d = new Data(Rh);
                        d = _divide(line);
                        //wprowadzanie danych z punktu pomiarowego 10 pomiarów dla punktu
                        if(d.time.equals(punkt.get(num).time)){
                            boolean two= false;
                            System.out.println("punkt pomiarowy "+(punkt.get(num).num+1)+" godzina: "+punkt.get(num).time);
                            if(num<punkt.size()-1){
                                two= punkt.get(num).time.equals(punkt.get(num+1).time);
                                }
                            int count=0;
                            do{
                                int col= typ.timecol+3*count;
                                int line1 = typ.startdata-typ.gap+typ.gaps*punkt.get(num).num;
                                String[] temp = d.temp.split(",");
                                String[] hum = d.hum.split(",");
                                sheet.setValueAt(temp[0], col, line1);
                                if(temp.length>1)
                                    sheet.setValueAt(temp[1], col+2, line1);
                                if(typ.RH){
                                    sheet.setValueAt(hum[0], col, line1+1);
                                    if(hum.length>1)
                                        sheet.setValueAt(hum[1], col+2, line1+1);
                                    }
                                if(two){
                                    line1 = typ.startdata-typ.gap+typ.gaps*punkt.get(num+1).num;
                                    sheet.setValueAt(temp[0], col, line1);
                                    if(temp.length>1)
                                        sheet.setValueAt(temp[1], col+2, line1);
                                    if(typ.RH){
                                        sheet.setValueAt(hum[0], col, line1+1);
                                        if(hum.length>1)
                                            sheet.setValueAt(hum[1], col+2, line1+1);
                                        }
                                }
                                line=in.readLine();
                                if(line != null)
                                d= _divide(line);
 
                                count++;
                            }while(count<10);
                            if(two) {
                                num++;
                                System.out.println("punkt pomiarowy "+(punkt.get(num).num+1)+" godzina: "+punkt.get(num).time);
                            }
                            if(num+1<points){
                                if(punkt.get(num).date.equals(punkt.get(num+1).date)){
                                    num++;
                                    continue;}
                            }
                            break;
                        }
                        line = in.readLine();
                    }
                    in.close();
                    }else{
                        fileNum=2;
                        System.out.println("brak pasującego pliku z dnia: "+punkt.get(num).date);
                    }
                }
                //zapis pliku
                sheet.getSpreadSheet().saveAs(typ.file);
            }catch(IOException e){
                e.printStackTrace();
            }
        }    
 
    //szukanie dostępnych plików z wynikami z rejestratorów i dane o punktach pomiarowych        
    static private void _putLoggerData(){
        try{
            final Sheet sheet = SpreadSheet.createFromFile(typ.file).getSheet(typ.Sheetname);
            //ścieżka do danych z rejestratorów + ich rozszerzenie
            String[] path = {
                    "C:\\Users\\Laboratorium\\Documents\\rejestratory\\Hobo\\",
                    "C:\\Users\\Laboratorium\\Documents\\rejestratory\\DT-172\\",
                    "C:\\Users\\Laboratorium\\Documents\\rejestratory\\testo\\",
                    "C:\\Users\\Laboratorium\\Documents\\rejestratory\\RC\\",
                    "C:\\Users\\Laboratorium\\Documents\\rejestratory\\Lascar\\",
                    "C:\\Users\\Laboratorium\\Documents\\rejestratory\\EBI\\"
                    };
            String[] kon = {".txt",".csv",".csv",".txt",".txt",".csv"};
            //szukanie danych rejestratorów uwzględnionych w zapisce
            for(int i=0; i<typ.devicenum; i++){
                String name = sheet.getValueAt(1,typ.startdata+typ.gap*i).toString();
                for(int j=0; j<path.length; j++){
                    String filelocation = path[j] + name + kon[j];
                    File f = new File(filelocation);
                    if(f.exists()){
                        Data das;
                        switch(j){
                            case 0:{
                                das = new Onset(Rh);
                                break;
                            }
                            case 1:{
                                das = new CEM(Rh);
                                break;
                            }
                            case 2:{
                                das = new Testo(Rh);
                                break;
                            }
                            case 3:{
                                das = new RC(Rh);
                                break;
                            }
                            case 4:{
                                das = new Lascar(Rh);
                                break;
                            }
                            default: das = new EBI(Rh);
                        }
                        System.out.println("znaleziono plik z danymi dla: "+filelocation);
                        das.set(i,f);
                        logger.add(das);
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    static ArrayList<Data> getPoints() throws IOException{
        typ.dataset(Rh);
        typ.Filesset(file);
        final Sheet sheet = SpreadSheet.createFromFile(typ.file).getSheet(typ.Sheetname);
        Data[] punkty = new Data[points];
        for (int i=0; i< points; i++){
            Data pr = new Data(Rh);
            pr.time = MetrologyMath.parseTime(sheet.getValueAt(typ.timecol,6+typ.gaps*i).toString());
            pr.date = MetrologyMath.parseDate(sheet.getValueAt(typ.datacol,6+typ.gaps*i).toString());
            pr.num=i;
            if(Rh){
                pr.temp=sheet.getValueAt(0,6+typ.gaps*i).toString();
                pr.hum=sheet.getValueAt(1,6+typ.gaps*i).toString();
            }else{
                pr.temp=sheet.getValueAt(1,6+typ.gaps*i).toString();
            }
            punkty[i]= pr;
        }
        _sort(punkty);
        return punkt;
    }
   
    static void clean(){
        logger.removeAll(logger);
    }
   
    static void set(boolean Rh_, File file_, int points_){
        Rh=Rh_;
        file=file_;
        points=points_;
    }
    
    static void run(){
 
        try{
            //zebranie danych z arkusza
            _putLoggerData();
            _putProbeData();
            final Sheet sheet = SpreadSheet.createFromFile(typ.file).getSheet(typ.Sheetname);
            //dane z rejestratorów
            if(logger.size()==0) System.out.println("brak danych z rejestratorów do wprowadzenia");
            else for(int i=0; i<logger.size();i++){
                int g = logger.get(i).number;
                System.out.println("wprowadzanie danych z pliku: "+ logger.get(i).file);
                Scanner sc = new Scanner(logger.get(i).file);
                //odrzucenie lini pliku bez danych liczbowych
                for(int j=0; j<logger.get(i).getN(); j++){
                    sc.nextLine();
                }
                int j =0;
                
                while(sc.hasNextLine()){
                    //rozdział danych z lini
                    String dd = sc.nextLine();
                    if(dd.equals(""));
                        Data d= logger.get(i).divide(dd);
                    //wprowadzanie danych dla punktu
                    if(d.time.equals(punkt.get(j).time) && d.date.equals(punkt.get(j).date)){
                        System.out.println("pobieranie danych dla punktu: "+ (punkt.get(j).num+1));
                        int count=0;
                        do{
                            int col= typ.timecol+3*count;
                            int line = typ.startdata+typ.gap*g+typ.gaps*punkt.get(j).num;
                            String[] temp = d.temp.split(",");
                            sheet.setValueAt(temp[0], col, line);
                            if(temp.length>1)
                                sheet.setValueAt(temp[1], col+2, line);
                            else
                                sheet.setValueAt("0", col+2, line);
                            if(typ.RH){
                                String[] hum = d.hum.split(",");
                                sheet.setValueAt(hum[0], col, line+1);
                                if(hum.length>1)
                                    sheet.setValueAt(hum[1], col+2, line+1);
                                else
                                    sheet.setValueAt("0", col+2, line+1);
                            }
                            //czy koniec pliku
                            if(sc.hasNextLine())
                            d= logger.get(i).divide(sc.nextLine());
                            else{
                                System.out.println("koniec pliku dla: "+ d.time+" "+d.date);
                                break;
                            }
                            count++;
                        }while(count<10);
                        j++;
                        if(j==points) break;
                    }
                    if(!sc.hasNextLine()){
                        System.out.println("brak danych dla punktu: "+(punkt.get(j).num+1));
                        j++;
                        if(j==points) break;
                        sc= new Scanner(logger.get(i).file);
                        for(int k=0; k<logger.get(i).getN(); k++){
                            sc.nextLine();
                        }
                    }
                }
                sc.close();
                sheet.getSpreadSheet().saveAs(typ.file);
            }
        }catch(IOException e){
               e.printStackTrace();
        }
        System.out.print("koniec wprowadzania danych");
    }
    
    public static void main(String[] args){
        try {
            punkt= getPoints();
        }catch (IOException e) {
            e.printStackTrace();
        }
        run();
    }
    
}