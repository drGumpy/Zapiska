package tests;

import java.io.File;

//przechowywanie danych o wzorcowanych puntkach
class Data{
  int number=0;
  File file=null;
  boolean RH= true;
  int num=0;
  String date="";
  String time="";
  String temp="";
  String hum="";
  //ustalanie rodzaju wzorcowania t/ t/Rh
  Data(boolean RH){
      this.RH=RH;
  }
  //ustalenie wartości nr. przyrządu na liście i plik z danymi
  void set(int number, File file){
      this.number= number;
      this.file= file;
  }
  //podzielenie linii na dane
  public Data divide(String nextLine) {
      return new Data(RH);
  }
  // liczba linii bez danych liczbowych
  public int getN() {
      return 0;
  }
  public String toString(){
      return temp+"\t"+hum;
  }
}
