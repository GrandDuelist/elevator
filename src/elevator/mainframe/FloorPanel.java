package elevator.mainframe;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.*;

public class FloorPanel extends JPanel implements Runnable{
	
	 public boolean isDown=false;
	 public boolean isUp =false;
	 private  JButton  UpButton= new JButton("↑");
	 private JButton  DownButton= new JButton("↓");
	 public JTextField ShowCurrentFloor =new JTextField(" ",1);
     private  int  FloorNumber ;
	 public  JButton  ElevoterButton= new JButton("          ");
	 //设置内部控制按钮
	 public JButton  InButton;
	 public boolean OPEN=false;
	 public boolean STOP=false;
	// private  ElevatorLabel  door= new ElevatorLabel();
	 //该层是否有内部请求
	 public  boolean inButtonNum =false;
	 
	 //上升时改变向上之事的颜色
	 public void setUpButtonColor(Color cor)
	 {
		 this.UpButton.setForeground(cor);
	 }
	 
	 public void setFirstDownUnable()
	 {
		 this.DownButton.setEnabled(false);
	 }
	 public void setLastUpUnable()
	 {
		 this.UpButton.setEnabled(false);
	 }
	 public void setDownButtonColor(Color cor)
	 {
		 this.DownButton.setForeground(cor);
	 }
	
	 //显示电梯所在楼层
	 public void ShowCurrentFloor(int cur)
	 {
		 
		 ShowCurrentFloor.setText("      "+ cur +"   ");
		 
	 }
	 
	 
	         
	 public FloorPanel(int floorNumber){
		     super();
	    	
		     FloorNumber= floorNumber;
	         
	    	
	    	JPanel ControlButtons=new JPanel();
	    	ControlButtons.setLayout(new BorderLayout());
	    	this.UpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(Mainframe.isOn&&!Mainframe.isCooperation){
				isUp=true;
				}
				if(Mainframe.isOn&&Mainframe.isCooperation)
				{
					Mainframe.UPRequestFloor=FloorNumber;
				}
			}
			});
	    	
	    	this.DownButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if(Mainframe.isOn&&!Mainframe.isCooperation){
					isDown=true;
					}
					if(Mainframe.isOn&&Mainframe.isCooperation)
					{
						Mainframe.DOWNRequestFloor=FloorNumber;
					}
				}
			});
	    	
	        ControlButtons.add(UpButton,BorderLayout.NORTH );
	        
	        InButton=new JButton(""+FloorNumber);
	        ShowCurrentFloor.setColumns(2);
	        ShowCurrentFloor.setBorder(getBorder());
	        ShowCurrentFloor.setEnabled(false);
	        ShowCurrentFloor.setText("      "+ 1 +"   ");
	        ShowCurrentFloor.setBackground(Color.GRAY);
	        ControlButtons.add(ShowCurrentFloor,BorderLayout.CENTER );
	        ControlButtons.add(DownButton,BorderLayout.SOUTH );
	        
	       
	        
	        this.setLayout(new BorderLayout());
	        this.add(ControlButtons,BorderLayout.EAST);
	        this.ElevoterButton.setBackground(Color.DARK_GRAY);
	        this.ElevoterButton.setEnabled(false);
	        this.add(this.ElevoterButton ,BorderLayout.CENTER );
	        InButton.setBackground(Color.GREEN);
	        InButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if(Mainframe.isOn)
					{
					  inButtonNum=true;
					}
					
				}
			});
	      //  ElevoterColor.setColor(Color.LIGHT_GRAY);
	        
	       // this.add(door,BorderLayout.CENTER );
	        //ElevoterColor.fillRect(ElevoterButton.getX (),ElevoterButton.getY (), ElevoterButton.HEIGHT, ElevoterButton.WIDTH);
	        this.setSize(20,5);
	        
	    	
			
	    	
	    	
	    }



	
	public void run() {
		
		
		
		
		
	}
	 

}
