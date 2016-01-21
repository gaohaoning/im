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

public class Detail_Reg_Info_UI
{
	public Network_Server network_Server;
	public JFrame frame;
	
	public AbstractTableModel abstractTableModel;
	public JTable table;
	public JScrollPane scrollPane;
	
	public JButton button_Add;
	public JButton button_Edit;
	public JButton button_Delete;
	public JButton button_Search;
	
	public Vector<Client_Info> vector_UI;
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
					//�����ʾ��Ϊ���Լ���ʾ���͡�����������ʾ������
					//ǰ����static main �������ʼ�� Vector
					//�����ڹ��캯�����ʼ�� Vector
//					final Vector<String> vector_UI=new Vector<String>();
//					for(int i=0;i<20;i++)
//					{
//						vector_UI.addElement(Integer.toString(i));
//					}
					Detail_Reg_Info_UI window = new Detail_Reg_Info_UI();
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
	public Detail_Reg_Info_UI()
	{
		vector_UI=new Vector<Client_Info>();
		initialize();
		frame.setVisible(true);
	}
	public Detail_Reg_Info_UI(Network_Server network_Server)
	{
		this.network_Server=network_Server;
		vector_UI=new Vector<Client_Info>();
		vector_UI=network_Server.infos_Reg;
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
		//�˴���ע�͵����ǲ����ô�UI���ر�ʱ������Ҳ���ر�
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
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
					return vector_UI.elementAt(rowIndex).ID;
				case 1:
					return vector_UI.elementAt(rowIndex).Name;
				case 2:
					return vector_UI.elementAt(rowIndex).Password;
				case 3:
					return vector_UI.elementAt(rowIndex).Key;
				}
//				vector_UI.elementAt(rowIndex);
				return null;
			}
			
			@Override
			public int getRowCount()
			{
				// TODO Auto-generated method stub
				//return 20;
				return vector_UI.size();
			}
			
			@Override
			public int getColumnCount()
			{
				// TODO Auto-generated method stub
				return 4;//������ʾ4������ ��ʱ
			}
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
				strings.addElement("Name");
				strings.addElement("Password");
				strings.addElement("Key");
				return strings.elementAt(column);
				//return super.getColumnName(column);
			}
			
		};
		
		
		table = new JTable(abstractTableModel);// table ���� abstractTableModel
		table.setBackground(Color.LIGHT_GRAY);
		table.setGridColor(Color.BLACK);
		table.setAutoscrolls(true);
		table.setShowGrid(true);
		//table.getSelectedRow();
		
		scrollPane = new JScrollPane(table);// scrollPane ���� table
		//scrollPane.setBounds(10, 10, 420, 200);
		scrollPane.setBounds(10, 10, 480, 200);
		scrollPane.setAutoscrolls(true);
		scrollPane.setAutoscrolls(true);
		frame.getContentPane().add(scrollPane);
		
		button_Add= new JButton("����");
		button_Add.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				new Update_UI_Add(network_Server);
			}
		});
		button_Add.setBounds(10, 220, 115, 30);
		frame.getContentPane().add(button_Add);
		
		button_Edit= new JButton("�޸�");
		button_Edit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//�޸���һ����Ҫ�� �� table.getSelectedRow() �� ��ò���
				new Update_UI_Edit(network_Server,table.getSelectedRow()); //�޸���һ����Ҫ�� AbstractTableModel ��ò���
			}
		});
		button_Edit.setBounds(125, 220, 115, 30);
		frame.getContentPane().add(button_Edit);
		
		button_Delete = new JButton("ɾ��");
		button_Delete.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//ɾ����һ����Ҫ�� �� table.getSelectedRow() �� ��ò���
				network_Server.infos_Reg.removeElementAt(table.getSelectedRow());
				network_Server.network_Server_UI.tabbedPane_Info.updateUI();//�ô˷�������ˢ���б� ���ӳ�
				network_Server.network_Server_UI.update_List_Online(network_Server.infos_Online);
				network_Server.network_Server_UI.update_List_Reg(network_Server.infos_Reg);
				network_Server.send_Broadcast_Client_Info_Online_Reg();
			}
		});
		button_Delete.setBounds(240, 220, 115, 30);
		frame.getContentPane().add(button_Delete);
		
		button_Search = new JButton("����");
		button_Search.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Update_UI_Search(network_Server);
			}
		});
		button_Search.setBounds(355, 220, 115, 30);
		frame.getContentPane().add(button_Search);
		
	}
}
