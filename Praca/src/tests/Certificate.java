package tests;

//dane do świadectwa
class Certificate{
  String num;
  Client user= new Client();
  Client declarant= new Client();
  Device device = new Device();
  String deviceSerial;
  Probe probe= new Probe();
  String[] probeSerial =null;
  String probe_ser;
  String calibrationDate;
  String calibrationCode;
  int[][] point;
  int channelNumber;
  IRData pyrometr;
  
  public String toString(){
      String s= "numer świadectwa: "+num+"\n";
      s+="Zgłaszający:\n";
      s+=declarant+"\n";
      s+="Użytkownik:\n";
      s+=user+"\n";
      s+="Urządzenie:\n";
      s+=device+"\t"+deviceSerial+"\n";
      if(!probeSerial.equals("")){
          s+="Sonda:\n";
          s+=probe+"\t"+probeSerial+"\n";
      }
      s+="kod wzorcowania:\t"+calibrationCode+"\n";
      s+="data wzorcowania:\t"+calibrationDate;
      return s;
  }
}
