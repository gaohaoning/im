package network.ui;

import java.awt.EventQueue;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import network.server.Network_Server;
import network.server.Network_Server.Server_Thread;
import network.util.Client_Info;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Detail_Online_Info_UI
{
	// ======================================================================
	public Network_Server network_Server;
	// ======================================================================
	public JFrame frame;
	
	public AbstractTableModel abstractTableModel;
	public JTable table;
	public JScrollPane scrollPane;
	
	//public Vector<Client_Info> vector_UI;
	public Vector<Server_Thread> vector_UI;
	// ======================================================================
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					//表格显示分为【自己显示】和【被主程序显示】两种
					//前者在static main 方法里初始化 Vector
					//后者在构造函数里初始化 Vector
//					//==============================================================
//					final Vector<String> vector_UI=new Vector<String>();
//					for(int i=0;i<20;i++)
//					{
//						vector_UI.addElement(Integer.toString(i));
//					}
//					//==============================================================
					Detail_Online_Info_UI window = new Detail_Online_Info_UI();
					window.frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Detail_Online_Info_UI()
	{
		vector_UI=new Vector<Server_Thread>();
		initialize();
		frame.setVisible(true);
	}
	public Detail_Online_Info_UI(Network_Server network_Server)
	{
		this.network_Server=network_Server;
		vector_UI=new Vector<Server_Thread>();
		vector_UI=network_Server.server_Threads;
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(300, 300, 500, 300);//(10, 10, 500, 300);
		//此处【注释掉】是不想让此UI被关闭时主程序也被关闭
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		//===================================================================
		//===================================================================
		//===================================================================
		abstractTableModel=new AbstractTableModel()
		{
			@Override
			public Object getValueAt(int rowIndex, int columnIndex)
			{
				// TODO Auto-generated method stub
				//return new Integer(rowIndex*columnIndex);
				switch(columnIndex)
				{
				case 0:
					return vector_UI.elementAt(rowIndex).client_Info_Thread.ID;
					//return network_Server.server_Threads.elementAt(rowIndex).client_Info_Thread.ID;
				case 1:
					return vector_UI.elementAt(rowIndex).client_Info_Thread.ip_Address;
					//return network_Server.server_Threads.elementAt(rowIndex).client_Info_Thread.ip_Address;
				case 2:
					return vector_UI.elementAt(rowIndex).client_Info_Thread.ip_Port;
					//return network_Server.server_Threads.elementAt(rowIndex).client_Info_Thread.ip_Port;
				}
				return null;
			}
			
			@Override
			public int getRowCount()
			{
				return vector_UI.size();
				//return network_Server.server_Threads.size();
			}
			
			@Override
			public int getColumnCount()
			{
				// TODO Auto-generated method stub
				return 3;//竖排显示4列资料 暂时
			}
			//======================================================
			@Override
			public void addTableModelListener(TableModelListener l)
			{
				// TODO Auto-generated method stub
				super.addTableModelListener(l);
			}

			@Override
			public void setValueAt(Object aValue, int rowIndex, int columnIndex)
			{
				// TODO Auto-generated method stub
				super.setValueAt(aValue, rowIndex, columnIndex);
			}

			@Override
			public boolean isCellEditable(int arg0, int arg1)
			{
				// TODO Auto-generated method stub
				//return super.isCellEditable(arg0, arg1);
				return false;
			}

			@Override
			public String getColumnName(int column)
			{
				// TODO Auto-generated method stub
				Vector<String> strings=new Vector<String>();
				strings.addElement("ID");
				strings.addElement("IP");
				strings.addElement("Port");
				return strings.elementAt(column);
				//return super.getColumnName(column);
			}
			
		};
		
		
		table = new JTable(abstractTableModel);// table 包含 abstractTableModel
		table.setBackground(Color.LIGHT_GRAY);
		table.setGridColor(Color.BLACK);
		table.setAutoscrolls(true);
		table.setShowGrid(true);
		//==============================================
		//table.getSelectedRow();
		//==============================================
		
		scrollPane = new JScrollPane(table);// scrollPane 包含 table
		//scrollPane.setBounds(10, 10, 420, 200);
		scrollPane.setBounds(10, 10, 480, 250);
		scrollPane.setAutoscrolls(true);
		scrollPane.setAutoscrolls(true);
		frame.getContentPane().add(scrollPane);
		
		// ===================================================================
		// ===================================================================
		// ===================================================================
		// ===================================================================
		// ===================================================================
		// ===================================================================
		// ===================================================================
		
	}
}
