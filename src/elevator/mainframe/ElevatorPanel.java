package elevator.mainframe;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import elevator.mainframe.*;
import java.util.Timer;
import javax.swing.*;



@SuppressWarnings("serial")
public class ElevatorPanel extends JPanel implements Runnable {

	
	public final static int GOINGDOWN=-1;
	public final static int GOINGUP=1;
	public final static int STOPING=0;
	
	public static int MAXFLOOR =20;
	
	public int CurrentFloor =1 ;
	public FloorPanel  AllFloors[];
	private boolean floorNeedStop[]= new boolean[MAXFLOOR];
	public   int ElevatorState=STOPING;
	private Panel FloorsPanel= new Panel();
	private int ElevatorNumber;
	private JLabel NumLabel;
	//紧急按钮
	public boolean Urgency = false;
	
	//定义一个电梯内部的按钮
	private Panel InControlButtons = new Panel();
    
	//private boolean FloorOPEN[]=new boolean[MAXFLOOR];
	
	//定义一个记录电梯上次请求层数的整型
	public int LastAllotFloor=1;
	//定义紧急制动按钮
	public MenuItem urgency;
	//解除紧急制动的按钮
	public MenuItem UnUrgency;
	
	//定义开关门门按钮
	public MenuItem OpenDoor;
	public MenuItem CloseDoor;
	
	//开关们标识符
	public boolean ISOPEN=false;
	
	public ElevatorPanel(int num)
	{
		
		super();
		this.ElevatorNumber=num;
		
		
		this.setInButtonsEvents();
		
		setElevator();
		setFloorIsStop();
		ShowDoorStop();
	
	  
	}
	
	//设置内部按钮的功能
	public void setInButtonsEvents()
	{
		this.setUrgencyEvent();
		this.setOpenDoorEvent();
		this.setCloseDoorEvent();
	}
	
	
	//集成内部手动开门按钮到菜单栏
	void setOpenDoorEvent()
	{
		OpenDoor=new MenuItem("电梯"+this.ElevatorNumber+"开门");
		this.OpenDoor.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent event)	{
    			if(ElevatorState==STOPING)
    			{
    			ISOPEN =true;
    			AllFloors[CurrentFloor-1].ElevoterButton.setBackground(Color.ORANGE);
    			
    			}
    		}});
		
	}
	
	//集成内部手动关门按钮到菜单栏
	void setCloseDoorEvent()
	{
		 CloseDoor =new MenuItem("电梯"+this.ElevatorNumber+"关门");
		this.CloseDoor.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent event) {
    			if(ElevatorState==STOPING)
    			{
    			ISOPEN =false;
    			AllFloors[CurrentFloor-1].ElevoterButton .setBackground(Color.WHITE);
    			
    			}
    		}});
		
	}
	
	//设置紧急制动的内部按钮，为了表现外部完整性；内部按钮集成到菜单中
	
	void setUrgencyEvent()
	{
		urgency= new MenuItem("紧急制动电梯"+this.ElevatorNumber);
		this.urgency.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent event) {
    			Urgency =true;
    			ElevatorState=STOPING;
    			
    		}
    	});
		this.UnUrgency=new MenuItem("解除电梯"+this.ElevatorNumber+"的制动");
		

		this.UnUrgency.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent event) {
    			Urgency =false;
    			//Elevators[ItemCount].Urgency=true;
    		}
    	});
	}
	
//查看是否有请求	，方便Mainframe统一管理
	
   public boolean isRequested()
   {
	    boolean is=false;
		for(int i=0;i<this.AllFloors.length;i++)
		{
			floorNeedStop[i]=(this.AllFloors[i].isDown||this.AllFloors[i].isUp);
			if(this.AllFloors[i].isDown||this.AllFloors[i].isUp)
			{
				is=true;
			}
		}
		return is;
   }
	
   
 //设置电梯开启时的延时和简单动作
   
   public void elevatorOpenAction() 
   {
	   
	   
		
		
		
			for(int i=0;i<this.AllFloors.length;i++)
			{
				//黄白闪两下
				if(AllFloors[i].OPEN) 
					{
					
					  for(int k=0;k<2;k++);
					  {
						
					   this.AllFloors [i].ElevoterButton .setBackground(Color.WHITE );
			//停靠耗时2秒(等同于运行两层的时间)，这对计算时间最短分配方案有用
					   try {
							Thread.sleep(500);
						   } catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						    }
					   this.AllFloors [i].ElevoterButton .setBackground(Color.ORANGE );
					   try {
							Thread.sleep(500);
						   } catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						    }
					
					  }
					  
					  //变黄（开门停）1秒
					  
					  
					  try {
							Thread.sleep(1000);
						   } catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						    }
						//变白（关门） 
					  this.AllFloors [i].ElevoterButton .setBackground(Color.white );
					  AllFloors [i].OPEN =false;   
					}
			}
				
		
			
	   
   }
   //设置显示选择过的楼层
   public void setHaveSelected()
   {
	   for(int i=0;i<this.AllFloors.length;i++)
		{
		
		   if(AllFloors[i].isDown||AllFloors[i].isUp)
			   {
			   AllFloors[i].InButton.setEnabled(false);
			   AllFloors[i].InButton.setBackground(Color.magenta);
			   }
		   else 
			   {
			   AllFloors[i].InButton.setEnabled(true);
			   AllFloors[i].InButton.setBackground(Color.GREEN);
			   }
		   

		}
	   
   }
   
  //紧急制动时，取消选择的楼层
   public void resumeHaveSelected()
   {
	   for(int k=0;k<this.AllFloors.length;k++)
		{
		   AllFloors[k].InButton.setEnabled(true);
		   AllFloors[k].InButton.setBackground(Color.GREEN);

		}
   }
   
	
//若电梯停在当前楼层，设置电梯显示
	public void setFloorIsStop()
	{
		for(int i=0;i<this.AllFloors.length;i++)
		{
		
			if(i==this.CurrentFloor-1)AllFloors[i].STOP =true;
			else AllFloors[i].STOP=false;
		}
		
	}
	

	
//电梯停在该层，颜色变白
	public void  ShowDoorStop()
	{
		
		for(int i=0;i<this.AllFloors.length;i++)
		{
			
			if(AllFloors[i].STOP) 
				{
				   this.AllFloors [i].ElevoterButton .setBackground(Color.white );
				
				 
				}
				
			  else this.AllFloors [i].ElevoterButton .setBackground(Color.DARK_GRAY );
			  this.AllFloors[i].ShowCurrentFloor (CurrentFloor);
		}
	}
	
	public void run() {
		
		
	
		while(true)
		{
			
			if(Mainframe.isOn&&!this.Urgency)
			{
			this.elevatorOperate();
			}
			else 
			{
				
			}
			
			
			
	//每隔一秒检测一次有没有请求
	try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	}
	}

		

	
//电梯运行
public synchronized void elevatorOperate()
{
	
	    this.setHaveSelected();
		departOperate();
		 this.setHaveSelected();
	    
	
		
}



//如果每部电梯单独运行运行，则调用以下算法
public void departOperate()
	{
	
	
			for(int i=0;i<this.AllFloors.length;i++)
	{
				if(AllFloors[i].isUp||AllFloors[i].isDown||AllFloors[i].inButtonNum){
					//如果正在门正打开，则不调度
				if(!this.ISOPEN)	
				{
					//如果是内部按钮请求；则记下请求层数；计算时间差用到
				
					if(AllFloors[i].inButtonNum&&Mainframe.isCooperation)this.LastAllotFloor=i+1;
					
					
					if(i+1>=this.CurrentFloor)
					{
						this.setUpColor(Color.RED);
						while(this.CurrentFloor <i+1)
						{
							try {
								Thread.sleep(1000);
								} catch (InterruptedException e) {
					             // TODO Auto-generated catch bloc
									e.printStackTrace();
									}
						//电源关闭和紧急按钮，电梯停止
								if(!Mainframe.isOn||this.Urgency)
									{
									for(int j=0;j<this.AllFloors.length;j++)
									{
										AllFloors[j].isUp=false;
										
									AllFloors[j].isDown=false;
									}
									this.resumeHaveSelected();

									break;
									}
								
								//允许运行途中有人搭便车	
								OperationWhenUping(i);
								
								upOneFloor();
								
				
			
				

			}
						if(Mainframe.isOn&&!this.Urgency )
						{
						AllFloors[i].OPEN=true;
						ElevatorState=STOPING;
						
						 AllFloors[i].InButton.setBackground(Color.red);
						AllFloors[i].InButton.setEnabled(false);
						this.elevatorOpenAction();
						AllFloors[i].InButton.setEnabled(true);
						 AllFloors[i].InButton.setBackground(Color.green);
						}
						this.setUpColor(Color.black);
						AllFloors[i].isUp = false;
						AllFloors[i].isDown = false;
						
			
		}
			
			
//表示请求楼层低于现在楼层		
		if(i+1<this.CurrentFloor)
		{
			{
				for(int j=this.CurrentFloor;j>i+1;j-- )
				{
					this.setDownColor(Color.RED);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//电源关闭和紧急按钮，电梯停止
					
					if(!Mainframe.isOn||this.Urgency)
					{
						for(int j1=0;j1<this.AllFloors.length;j1++)
						{
						AllFloors[j1].isUp=false;
						
					AllFloors[j1].isDown=false;
						}
					this.resumeHaveSelected();
					break;
					}
					//允许下行中有人搭便车
					OperationWhenDowning(i);
					downOneFloor();	
					
				}
				if(Mainframe.isOn&&!this.Urgency)
				{
				AllFloors[i].OPEN=true;
				this.elevatorOpenAction();
				AllFloors[i].isUp = false;
				
				
				}
				this.setDownColor(Color.black);
				ElevatorState=STOPING;
				AllFloors[i].isDown = false;
			
			}
			}
		}
		AllFloors[i].inButtonNum=false;
		}
				
				}
			}
		



// 优化算法，若电梯正在下行，若请求为下且可以搭便车，则允许搭便车
public void OperationWhenDowning(int lastRequestFloor)
{
	
	 for(int i2=0;i2<this.AllFloors.length;i2++)
	 {
		 setHaveSelected();
	 if(AllFloors[i2].isDown&&(this.CurrentFloor-i2<this.CurrentFloor-lastRequestFloor))
	 {
		 //递归调用，多次请求均可搭便车
		 OperationWhenDowning(i2);
		 
		 if(this.CurrentFloor-1==i2)
			 {
			  
			    AllFloors[i2].OPEN=true;
			    AllFloors[i2].InButton.setBackground(Color.RED);
			    this.setDownColor(Color.black);
			    AllFloors[i2].InButton.setEnabled(false);
				this.elevatorOpenAction();
				
				  this.setDownColor(Color.RED);
				AllFloors[i2].InButton.setEnabled(true);
				 AllFloors[i2].InButton.setBackground(Color.green);
				AllFloors[i2].isUp = false;
			    AllFloors[i2].isDown = false;
			 
			 }
			 }
		 
	 }
	 }

//优化算法，若电梯正在上行，若请求为上且可以搭便车，则允许搭便车			 	
public void OperationWhenUping(int lastRequestFloor)
{
	
	 for(int i2=0;i2<this.AllFloors.length;i2++)
	 {
		 setHaveSelected();
	 if(AllFloors[i2].isUp&&(i2-this.CurrentFloor<lastRequestFloor-this.CurrentFloor))
	 {
		 
		 
		 OperationWhenUping(i2); //递归算法，允许多次搭便车
		 if(this.CurrentFloor-1==i2)
			 {
			    
			    AllFloors[i2].OPEN=true;
			    AllFloors[i2].InButton.setBackground(Color.RED);
			    this.setUpColor(Color.black);
			    AllFloors[i2].InButton.setEnabled(false);
				this.elevatorOpenAction();
				 this.setUpColor(Color.red);
				AllFloors[i2].InButton.setEnabled(true);	 
				AllFloors[i2].InButton.setBackground(Color.green);
				
				
				AllFloors[i2].isUp = false;
				
			    AllFloors[i2].isDown = false;
			 
			 }
			 }
		 
	 }
	 }
	
//电梯上升一层
public void upOneFloor()
	{
		
		if(this.CurrentFloor<MAXFLOOR)
		{
			
			ElevatorState=GOINGUP;
			
			this.CurrentFloor ++;
			setFloorIsStop();
			ShowDoorStop();
		
		
		}
		
	}
	
//电梯下降一层
public void  downOneFloor()
	{
		if(this.CurrentFloor>1)
	{
		ElevatorState=GOINGDOWN;
		this.CurrentFloor --;
		setFloorIsStop();
		ShowDoorStop();
		
	}
	
	}
	 
  //设置当前层并显示出来	
	public void setCurrentFloor(int cur)
	{
		this.CurrentFloor = cur;
		setFloorIsStop();
		ShowDoorStop();
	}
	
//设置内部按钮布局	
	public void  setIncontrolButtons()
	{
		this.InControlButtons.setLayout(new GridLayout(this.MAXFLOOR,1));
		for(int i=AllFloors.length-1;  i>=0; i--)
    	{
    		
    		this.InControlButtons.add(AllFloors[i].InButton);
    	}
	}
//设置单个电梯布局
	 public  void  setElevator()
	    {
		 this.setLayout(new BorderLayout());
		 AllFloors = new  FloorPanel[this.MAXFLOOR ];
		 setFloorsPanel();
		 this.add(FloorsPanel,BorderLayout.CENTER);
		 setIncontrolButtons();
		 
		
		 this.add(InControlButtons,BorderLayout.EAST);
		 NumLabel=new JLabel("Elevator"+this.ElevatorNumber+"    电梯外     电梯内 ");
		 this.add(NumLabel,BorderLayout.SOUTH);
		 
	    }
	 
	  public void setFloorsPanel()
	  {
		    FloorsPanel.setLayout(new GridLayout(MAXFLOOR,1));
		   
	    	for(int i=AllFloors.length-1;  i>=0; i--)
	    	{
	    		
	    		AllFloors[i]=new FloorPanel(i+1);
	    	    FloorsPanel.add(AllFloors[i]);
	    	   
	    	}
	    	AllFloors[0].setFirstDownUnable();
	    	AllFloors[AllFloors.length -1].setLastUpUnable();
	    	 
	    }
	  
	  public void setUpColor(Color col)
	  {
		  for(int i=AllFloors.length-1;  i>=0; i--)
	    	{
	    	
			  AllFloors[i].setUpButtonColor(col);
	    	}
		  
		  
	  	}
	  public void setDownColor(Color col)
	  {
		  for(int i=AllFloors.length-1;  i>=0; i--)
	    	{
	    	
			  AllFloors[i].setDownButtonColor(col);
	    	}
		  
		  
	  	}
	 
      



	 
}


