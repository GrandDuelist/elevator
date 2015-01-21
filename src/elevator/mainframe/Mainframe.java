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
	
	//设置菜单栏  主要包括运行控制 和 集成部分内部电梯按钮
	private MenuBar Select;
	
	//运行方式选择菜单，可选择单独运行和协作运行
	private Menu  SelectMenu;
	private MenuItem DepartOperation;
	private MenuItem Cooperation;
	
	//电源开关菜单
	private Menu  OnOffMenu;
	private MenuItem On;
	private MenuItem Off;

	//集成内部按钮到菜单；紧急制动和解除
	private Menu  UrgencySelect  =new Menu("紧急制动按钮");;
	private Menu  UnUrgencySelect= new Menu ("解除电梯制动");
	
	//集成内部按钮到菜单；开门和关门 //仅仅在停靠时可以手动开关门
	private Menu OpenDoorSelect=new Menu("电梯开门");
	private Menu CloseDoorSelect=new Menu("电梯关门");
	
	//设置一个查看日志的菜单
	private Menu GetRecord = new Menu("日志");
	private  MenuItem  LookRecord= new MenuItem("查看日志");
	
	public static int MAXELEVATOR=5 ;
	private Thread elevator[];
	//协同调度时用两个整型变量分别记录向上和向下请求的楼层
	public static int UPRequestFloor=-1;
	public static int DOWNRequestFloor=-1;	
//	public String RecordString;
	

	
	
	  
	private int PresentElevatorFloor[]=new int [MAXELEVATOR];
	public int DistanceRequestCurrent[]= new int [MAXELEVATOR]; 
	

	public Mainframe(){
		super("Elevator_1152703_方志晗");
		 
		
		
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
		
		//开启调度日志文件；
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

	  
	   //协作调度的方式 
	   if(Mainframe.isCooperation)
	   
		   allotElevator();
      

	  //每隔一秒检测一次有没有请求
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    }
  
	    
	 }
	    
      

	 
	
//协同调度，由主架构统一调度
	public void allotElevator()
	{
		if(UPRequestFloor!=-1)
		{
		     //请求的楼层写入调度日志
			Record.WriteUpRequestToRecord(UPRequestFloor);
			//当前各个电梯的状态写入日志
			writeElevatorsStateToRecord();
			this.Elevators[getElevator(UPRequestFloor)-1].AllFloors[UPRequestFloor-1].isUp=true;
		    this.Elevators[getElevator(UPRequestFloor)-1].LastAllotFloor=UPRequestFloor;
		    //写入调度请况到日志
		    Record.WriteAllotToRecord(getElevator(UPRequestFloor), UPRequestFloor);
		
			UPRequestFloor=-1;
		}
		
		if(DOWNRequestFloor!=-1)
		{
			//写入有要求的楼层到日志
			Record.WriteDownRequestToRecord(DOWNRequestFloor);
			//当前各个电梯的调度状态写入调度日志
			writeElevatorsStateToRecord();
			this.Elevators[getElevator(DOWNRequestFloor)-1].AllFloors[DOWNRequestFloor-1].isDown=true;
			//写入调度日志
			 Record.WriteAllotToRecord(getElevator(DOWNRequestFloor), DOWNRequestFloor);
			this.Elevators[getElevator(DOWNRequestFloor)-1].LastAllotFloor=DOWNRequestFloor;
			DOWNRequestFloor=-1;
		}
	}
	
	//将每个电梯当前的层数存入数组
	public void getPresentElevatorFloor()
	{

		for(int i=0;i<this.Elevators.length ;i++)
		{
			this.PresentElevatorFloor[i]=this.Elevators[i].CurrentFloor;
		
		}
		
	}
	
	//得到每层请求距离每层电梯的值；
	//正数表示电梯在请求上方
	public void getDistance(int RequestFloor)
	{

		for(int i=0;i<this.PresentElevatorFloor.length ;i++)
		{
			this.DistanceRequestCurrent[i]= this.PresentElevatorFloor[i]-
			                                RequestFloor;
		}
	}
	
	
	
	//找出去请求层耗时最短的电梯
	//若电梯正在停靠，则只许计算层数差
	//若电梯正在运行，则计算出它到达上次目的地+开门等待时间（2m 折合两层）+从目的地到这次请求层数之差
	//尽量使等待时间最短
	public int getElevator(int floor)
	{
		
		getPresentElevatorFloor();
		int TargetElevator = 1;
		getDistance(floor);

		int min=ElevatorPanel.MAXFLOOR+1;
		int temp=0;
		for(int i=0;i<this.Elevators.length;i++)
		{
			
			
			//如果当前状态为停靠
			if(this.Elevators[i].ElevatorState==ElevatorPanel.STOPING)
			{
				//取当前层到请求层距离的绝对值
				temp = Math.abs(DistanceRequestCurrent[i]);
			}
			
			//当电梯正在上行
			if(this.Elevators[i].ElevatorState==ElevatorPanel.GOINGUP)
			{
				//大于或等于当前层
					if(this.PresentElevatorFloor[i]<=floor)
					{
						//直接算差值，即为等待时间
						
						temp =floor - this.PresentElevatorFloor[i];
						
					}
					//小于当前层
					else{
						
						//等待时间为 到达目的地再下来的时间+开门时间
						temp=this.Elevators[i].LastAllotFloor-this.PresentElevatorFloor[i]
						                       +this.Elevators[i].LastAllotFloor-floor+2;
					}
		
			}
			
			
			//如果电梯正在下降
			if(this.Elevators[i].ElevatorState==ElevatorPanel.GOINGDOWN)
			{
				//小于或等于当前层
					if(this.PresentElevatorFloor[i]>=floor)
					{
						//直接算差值，即为等待时间
						temp=this.PresentElevatorFloor[i]-floor;
					}
					//大于当前层
					else{
						//等待时间为 到达目的地再上来的时间+开门时间
						temp=this.PresentElevatorFloor[i]-this.Elevators[i].LastAllotFloor
						+floor -this.Elevators[i].LastAllotFloor+2;
					}
			
				
				
			}
			
			
			//如果当前状态可被调度
			if(temp<min&&(!this.Elevators[i].ISOPEN)
					&&(!this.Elevators[i].Urgency))
			{ 
				min=temp;
				TargetElevator=i+1;
			}
			
		}
		
		return TargetElevator;
		
		
	}
	
	

	//设置选择栏
	public void setMenuBar()
	{
		
		Select= new MenuBar();
		//选择电梯电源
		//OnOff= new JMenuBar();
		this.OnOffMenu = new Menu("电梯开关");
		On= new MenuItem("打开电源");
		Off=new MenuItem("关闭电源");
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
		
	   //紧急按钮
       //开关们按钮
        for(int ItemCount=0;ItemCount<this.Elevators.length;ItemCount++)
        {
      
        	this.UrgencySelect.add(this.Elevators[ItemCount].urgency);
      		this.UnUrgencySelect.add(this.Elevators[ItemCount].UnUrgency);
      		this.OpenDoorSelect.add(this.Elevators[ItemCount].OpenDoor);
      		this.CloseDoorSelect.add(this.Elevators[ItemCount].CloseDoor);
       }
    	
        
        
        
        //设置查看调度日志的菜单
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
		
        
		//选择运行方式
		SelectMenu=new Menu ("运行方式");
		Cooperation=new MenuItem("协作运行");
		Cooperation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				isCooperation=true;

			}

			
		});
		this.DepartOperation =new MenuItem("单独运行");
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
	
	
	
	//制作所有电梯的界面
	public void setElevators(){
		
		this.Elevators =new ElevatorPanel[Mainframe.MAXELEVATOR];
	
		for(int i=0;i<this.Elevators.length;i++)
		{
			Elevators[i]=new ElevatorPanel(i+1);
		    
		    this.MainPanel.add(Elevators[i]);
		    
		}
		
	}
	 
	  //将调度时所有电梯的状态写入调度日志
    public void writeElevatorsStateToRecord()
    {
  	  
  	  //调用 写入函数
    	
    	Record.WriteToFile("当前电梯状态：");
    	Record.WriteToFile("--------------");
  	  for(int i=0;i<this.Elevators.length;i++)
  	  {//若停靠，则将状态写入日志
  		  if(Elevators[i].ElevatorState==ElevatorPanel.STOPING)
  		  {
  			  if(Elevators[i].ISOPEN)
  			  {
  			  Record.WriteToFile("电梯"+(i+1)+"正停靠在"+Elevators[i].CurrentFloor+"楼-->开门");
  			  }
  			  else if(Elevators[i].Urgency)
  				  Record.WriteToFile("电梯"+(i+1)+"正停靠在"+Elevators[i].CurrentFloor+"楼-->被制动");
  			  else Record.WriteToFile("电梯"+(i+1)+"正停靠在"+Elevators[i].CurrentFloor+"楼");
  			  
  		  }
  		  //若正在运行，将正在几楼前往几楼写入日志
  		  else 
  			  Record.WriteCurrentElevatorStateToRecord(i+1, Elevators[i].CurrentFloor,Elevators[i].LastAllotFloor);
  	  }
  	Record.WriteToFile("---------------------");
  	  
    }
    
    
	
    



}
    
    
    
    
	
    
	
	
	 
	


