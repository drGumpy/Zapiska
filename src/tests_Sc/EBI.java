package tests;

class EBI extends Data{
    EBI(boolean RH) {
        super(RH);
    }
    
    public int getN(){
        return 10;
    }
 
    public Data divide(String line){
        String[] Data = line.split(",");
        String[] when =Data[0].split(" ");
        
        Data d= new Data(RH);
        
        String[] linedate = when[0].split("-");
        if(linedate.length<2)
            System.out.println(linedate[0]);
        d.date = linedate[2]+"."+linedate[1]+"."+linedate[0];
        if(when.length<2)
            d.time="00:00";
        else{
            String[] linetime = when[1].split(":");
            d.time = linetime[0]+":"+linetime[1];
        }
         String[] tempData;
         
         if(RH){
             String[] humData=Data[1].split("\\.");
            if(humData.length>1)
                d.hum = humData[0]+","+humData[1];
            else
                d.hum =humData[0]+",0";
            tempData=Data[2].split("\\.");
            if(tempData.length>1)
                d.temp = tempData[0]+","+tempData[1];
            else
                d.temp =tempData[0]+",0";
         }else{
             tempData=Data[1].split("\\.");
             if(tempData.length>1)
                 d.temp = tempData[0]+","+tempData[1];
             else
                 d.temp =tempData[0]+",0";
         }
         
         return d;    
    }
}
