package tests;

class DataProbe {
	boolean question=true;
    int valueT;
    int valueRh;
    double correctionT;
    double correctionRh;
    double uncertaintyT;
    double uncertaintyRh;
    double driftT;
    double driftRh;
    
    public String toString(){
        String s;
        s=valueT+"\t\t";
        s+=correctionT+"\t";
        s+=uncertaintyT+"\t";
        s+=driftT+"\t";
        try {
            s+="\n";
            s+=valueRh+"\t\t";
            s+=correctionRh+"\t";
            s+=uncertaintyRh+"\t";
            s+=driftRh+"\t";
        }catch (NullPointerException e){
            
        }
        return s;
    }
}
