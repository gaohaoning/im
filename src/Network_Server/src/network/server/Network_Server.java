package network.server;

import java.awt.Container;
import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

import javax.swing.*;

import network.codec.*;
import network.util.*;
import network.ui.*;

public class Network_Server
{
	public Codec codec;
	public ServerSocket server_Socket;
	public int server_Port = 11111;
	public Vector<Server_Thread> server_Threads;// ���� ��ͻ���ͨ���߳� �ļ���
	public Vector<Client_Info> infos_Online;// ���С������û������Զ�̬�޸�
	public Vector<Client_Info> infos_Reg;// ���С�ע���û�����ʼʱ���ļ���ȡ
	public String str_Client_Info_Online;//��String��ʽά���û�ID��Name��Ϣ
	public String str_Client_Info_Reg;//��String��ʽά���û�ID��Name��Ϣ
	public Network_Server_UI network_Server_UI;// ��UIһֱ����	
	public Detail_Reg_Info_UI detail_Reg_Info_UI;
	public Detail_Online_Info_UI detail_Online_Info_UI;
	public Inf_UI inf_UI;
	
	public boolean read_Infos_Reg_Default()//�����ȴ��ڱ������� Ȼ������Ǵ��ļ��ж�ȡ��д�빦��
	{
		try
		{
			infos_Reg.addElement(new Client_Info("1", "Beckham", "000"));
			infos_Reg.addElement(new Client_Info("2", "Ronaldo", "000"));
			infos_Reg.addElement(new Client_Info("3", "Messi", "000"));
			infos_Reg.addElement(new Client_Info("4", "Zidane", "000"));
			infos_Reg.addElement(new Client_Info("5", "Figo", "000"));
			infos_Reg.addElement(new Client_Info("6", "Carlos", "000"));
			infos_Reg.addElement(new Client_Info("7", "Giggs", "000"));
			infos_Reg.addElement(new Client_Info("8", "Raul", "000"));
			infos_Reg.addElement(new Client_Info("9", "Inzaghi", "000"));
			infos_Reg.addElement(new Client_Info("10", "Robinho", "000"));
			infos_Reg.addElement(new Client_Info("11", "Nedved", "000"));
			infos_Reg.addElement(new Client_Info("12", "Henry", "000"));
			infos_Reg.addElement(new Client_Info("13", "Owen", "000"));
			infos_Reg.addElement(new Client_Info("14", "Crespo", "000"));
			infos_Reg.addElement(new Client_Info("15", "Totti", "000"));
			show("��ȡĬ��ע���û���Ϣ �ɹ�");
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			show("��ȡĬ��ע���û���Ϣ �쳣");
			return false;
		}
	}
	
	public boolean write_User_Info_Reg_to_File(String file_String)//����ע���û�����Ϣ ����
	{
		try
		{
			File file_UserInfo=new File(file_String);
			ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(file_UserInfo));	
			out.writeObject(infos_Reg);
			out.close();
			show("write_User_Info_Reg_to_File �ɹ�");
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			show("write_User_Info_Reg_to_File �쳣");
			return false;
		}
	}

	public boolean read_User_Info_Reg_from_File(String file_String)
	{
		try
		{
			File file_UserInfo=new File(file_String);
			ObjectInputStream in=new ObjectInputStream(new FileInputStream(file_UserInfo));
			infos_Reg=(Vector<Client_Info>)in.readObject();
			in.close();
			show("read_User_Info_Reg_from_File �ɹ� : "+infos_Reg.toString());
			network_Server_UI.update_List_Reg(infos_Reg);
			send_Broadcast_Client_Info_Online_Reg();
			detail_Reg_Info_UI.vector_UI=infos_Reg;			
			network_Server_UI.tabbedPane_Info.updateUI();//�ô˷�������ˢ���б� ���ӳ�
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			show("read_User_Info_Reg_from_File �쳣");
			return false;
		}
	}

	synchronized public void add_Info_from_Reg_to_Online_by_ID(String id)
	{
		for(int i=0;i<infos_Reg.size();i++)
		{
			if(infos_Reg.elementAt(i).ID.equals(id))
			{
				infos_Online.addElement(infos_Reg.elementAt(i));
				return;
			}
		}
	}

	synchronized public void add_Info_to_Reg_by_Client_Info(Client_Info client_Info)
	{
		infos_Reg.addElement(client_Info);
	}

	synchronized public boolean replace_ClientInfo_fromReg_toOnline_byID(String str_ID)
	{
		for(int i=0;i<infos_Reg.size();i++)
		{
			if(infos_Reg.elementAt(i).ID.equals(str_ID))
			{			
				for(int ii=0;ii<infos_Online.size();ii++)
				{
					if(infos_Online.elementAt(ii).ID.equals(str_ID))
					{
						infos_Online.elementAt(ii).Password=
								infos_Reg.elementAt(i).Password;
						infos_Online.elementAt(ii).Name=
								infos_Reg.elementAt(i).Name;
						infos_Online.elementAt(ii).Key=
								infos_Reg.elementAt(i).Key;						
						return true;
					}
				}
			}
		}
		return false;
	}

	public Network_Server()
	{
		try
		{
			codec = new Codec(this);
			network_Server_UI = new Network_Server_UI(this);
			inf_UI =new Inf_UI(this);
			inf_UI.frame.setVisible(false);
			server_Threads =new Vector<Server_Thread>();
			infos_Online =new Vector<Client_Info>();
			infos_Reg =new Vector<Client_Info>();
			read_Infos_Reg_Default();
			network_Server_UI.update_List_Reg(infos_Reg);
			detail_Reg_Info_UI=new Detail_Reg_Info_UI(this);
			detail_Reg_Info_UI.frame.setVisible(false);
			detail_Online_Info_UI=new Detail_Online_Info_UI(this);
			detail_Online_Info_UI.frame.setVisible(false);
			server_Socket = new ServerSocket(server_Port);
			show("����������ʼ�������˿ڡ� : "+server_Port+"���ɹ���");
			Socket incoming;
			while (true)
			{			
				incoming = server_Socket.accept();//����
				new Server_Thread_Verification(incoming).start();//ִ�� Thread.run();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			show("����������ʼ���� �쳣");
		}
	}

	public Network_Server(InetAddress address,int server_Port)
	{
		try
		{
			this.server_Port=server_Port;
			codec = new Codec(this);
			network_Server_UI = new Network_Server_UI(this);
			inf_UI =new Inf_UI(this);
			inf_UI.frame.setVisible(false);
			server_Threads =new Vector<Server_Thread>();
			infos_Online =new Vector<Client_Info>();
			infos_Reg =new Vector<Client_Info>();
			read_Infos_Reg_Default();
			network_Server_UI.update_List_Reg(infos_Reg);
			detail_Reg_Info_UI=new Detail_Reg_Info_UI(this);
			detail_Reg_Info_UI.frame.setVisible(false);
			detail_Online_Info_UI=new Detail_Online_Info_UI(this);
			detail_Online_Info_UI.frame.setVisible(false);
			server_Socket = new ServerSocket(server_Port);
			show("����������ʼ�������˿ڡ� : "+server_Port+"���ɹ���");	
			Socket incoming;
			while (true)
			{			
				incoming = server_Socket.accept();//����
				new Server_Thread_Verification(incoming).start();//ִ�� Thread.run();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			show("����������ʼ���� �쳣");
		}
	}
	
	public class Server_Thread_Verification extends Thread
	{
		public Client_Info client_Info_Thread;// ά���߳��� �û���Ϣ
		public Socket socket_Thread;
		public InetAddress address_Thread;
		public int port_Thread;
		public ObjectInputStream in_obj;
		public ObjectOutputStream out_obj;
		public Server_Thread_Verification(Socket socket)
		{
			try
			{
				socket_Thread = socket;
				address_Thread=socket_Thread.getInetAddress();
				port_Thread=socket_Thread.getPort();				
				show("�����ͻ������� :");
				show("Client IP : " + address_Thread + " Port : "+port_Thread);
				
				in_obj=new ObjectInputStream(socket_Thread.getInputStream());
				out_obj=new ObjectOutputStream(socket_Thread.getOutputStream());
				show("����ʼ�������������OK");
			} catch (Exception e)
			{
				e.printStackTrace();
				show("����ʼ������������� �쳣");
			}
		}
		@Override
		public void run()
		{
			try
			{
				Msg msg = (Msg) in_obj.readObject();
				show("���յ������ͻ�����Ϣ�� : " + msg.msg_Type);
				switch (msg.msg_Type)
				{
				case login:
					server_Msg_Handler_Login(msg);
					break;
				case register:
					server_Msg_Handler_Register(msg);
					break;
				}
			} catch (SocketException socketException)//�ͻ���˽�ԶϿ�ʱ���쳣
			{				
				socketException.printStackTrace();
				show("�ͻ���˽�ԶϿ�ʱ���쳣 "+"SocketException");//��Ҫ�ر���Ӧ�߳�//�Ƴ��������û�����
			} catch(NullPointerException nullPointerException)
			{				
				nullPointerException.printStackTrace();
				show("�����������߳��쳣 "+"NullPointerException");
			} catch (Exception e)
			{
				e.printStackTrace();
				show("Server_Thread_Verification �쳣");
			}
		}
		
		public boolean send_Unicast(Msg msg)//���ڵ��������� �߳����� ���Բ���Ҫָ��IP��ַ ֱ��ʹ���߳��е������
		{
			try
			{
				out_obj.writeObject(msg);
				out_obj.flush();
				return true;
			} catch (Exception e)
			{
				show("send_Unicast �쳣");
				return false;
			}
		}
		
		synchronized public boolean server_Msg_Handler_Login(Msg msg)
		{
			try
			{
				client_Info_Thread = msg.msg_Client_Info;
				show("���յ���Ϣ��:���ͻ��˵�½��:");
				show(msg.msg_Content);
				show(msg.msg_Client_Info.ID+msg.msg_Client_Info.Name+msg.msg_Client_Info.Password);
				if (user_Verification_Login(msg.msg_Client_Info))// ��֤�ɹ� ���� accept
				{
					show("���û���֤�ɹ���: " + msg.msg_Client_Info.ID);
					// ����Ҫ�жϸ��û��Ƿ���ǰ�Ѿ��ڱ𴦵�¼
					if(!is_Client_Already_Online_by_ID(msg.msg_Client_Info.ID))// ���û���δ��¼
					{
						show("���û���֤�ɹ�����δ�ڱ𴦵�¼��: " + msg.msg_Client_Info.ID);						
						Msg msg_Accept=new Msg(Msg_Type.login_echo_accept);
						send_Unicast(msg_Accept);
						add_Info_from_Reg_to_Online_by_ID(msg.msg_Client_Info.ID);
						network_Server_UI.update_List_Online(infos_Online);
						network_Server_UI.update_List_Reg(infos_Reg);
						Server_Thread server_Thread=new Server_Thread(client_Info_Thread, socket_Thread, 
								address_Thread, port_Thread, in_obj, out_obj);
						server_Thread.start();
						//��¼�ɹ��� Ҫ��Client���Ϳͻ����ϸ�����Ϣ ���� Update_Info
						send_Detail_Update_Info_Back_To_Login_User(msg);
					} else// ���û��Ѿ��ڱ𴦵�¼
					{
						show("���û���֤�ɹ������Ѿ��ڱ𴦵�¼��");
						//�Ȱ��Ѿ����ߵ��߳�
						Server_Thread thread_To_Kickout=get_Server_Thread_by_Client_ID(msg.msg_Client_Info.ID);
						Msg msg_Kickout=new Msg(Msg_Type.kickedout);
						thread_To_Kickout.send_Unicast(msg_Kickout);
						//�߳�����߳�Socket�����쳣 //���쳣���� ͨ���߳��д���
						thread_To_Kickout.close_Socket_Inside_Thread();
						thread_To_Kickout.remove_Thread_Inside_Thread();
						thread_To_Kickout.remove_Info_Online_Inside_Thread();
						//�ٰѵ�ǰ�ĵ�¼��
						show("���û���֤�ɹ������Ѿ��𴦵�¼���������߳��� ");						
						Msg msg_Accept=new Msg(Msg_Type.login_echo_accept);
						send_Unicast(msg_Accept);
						//(1)
						add_Info_from_Reg_to_Online_by_ID(msg.msg_Client_Info.ID);
						network_Server_UI.update_List_Online(infos_Online);
						network_Server_UI.update_List_Reg(infos_Reg);
						Server_Thread server_Thread=new Server_Thread(client_Info_Thread, socket_Thread, 
								address_Thread, port_Thread, in_obj, out_obj);
						server_Thread.start();
						//(2)
						add_Info_from_Reg_to_Online_by_ID(msg.msg_Client_Info.ID);
						network_Server_UI.update_List_Online(infos_Online);
						network_Server_UI.update_List_Reg(infos_Reg);
						//��¼�ɹ��� Ҫ��Client���Ϳͻ����ϸ�����Ϣ ���� Update_Info
						send_Detail_Update_Info_Back_To_Login_User(msg);
						send_Broadcast_Client_Info_Online_Reg();
						show_Num_of_server_Threads_clientInfos_online();
					}
				} else// ��֤ʧ�� �ܾ� reject				
				{
					show("���û���֤ʧ�ܡ�: " + msg.msg_Client_Info.ID);
					Msg msg_Reject=new Msg(Msg_Type.login_echo_reject);
					send_Unicast(msg_Reject);
				}
				show_Num_of_server_Threads_clientInfos_online();
				return true;
			} catch (Exception e)
			{
				e.printStackTrace();
				show("server_Msg_Handler_Login �쳣");
				return false;
			}
		}

		synchronized public boolean server_Msg_Handler_Register(Msg msg)
		{
			try
			{
				client_Info_Thread = msg.msg_Client_Info;
				show("���յ���Ϣ��:���ͻ���ע�᡿:");
				show(msg.msg_Content);
				show(msg.msg_Client_Info.ID+msg.msg_Client_Info.Name+msg.msg_Client_Info.Password);
				if (user_Verification_Register(msg.msg_Client_Info))// ��֤�ɹ� ����// accept��ע��ɹ���ֱ�ӵ�½��																
				{
					show("���û�ע��ɹ���: " + msg.msg_Client_Info.ID);
					Msg msg_Accept=new Msg(Msg_Type.register_echo_accept);
					send_Unicast(msg_Accept);
					add_Info_to_Reg_by_Client_Info(msg.msg_Client_Info);
					network_Server_UI.update_List_Reg(infos_Reg);
					add_Info_from_Reg_to_Online_by_ID(msg.msg_Client_Info.ID);
					network_Server_UI.update_List_Online(infos_Online);
					network_Server_UI.update_List_Reg(infos_Reg);
					Server_Thread server_Thread=new Server_Thread(client_Info_Thread, socket_Thread, 
							address_Thread, port_Thread, in_obj, out_obj);
					server_Thread.start();
					//��¼�ɹ��� Ҫ��Client���Ϳͻ����ϸ�����Ϣ ���� Update_Info
					send_Detail_Update_Info_Back_To_Login_User(msg);
				} else// ��֤ʧ�� �ܾ� reject
				{
					show("���û���֤ʧ�ܡ�: " + msg.msg_Client_Info.ID);
					Msg msg_Reject=new Msg(Msg_Type.register_echo_reject);
					send_Unicast(msg_Reject);
				}
				show_Num_of_server_Threads_clientInfos_online();
				return true;
			} catch (Exception e)
			{
				e.printStackTrace();
				show("server_Msg_Handler_Register �쳣");
				return false;
			}
		}
		
		synchronized public void send_Detail_Update_Info_Back_To_Login_User(Msg msg)
		{
			Msg msg_Update_Info=new Msg(Msg_Type.user_data_update);
			msg_Update_Info.msg_Update_Info=new Update_Info();//����� new ������
			
			for(int i=0;i<infos_Reg.size();i++)
			{
				if(infos_Reg.elementAt(i).ID.equals(msg.msg_Client_Info.ID))
				{
					show("��["+infos_Reg.elementAt(i).ID+"] �����û���ϸ��Ϣ :");
					show(infos_Reg.elementAt(i).ID);
					show(infos_Reg.elementAt(i).Password);
					show(infos_Reg.elementAt(i).Name);
					show(infos_Reg.elementAt(i).Key);
					
					msg_Update_Info.msg_Update_Info.user_ID=infos_Reg.elementAt(i).ID;
					msg_Update_Info.msg_Update_Info.user_Password=infos_Reg.elementAt(i).Password;
					msg_Update_Info.msg_Update_Info.user_Name=infos_Reg.elementAt(i).Name;
					msg_Update_Info.msg_Update_Info.user_Key=infos_Reg.elementAt(i).Key;								
				}
			}
			send_Unicast(msg_Update_Info);			
		}
	}
	
	public boolean user_Verification_Login(Client_Info client_Info)// �û���½��֤
	{
		for (int i = 0; i < infos_Reg.size(); i++)
		{
			if (infos_Reg.elementAt(i).ID.equals(client_Info.ID))// ��֤�Ƿ���ڴ�ID
			{
				if (infos_Reg.elementAt(i).Password.equals(client_Info.Password))// ��֤����
				{
					return true;
				}
			}
		}
		return false;// ���ݿ����޴��û����ϣ�����֤ʧ��
	}
	
	public boolean user_Verification_Register(Client_Info client_Info)// �û������֤
	{
		for (int i = 0; i < infos_Reg.size(); i++)
		{
			if (infos_Reg.elementAt(i).ID.equals(client_Info.ID))// ��֤�Ƿ���ڴ�ID
			{
				return false;
			}
		}
		return true;
	}

	public boolean is_Client_Already_Online_by_ID(String id)
	{
		for(int i=0;i<infos_Online.size();i++)
		{
			if(infos_Online.elementAt(i).ID.equals(id))
			{
				return true;
			}
		}
		return false;
	}

	public boolean is_Client_ID_Already_Exist(String id)
	{
		for(int i=0;i<infos_Reg.size();i++)
		{
			if(infos_Reg.elementAt(i).ID.equals(id))
			{
				return true;
			}
		}
		return false;
	}

	public boolean send_Broadcast(Msg msg)
	{		
		try
		{
			for(int i=0;i<server_Threads.size();i++)
			{
				server_Threads.elementAt(i).send_Unicast(msg);//��ÿһ���߳�����ͻ��˷��͹㲥
			}
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			show("send_Broadcast �쳣");
			return false;
		}
	}

	public boolean send_Broadcast_Client_Info_Online_Reg()//ʹ�ù㲥
	{
		try
		{
			Msg msg=new Msg(Msg_Type.users_update);	
			
//			//(1) ��Object [] �����û��б� ���û�������Ϣʱ�����ͻ����޷�����Name
//			msg.online=infos_Online.toArray();
//			msg.reg=infos_Reg.toArray();
			
			//(2) ��String �����б����Ƚ� �����������
			generate_Str_Client_Info_Online();
			msg.online_String=str_Client_Info_Online;
			generate_Str_Client_Info_Reg();
			msg.reg_String=str_Client_Info_Reg;			
			
			send_Broadcast(msg);//�㲥	
			show("���㲥�û��б���Ϣ��");
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			show("���㲥�û��б���Ϣ�쳣��");
			return false;
		}
	}

	public boolean on_Btn_Broadcast(String string)
	{
		try
		{
			Msg msg=new Msg(Msg_Type.broadcast);
			msg.msg_Content=string;			
			send_Broadcast(msg);//�㲥			
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			show("on_Btn_Broadcast �쳣");
			return false;
		}
	}

	public Server_Thread get_Server_Thread_by_Client_ID(String str_ID)
	{
		for(int i=0;i<server_Threads.size();i++)
		{
			if(server_Threads.elementAt(i).client_Info_Thread.ID.equals(str_ID))
			{
				return server_Threads.elementAt(i);
			}
		}
		return null;
	}

	synchronized public void KickOut_User_Outside_Thread_by_ID(String str_ID)
	{
		for(int i=0;i<server_Threads.size();i++)
		{
			if(server_Threads.elementAt(i).client_Info_Thread.ID.equals(str_ID))
			{
				server_Threads.elementAt(i).KickOut_User_Inside_Thread();
				send_Broadcast_Client_Info_Online_Reg();
				return;
			}
		}
	}

	//������������÷������˶���Java���ֵġ�������ɱ������
	public String get_Key_Codec_from_Reg_by_ID(String receiverID)
	{
		for(int i=0;i<infos_Reg.size();i++)
		{
			if(infos_Reg.elementAt(i).ID.equals(receiverID))
			{
				//String.equals() ��������� null ���׳��쳣 ��ע�⡿
				// null ������ ""
				//�ҵ��˸ý����ߵ�ע�����ϣ�û����Կ���� ����String�� ��""��
				//��Ҫ�������� "" ������ null
				if(String.valueOf(infos_Reg.elementAt(i).Key).equals("null"))
				{
					return "";
				}
				else
				{
					return infos_Reg.elementAt(i).Key;
				}
			}
		}
		return "";//û�ҵ�Ҳ���� �� "" ���ڿ϶��ҵõ� ��������һ�㲻�ᱻִ��
	}

	public class Server_Thread extends Thread
	{
		public Client_Info client_Info_Thread;// ά���߳��� �û���Ϣ
		private long thread_ID = this.getId();
		private String thread_Name = this.getName();
		public Socket socket_Thread;
		public InetAddress address_Thread;
		public int port_Thread;
		public ObjectInputStream in_obj;
		public ObjectOutputStream out_obj;
		public Server_Thread(Client_Info client_Info_Thread,
				Socket socket_Thread, InetAddress address_Thread,
				int port_Thread, ObjectInputStream in_obj,
				ObjectOutputStream out_obj)
		{
			this.client_Info_Thread = client_Info_Thread;
			this.socket_Thread = socket_Thread;
			this.address_Thread = address_Thread;
			this.port_Thread = port_Thread;
			this.in_obj = in_obj;
			this.out_obj = out_obj;
			this.client_Info_Thread.ip_Address=this.address_Thread;
			this.client_Info_Thread.ip_Port=this.port_Thread;
			show("�û�����½/ע�᡿�ɹ� ��ʼ�����ͻ�����Ϣ");
			show("Thread_ID : " + Long.toString(thread_ID) + " Thread_Name : " + thread_Name);
			show("Client IP : " + address_Thread + " Port : "+port_Thread);
			server_Threads.addElement(this);
			show("����������ά�����ͻ��߳����� : "+server_Threads.size());
			//��Verification�߳����� :	//infos_Online++;	//infos_Reg++;
			send_Broadcast_Client_Info_Online_Reg();//�㲥�����������û������б�
		}
		@Override
		public void run()
		{
			try
			{
				while(true)
				{
					Msg msg = (Msg) in_obj.readObject();
					show("���յ������ͻ�����Ϣ�� : " + msg.msg_Type);
					switch (msg.msg_Type)
					{
					case chat:
						show("["+msg.senderID+"]TO["+msg.recieverID+"] : "+msg.msg_Content);
						server_Msg_Handler_Chat(msg);
						break;
					case user_data_update:
						show("���յ������û����ϸ��¡�");
						server_Msg_Handler_User_Data_Update(msg);
						break;
					case chat_codec://���յ����б�����Ե��û���Ϣ
						show("["+msg.senderID+"]TO["+msg.recieverID+"] : "+msg.msg_Content);
						server_Msg_Handler_Chat_Codec(msg);
						break;
					}
				}
			} catch (SocketException socketException)//�ͻ���˽�ԶϿ�ʱ���쳣
			{				
				socketException.printStackTrace();
				show("�ͻ���˽�ԶϿ�ʱ���쳣 "+"SocketException");//��Ҫ�ر���Ӧ�߳�//�Ƴ��������û�����
				close_Socket_Inside_Thread();
				remove_Thread_Inside_Thread();
				remove_Info_Online_Inside_Thread();
				
				network_Server_UI.update_List_Online(infos_Online);
				network_Server_UI.update_List_Reg(infos_Reg);
				
				send_Broadcast_Client_Info_Online_Reg();
				show_Num_of_server_Threads_clientInfos_online();
			} catch(NullPointerException nullPointerException)
			{				
				nullPointerException.printStackTrace();
				show("�����������߳��쳣 "+"NullPointerException");
			} catch (Exception e)
			{
				e.printStackTrace();
				show("Server_Thread �쳣");
			}
			
		}

		//���ڵ��������� �߳����� ���Բ���Ҫָ��IP��ַ ֱ��ʹ���߳��е������
		public boolean send_Unicast(Msg msg)//���ڵ��������� �߳����� ���Բ���Ҫָ��IP��ַ ֱ��ʹ���߳��е������
		{
			try
			{
				out_obj.writeObject(msg);
				out_obj.flush();
				return true;
			} catch (Exception e)
			{
				e.printStackTrace();
				show("Server_Thread send_Unicast �쳣");
				return false;
			}
		}

		synchronized public void close_Socket_Inside_Thread()
		{
			try
			{
				out_obj.close();
				in_obj.close();
				socket_Thread.close();
			} catch (Exception e)
			{
				e.printStackTrace();
				show("close_Socket_Inside_Thread �쳣");
			}
		}
		
		synchronized public void remove_Thread_Inside_Thread()
		{
			try
			{
				server_Threads.removeElement(this);
			} catch (Exception e)
			{
				e.printStackTrace();
				show("remove_Thread_Inside_Thread �쳣");
			}
		}
		
		synchronized public void remove_Info_Online_Inside_Thread()
		{
			try
			{
				for(int i=0;i<infos_Online.size();i++)
				{
					if(infos_Online.elementAt(i).ID.equals(client_Info_Thread.ID))
					{
						infos_Online.removeElement(infos_Online.elementAt(i));
						return;//���ӵĹؼ�
					}
				}
			} catch (Exception e)
			{
				e.printStackTrace();
				show("remove_Info_Online_Inside_Thread �쳣");
			}
		}
		
		synchronized public void KickOut_User_Inside_Thread()
		{
			Msg msg_Kickout=new Msg(Msg_Type.kickedout);
			send_Unicast(msg_Kickout);
			//�߳�����߳�Socket�����쳣 //���쳣���� ͨ���߳��д���
			close_Socket_Inside_Thread();
			remove_Thread_Inside_Thread();
			remove_Info_Online_Inside_Thread();
			network_Server_UI.update_List_Online(infos_Online);
			network_Server_UI.update_List_Reg(infos_Reg);
		}
		
		synchronized public boolean server_Msg_Handler_Chat(Msg msg)
		{
			try
			{
				Server_Thread receiver_Thread=get_Server_Thread_by_Client_ID(msg.recieverID);
				if(receiver_Thread==null)
				{
					show("��Chat���������ߡ� �������ߡ�");
					return false;
				}
				//����ĳһ���߳̽��յ���Ϣ����recieverID����������һ���߳�
				receiver_Thread.send_Unicast(msg);
				//����ĳһ���߳̽��յ���Ϣ����recieverID����������һ���߳�				
				return true;
			} catch (Exception e)
			{
				e.printStackTrace();
				show("server_Msg_Handler_Chat �쳣");
				return false;
			}
		}
		
		synchronized public boolean server_Msg_Handler_Chat_Codec(Msg msg)
		{
			try
			{
				//(0)�����ڷ������˸�����Կ����Ϣ�������
				//(0)(1) ����·�������Ӧ��ά���� KEY �Ƿ����
				String key_For_Receive=get_Key_Codec_from_Reg_by_ID(msg.senderID);
				if(key_For_Receive.equals(""))
				{
					show_UI_Dialog("��������ʧ�˷����� : ["+msg.senderID+"]����Կ");	
					return false;
				}
				//(0)(2) ������ KEY ������Ľ������
				String message_decoded=codec.Decode(key_For_Receive, msg.msg_Content);
				show("������ : ["+msg.senderID+"]����["+msg.recieverID+"]������ ����Ϊ:");
				show(message_decoded);
				//(1)�ȼ����Ϣ�������Ƿ�����
				Server_Thread receiver_Thread=get_Server_Thread_by_Client_ID(msg.recieverID);
				if(receiver_Thread==null)
				{
					show("��Chat���������ߡ� �������ߡ�");
					return false;
				}
				//(2)�ټ������û��Ƿ��Ѿ����ñ�����Կ
				// A.û����Կ���á����ġ�ת�� B.����Կ������Կ�����ܡ���ת��
				//String.equals() ��������� null ���׳��쳣 ��ע�⡿
				//������������÷������˶���Java���ֵġ�������ɱ������
				String key_For_Send=get_Key_Codec_from_Reg_by_ID(msg.recieverID);
				if(key_For_Send.equals(""))
				{
					//����ת��
					Msg msg_codec_off=new Msg(Msg_Type.chat);
					msg_codec_off.senderID=msg.senderID;
					msg_codec_off.recieverID=msg.recieverID;
					msg_codec_off.msg_Content=message_decoded;
					
					show("�������� :");
					show(message_decoded);
					receiver_Thread.send_Unicast(msg_codec_off);
				}
				else
				{
					//����ת��
					//�ȼ���
					String message_coded=codec.Code(key_For_Send, message_decoded);
					show("���ܺ����� :");
					show(message_coded);
					//��ת��
					Msg msg_codec_on=new Msg(Msg_Type.chat_codec);
					msg_codec_on.senderID=msg.senderID;
					msg_codec_on.recieverID=msg.recieverID;
					msg_codec_on.msg_Content=message_coded;
					
					show("�������� :");
					show(message_coded);					
					receiver_Thread.send_Unicast(msg_codec_on);					
				}
				return true;
			} catch (Exception e)
			{
				e.printStackTrace();
				show("server_Msg_Handler_Chat_Codec �쳣");
				return false;
			}
		}
		
		synchronized public boolean server_Msg_Handler_User_Data_Update(Msg msg)
		{
			try
			{
				String id_Change=msg.msg_Update_Info.user_ID;
				for(int i=0;i<infos_Reg.size();i++)
				{
					if(infos_Reg.elementAt(i).ID.equals(id_Change))
					{
						infos_Reg.elementAt(i).Password=msg.msg_Update_Info.user_Password;
						infos_Reg.elementAt(i).Name=msg.msg_Update_Info.user_Name;
						infos_Reg.elementAt(i).Key=msg.msg_Update_Info.user_Key;
						//���� ��sync����Reg����Ϣ����������Online
						//���� ��replace����Reg����Ϣ���滻����Online
						replace_ClientInfo_fromReg_toOnline_byID(id_Change);
						
						network_Server_UI.update_List_Online(infos_Online);
						network_Server_UI.update_List_Reg(infos_Reg);
						
						send_Broadcast_Client_Info_Online_Reg();
						show("���㲥���������û����ϸ��¡�");
					}
				}
				network_Server_UI.tabbedPane_Info.updateUI();//�ô˷�������ˢ���б� ���ӳ�
				return true;
			} catch (Exception e)
			{
				show("server_Msg_Handler_User_Data_Update �쳣");
				return false;
			}
		}	
	}
	
	public static void main(String[] args)
	{
		new Network_Server();
	}
	
	public void show(String string)
	{
		string=get_Time()+string;
		System.out.println(string);
		network_Server_UI.show(string);
	}
	
	public void show_(String string)//���������
	{
		System.out.print(string);
		network_Server_UI.show_(string);
	}
	
	public void show_UI_Dialog(String string)//�ԶԻ���ʽ��ʾ��ʾ��Ϣ
	{
		string=get_Time()+string;
		System.out.println(string);
		new Inf_UI(string);
	}
	
	public void show_Num_of_server_Threads_clientInfos_online()
	{
		int num1=server_Threads.size();
		int num2=infos_Online.size();
		
		network_Server_UI.textField_broadcast.setText("[��������] �û��߳��� :"+Integer.toString(num1)
				+"[�����û���] : "+Integer.toString(num2));
	}
	
	public String get_Time()
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		String str_time = simpleDateFormat.format(new Date());
		return "["+str_time+"]";
	}
	
	public void generate_Str_Client_Info_Online()
	{
		str_Client_Info_Online="";
		for(int i=0;i<infos_Online.size();i++)
		{
			str_Client_Info_Online=str_Client_Info_Online+infos_Online.elementAt(i).ID+":"
					+infos_Online.elementAt(i).Name+";";
		}
	}
	
	public void generate_Str_Client_Info_Reg()
	{
		str_Client_Info_Reg="";
		for(int i=0;i<infos_Reg.size();i++)
		{
			str_Client_Info_Reg=str_Client_Info_Reg+infos_Reg.elementAt(i).ID+":"
					+infos_Reg.elementAt(i).Name+";";
		}
	}
	
	public void show_Msg(String title,String msg)
	{
		JOptionPane optionPane=new JOptionPane();
		optionPane.showConfirmDialog(null, msg, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
	}

}
