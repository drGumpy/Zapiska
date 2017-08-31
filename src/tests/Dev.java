package tests;

//wyniki pomiarów urządzenia
class Dev{
  int num;
  boolean[] q;
  String[] time;
  double[][] dataT;
  double[][] dataRh;
  double[] standardT;
  double[] standardRh;
  double[] averageT;
  double[] averageRh;
  String name;
  public String toString(){
      String s="-------- \n";
      s=name+"\n";
      for(int i=0; i<dataT.length;i++){
          for(int j=0; j<dataT[i].length; j++)
              s+=dataT[i][j]+" ";
          s+="\t"+averageT[i]+"\t"+standardT[i]+"\n";}
      try {
          for(int i=0; i<dataRh.length;i++){
              for(int j=0; j<dataT[i].length; j++)
                  s+=dataRh[i][j]+" ";
              s+="\t"+averageRh[i]+"\t"+standardRh[i]+"\n";}
      }catch (NullPointerException e) {
          s+="brak danych dla wilgotności\n";
      }
      s+="-------- \n";
      return s;
  }
}
