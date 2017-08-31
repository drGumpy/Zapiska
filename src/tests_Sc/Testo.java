package tests;

class Testo extends Data{
    Testo(boolean RH) {
        super(RH);
    }
    
    public int getN(){
        return 2;
    }
 
    public Data divide(String line){
        String[] Data = line.split(";");
        String[] when =Data[1].split(" ");
        
        Data d= new Data(RH);
        d.num =Integer.parseInt(Data[0]);
        
        String[] linedate = when[0].split("-");
        d.date = linedate[2]+"."+linedate[1]+"."+linedate[0];
        
        String[] linetime = when[1].split(":");
         d.time = linetime[0]+":"+linetime[1];
         String[] tempData=Data[2].split(",");
        if(tempData.length>1)
            d.temp = Data[2];
        else
            d.temp =tempData[0]+",0";
        if(RH){
            String[] humData=Data[3].split(",");
            if(humData.length>1)
                d.hum = Data[3];
            else
                d.hum =humData[0]+",0";        
        }
        return d;
    }
}
