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
	//������ť
	public boolean Urgency = false;
	
	//����һ�������ڲ��İ�ť
	private Panel InControlButtons = new Panel();
    
	//private boolean FloorOPEN[]=new boolean[MAXFLOOR];
	
	//����һ����¼�����ϴ��������������
	public int LastAllotFloor=1;
	//��������ƶ���ť
	public MenuItem urgency;
	//��������ƶ��İ�ť
	public MenuItem UnUrgency;
	
	//���忪�����Ű�ť
	public MenuItem OpenDoor;
	public MenuItem CloseDoor;
	
	//�����Ǳ�ʶ��
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
	
	//�����ڲ���ť�Ĺ���
	public void setInButtonsEvents()
	{
		this.setUrgencyEvent();
		this.setOpenDoorEvent();
		this.setCloseDoorEvent();
	}
	
	
	//�����ڲ��ֶ����Ű�ť���˵���
	void setOpenDoorEvent()
	{
		OpenDoor=new MenuItem("����"+this.ElevatorNumber+"����");
		this.OpenDoor.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent event)	{
    			if(ElevatorState==STOPING)
    			{
    			ISOPEN =true;
    			AllFloors[CurrentFloor-1].ElevoterButton.setBackground(Color.ORANGE);
    			
    			}
    		}});
		
	}
	
	//�����ڲ��ֶ����Ű�ť���˵���
	void setCloseDoorEvent()
	{
		 CloseDoor =new MenuItem("����"+this.ElevatorNumber+"����");
		this.CloseDoor.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent event) {
    			if(ElevatorState==STOPING)
    			{
    			ISOPEN =false;
    			AllFloors[CurrentFloor-1].ElevoterButton .setBackground(Color.WHITE);
    			
    			}
    		}});
		
	}
	
	//���ý����ƶ����ڲ���ť��Ϊ�˱����ⲿ�����ԣ��ڲ���ť���ɵ��˵���
	
	void setUrgencyEvent()
	{
		urgency= new MenuItem("�����ƶ�����"+this.ElevatorNumber);
		this.urgency.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent event) {
    			Urgency =true;
    			ElevatorState=STOPING;
    			
    		}
    	});
		this.UnUrgency=new MenuItem("�������"+this.ElevatorNumber+"���ƶ�");
		

		this.UnUrgency.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent event) {
    			Urgency =false;
    			//Elevators[ItemCount].Urgency=true;
    		}
    	});
	}
	
//�鿴�Ƿ�������	������Mainframeͳһ����
	
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
	
   
 //���õ��ݿ���ʱ����ʱ�ͼ򵥶���
   
   public void elevatorOpenAction() 
   {
	   
	   
		
		
		
			for(int i=0;i<this.AllFloors.length;i++)
			{
				//�ư�������
				if(AllFloors[i].OPEN) 
					{
					
					  for(int k=0;k<2;k++);
					  {
						
					   this.AllFloors [i].ElevoterButton .setBackground(Color.WHITE );
			//ͣ����ʱ2��(��ͬ�����������ʱ��)����Լ���ʱ����̷��䷽������
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
					  
					  //��ƣ�����ͣ��1��
					  
					  
					  try {
							Thread.sleep(1000);
						   } catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						    }
						//��ף����ţ� 
					  this.AllFloors [i].ElevoterButton .setBackground(Color.white );
					  AllFloors [i].OPEN =false;   
					}
			}
				
		
			
	   
   }
   //������ʾѡ�����¥��
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
   
  //�����ƶ�ʱ��ȡ��ѡ���¥��
   public void resumeHaveSelected()
   {
	   for(int k=0;k<this.AllFloors.length;k++)
		{
		   AllFloors[k].InButton.setEnabled(true);
		   AllFloors[k].InButton.setBackground(Color.GREEN);

		}
   }
   
	
//������ͣ�ڵ�ǰ¥�㣬���õ�����ʾ
	public void setFloorIsStop()
	{
		for(int i=0;i<this.AllFloors.length;i++)
		{
		
			if(i==this.CurrentFloor-1)AllFloors[i].STOP =true;
			else AllFloors[i].STOP=false;
		}
		
	}
	

	
//����ͣ�ڸò㣬��ɫ���
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
			
			
			
	//ÿ��һ����һ����û������
	try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	}
	}

		

	
//��������
public synchronized void elevatorOperate()
{
	
	    this.setHaveSelected();
		departOperate();
		 this.setHaveSelected();
	    
	
		
}



//���ÿ�����ݵ����������У�����������㷨
public void departOperate()
	{
	
	
			for(int i=0;i<this.AllFloors.length;i++)
	{
				if(AllFloors[i].isUp||AllFloors[i].isDown||AllFloors[i].inButtonNum){
					//������������򿪣��򲻵���
				if(!this.ISOPEN)	
				{
					//������ڲ���ť����������������������ʱ����õ�
				
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
						//��Դ�رպͽ�����ť������ֹͣ
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
								
								//��������;�����˴�㳵	
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
			
			
//��ʾ����¥���������¥��		
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
					//��Դ�رպͽ�����ť������ֹͣ
					
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
					//�������������˴�㳵
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
		



// �Ż��㷨���������������У�������Ϊ���ҿ��Դ�㳵���������㳵
public void OperationWhenDowning(int lastRequestFloor)
{
	
	 for(int i2=0;i2<this.AllFloors.length;i2++)
	 {
		 setHaveSelected();
	 if(AllFloors[i2].isDown&&(this.CurrentFloor-i2<this.CurrentFloor-lastRequestFloor))
	 {
		 //�ݹ���ã����������ɴ�㳵
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

//�Ż��㷨���������������У�������Ϊ���ҿ��Դ�㳵���������㳵			 	
public void OperationWhenUping(int lastRequestFloor)
{
	
	 for(int i2=0;i2<this.AllFloors.length;i2++)
	 {
		 setHaveSelected();
	 if(AllFloors[i2].isUp&&(i2-this.CurrentFloor<lastRequestFloor-this.CurrentFloor))
	 {
		 
		 
		 OperationWhenUping(i2); //�ݹ��㷨�������δ�㳵
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
	
//��������һ��
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
	
//�����½�һ��
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
	 
  //���õ�ǰ�㲢��ʾ����	
	public void setCurrentFloor(int cur)
	{
		this.CurrentFloor = cur;
		setFloorIsStop();
		ShowDoorStop();
	}
	
//�����ڲ���ť����	
	public void  setIncontrolButtons()
	{
		this.InControlButtons.setLayout(new GridLayout(this.MAXFLOOR,1));
		for(int i=AllFloors.length-1;  i>=0; i--)
    	{
    		
    		this.InControlButtons.add(AllFloors[i].InButton);
    	}
	}
//���õ������ݲ���
	 public  void  setElevator()
	    {
		 this.setLayout(new BorderLayout());
		 AllFloors = new  FloorPanel[this.MAXFLOOR ];
		 setFloorsPanel();
		 this.add(FloorsPanel,BorderLayout.CENTER);
		 setIncontrolButtons();
		 
		
		 this.add(InControlButtons,BorderLayout.EAST);
		 NumLabel=new JLabel("Elevator"+this.ElevatorNumber+"    ������     ������ ");
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


