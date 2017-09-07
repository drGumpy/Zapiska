package tests;

class Onset extends Data{
    Onset(boolean RH) {
        super(RH);
    }
    
    public int getN(){
        return 3;
    }
    
    public Data divide(String line){
        String[] Data = line.split(";");
        String[] when =Data[1].split(" ");
        
        Data d= new Data(RH);
        d.num =Integer.parseInt(Data[0]);
        
        String[] linedate = when[0].split("\\.");
        
        d.date = linedate[1]+"."+linedate[0]+".20"+linedate[2];
        
        d.temp = Data[2];
        
        if(RH) d.hum = Data[3];
        
        String[] linetime = when[1].split(":");
        d.time = linetime[0]+":"+linetime[1];
        return d;
    }
}
