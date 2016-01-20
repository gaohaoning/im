package network.util;

import java.io.Serializable;
import java.net.InetAddress;

public class Client_Info implements Serializable
{
	//������Ϣ
	//======================================================
	public String ID;
	public String Name;
	public String Password;
	//======================================================
	//��ϸ��Ϣ
	//======================================================
	public String Info_Detail;
	//======================================================
	//�������
	//======================================================
	public String Key;
	//======================================================
	//������Ϣ
	//======================================================
	public InetAddress ip_Address;
	public int ip_Port;
	//======================================================
	
	public Client_Info()
	{
		super();
	}
	
	public Client_Info(String clientID, String clientName, String clientPassword)
	{
		super();
		this.ID = clientID;
		this.Name = clientName;
		this.Password = clientPassword;
	}
}
