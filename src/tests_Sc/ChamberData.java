package tests;

//dane o wzorcowanym punkcie komory
class ChamberData{
 int valueRh;
 int valueT;
 double t1;
 double t2;
 double Rh1;
 double Rh2;
 
 public String toString(){
     return valueT+"\t"+t1+"\t"+t2+"\t"+valueRh+"\t"+Rh1+"\t"+Rh2;
 }
}
