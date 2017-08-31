package tests;

class IRData {
	double[] blackBodyError;
	double emissivity;
	int distance;
	double[] reference;
	
	public String toString(){
		return "Odległość pirometru wzorcowanego od źródła w czasie wzorcowania wynosiła: "+distance+ " mm";
	}

}
