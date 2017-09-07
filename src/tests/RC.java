package tests;

class RC extends Data{
    RC(boolean RH) {
        super(RH);
    }
    
    public int getN(){
        return 23;
    }
 
    public Data divide(String line){
        String[] Data = line.split("\t");
        if(Data.length<2)
            System.out.println("error:"+ line);
        String[] when =Data[2].split(" ");
        Data d= new Data(RH);
        d.num =Integer.parseInt(Data[0]);
        
         String[] linedate = when[0].split("-");
         d.date = linedate[2]+"."+linedate[1]+"."+linedate[0];
         
         String[] linetime = when[1].split(":");
         d.time = linetime[0]+":"+linetime[1];
         
         String[] tempData=Data[4].split("\\.");
        if(tempData.length>1)
            d.temp = tempData[0]+","+tempData[1];
        else
            d.temp =tempData[0]+",0";
         
         if(RH){
             String[] humData=Data[6].split("\\.");
            if(humData.length>1)
                d.hum = humData[0]+","+humData[1];
            else
                d.hum =humData[0]+",0";
         }
         
         return d;    
    }
}
