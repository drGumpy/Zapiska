package tests;

abstract class PaternProbe{
    protected int n;
    protected int rangeNum;
    protected DataProbe[] data;
    protected double driftT, driftRh;
    protected int[][] ranges;
    void print(){
        System.out.println("odnaleźione punkty:");
        for(int i=0; i<data.length; i++){
            System.out.println(data[i]);
        }
        System.out.println("zakresy pomiarowe");
        for(int i=0; i<ranges.length; i++){
            for(int j=0; j<ranges[i].length; j++)
                System.out.print(ranges[i][j]+" ");
            System.out.println("");
        }
    }
    DataProbe get(int t, int Rh){
        return new DataProbe();
    }
}
