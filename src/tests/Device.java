package tests;

//dane o urządzeniach
class Device{
	String model;
	String type;
	String producent;
	String resolutionT;
	String resolutionRh;
	String[] channel;
	public String toString(){
		return type+"\t"+model+"\t"+producent;
	}
}
