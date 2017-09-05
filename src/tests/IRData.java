package tests;

class IRData {
	double[] blackBodyError;
	double emissivity;
	int distance;
	double[] reference;
	
	public String toString(){
		return String.format(DisplayedText.distanceIR, distance);
	}

}
