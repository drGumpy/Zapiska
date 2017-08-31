package tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class RhProbe extends PaternProbe {
    //zbieranie danych o wzorcu
    RhProbe(File file) throws FileNotFoundException{
        Scanner sc = new Scanner(file);
        n=sc.nextInt();
        driftT = sc.nextDouble();
        driftRh = sc.nextDouble();
        data = new DataProbe[n];
        for(int i=0; i<n; i++){
            data[i] = new DataProbe();
            data[i].valueT= Integer.parseInt(sc.next());
            data[i].valueRh=sc.nextInt();
            data[i].correctionT=sc.nextDouble();
            data[i].correctionRh=sc.nextDouble();
            data[i].uncertaintyT=sc.nextDouble();
            data[i].uncertaintyRh=sc.nextDouble();
            data[i].driftT=driftT;
            data[i].driftRh=driftRh;
        }
        //zakresy pomiarowe
        rangeNum=sc.nextInt();
        ranges = new int[rangeNum][4];
        // t_min, t_max, Rh_min, Rh_max
        for(int i=0; i<rangeNum; i++){
            for(int j=0; j<4; j++){
                ranges[i][j]=sc.nextInt();
            }
        }
        sc.close();
    }
    
    //dane o sondzie dla konkretnego punktu t, Rh
    DataProbe get(int t, int Rh){
        for(int i=0; i<n; i++){
            if(data[i].valueT ==t && data[i].valueRh==Rh)
                return data[i];
        }
        for(int i=0; i<rangeNum; i++){
            if(ranges[i][0]<=t && ranges[i][1]>=t){
                if(ranges[i][2]<=Rh && ranges[i][3]>=Rh){
                    return find(ranges[i], t, Rh);
                }
            }
        }
        DataProbe s=new DataProbe();
        s.question=false;
        return s;
    }
    
    //Przeszukiwanie zakresów pomiarowych
    private DataProbe find(int[] range, int t, int rh) {
    	DataProbe sol = new DataProbe();
        //sprawdzenie zakresów
        if(t==range[0] || t==range[1]){
            sol=easyCalculateT(t, rh, range[2], range[3]);
            sol.valueT=t;
            sol.valueRh=rh;
            return sol;
        }
        if(rh==range[2] || rh==range[3]){
            sol=easyCalculateRh(t, rh, range[0], range[1]);
            sol.valueT=t;
            sol.valueRh=rh;
            return sol;
        }
        DataProbe d1 = null, d2=null, d3=null, d4=null;
        int b=0;
        
        for(int i=0; i<n; i++){
            if(data[i].valueT ==range[0] && data[i].valueRh==range[2]){
                d1=data[i];
                b++;
                continue;
            }
            if(data[i].valueT ==range[1] && data[i].valueRh==range[2]){
                d2=data[i];
                b++;
                continue;
            }
            if(data[i].valueT ==range[0] && data[i].valueRh==range[3]){
                d3=data[i];
                b++;
                continue;
            }
            if(data[i].valueT ==range[1] && data[i].valueRh==range[3]){
                d4=data[i];
                b++;
                continue;
            }
            if(b==4) break;
        }
        if(b<4){
            sol.question=false;
            return sol;
        }
 
        // wyliczenia dla punktu pomiarowego
        double correctionT= (t-range[0])/(range[1]-range[0]);
        double correctionRh = (rh-range[2])/(range[3]-range[2]);
        sol.valueRh=rh;
        sol.valueT=t;
        sol.correctionRh = MetrologyMath.calculate(correctionT, correctionRh, d1.correctionRh, d2.correctionRh,
                d3.correctionRh, d4.correctionRh);
        sol.correctionT = MetrologyMath.calculate(correctionT, correctionRh, d1.correctionT, d2.correctionT,
                d3.correctionT, d4.correctionT);
        sol.driftRh = driftRh;
        sol.driftT = driftT;
        sol.uncertaintyRh= Math.max(Math.max(d1.uncertaintyRh, d2.uncertaintyRh),
                Math.max(d3.uncertaintyRh, d3.uncertaintyRh));
        sol.uncertaintyT = Math.max(Math.max(d1.uncertaintyT, d2.uncertaintyT),
                Math.max(d3.uncertaintyT, d3.uncertaintyT));
        return sol;
    }
    
    //wyznaczanie wartości poprawek
    private DataProbe easyCalculateRh(int t, int rh, int t1, int t2){
    	DataProbe d1 = null, d2=null;
        int b=0;
        for(int i=0; i<n; i++){
            if(data[i].valueT ==t1 && data[i].valueRh==rh){
                d1=data[i];
                b++;
                continue;
            }
            if(data[i].valueT ==t2 && data[i].valueRh==rh){
                d2=data[i];
                b++;
                continue;
            }            
            if(b==2) break;
        }
        double cor= (t-t1)/(t2-t1);
        return MetrologyMath.easyCalculate(cor, d1, d2);
    }
    
  //wyznaczanie wartości poprawek
    private DataProbe easyCalculateT(int t, int rh, int rh1, int rh2) {
    	DataProbe d1 = null, d2=null;
        int b=0;
        for(int i=0; i<n; i++){
            if(data[i].valueT ==t && data[i].valueRh==rh1){
                d1=data[i];
                b++;
                continue;
            }
            if(data[i].valueT ==t && data[i].valueRh==rh2){
                d2=data[i];
                b++;
                continue;
            }            
            if(b==2) break;
        }
        double cor= (rh-rh1)/(rh2-rh1);
        return MetrologyMath.easyCalculate(cor, d1, d2);
    }
}
