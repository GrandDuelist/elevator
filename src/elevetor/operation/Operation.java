package elevetor.operation;
import javax.swing.JOptionPane;

import elevator.mainframe.*;

public class Operation {
	
	public static void main(String arg[])
	{
		Object[] possibleValues = { "1",  "2",  "3","4",  "5",  "6",  "7",  "8",   }; //�û���ѡ����Ŀ
		 Object selectedValue = JOptionPane.showInputDialog(null, "ѡ�������", "��ӭ�������õ���",JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[4]);
		 //����ȡ��������selectedValue==null;ϵͳ�˳�
		 if(selectedValue==null)System.exit(1);
		 //����ѡ�����õ��ݵ���
		 Mainframe.MAXELEVATOR=Integer.parseInt((String)selectedValue);
		 String inputValue = JOptionPane.showInputDialog("������¥����������С��100��");
		 
		
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
				 JOptionPane.showMessageDialog(null, "����¥��������Ҫ����Ĭ��Ϊ20��", "¥��������", JOptionPane.ERROR_MESSAGE);
			 }
		 }
		 else 
		 {
			 JOptionPane.showMessageDialog(null, "����¥��������Ҫ����Ĭ��Ϊ20��", "¥��������", JOptionPane.ERROR_MESSAGE);
		 }
	     new Mainframe();
	    
	}
	
	
	

}
