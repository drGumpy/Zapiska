package tests;
 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Chamber {
    private boolean Rh;
    private int[][] ranges;
    private int rangeNum;
    private int n;
    private ArrayList<ChamberData> standardPoint= new ArrayList<ChamberData>();
    private ChamberData[] data;
    
    //pobranie danych o komorze z pliku
    private void _find(File file) throws FileNotFoundException{
        Scanner in= new Scanner(file);
        n= in.nextInt();
        for(int i=0; i<n; i++){
        	ChamberData d = new ChamberData();
            d.valueT = in.nextInt();
            if(Rh)
                d.valueRh= in.nextInt();
            d.t1= in.nextDouble();
            d.t2= in.nextDouble();
            if(Rh){
                d.Rh1= in.nextDouble();
                d.Rh2= in.nextDouble();
            }
            standardPoint.add(d);
        }
        rangeNum=in.nextInt();
        int m;
        if(Rh){
            ranges= new int[rangeNum][4];
            m=4;
        }else{
            ranges= new int[rangeNum][2];
            m=2;
        }
        for(int i=0; i<rangeNum; i++){
            for(int j=0; j<m; j++){
                ranges[i][j]= in.nextInt();
            }
        }
        in.close();
    }
 
    //odnalezienie maksimów
    private ChamberData _getMax(ChamberData d1, ChamberData d2){
    	ChamberData d= new ChamberData();
        d.t1 = Math.max(d1.t1, d2.t1);
        d.t2 = Math.max(d1.t2, d2.t2);
        if(Rh){
            d.Rh1 = Math.max(d1.Rh1, d2.Rh1);
            d.Rh2 = Math.max(d1.Rh2, d2.Rh2);
        }        
        return d;
    }
    
    // wyznaczenie danych o punkcie  dla wzorcowania t +Rh  
    private ChamberData _calculateRh(int t, int rh, int t1, int t2) {
    	ChamberData d1 = null, d2=null, d=null;
        int b=0;
        for(int i=0; i<standardPoint.size(); i++){
            if(standardPoint.get(i).valueT ==t1 && standardPoint.get(i).valueRh==rh){
                d1=standardPoint.get(i);
                b++;
                continue;
            }
            if(standardPoint.get(i).valueT ==t2 && standardPoint.get(i).valueRh==rh){
                d2=standardPoint.get(i);
                b++;
                continue;
            }            
            if(b==2) break;
        }
        if(b<2) return new ChamberData();
        d=_getMax(d1, d2);
        d.valueT=t;
        d.valueRh=rh;
        return d;
    }
 
    // wyznaczenie danych o punkcie  dla wzorcowania t +Rh 
    private ChamberData _calculateT(int t, int rh, int rh1, int rh2) {
    	ChamberData d1 = null, d2=null, d=null;
        int b=0;
        for(int i=0; i<n; i++){
            if(standardPoint.get(i).valueT ==t && standardPoint.get(i).valueRh==rh1){
                d1=standardPoint.get(i);
                b++;
                continue;
            }
            if(standardPoint.get(i).valueT ==t && standardPoint.get(i).valueRh==rh2){
                d2=standardPoint.get(i);
                b++;
                continue;
            }            
            if(b==2) break;
        }
        if(b<2) return d;
        d=_getMax(d1, d2);
        d.valueT=t;
        d.valueRh=rh;
        return d;
    }
    
    // wyznaczenie danych o punkcie dla wzorcowania t +Rh
    private ChamberData _findPoint(int[] range, int t, int rh) {
        //sprawdzenie zakresów
        if(t==range[0] || t==range[1]){
            return _calculateT(t, rh, range[2], range[3]);
        }
        if(rh==range[2] || rh==range[3]){
            return _calculateRh(t, rh, range[0], range[1]);
        }
        ChamberData d1 = null, d2=null, d3=null, d4=null, d = new ChamberData();
        int b=0;
        for(int i=0; i<standardPoint.size(); i++){
            if(standardPoint.get(i).valueT ==range[0] &&
            		standardPoint.get(i).valueRh==range[2]){
                d1=standardPoint.get(i);
                b++;
                continue;
            }
            if(standardPoint.get(i).valueT ==range[1] &&
            		standardPoint.get(i).valueRh==range[2]){
                d2=standardPoint.get(i);
                b++;
                continue;
            }
            if(standardPoint.get(i).valueT ==range[0] &&
            		standardPoint.get(i).valueRh==range[3]){
                d3=standardPoint.get(i);
                b++;
                continue;
            }
            if(standardPoint.get(i).valueT ==range[1] &&
            		standardPoint.get(i).valueRh==range[3]){
                d4=standardPoint.get(i);
                b++;
                continue;
            }
            if(b==4) break;
        }
        if(b<4) return d;
        d=_getMax(_getMax(d1,d2), _getMax(d3,d4));
        d.valueT=t;
        d.valueRh=rh;
        return d;
    }
 
    //znalezienie danych o punkcie pomiarowym dla wzorcowania t
    private ChamberData _findPoint(int[] range, int t){
    	ChamberData d1 = null, d2=null, d=new ChamberData();
        int b=0;
        for(int i=0; i<standardPoint.size(); i++){
            if(standardPoint.get(i).valueT ==range[0]){
                d1=standardPoint.get(i);
                b++;
                continue;
            }
            if(standardPoint.get(i).valueT ==range[1]){
                d2=standardPoint.get(i);
                b++;
                continue;
            }            
            if(b==2) break;
        }
        if(b<2) return d;
        d=_getMax(d1, d2);
        d.valueT=t;
        return d;
    }
    
    //znalezienie danych o punkcie pomiarowym dla wzorcowania t +Rh
    private ChamberData _findPointRh(Data point){
        int t = Integer.parseInt(point.temp);
        int Rh = Integer.parseInt(point.hum);
 
        for(int i=0; i<standardPoint.size(); i++){
            if(t==standardPoint.get(i).valueT
                && Rh == standardPoint.get(i).valueRh){
                    return standardPoint.get(i);
                    }
        }
        for(int i=0; i<rangeNum; i++){
            if(ranges[i][0]<=t && ranges[i][1]>=t){
                if(ranges[i][2]<=Rh && ranges[i][3]>=Rh){
                    return _findPoint(ranges[i], t, Rh);
                }
            }
        }
        ChamberData d = new ChamberData();
        return d;
    }
    
    //znalezienie danych o punkcie pomiarowym dla wzorcowania t
    private ChamberData _findPointT(Data point){
        int t = Integer.parseInt(point.temp);
        for(int i=0; i<standardPoint.size(); i++){
            if(t==standardPoint.get(i).valueT){
                    return standardPoint.get(i);
            }
        }
        for(int i=0; i<rangeNum; i++){
            if(ranges[i][0]<t && ranges[i][1]>t){
                    return _findPoint(ranges[i], t);
            }
        }
        ChamberData d = new ChamberData();
        return d;
    }    
    
    //przypisanie danych o punktach pomiarowych
    void getPoints(ArrayList<Data> points){
        int num= points.size();
        data= new ChamberData[num];
        for(int i=0; i<num; i++){
            if(Rh)
                data[i]=_findPointRh(points.get(i));
            else
                data[i]=_findPointT(points.get(i));
        }
    }
    
    //rozpoczęcie programu
    void start(boolean Rh){
        this.Rh=Rh;
        File file;
        if(Rh)
            file= new File(DisplayedText.dataPath+"12-03914 Rh.txt");
        else
            file= new File(DisplayedText.dataPath+"12-03914 t.txt");
        try {
            _find(file);
        } catch (FileNotFoundException e) {
            System.out.println("zły plik");
            e.printStackTrace();
        }
    }
    
    void print(){
        System.out.println("Punty do ze świadectwa:");
        for(int i=0; i<standardPoint.size();i++){
            System.out.println(i+"\t"+standardPoint.get(i));
        }
        System.out.println("zakresy:");
        for(int i=0; i<rangeNum;i++){
            System.out.print(i);
            for(int j=0; j<ranges[i].length; j++)
                System.out.print("\t"+ranges[i][j]);
            System.out.println("");
        }
        try {
            System.out.println("wyniki punktów:");
            for(int i=0; i<data.length;i++){
                System.out.println(i+"\t"+data[i]);
            }
        }catch (NullPointerException e) {
                System.out.println("brak wprowadzonych punktów pomiarowych");
        }
    }
    // przekazanie informacji o plikach
    ChamberData[] get(){
        return data;
    }
    
}
