package elevator.mainframe;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


@SuppressWarnings("serial")
public class Mainframe extends JFrame{
	
	
	public static boolean isCooperation=true;
	public static boolean isOn=true;
	private ElevatorPanel[] Elevators;
	private JPanel MainPanel= new JPanel();
	
	
	//private JMenuBar Select;
	
	//���ò˵���  ��Ҫ�������п��� �� ���ɲ����ڲ����ݰ�ť
	private MenuBar Select;
	
	//���з�ʽѡ��˵�����ѡ�񵥶����к�Э������
	private Menu  SelectMenu;
	private MenuItem DepartOperation;
	private MenuItem Cooperation;
	
	//��Դ���ز˵�
	private Menu  OnOffMenu;
	private MenuItem On;
	private MenuItem Off;

	//�����ڲ���ť���˵��������ƶ��ͽ��
	private Menu  UrgencySelect  =new Menu("�����ƶ���ť");;
	private Menu  UnUrgencySelect= new Menu ("��������ƶ�");
	
	//�����ڲ���ť���˵������ź͹��� //������ͣ��ʱ�����ֶ�������
	private Menu OpenDoorSelect=new Menu("���ݿ���");
	private Menu CloseDoorSelect=new Menu("���ݹ���");
	
	//����һ���鿴��־�Ĳ˵�
	private Menu GetRecord = new Menu("��־");
	private  MenuItem  LookRecord= new MenuItem("�鿴��־");
	
	public static int MAXELEVATOR=5 ;
	private Thread elevator[];
	//Эͬ����ʱ���������ͱ����ֱ��¼���Ϻ����������¥��
	public static int UPRequestFloor=-1;
	public static int DOWNRequestFloor=-1;	
//	public String RecordString;
	

	
	
	  
	private int PresentElevatorFloor[]=new int [MAXELEVATOR];
	public int DistanceRequestCurrent[]= new int [MAXELEVATOR]; 
	

	public Mainframe(){
		super("Elevator_1152703_��־��");
		 
		
		
		try
		{
			
			Init();
		}
		catch(Exception exception)
		{

			exception.printStackTrace();
	 	}
	}
	
	
	private void Init()throws Exception{
		
		//����������־�ļ���
		 Record.RecordFileInit();
		 setSize(1350,750);
		
		 elevator=new Thread[Mainframe.MAXELEVATOR];
		 
		 this.MainPanel.setLayout(new FlowLayout());
		 this.setLayout(new BorderLayout());

		 
	    setElevators();
	    setMenuBar();
	    this.MainPanel.setPreferredSize(new Dimension(200+MAXELEVATOR*100,30+74*ElevatorPanel.MAXFLOOR));
	    
	   JScrollPane scroll = new JScrollPane(MainPanel); 
	  
	   scroll.getVerticalScrollBar().setValue(600
		);

	    scroll.setVerticalScrollBarPolicy( 
	    		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll.getVerticalScrollBar().setUnitIncrement(30);
	    scroll.getVerticalScrollBar().setBlockIncrement(5);
	    scroll.setHorizontalScrollBarPolicy( 
	    		JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
	  
	    scroll.setSize(50,50);
		 this.add(scroll,BorderLayout.CENTER);
		 this.setMenuBar(Select);
	    this.setVisible(true);
		 
	    for(int i=0;i<this.elevator.length ;i++)
	    {
	    elevator[i]=new Thread(this.Elevators[i]);
	    elevator[i].start();
	    
	    }
	 while(true)
	 {

	  
	   //Э�����ȵķ�ʽ 
	   if(Mainframe.isCooperation)
	   
		   allotElevator();
      

	  //ÿ��һ����һ����û������
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    }
  
	    
	 }
	    
      

	 
	
//Эͬ���ȣ������ܹ�ͳһ����
	public void allotElevator()
	{
		if(UPRequestFloor!=-1)
		{
		     //�����¥��д�������־
			Record.WriteUpRequestToRecord(UPRequestFloor);
			//��ǰ�������ݵ�״̬д����־
			writeElevatorsStateToRecord();
			this.Elevators[getElevator(UPRequestFloor)-1].AllFloors[UPRequestFloor-1].isUp=true;
		    this.Elevators[getElevator(UPRequestFloor)-1].LastAllotFloor=UPRequestFloor;
		    //д������������־
		    Record.WriteAllotToRecord(getElevator(UPRequestFloor), UPRequestFloor);
		
			UPRequestFloor=-1;
		}
		
		if(DOWNRequestFloor!=-1)
		{
			//д����Ҫ���¥�㵽��־
			Record.WriteDownRequestToRecord(DOWNRequestFloor);
			//��ǰ�������ݵĵ���״̬д�������־
			writeElevatorsStateToRecord();
			this.Elevators[getElevator(DOWNRequestFloor)-1].AllFloors[DOWNRequestFloor-1].isDown=true;
			//д�������־
			 Record.WriteAllotToRecord(getElevator(DOWNRequestFloor), DOWNRequestFloor);
			this.Elevators[getElevator(DOWNRequestFloor)-1].LastAllotFloor=DOWNRequestFloor;
			DOWNRequestFloor=-1;
		}
	}
	
	//��ÿ�����ݵ�ǰ�Ĳ�����������
	public void getPresentElevatorFloor()
	{

		for(int i=0;i<this.Elevators.length ;i++)
		{
			this.PresentElevatorFloor[i]=this.Elevators[i].CurrentFloor;
		
		}
		
	}
	
	//�õ�ÿ���������ÿ����ݵ�ֵ��
	//������ʾ�����������Ϸ�
	public void getDistance(int RequestFloor)
	{

		for(int i=0;i<this.PresentElevatorFloor.length ;i++)
		{
			this.DistanceRequestCurrent[i]= this.PresentElevatorFloor[i]-
			                                RequestFloor;
		}
	}
	
	
	
	//�ҳ�ȥ������ʱ��̵ĵ���
	//����������ͣ������ֻ����������
	//�������������У��������������ϴ�Ŀ�ĵ�+���ŵȴ�ʱ�䣨2m �ۺ����㣩+��Ŀ�ĵص�����������֮��
	//����ʹ�ȴ�ʱ�����
	public int getElevator(int floor)
	{
		
		getPresentElevatorFloor();
		int TargetElevator = 1;
		getDistance(floor);

		int min=ElevatorPanel.MAXFLOOR+1;
		int temp=0;
		for(int i=0;i<this.Elevators.length;i++)
		{
			
			
			//�����ǰ״̬Ϊͣ��
			if(this.Elevators[i].ElevatorState==ElevatorPanel.STOPING)
			{
				//ȡ��ǰ�㵽��������ľ���ֵ
				temp = Math.abs(DistanceRequestCurrent[i]);
			}
			
			//��������������
			if(this.Elevators[i].ElevatorState==ElevatorPanel.GOINGUP)
			{
				//���ڻ���ڵ�ǰ��
					if(this.PresentElevatorFloor[i]<=floor)
					{
						//ֱ�����ֵ����Ϊ�ȴ�ʱ��
						
						temp =floor - this.PresentElevatorFloor[i];
						
					}
					//С�ڵ�ǰ��
					else{
						
						//�ȴ�ʱ��Ϊ ����Ŀ�ĵ���������ʱ��+����ʱ��
						temp=this.Elevators[i].LastAllotFloor-this.PresentElevatorFloor[i]
						                       +this.Elevators[i].LastAllotFloor-floor+2;
					}
		
			}
			
			
			//������������½�
			if(this.Elevators[i].ElevatorState==ElevatorPanel.GOINGDOWN)
			{
				//С�ڻ���ڵ�ǰ��
					if(this.PresentElevatorFloor[i]>=floor)
					{
						//ֱ�����ֵ����Ϊ�ȴ�ʱ��
						temp=this.PresentElevatorFloor[i]-floor;
					}
					//���ڵ�ǰ��
					else{
						//�ȴ�ʱ��Ϊ ����Ŀ�ĵ���������ʱ��+����ʱ��
						temp=this.PresentElevatorFloor[i]-this.Elevators[i].LastAllotFloor
						+floor -this.Elevators[i].LastAllotFloor+2;
					}
			
				
				
			}
			
			
			//�����ǰ״̬�ɱ�����
			if(temp<min&&(!this.Elevators[i].ISOPEN)
					&&(!this.Elevators[i].Urgency))
			{ 
				min=temp;
				TargetElevator=i+1;
			}
			
		}
		
		return TargetElevator;
		
		
	}
	
	

	//����ѡ����
	public void setMenuBar()
	{
		
		Select= new MenuBar();
		//ѡ����ݵ�Դ
		//OnOff= new JMenuBar();
		this.OnOffMenu = new Menu("���ݿ���");
		On= new MenuItem("�򿪵�Դ");
		Off=new MenuItem("�رյ�Դ");
		this.On.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				isOn=true;
				}});
		
		this.Off.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				isOn=false;
			}});

		this.OnOffMenu.add(On);
		this.OnOffMenu.add(Off);
		
		this.Select.add(this.OnOffMenu);
		
	   //������ť
       //�����ǰ�ť
        for(int ItemCount=0;ItemCount<this.Elevators.length;ItemCount++)
        {
      
        	this.UrgencySelect.add(this.Elevators[ItemCount].urgency);
      		this.UnUrgencySelect.add(this.Elevators[ItemCount].UnUrgency);
      		this.OpenDoorSelect.add(this.Elevators[ItemCount].OpenDoor);
      		this.CloseDoorSelect.add(this.Elevators[ItemCount].CloseDoor);
       }
    	
        
        
        
        //���ò鿴������־�Ĳ˵�
        this.LookRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String command = "notepad d:\\record.txt";
				try {
					 Runtime.getRuntime().exec(command);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}});
        
        
        GetRecord.add(this.LookRecord);
		
        
		//ѡ�����з�ʽ
		SelectMenu=new Menu ("���з�ʽ");
		Cooperation=new MenuItem("Э������");
		Cooperation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				isCooperation=true;

			}

			
		});
		this.DepartOperation =new MenuItem("��������");
		DepartOperation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				isCooperation=false;
			}	
		});
		this.SelectMenu.add(Cooperation);
		this.SelectMenu.add(DepartOperation);
		
		this.Select.add(GetRecord);
		Select.add(SelectMenu);
		this.Select.add(this.UrgencySelect);
		this.Select.add(this.UnUrgencySelect);
		this.Select.add(this.OpenDoorSelect);
		this.Select.add(this.CloseDoorSelect);
		
	}
	
	
	
	//�������е��ݵĽ���
	public void setElevators(){
		
		this.Elevators =new ElevatorPanel[Mainframe.MAXELEVATOR];
	
		for(int i=0;i<this.Elevators.length;i++)
		{
			Elevators[i]=new ElevatorPanel(i+1);
		    
		    this.MainPanel.add(Elevators[i]);
		    
		}
		
	}
	 
	  //������ʱ���е��ݵ�״̬д�������־
    public void writeElevatorsStateToRecord()
    {
  	  
  	  //���� д�뺯��
    	
    	Record.WriteToFile("��ǰ����״̬��");
    	Record.WriteToFile("--------------");
  	  for(int i=0;i<this.Elevators.length;i++)
  	  {//��ͣ������״̬д����־
  		  if(Elevators[i].ElevatorState==ElevatorPanel.STOPING)
  		  {
  			  if(Elevators[i].ISOPEN)
  			  {
  			  Record.WriteToFile("����"+(i+1)+"��ͣ����"+Elevators[i].CurrentFloor+"¥-->����");
  			  }
  			  else if(Elevators[i].Urgency)
  				  Record.WriteToFile("����"+(i+1)+"��ͣ����"+Elevators[i].CurrentFloor+"¥-->���ƶ�");
  			  else Record.WriteToFile("����"+(i+1)+"��ͣ����"+Elevators[i].CurrentFloor+"¥");
  			  
  		  }
  		  //���������У������ڼ�¥ǰ����¥д����־
  		  else 
  			  Record.WriteCurrentElevatorStateToRecord(i+1, Elevators[i].CurrentFloor,Elevators[i].LastAllotFloor);
  	  }
  	Record.WriteToFile("---------------------");
  	  
    }
    
    
	
    



}
    
    
    
    
	
    
	
	
	 
	


