package elevator.mainframe;
import java.io.*;

public class Record {
	
	public static  void  RecordFileInit() 
	{
		File f = new File("d:\\record.txt") ;
//���ԭ����־
			FileWriter fw5 = null;
			try {
				fw5 = new FileWriter(f);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			BufferedWriter bw1 = new BufferedWriter(fw5);
			try {
				bw1.write("");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				f.createNewFile() ;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	
	
	//������ĵ���д����־
	public static void WriteAllotToRecord(int elevatorNum,int floorNum)
	{
		
		String str ="����"+elevatorNum+"������ǰ��"+floorNum+"¥\n";
		WriteToFile(str);
		WriteToFile("---------------------------------�������");
	
	}
	//������ʱ�ĸ�������״̬д����־
	public static void WriteCurrentElevatorStateToRecord(int elevatorNum,int currentNum,int lastRequestNum)
	{  
		String str ="����"+elevatorNum+"����"+currentNum+"¥"+"���Ҫ�ﵽ"+lastRequestNum+"¥\n";
		WriteToFile(str);
		
	}
	
	//�������������¥��д����־
	public static void WriteUpRequestToRecord(int floorNum)
	{
	   
		String str ="¥��"+floorNum+"����������\n";
		WriteToFile(str);
		WriteToFile("---------------------");
	}
	
  //�������������¥��д����־
	public static void WriteDownRequestToRecord(int floorNum)
	{
	
		String str ="¥��"+floorNum+"����������";
		
		WriteToFile(str);
		WriteToFile("---------------------");
	}

//д����־
public static void WriteToFile(String str)
{
	// 1����ʾҪ����record.txt�ļ�
	File f = new File("d:\\record.txt") ;
	OutputStream out = null ;
	// 2��ͨ������ʵ����
	// ʹ��FileOutputStream����
	try
	{
		out = new FileOutputStream(f,true) ;
	}
	catch (Exception e)
	{
	}
	  OutputStreamWriter osw = new OutputStreamWriter(out);
	 BufferedWriter bw = new BufferedWriter(osw);
	
	 
	 try {
		bw.write(str);
	} catch (IOException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}
	 try {
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		  try {
			bw.newLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}     
		  try {
			bw.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    
		  try {
			osw.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    
		  try {
			out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		
	
	}
	
	
}
