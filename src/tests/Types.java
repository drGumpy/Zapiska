package tests;

import java.io.File;

//dane na arkusza kalkulacyjnego z danymi o wzorcowaniu
class Types{
 public File file;
 public String path;
 public int points;
 public boolean RH;
 String Sheetname;
 public String probe;
 int datacol;
 int timecol;
 int gaps;
 int startdata;
 int gap=1;
 int devicenum;
 // ustalenie arkusza docelowego
 void Filesset(File file){
     this.file=file;
 }
 // ustalenie liczby punktów pomiarowyc
 void pointset(int points){
     this.points=points;
 }
 void probeset(String probe){
     this.probe=probe;
 }
 // wprowadzenie danych na temat wzorcowania i arkusza
 void dataset(boolean Rh){
     this.RH=Rh;
     if(RH){ //arkusz "Zapiska Temp & RH"
         datacol=34;
         timecol=3;
         gaps= 45;
         startdata =10;
         gap=2;
         Sheetname = "Zapiska Temp & RH";
         probe = "61602551";
         devicenum=21;
     }else{    //arkusz "Zapiska temp."
         datacol=33;
         timecol=2;
         gaps= 34;
         startdata=9;
         Sheetname = "Zapiska temp.";
         probe = "20098288";
         devicenum=31;
     }
 }
}
