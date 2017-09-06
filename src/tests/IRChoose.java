package tests;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class IRChoose extends JDialog{
	private JRadioButton[] set;
	private JRadioButton[] copy;
	private JTextField[] emissivity;
	private JTextField[] distance;
	private JTextField[][] referenceValue;
			
	private JComboBox<String>[][] blackBodyChoose;
	
	private String path = DisplayedText.dataPath;
	
	private String[] blackBody= {"10000236", "10000220"};
	private static ArrayList<Certificate> data = new ArrayList<Certificate>();
	
	private int num;
	
	private boolean _compareArray(int[][] point, int[][] point2){
		if(point[0].length!=point2[0].length)
			return false;
		for(int i=0; i<point.length; i++){
			if(point[0][i]!=point2[0][i])
				return false;
		}
		return true;
	}
	
	private int _findNumber(){
		int max=0;
		for(int i=0; i<data.size(); i++){
			if(data.get(i).point[0].length>max)
				max = data.get(i).point[0].length;
		}
		return max;
	}
	
	private double _valideReferenceValue(String data){
		data = data.replace(",", ".");
		data = data.replace("[^\\d.]", "");
		double ans;
		try{
			ans = Double.parseDouble(data);
		}catch (NumberFormatException e){
			ans=0;
		}
		return ans;
	}
	
	private void _referenceValueListener(int num, int i){
		referenceValue[num][i].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				double check = _valideReferenceValue(referenceValue[num][i].getText());
				if(check ==0)
					referenceValue[num][i].setText(data.get(num).point[0][i]+".0");
				else
					referenceValue[num][i].setText(check+"");
			}
		});
	}
	
	private double _valideEmissivity(String data){
		data = data.replace(",", ".");
		data = data.replace("[^\\d.]", "");
		double ans;
		try{
			ans = Double.parseDouble(data);
		}catch (NumberFormatException e){
			ans=0.97;
		}
		if(ans>1 || ans<0) ans =0.97;
		return ans;
	}
	
	private int _valideDistance(String data){
		data = data.replace(",", ".");
		data = data.replace("[^\\d.]", "");
		data = data+".";
		data = data.substring(0, data.indexOf("."));
		int ans;
		try{
			ans = Integer.parseInt(data);
		}catch (NumberFormatException e){
			ans=200;
		}
		if(ans>1000 || ans<100) ans =200;
		return ans;
	}
	
	private JPanel _pointData(int num){
		JPanel jp = new JPanel();
	
		jp.setLayout(new GridBagLayout());
		int[][] array= data.get(num).point;	

		GridBagConstraints c= new GridBagConstraints();
		c.anchor =GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		JLabel[] points = new JLabel[array[0].length];
		
		int corretion=0;
		for(int i=0; i<array[0].length; i++){
			blackBodyChoose[num][i]= new JComboBox<String>();
			referenceValue[num][i] = new JTextField();
			points[i] = new JLabel();
			points[i].setText(array[0][i]+"");
			c.gridy++;
			c.gridx=0;
			c.ipadx=10;
			jp.add(points[i], c);
			c.gridx=1;
			jp.add(blackBodyChoose[num][i], c);
			c.gridx=2;
			jp.add(referenceValue[num][i], c);
			for(int j=0; j<blackBody.length; j++){
				blackBodyChoose[num][i].insertItemAt(blackBody[j], j);
				referenceValue[num][i].setText(array[0][i]+".0");
			}
			if(array[0][i]==25){
				corretion=1;
				blackBodyChoose[num][i].insertItemAt("radiator", blackBody.length);
				blackBodyChoose[num][i].setSelectedItem("radiator");
			}
			else
				blackBodyChoose[num][i].setSelectedItem(blackBody[(i+corretion)%2]);
			_referenceValueListener(num, i);
		}
		
		return jp;
	}
	
	private JPanel _device(int num){
		JPanel jp = new JPanel();
		jp.setMinimumSize(new Dimension(250, 200));
		jp.setLayout(new GridBagLayout());
		int[][] array= data.get(num).point;	
		blackBodyChoose[num]= new JComboBox[array[0].length];
		referenceValue[num]= new JTextField[array[0].length];	
		GridBagConstraints c= new GridBagConstraints();
		c.anchor = GridBagConstraints.PAGE_START;
		c.gridx=0;
		c.gridy=0;
		c.gridwidth=2;
		set[num] = new JRadioButton("aktywny");
		set[num].setSelected(true);
		set[num].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				boolean selected = set[num].isSelected();
				copy[num].setEnabled(selected);
				emissivity[num].setEditable(selected);
				distance[num].setEditable(selected);
				for(int j=0; j<array[0].length; j++){
					blackBodyChoose[num][j].setEnabled(selected);
					referenceValue[num][j].setEnabled(selected);
				}
			}
		});
		jp.add(set[num], c);
		
		c.gridy=1;
		c.gridwidth=2;
		copy[num] = new JRadioButton("zastosuj dla wszystkich");		
		copy[num].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				boolean selected = true;
				if(copy[num].isSelected()){
					selected = false;
				}
				if(!selected){
					int n= copy.length;
					String em = emissivity[num].getText();
					String dis = distance[num].getText();
					for(int i=0; i<n; i++){
						if(set[i].isSelected()
								&& _compareArray(data.get(i).point, data.get(num).point)){
							copy[i].setSelected(true);
							emissivity[i].setEditable(selected);
							emissivity[i].setText(em);
							distance[i].setEditable(selected);
							distance[i].setText(dis);
							for(int j=0; j<array[0].length; j++){ //
								blackBodyChoose[i][j].setSelectedItem(
										blackBodyChoose[num][j].getSelectedItem());
								blackBodyChoose[i][j].setEnabled(selected);
								referenceValue[i][j].setText(
										referenceValue[num][j].getText());
								referenceValue[i][j].setEnabled(selected);	
							}
						}
					}
				}else{
					emissivity[num].setEditable(selected);
					distance[num].setEditable(selected);
					for(int j=0; j<array[0].length; j++){
						blackBodyChoose[num][j].setEnabled(selected);
						referenceValue[num][j].setEnabled(selected);
					}
				}
			}
		});
		jp.add(copy[num], c);
		
		c.gridwidth=1;
		c.gridy=2;
		JLabel modelT = new JLabel();
		modelT.setText("model");
		jp.add(modelT, c);
		JTextField model = new JTextField(10);
		model.setEditable(false);
		model.setText(data.get(num).device.model);
		c.gridx=1;
		jp.add(model, c);
		
		c.gridx=0;
		c.gridy=3;
		JLabel serialT = new JLabel();
		serialT.setText("numer seryjny");
		jp.add(serialT, c);
		JTextField serial = new JTextField(10);
		serial.setEditable(false);
		serial.setText(data.get(num).deviceSerial);
		c.gridx=1;
		jp.add(serial, c);

		c.gridx=0;
		c.gridy=4;
		JLabel emissivityT = new JLabel();
		emissivityT.setText("emisyjność");
		jp.add(emissivityT, c);
		emissivity[num] = new JTextField(10);
		emissivity[num].setEditable(true);
		emissivity[num].setText("0,97");
		c.gridx=1;
		emissivity[num].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				double data = _valideEmissivity(emissivity[num].getText());
				emissivity[num].setText(data+"");
			}
		});
		jp.add(emissivity[num], c);
		
		c.gridx=0;
		c.gridy=5;
		JLabel distanceT = new JLabel();
		distanceT.setText("odlegległość");
		jp.add(distanceT, c);
		distance[num] = new JTextField(10);
		distance[num].setEditable(true);
		distance[num].setText("200");
		c.gridx=1;
		distance[num].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int data = _valideDistance(distance[num].getText());
				distance[num].setText(data+"");
			}
		});
		jp.add(distance[num], c);
		
		c.gridx=0;
		c.gridy=6;
		c.gridwidth=2;
		jp.add(_pointData(num), c);
		
		jp.setBorder(new TitledBorder(data.get(num).declarant.name));
		return jp;
	}
	
	private void _close(){
		this.dispose();
	}
	
	private int[] _findPoints(){
		HashSet<Integer> points = new HashSet<Integer>();
		int correction=0;
		int size = data.size();
		for(int i=0; i<size-correction; i++){
			if(!set[i].isSelected()){
				continue;
			}
			for(int j=0; j<data.get(i).point[0].length; j++)
				points.add(data.get(i).point[0][j]);
		}
		int[] array= new int[points.size()];
		int i=0;
		for(Integer n: points){
			array[i]=n.intValue();
			i++;					
		}
		Arrays.sort(array);
		return array;
	}
	
	private void _remove(ArrayList<Integer> toRemove){
		for(int i=0; i<toRemove.size(); i++){
				data.remove(toRemove.get(i).intValue());
		}
	}
	
	private void _setData(){
		int[] points= _findPoints();
		ArrayList<Integer> toRemove = new ArrayList<Integer>();
		DataProbe[][] blackBodyError= new DataProbe[blackBody.length][points.length];
		TProbe[] blackBodyData= new TProbe[blackBody.length];
		for(int i=0; i<blackBody.length; i++){
			try {
				blackBodyData[i]= new TProbe(
						new File(path+blackBody[i]+".txt"));
				for(int j=0; j<points.length; j++){
					blackBodyError[i][j]= blackBodyData[i].get(points[i], 0);
				}
			} catch (FileNotFoundException e) {
				System.out.println(blackBody[i]+": adres error");
			}
		}
		for(int i=0; i<data.size(); i++){
			if(!set[i].isSelected()){
				toRemove.add(i);
				continue;
			}
			data.get(i).pyrometr = new IRData();
			data.get(i).pyrometr.blackBodyError= new double[data.get(i).point[0].length];
			data.get(i).pyrometr.emissivity=_valideEmissivity(emissivity[i].getText());
			data.get(i).pyrometr.distance =_valideDistance(distance[i].getText());
			data.get(i).pyrometr.reference= new double[data.get(i).point[0].length];
			for(int j=0; j<data.get(i).point[0].length;j++){
				data.get(i).pyrometr.reference[j]=
						_valideReferenceValue(referenceValue[i][j].getText());
				
				if(blackBodyChoose[i][j].getSelectedItem().equals("radiator")){
					data.get(i).pyrometr.blackBodyError[j]=0.2;
					continue;
				}
				
				int k=0;
				for(;blackBodyChoose[i][j].getSelectedItem().equals(blackBody[k]);k++){}
				data.get(i).pyrometr.blackBodyError[j]= 
						blackBodyError[k][j].uncertaintyT;
			}
		}
		Collections.reverse(toRemove);
		_remove(toRemove);
	}
	
	private JPanel _panel(int n1, int n2){
		JPanel jp = new JPanel();
		GridBagConstraints c= new GridBagConstraints();
		jp.setLayout(new GridBagLayout());
		c.anchor =GridBagConstraints.PAGE_START;
		for(int i=n1; i<n2; i++){
			c.gridx=i%3;
			c.gridy=i/3;
			jp.add(_device(i), c);
		}
		JButton accept= new JButton("zatwierdź");
		c.gridy=4;
		c.gridx=0;
		c.gridwidth=3;
		jp.add(accept,c);
		accept.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){		
				_setData();
				_close();
			}
		});
		return jp;
	}
	
	private void _start(){
		int n;
		if(_findNumber()>4)
			n=3;
		else
			n=6;
		set			= 	new JRadioButton[num];
		copy		= 	new JRadioButton[num];
		emissivity	= 	new JTextField[num];
		distance	= 	new JTextField[num];

		if(num>n){
			JTabbedPane tabbedPane = new JTabbedPane();
			for(int i=0; i<num; i+=n){
				if(num>i+n)
					tabbedPane.addTab(i+1+" � "+(i+n), _panel(i, i+n));
				else
					tabbedPane.addTab(i+1+" � "+num, _panel(i, num));
			}
			add(tabbedPane);
		}else{
			add(_panel(0, num));
		}
	}
	
	public IRChoose(Frame owner, boolean modal, ArrayList<Certificate> _data){
		super(owner, modal);
		data= _data;
		num = data.size();
		blackBodyChoose = new JComboBox[num][];
		referenceValue = new JTextField[num][];
		_start();
		setSize(700,600);
		setTitle("dane o badanych pirometrach");
		setVisible(true);
	}
	
}
