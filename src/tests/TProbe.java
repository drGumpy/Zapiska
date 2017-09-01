package tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class TProbe extends PaternProbe {
    //zbieranie danych o wzorcu
    TProbe(File file) throws FileNotFoundException{
        Scanner sc = new Scanner(file);
        n=sc.nextInt();
        driftT = sc.nextDouble();
        data = new DataProbe[n];
        sc.nextLine();
        for(int i=0; i<n; i++){
            data[i] =new DataProbe();
            data[i].valueT=sc.nextInt();
            data[i].correctionT=sc.nextDouble();
            data[i].uncertaintyT=sc.nextDouble();
            data[i].driftT=driftT;
        }
        //zakresy pomiarowe
        rangeNum=sc.nextInt();
        ranges = new int[rangeNum][2];
        for(int i=0; i<rangeNum; i++){
            for(int j=0; j<2; j++){
                ranges[i][j]=sc.nextInt();
            }
        }
        sc.close();
    }
    
    //dane o sondzie dla konkretnego punktu t, Rh
    DataProbe get(int t, int Rh){
        for(int i=0; i<n; i++){
            if(data[i].valueT ==t)
                return data[i];
        }
        for(int i=0; i<rangeNum; i++){
            if(ranges[i][0]<=t && ranges[i][1]>=t){
                    return find(ranges[i], t);
            }
        }
        DataProbe s=new DataProbe();
        s.question=false;
        return s;
    }
    
    //Przeszukiwanie zakresów pomiarowych
    private DataProbe find(int[] range, int t) {
    	DataProbe d1 = null, d2=null, sol = new DataProbe();
        int b=0;
        
        for(int i=0; i<n; i++){
            if(data[i].valueT ==range[0]){
                d1=data[i];
                b++;
                continue;
            }
            if(data[i].valueT ==range[1]){
                d2=data[i];
                b++;
                continue;
            }
            if(b==2) break;
        }
        if(b<2){
            sol.question=false;
            return sol;
        }
        // wyliczenia dla punktu pomiarowego
        double correctionT= (t-range[0])/(range[1]-range[0]);
        sol.valueT=t;
        sol.correctionT = MetrologyMath.calculate(correctionT, d1.correctionT, d2.correctionT);
        sol.driftT = driftT;
        sol.uncertaintyT = Math.max(d1.uncertaintyT, d2.uncertaintyT);
        return sol;
    }    
}
