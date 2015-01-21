package elevetor.operation;
import javax.swing.JOptionPane;

import elevator.mainframe.*;

public class Operation {
	
	public static void main(String arg[])
	{
		Object[] possibleValues = { "1",  "2",  "3","4",  "5",  "6",  "7",  "8",   }; //用户的选择项目
		 Object selectedValue = JOptionPane.showInputDialog(null, "选择电梯数", "欢迎，请设置电梯",JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[4]);
		 //按了取消键，及selectedValue==null;系统退出
		 if(selectedValue==null)System.exit(1);
		 //根据选择设置电梯的数
		 Mainframe.MAXELEVATOR=Integer.parseInt((String)selectedValue);
		 String inputValue = JOptionPane.showInputDialog("请输入楼层数（输入小于100）");
		 
		
		 boolean isNum=true;
		 if(inputValue==null||inputValue.equals(""))isNum=false;
		 else
		 {
		 for(int i=0;i<inputValue.length();i++)
		 {
			 
			 if(inputValue.charAt(i)<'0'||inputValue.charAt(i)>'9')
					 
				 
					 isNum=false;
		 }
		 }
		 
		 if(isNum)
		 {
			 int a=Integer.parseInt(inputValue);
		 
		   if(a<=100&&a>0)ElevatorPanel.MAXFLOOR=a;
		 
		   else 
			 {
				 JOptionPane.showMessageDialog(null, "输入楼层数不合要求（已默认为20）", "楼层数错误", JOptionPane.ERROR_MESSAGE);
			 }
		 }
		 else 
		 {
			 JOptionPane.showMessageDialog(null, "输入楼层数不合要求（已默认为20）", "楼层数错误", JOptionPane.ERROR_MESSAGE);
		 }
	     new Mainframe();
	    
	}
	
	
	

}
