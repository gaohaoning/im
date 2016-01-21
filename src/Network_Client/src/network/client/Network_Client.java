package network.client;
import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import javax.swing.*;
import network.codec.*;
import network.util.*;
import network.ui.*;

public class Network_Client
{
	public Codec codec;
	public Socket socket;
	public ObjectInputStream in_obj;
	public ObjectOutputStream out_obj;
	public Login_UI login_UI;
	public Network_Client_UI network_Client_UI;
	public Update_UI update_UI;
	
	public Login_Info login_Info;//�������û���½��Ϣ��
	public String key_Codec_Local;//�������û�������Ϣ��//ֻ�е������û��޸������ϲ�ͬ�����������˲Ÿ�����ֵ
	
	public Client_Info client_Info_Local;//�������û�����Ϣ
	public Vector<Client_Info> infos_Online;// ���С������û������Զ�̬�޸�
	public Vector<Client_Info> infos_Reg;// ���С�ע���û�����ʼʱ���ļ���ȡ
	public String str_Client_Info_Online;//��String��ʽά���û�ID��Name��Ϣ
	public String str_Client_Info_Reg;//��String��ʽά���û�ID��Name��Ϣ
	
	public Client_Thread client_Thread;//�������ͨ�ŵ��߳�
	public Network_Client()
	{
		codec=new Codec(this);
		client_Info_Local = new Client_Info();
		infos_Online = new Vector<Client_Info>();
		infos_Reg = new Vector<Client_Info>();
		
		login_UI=new Login_UI(this);//���� GUI
		network_Client_UI=new Network_Client_UI(this);//���� GUI
//		network_Client_UI.frame.setVisible(false);
		update_UI=new Update_UI(this);//���� GUI
//		update_UI.frame.setVisible(false);
	}
	public boolean init_Socket()
	{
		try
		{
			InetAddress server_ip=InetAddress.getByName(login_Info.address_server_str);
			int server_port=Integer.parseInt(login_Info.port_server);
			show_Login("�����ӷ�������"+"��ַ: "+server_ip+" �˿�: "+server_port+"...");
			
			socket=new Socket(server_ip, server_port);
			show_Login("�����ӷ������� OK");
			
			out_obj=new ObjectOutputStream(socket.getOutputStream());
			in_obj=new ObjectInputStream(socket.getInputStream());
			show_Login("������������� OK");
		} catch (Exception e)
		{
			// TODO: handle exception
			show_Login("����ʼ�����ӡ� �쳣");
			return false;
		}
		return true;
	}
	public boolean init_Socket(int port_Local)
	{
		try
		{
			InetAddress server_ip=InetAddress.getByName(login_Info.address_server_str);
			int server_port=Integer.parseInt(login_Info.port_server);
			show_Login("�����ӷ�������"+"��ַ: "+server_ip+" �˿�: "+server_port+"...");
			
			//socket=new Socket(server_ip, server_port);
			socket=new Socket(server_ip, server_port,InetAddress.getLocalHost() , port_Local);//ָ�����ض˿�
			show_Login("�����ӷ������� OK");
			
			out_obj=new ObjectOutputStream(socket.getOutputStream());
			in_obj=new ObjectInputStream(socket.getInputStream());
			show_Login("������������� OK");
		} catch (Exception e)
		{
			// TODO: handle exception
			show_Login("����ʼ�����ӡ� �쳣");
			return false;
		}
		return true;
	}
	public void close_Socket(Thread thread)
	{
		try
		{
			in_obj.close();
			out_obj.close();
			socket.close();
			show("����������Ͽ����ӡ�");
			
			if(thread.isAlive())
			{
				show("���ر� �����̡߳� : "+thread.getId()+" "+thread.getName());
				thread.interrupt();
				show("���ر� �����̡߳� : OK");				
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			show("close_Socket �쳣");
		}
	}
	public void on_Btn_Login(String address_server_str, String port_server,String id_login, String password_login)
	{
		login_Info=new Login_Info(address_server_str, port_server, id_login, password_login);
		if(!init_Socket())//���ӷ����� ���������
		{
			show_Login("�޷����ӵ�������������[��ַ]��[�˿�]");
			return;
		}
		
		client_Thread=new Client_Thread();
		client_Thread.start();
		
		send_Msg_Login();		
	}
	public void on_Btn_Register(String address_server_str, String port_server,String id_login, String password_login)
	{
		login_Info=new Login_Info(address_server_str, port_server, id_login, password_login);		
		if(!init_Socket())//���ӷ����� ���������
		{
			show_Login("�޷����ӵ�������������[��ַ]��[�˿�]");
			return;
		}
		
		client_Thread=new Client_Thread();
		client_Thread.start();
		
		send_Msg_Register();
	}
	
	public void on_Btn_Login(int port_Local,String address_server_str, String port_server,String id_login, String password_login)
	{
		login_Info=new Login_Info(address_server_str, port_server, id_login, password_login);
		if(!init_Socket(port_Local))//���ӷ����� ���������
		{
			show_Login("�޷����ӵ�������������[��ַ]��[�˿�]");
			return;
		}
		
		client_Thread=new Client_Thread();
		client_Thread.start();
		
		send_Msg_Login();		
	}
	public void on_Btn_Register(int port_Local,String address_server_str, String port_server,String id_login, String password_login)
	{
		login_Info=new Login_Info(address_server_str, port_server, id_login, password_login);		
		if(!init_Socket(port_Local))//���ӷ����� ���������
		{
			show_Login("�޷����ӵ�������������[��ַ]��[�˿�]");
			return;
		}
		
		client_Thread=new Client_Thread();
		client_Thread.start();
		
		send_Msg_Register();
	}
	public void on_Btn_Edit()
	{
		update_UI.frame.setVisible(true);
		update_UI.textField_id.setText(client_Info_Local.ID);
	}
	
	public void on_Button_Send_Update(Update_Info update_Info)
	{
		Msg msg_Update=new Msg(Msg_Type.user_data_update);
		msg_Update.msg_Update_Info=update_Info;
		send_Msg(msg_Update);
	}
	public boolean send_Msg(Msg msg)
	{
		try
		{
			out_obj.writeObject(msg);
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			show("send_Msg �쳣");
			return false;
		}
	}
	public void send_Msg_Login()
	{
		try
		{
			Msg msg=new Msg(Msg_Type.login);
			msg.msg_Client_Info=new Client_Info();			
			msg.msg_Content="This is a Login Request";
			msg.msg_Client_Info.ID=login_Info.id_login;
			msg.msg_Client_Info.Password=login_Info.password_login;			
			login_UI.show("Sending Msg : "+msg);
			out_obj.writeObject(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
			login_UI.show("send_Msg_Login �쳣");
		}
	}
	public void send_Msg_Register()
	{
		try
		{
			Msg msg=new Msg(Msg_Type.register);
			msg.msg_Client_Info=new Client_Info();			
			msg.msg_Content="This is a Register Request";
			msg.msg_Client_Info.ID=login_Info.id_login;
			msg.msg_Client_Info.Password=login_Info.password_login;			
			login_UI.show("Sending Msg : "+msg);
			out_obj.writeObject(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
			login_UI.show("send_Msg_Register �쳣");
		}
	}
	public class Client_Thread extends Thread
	{
		private long thread_ID = this.getId();
		private String thread_Name = this.getName();
		Msg msg_R;
		@Override
		public void run()
		{
			show_Login("����ʼ���� Server ��Ϣ...��");
			show_Login("���̡߳�ID : "+Long.toString(thread_ID)+" Name : "+thread_Name);
			try
			{
				while(true)
				{
					msg_R = (Msg) in_obj.readObject();					
					show("�յ�����������Ϣ�������͡�: " + msg_R.msg_Type);
					
					switch (msg_R.msg_Type)
					{
					case login_echo_accept:
						show("��¼�ɹ� Login Success");
						login_UI.frame.setVisible(false);//���ɼ� login_UI
						network_Client_UI.frame.setVisible(true);
						network_Client_UI.frame.setTitle("Client : "+login_Info.id_login);//���������Ա�ʶ��ͬ�ͻ���
						break;
					case login_echo_reject:
						show("��¼ʧ�� Login Failed");
						close_Socket(this);
						break;
					case register_echo_accept:
						show("ע��ɹ� Register Success");
						login_UI.frame.setVisible(false);//���ɼ� login_UI
						network_Client_UI.frame.setVisible(true);
						network_Client_UI.frame.setTitle("Client : "+login_Info.id_login);
						break;
					case register_echo_reject:
						show("ע��ʧ�� Register Failed");
						close_Socket(this);
						break;
					case users_update:
						show("�յ������ߡ��͡�ע�᡿�û�������Ϣ $$$$$$$$$$$$$$$$$$$$ : ");	
						show(msg_R.online_String);
						show(msg_R.reg_String);
						
						update_User_List_Online_by_String(msg_R.online_String);
						update_User_List_Reg_by_String(msg_R.reg_String);
						
						str_Client_Info_Online=msg_R.online_String;
						str_Client_Info_Reg=msg_R.reg_String;
						break;
						case kickedout:
						network_Client_UI.frame.setVisible(false);
						close_Socket(this);
						//show_UI_Dialog("���㱻�߳��� \n"+" ԭ��: \n 1.��ͬID�û���ص�½ \n 2.����������");
						show_Msg("����", "���㱻�߳��� \n"+" ԭ��: \n 1.��ͬID�û���ص�½ \n 2.����������");
						break;
					case broadcast:
						show("�յ����������㲥��"+msg_R.toString());
						show(msg_R.msg_Content);
						show_Chat("���������㲥��");
						show_Chat(msg_R.msg_Content);
						break;
					case chat:
						String str_chat="["+msg_R.senderID+":"+msg_R.senderName+"]��["
								+msg_R.recieverID+":"+msg_R.recieverName+"]˵ : "+msg_R.msg_Content;
						show(str_chat);
						show_Chat(str_chat);
						break;
					case chat_codec:
						show_Chat_CODEC_Msg_on_textArea(msg_R);//������������벢��ʾ
						break;
					case user_data_update:
						show("�յ�����������ע���û���Ϣ���¡�");
						show(msg_R.msg_Update_Info.user_ID);
						show(msg_R.msg_Update_Info.user_Password);
						show(msg_R.msg_Update_Info.user_Name);
						show(msg_R.msg_Update_Info.user_Key);						
						set_Server_Echo_Detail_To_Local_And_Update_UI(msg_R);
						break;
					default:
						show("��δ֪��Ϣ���͡�");
					}
					
					//terminate_Thread_and_Restart_Thread();
				}
			} catch (Exception e)
			{
				e.printStackTrace();
				show("�ͻ��˼����߳��쳣 : Run Exception");
			}
			
		}
	}
	public void show_Chat_CODEC_Msg_on_textArea(Msg msg)//������������벢��ʾ
	{
		//����ʾ�յ�����
		show("�յ� : ["+msg.senderID+"] ���� ["+msg.recieverID+"] ������ :");
		//show_Chat("�յ� : ["+msg.senderID+"] ���� ["+msg.recieverID+"] ������ :");
		show(msg.msg_Content);
		//show_Chat(msg.msg_Content);
		//���ݱ����û�ά������Կ��Ϣ KEY ���� ���ܲ���ʾ
		String decoded=codec.Decode(key_Codec_Local, msg.msg_Content);
		
		String str_chat="["+msg.senderID+":"+msg.senderName+"]��["
				+msg.recieverID+":"+msg.recieverName+"]˵ : "+decoded+"[�ѽ���]";
		
		show(str_chat);
		show_Chat(str_chat);
	}
	public void set_Server_Echo_Detail_To_Local_And_Update_UI(Msg msg_R)
	{
		client_Info_Local.ID=msg_R.msg_Update_Info.user_ID;
		client_Info_Local.Password=msg_R.msg_Update_Info.user_Password;
		client_Info_Local.Name=msg_R.msg_Update_Info.user_Name;
		client_Info_Local.Key=msg_R.msg_Update_Info.user_Key;
		
		key_Codec_Local=msg_R.msg_Update_Info.user_Key;
		
		update_UI.textField_id.setText(client_Info_Local.ID);
		update_UI.textField_password.setText(client_Info_Local.Password);
		update_UI.textField_name.setText(client_Info_Local.Name);
		update_UI.textField_key.setText(client_Info_Local.Key);
		
		network_Client_UI.frame.setTitle(client_Info_Local.ID+":"+client_Info_Local.Name);
	}
	public static void main(String[] args)
	{
		new Network_Client();
	}
	public void show(String string)
	{
		string=get_Time()+string;
		System.out.println(string);
		network_Client_UI.show(string);
		login_UI.show(string);
	}
	public void show_(String string)//���������
	{
		System.out.print(string);
		network_Client_UI.show_(string);
	}
	public void show_Login(String string)
	{
		string=get_Time()+string;
		System.out.println(string);
		login_UI.show(string);
	}
	public void show_UI_Dialog(String string)//�ԶԻ���ʽ��ʾ��ʾ��Ϣ
	{
		string=get_Time()+string;
		System.out.println(string);
		new Inf_UI(string);
	}
	public void show_Chat(String string)
	{
		string=get_Time()+string;
		network_Client_UI.show_Chat(string);
	}
	public String get_Time()
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		String str_time = simpleDateFormat.format(new Date());
		return "["+str_time+"]";
	}
	public void update_User_List_Online_by_String(String string)
	{	
		try
		{	
			String string_temp[]=string.split(";");	
			network_Client_UI.defaultListModel_online.clear();
			for(int i=0;i<string_temp.length;i++)
			{
				network_Client_UI.defaultListModel_online.addElement(string_temp[i]);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			show("update_User_List_Online_by_String ������ Online���쳣");
		}
	}
	
	public void update_User_List_Reg_by_String(String string)
	{	
		try
		{	
			String string_temp[]=string.split(";");	
			network_Client_UI.defaultListModel_reg.clear();
			for(int i=0;i<string_temp.length;i++)
			{
				network_Client_UI.defaultListModel_reg.addElement(string_temp[i]);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			show("update_User_List_Reg_by_String ������ Reg���쳣");
		}
	}
	public String get_Client_ID_Name_from_Str_Online_By_Index(int index)
	{
		String string_temp[]=str_Client_Info_Online.split(";");
		return string_temp[index];
	}
	public String get_Client_ID_Name_from_Str_Reg_By_Index(int index)
	{
		String string_temp[]=str_Client_Info_Reg.split(";");
		return string_temp[index];
	}
	public void show_Msg(String title,String msg)
	{
		JOptionPane optionPane=new JOptionPane();
		optionPane.showConfirmDialog(null, msg, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
	}
}

