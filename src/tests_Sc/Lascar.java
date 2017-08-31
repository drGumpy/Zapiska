package tests;

class Lascar extends Data{
    Lascar(boolean RH) {
        super(RH);
    }
    
    public int getN(){
        return 1;
    }
 
    public Data divide(String line){
        String[] Data = line.split(",");
        if(Data.length<2)
            System.out.println("error:"+ line);
        String[] when =Data[1].split(" ");
        Data d= new Data(RH);
        d.num =Integer.parseInt(Data[0]);
        
         String[] linedate = when[0].split("-");
         d.date = linedate[2]+"."+linedate[1]+"."+linedate[0];
         
         String[] linetime = when[1].split(":");
         d.time = linetime[0]+":"+linetime[1];
         
         d.temp= Data[2].replace(".", ",");
         
         if(RH){
             d.hum= Data[3].replace(".", ",");
         }
         
         return d;    
    }
}
