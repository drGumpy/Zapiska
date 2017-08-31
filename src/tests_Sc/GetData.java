package tests;
 
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
 
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;


public class GetData {
    static private ArrayList<Dev> devices = new ArrayList<Dev>();
    static private ArrayList<Data> point = new ArrayList<Data>();
    
    static private Dev patern = new Dev();
    
    static private Types typ = new Types();
        
    static void setData(boolean Rh){
        typ.dataset(Rh);    
    }
    
    static void setFile(File  add_file){
        typ.Filesset(add_file);
    }
    
    // pobranie danych wzorca
    private static void _findProbeData(int points) throws IOException{
        final Sheet sheet = SpreadSheet.createFromFile(typ.file).getSheet(typ.Sheetname);
        patern.time = new String[points];
        patern.dataT= new double[points][10];
        patern.standardT= new double[points];
        patern.averageT= new double[points];
        if(typ.RH){
            patern.dataRh= new double[points][10];
            patern.standardRh= new double[points];
            patern.averageRh= new double[points];
        }
        patern.name="wzorzec";
        int line = typ.startdata-typ.gap;
        for(int j=0; j<points; j++){
            int col=typ.timecol;
            for(int k=0; k<10; k++){
                patern.time[j] =MetrologyMath.parseTime(
                        sheet.getValueAt(typ.timecol, line-2).toString());
                String a, b;
                a=sheet.getValueAt(col,line).toString();
                b=sheet.getValueAt(col+2,line).toString();
                patern.dataT[j][k]=Double.parseDouble(a+"."+b);
                if(typ.RH){
                    a=sheet.getValueAt(col,line+1).toString();
                    b=sheet.getValueAt(col+2,line+1).toString();
                    patern.dataRh[j][k]=Double.parseDouble(a+"."+b);
                }
                col+=3;
            }
            patern.standardT[j] = MetrologyMath.standardDeviation(patern.dataT[j]);
            patern.averageT[j] = MetrologyMath.average(patern.dataT[j]);
            if(typ.RH){
                patern.standardRh[j] = MetrologyMath.standardDeviation(patern.dataRh[j]);
                patern.averageRh[j] = MetrologyMath.average(patern.dataRh[j]);
            }
            line+=typ.gaps;
        }
        //System.out.println(patern);
    }
    
    static Dev getPatern() throws IOException{
        _findProbeData(point.size());
        return patern;
    }
    
    static ArrayList<Data> getPoint(){
        return point;
    }
    
    //pobranie danych o urządzeniach
    static ArrayList<Dev> findData(int points) throws IOException{
        final Sheet sheet = SpreadSheet.createFromFile(typ.file).getSheet(typ.Sheetname);
        for(int i=0; i<points; i++){
            Data add = new Data(typ.RH);
            add.time = sheet.getValueAt(typ.timecol,6+typ.gaps*i).toString();
            if(typ.RH){
                add.temp =sheet.getValueAt(0,6+typ.gaps*i).toString();
                add.hum =sheet.getValueAt(1,6+typ.gaps*i).toString();
            }else
                add.temp =sheet.getValueAt(1,6+typ.gaps*i).toString();
            point.add(add);
        }
        for(int i=0; i<typ.devicenum; i++){
            Dev device = new Dev();
            
            int line = typ.startdata+typ.gap*i;
            device.name= sheet.getValueAt(1,line).toString();
            if(device.name.equals(""))
                continue;
            int col;
            device.num=i;
            device.q= new boolean[points];
            device.dataT= new double[points][10];
            device.standardT= new double[points];
            device.averageT= new double[points];
            if(typ.RH){
                device.dataRh= new double[points][10];
                device.standardRh= new double[points];
                device.averageRh= new double[points];
            }
            for(int j=0; j<points; j++){
                col=typ.timecol;
                try{
                    for(int k=0; k<10; k++){
                        String a, b;
                        a=sheet.getValueAt(col,line).toString();
                        b=sheet.getValueAt(col+2,line).toString();
                    //    if(a.equals("")) break;
                        device.dataT[j][k]=Double.parseDouble(a+"."+b);
                        if(typ.RH){
                            a=sheet.getValueAt(col,line+1).toString();
                            if(a.equals("-"))
                                device.dataRh[j][k]=-1;    
                            else{
                                b=sheet.getValueAt(col+2,line+1).toString();
                                device.dataRh[j][k]=Double.parseDouble(a+"."+b);
                            }
                        }
                        col+=3;
                    }
                    device.standardT[j] = MetrologyMath.standardDeviation(device.dataT[j]);
                    device.averageT[j] = MetrologyMath.average(device.dataT[j]);
                    if(typ.RH){
                        device.standardRh[j] = MetrologyMath.standardDeviation(device.dataRh[j]);
                        device.averageRh[j] = MetrologyMath.average(device.dataRh[j]);
                    }
                }catch(NumberFormatException e){
                    device.q[j]=true;
                }
                line+=typ.gaps;
            }            
            devices.add(device);
        }
        return devices;
    }
    
    static void print(){
        System.out.println("punkty pomiarowe");
        for(int i=0; i<point.size(); i++){
            System.out.println(point.get(i));
        }
        System.out.println("dane z wzorca");
        try{
            System.out.println(patern);
            }catch (NullPointerException e) {
                System.out.println("brak");
            }
        
        System.out.println("urządzenia");
        for(int i=0; i<devices.size(); i++){
             try{
                System.out.println(devices.get(i));
             }catch (NullPointerException e) {
                 System.out.println("brak");
             }
        }
        
    }
}
 
