package elevator.mainframe;
import java.io.*;

public class Record {
	
	public static  void  RecordFileInit() 
	{
		File f = new File("d:\\record.txt") ;
//清空原有日志
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
	
	
	
	//将分配的电梯写入日志
	public static void WriteAllotToRecord(int elevatorNum,int floorNum)
	{
		
		String str ="电梯"+elevatorNum+"被调度前往"+floorNum+"楼\n";
		WriteToFile(str);
		WriteToFile("---------------------------------调度完成");
	
	}
	//将调度时的各个电梯状态写入日志
	public static void WriteCurrentElevatorStateToRecord(int elevatorNum,int currentNum,int lastRequestNum)
	{  
		String str ="电梯"+elevatorNum+"正在"+currentNum+"楼"+"最后要达到"+lastRequestNum+"楼\n";
		WriteToFile(str);
		
	}
	
	//将有向上请求的楼层写入日志
	public static void WriteUpRequestToRecord(int floorNum)
	{
	   
		String str ="楼层"+floorNum+"有向上请求\n";
		WriteToFile(str);
		WriteToFile("---------------------");
	}
	
  //将有向下请求的楼层写入日志
	public static void WriteDownRequestToRecord(int floorNum)
	{
	
		String str ="楼层"+floorNum+"有向下请求";
		
		WriteToFile(str);
		WriteToFile("---------------------");
	}

//写入日志
public static void WriteToFile(String str)
{
	// 1、表示要操作record.txt文件
	File f = new File("d:\\record.txt") ;
	OutputStream out = null ;
	// 2、通过子类实例化
	// 使用FileOutputStream子类
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
