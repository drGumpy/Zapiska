package tests;

//dane klienta
class Client{
  String name;
  String address;
  String postalCode;
  String place;
  public String toString(){
      return address+", "+postalCode+" "+place;
  }
}
