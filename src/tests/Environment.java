package tests;
 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
 
public class Environment {
	//dane o wzorcowaniu
    private DataProbe[] certificateData= new DataProbe[4];
    
    double[] new_data = new double[4];
    
    int[] range = new int[4];
    
    //plik z informacjami o urzÄ…dzeniu do odczytu danych
    private File doc = new File("C:\\Users\\Laboratorium\\Desktop\\Laboratorium\\generacja\\w-srod.txt");
    
    //pobranie danych o wzorcowaniu
    private void _find(){
        try {
            Scanner sc = new Scanner(doc);
            for(int i=0; i<4; i++){
            	certificateData[i]= new DataProbe();
            	certificateData[i].valueT=sc.nextInt();
            	certificateData[i].valueRh=sc.nextInt();
            	certificateData[i].correctionT=sc.nextDouble();
            	certificateData[i].correctionRh=sc.nextDouble();
            	certificateData[i].uncertaintyT=sc.nextDouble();
            	certificateData[i].uncertaintyRh=sc.nextDouble();                
            }
            sc.close();
            range[0] =certificateData[0].valueT;
            range[1] =certificateData[3].valueT;
            range[2] =certificateData[0].valueRh;
            range[3] =certificateData[3].valueRh;
        }catch (FileNotFoundException e){}    
    }
    
    // uwzglÄ™dnienie poprawek
    private void _findData(double a, double b, int i){
        double correctionT = (a-range[0])/(range[1]-range[0]);
        double correctionRh = (b-range[2])/(range[3]-range[2]);
        double     c_t= MetrologyMath.calculate(correctionT, correctionRh,
        			certificateData[0].correctionT, certificateData[2].correctionT,
        			certificateData[1].correctionT, certificateData[3].correctionT),
                c_Rh=MetrologyMath.calculate(correctionT, correctionRh,
                		certificateData[0].correctionRh, certificateData[2].correctionRh,
                		certificateData[1].correctionRh, certificateData[3].correctionRh);
        
        new_data[0+i]= a + c_t;
        new_data[2+i]= b + c_Rh;
    }
    
    //uzyskanie danych wyjÅ›ciowych
    private void _corection() {
        double a= Math.max(Math.max(certificateData[0].uncertaintyT, certificateData[1].uncertaintyT),
                Math.max(certificateData[2].uncertaintyT, certificateData[3].uncertaintyT)),
               b= Math.max(Math.max(certificateData[0].uncertaintyRh, certificateData[1].uncertaintyRh),
                        Math.max(certificateData[2].uncertaintyRh, certificateData[3].uncertaintyRh));
        new_data[0]-=a;
        new_data[1]+=a;
        new_data[2]-=b;
        new_data[3]+=b;
        new_data[0]=MetrologyMath.round_d(new_data[0], 0.1);
        new_data[1]=MetrologyMath.round_d(new_data[1], 0.1);
        new_data[2]=MetrologyMath.round_d(new_data[2], 0.1);
        new_data[3]=MetrologyMath.round_d(new_data[3], 0.1);
    }
    
    void print(){
        for(int i=0; i<certificateData.length; i++)
            System.out.println(certificateData[i]+"\n");
    }
    
    //przekazanie danych do bezpoÅ›redniego umieszczenia na Å›wiadectwie
    String[] calculateData(double[] data){
        _find();
        _findData(data[0], data[2], 0);
        _findData(data[1], data[3], 1);
        _corection();
        String[] solution = new String[2];
        solution[0]= "("+new_data[0]+ " ÷ "+new_data[1]+")°C";
        solution[0]= solution[0].replace(".", ",");
        solution[1]= "("+new_data[2]+ " ÷ "+new_data[3]+")%Rh";
        solution[1]= solution[1].replace(".", ",");
        return solution;
    }    
}