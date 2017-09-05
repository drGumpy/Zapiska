package tests;
 
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class Console extends JFrame {
    
	//dane na temat wzorcowania
    static ArrayList<Certificate> data = new ArrayList<Certificate>();
    static ArrayList<Data> calPoint = new ArrayList<Data>();
    
    private static DataProbe[] dataProbe;
    
    private static ArrayList<Dev> devices = new ArrayList<Dev>();
    private static ArrayList<Data> point = new ArrayList<Data>();    
    private static Dev patern = new Dev();
    
    private static ChamberData[] chamberData;
    
    //parametry wzorcowania
    private static boolean check = true;
    private static boolean Rh = false;
    private static int points=3;
    
    //pozycja arkusza, zapisu œwiadectw i zapisek wzorcowania
    private static JTextField
     sheet = new JTextField(50),
     certificate = new JTextField(50),
     notes = new JTextField(50);
    
    private static AbstractButton t, rh;
    
    private static JComboBox<Integer> pointsBox = new JComboBox<Integer>();
    
    //informacje odnoœnie warunków œrodowiskowych
    private static JTextField[] environment = new JTextField[4];   
    private static double enviromentCondition[] = {22.0, 22.0, 45.0, 45.0};
    
    //przypisanie warunków œrodowiskowych
    
    private static JPanel _environ(){
        JPanel jp = new JPanel();
        jp.setPreferredSize(new Dimension(400, 80));
        jp.setMaximumSize(jp.getPreferredSize()); 
        jp.setMinimumSize(jp.getPreferredSize());
        jp.setBorder(new TitledBorder("Warunki œrodowiskowe"));
        String[] title = {"t min    ","t max   ","Rh min ","Rh max"};
        for(int i=0; i<4;i++){
            environment[i] = new JTextField(10);
            environment[i].setName(title[i]);
            if(i<2)
                environment[i].setText("22.000");
            else
                environment[i].setText("45.000");
            jp.add(environment[i]);
            JLabel label= new JLabel();
            label.setText(title[i]);
            jp.add(label);
        }

        
        environment[0].addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
                String data = environment[0].getText().replace(",", ".");
                if(!MetrologyMath.validate(data))
                    environment[0].setText("22.000");
                else{
                    String data2 = environment[1].getText();
                    if(Double.parseDouble(data)>Double.parseDouble(data2))
                        environment[0].setText("22.000");
                    else{
                        enviromentCondition[0]= Double.parseDouble(data);
                        environment[0].setText(data);
                    }
                }
        	}
        });
        
        environment[1].addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                String data = environment[1].getText().replace(",", ".");
                if(!MetrologyMath.validate(data))
                    environment[1].setText("22.000");
                else{
                    String data2 = environment[0].getText();
                    if(Double.parseDouble(data)<Double.parseDouble(data2))
                        environment[1].setText("22.000");
                    else{
                        enviromentCondition[1]= Double.parseDouble(data);
                        environment[1].setText(data);
                    }
                }
            }
            
        });
        
        environment[2].addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                String data = environment[2].getText().replace(",", ".");
                if(!MetrologyMath.validate(data))
                    environment[2].setText("45.000");
                else{
                    String data2 = environment[3].getText();
                    if(Double.parseDouble(data)>Double.parseDouble(data2))
                        environment[2].setText("45.000");
                    else{
                        enviromentCondition[2]= Double.parseDouble(data);
                        environment[2].setText(data);
                    }
                }
            }
            
        });
        
        environment[3].addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                String data = environment[3].getText().replace(",", ".");
                if(!MetrologyMath.validate(data))
                    environment[3].setText("45.000");
                else{
                    String data2 = environment[2].getText();
                    if(Double.parseDouble(data)<Double.parseDouble(data2))
                        environment[3].setText("45.000");
                    else{
                        enviromentCondition[3]= Double.parseDouble(data);
                        environment[3].setText(data);
                    }
                }
            }            
        });
        return jp;
    }
    
    private static JPanel _choosePath(String name, String path, JTextField field){
    	 JButton b= new JButton("zmieñ");
         JPanel jp = new JPanel();
         jp.setPreferredSize(new Dimension(650, 50));
         jp.setMinimumSize(jp.getPreferredSize());
         jp.setLayout(new GridBagLayout());
         GridBagConstraints c = new GridBagConstraints();
         c.gridx=1;
         
         jp.setBorder(new TitledBorder(name));
         jp.add(b, c);
         field.setText(path);
         field.setEditable(false);
         c.gridx=2;
         jp.add(field);
         b.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e) {
                 JFileChooser c = new JFileChooser();
                 c.showOpenDialog(c);
                 field.setText(c.getSelectedFile().toString());    
             }
         });
        return jp; 
    }   
   
    //rodzaj wykonywanego wzorcowania
    private static JPanel _calibrationType() {
        ButtonGroup bg = new ButtonGroup();
        JPanel jp = new JPanel();
        String title = "iloœæ punktów pomiarowych i rodzaj wzorcowania";
        title = title.substring(title.lastIndexOf('.') + 1);
        jp.setBorder(new TitledBorder(title));
        
        t  = new JRadioButton("temperatura");
        rh = new JRadioButton("temperatura i wilgotnoœæ");
 
        for(int i = 1; i < 7; i++)
               pointsBox.addItem(i);
        
        bg.add(t);
        t.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(check){
                    Rh= false;
                    points=3;
                    pointsBox.setSelectedIndex(2);
                }
            }
        });
        t.setSelected(true);
        bg.add(rh);
        rh.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(check){
                    Rh= true;
                    points=5;
                    pointsBox.setSelectedIndex(4);
                }
            }
        });
        
        jp.add(pointsBox);
        pointsBox.setSelectedIndex(2);
        pointsBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            points= (int) pointsBox.getSelectedItem();
            }
        });
        jp.add(t);
        jp.add(rh);
        return jp;
    }
    
    private void close(){
		super.dispose();
	}
    
    //wzorcowanie pirometrów
    private JPanel _pyrometers(){
    	JPanel jp = new JPanel();
    	JButton calibrationData = new JButton("wybierz zlecenia");
    	JButton generation= new JButton("generuj œwiadetwa");
    	
    	calibrationData.setMinimumSize(new Dimension(200, 23));
    	generation.setMinimumSize(new Dimension(200, 23));
    	generation.setEnabled(false);
    	
    	calibrationData.addActionListener(new ActionListener(){    
            public void actionPerformed(ActionEvent e){
                File file = new File(sheet.getText());
                CertificateData.setFile(file);
                CertificateData.calibration=5;
                try {
                    CertificateData.run();
                    data=CertificateData.getData();
                } catch (IOException e1) {}
                new IRChoose(Console.this, true, data);
                try {
                    GetData.setData(false);
                    GetData.IR();
                    GetData.setFile(file);
                    devices=GetData.findData(6);
                    point=GetData.getPoint();
                } catch (IOException e1) {}
                try {
                    dataProbe = new DataProbe[point.size()];
                    PaternProbe probe;
                    probe= new TProbe(new File("C:\\Users\\Laboratorium\\Desktop\\Laboratorium\\generacja\\12030011.txt"));
                    for(int i=0; i<point.size(); i++){
                        int t=Integer.parseInt(point.get(i).temp);
                        dataProbe[i]=probe.get(t, 0);
                    }
                } catch (IOException e1) {}
                generation.setEnabled(true);
            }    
    	});
    	
    	generation.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e) {       	
                 IRGenerate make = new IRGenerate();
                 make.putDataProbe(dataProbe);
                 make.putDevice(devices);
                 make.putPaths(notes.getText(), certificate.getText());
                 Environment d= new Environment();
                 make.putEnvironment(d.calculateData(enviromentCondition));
                 make.run(data);
                 try {
                     File file = new File(sheet.getText());
                     PutDate.putFile(file);
                     PutDate.date(make.get_done());
                 } catch (IOException e1) {
                     e1.printStackTrace();
                 }
                 System.out.println("koniec wprowadzania");
                 close();
             }
    	});


        jp.add(calibrationData);
        jp.add(generation);
    	
    	return jp;
    }
    
    //wzorcowanie w komorze klimatycznej
    private JPanel _climateChamber(){
    	JPanel jp = new JPanel();
    	jp.setLayout(new GridBagLayout());
        JButton dattaLogger= new JButton("dane z rejestratorów");
        JButton clientData= new JButton("wybierz zlecenia");
        JButton generation= new JButton("generuj œwiadetwa");
        
        dattaLogger.setMinimumSize(new Dimension(200, 23));
        clientData.setMinimumSize(new Dimension(200, 23));
        generation.setMinimumSize(new Dimension(200, 23));

        //wprowadzenie danych o rejestratorach
        dattaLogger.addActionListener(new ActionListener(){    
            public void actionPerformed(ActionEvent e) {
                long startTime = System.currentTimeMillis();
                
                File file = new File(sheet.getText());
                PutData.set(Rh, file , points);
                try {
                    calPoint=PutData.getPoints();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                PutData.run();
                PutData.clean();
                check=false;
                pointsBox.setEnabled(false);
                t.setEnabled(false);
                rh.setEnabled(false);
                clientData.setEnabled(true);
                long endTime   = System.currentTimeMillis();
                System.out.println(" w czasie: " +(endTime - startTime)/1000.0+" s");
            }
        });
        //pozyskanie danych do Å›wiadectwa
        clientData.addActionListener(new ActionListener(){    
            public void actionPerformed(ActionEvent e) {
                long startTime = System.currentTimeMillis();
                File file = new File(sheet.getText());
                CertificateData.setFile(file);
                if(Rh)
                	CertificateData.calibration=3;
                else
                	CertificateData.calibration=1;
                try {
                    CertificateData.run();
                    data=CertificateData.getData();
                } catch (IOException e1) {
                }
                try {
                    GetData.setData(Rh);
                    GetData.setFile(file);
                    devices=GetData.findData(points);
                    patern=GetData.getPatern();
                    point=GetData.getPoint();
                } catch (IOException e1) {System.out.println("b³¹d pobierania danych");}
                
                try {
                    dataProbe = new DataProbe[point.size()];
                    PaternProbe probe;
                    if(Rh)
                        probe= new RhProbe(new File("C:\\Users\\Laboratorium\\Desktop\\Laboratorium\\generacja\\61602551.txt"));
                    else
                        probe= new TProbe(new File("C:\\Users\\Laboratorium\\Desktop\\Laboratorium\\generacja\\13.026.txt"));
                    for(int i=0; i<point.size(); i++){
                        int t=Integer.parseInt(point.get(i).temp);
                        int rh=0;
                        if(Rh)
                            rh=Integer.parseInt(point.get(i).hum);
                        dataProbe[i]=probe.get(t, rh);
                    }
                } catch (IOException e1) {System.out.println("b³¹d wzorca");}
                
                Chamber cham= new Chamber();
                cham.start(Rh);
                cham.getPoints(point);
                chamberData=cham.get();
                generation.setEnabled(true);
                long endTime   = System.currentTimeMillis();
                System.out.println("czas: " +(endTime - startTime)/1000.0 + " s");
            }
        });
        //wygenerowanie œwiadectw wzorcowania
        generation.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Generate make = new Generate();
                make.putChamber(chamberData);
                make.putDataProbe(dataProbe);
                make.putDevice(devices);
                make.putPatern(patern);
                make.putPaths(notes.getText(), certificate.getText());
                Environment d= new Environment();
                make.putEnvironment(d.calculateData(enviromentCondition));
                make.run(data);
                try {
                    File file = new File(sheet.getText());
                    PutDate.putFile(file);
                    PutDate.date(make.getDone());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
 
                System.out.println("koniec wprowadzania");
                close();
            }
        });
        generation.setEnabled(false);
        
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        jp.add(_calibrationType(), c);
        
        c.gridwidth = 1;
        c.ipadx=10;
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridx = 0;
        c.gridy = 1;
        jp.add(dattaLogger, c);
       
        c.gridx = 1;
        jp.add(clientData,c);       

        c.gridx = 2;
        jp.add(generation, c);
        return jp;
    }

    public Console(){
    	//JPanel jp = new JPanel();
    	setLayout(new GridBagLayout());
    	GridBagConstraints c = new GridBagConstraints();
    	
    	c.fill= GridBagConstraints.VERTICAL;
    	c.anchor =GridBagConstraints.PAGE_START;
    	c.weighty=0.2;
    	c.gridy=0;
    	add(_environ(), c);
    	
    	c.weighty=0.1;
    	c.weightx=1;
    	c.gridy=1;
    	add(_choosePath("Arkusz z danymi",
    			"C:\\Users\\Laboratorium\\Desktop\\Laboratorium.ods", sheet), c);
    	
    	c.gridy=2;
    	add(_choosePath("Folder zapisu zapisek z wzorcowania",
    			"C:\\Users\\Laboratorium\\Desktop\\Laboratorium\\Wyniki wzorcowañ\\Zapiski\\",
    			notes), c);
    	
    	c.gridy=3;
    	add(_choosePath("Folder zapisu œwiadectw wzorcowania",
    			"C:\\Users\\Laboratorium\\Desktop\\Laboratorium\\Wyniki wzorcowañ\\œwiadectwa wzorcowania\\", 
    			certificate), c);
    	
    	JTabbedPane tabbedPane = new JTabbedPane();
    	tabbedPane.addTab("komora klimatyczna", _climateChamber());
    	tabbedPane.addTab("pirometry", _pyrometers());
    	tabbedPane.setMaximumSize(new Dimension(800, 50));

    	c.weighty=0.2;
    	c.weightx=1;
    	c.gridy=4;
    	c.ipady= 200;
    	add(tabbedPane, c);
    }
       
    //uruchomienie programu
    public static void run(){
        SwingUtilities.invokeLater(new Runnable(){
            Console f = new Console();
            public void run(){
                f.setTitle("wydawanie œwiadectw");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setSize(800,600);
                f.setVisible(true);
            }
        });
    }
    
    public static void main(String[] args) {
        run();    
    }
}