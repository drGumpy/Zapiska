package tests;

public class StandardPoint {
	
	static int toInt(String data){
		data = data.replaceAll("[^\\d.]", "");
		return Integer.parseInt(data);
	}
	
	static int[] humPoint(String data){
		int[] point = new int[2];
		data = data.replaceAll("\\[|\\]", "");
		System.out.println(data);
		String[] d = data.split(";");
		point[0]= toInt(d[0]);
		point[1]= toInt(d[1]);
		return point;
	}
	
	static int[][] point(String data, int code){
		data= data.replaceAll(" ", "");
		String[] points = data.split(",");
		int[][] point;
		switch(code){
		case 1:
		case 2:
		case 5:
			point = new int[1][points.length];
			for(int i=0; i<points.length; i++){
				point[0][i]= toInt(points[i]);
			}
			break;
		case 3:
			point = new int[2][points.length];
			for(int i=0; i<points.length; i++){
				int[] number = humPoint(points[i]);
				point[0][i]= number[0];
				point[1][i]= number[1];
			}
			break;
		default: return null;
		}
		return point;	
	}

	static int[][] point(int code){
		int[][] point;
		switch(code){
		case 1:
		case 2:
			point = new int[][] {{-25,0,25}};
			break;
		case 3:
			point = new int[][] {{15,25,25,25,35},{50,30,50,70,50}};
			break;
		case 5:
			point = new int[][] {{25,90,180}};
			break;
		default: return null;
		}
		return point;
	}
	
}
