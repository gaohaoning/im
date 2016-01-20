package network.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.DefaultListModel;

public class Msg implements Serializable
{
	public Msg_Type msg_Type;

	public String msg_Content;
	public String msg_Content_ex;
	
	public Object [] online;
	public Object [] reg;
	
	public String online_String;
	public String reg_String;
	
	public boolean is_Codec;//标志发来的信息是否是加密的
	
	public String key_Codec;
	
	public int count;
	public int count_ex;
	
	public String senderID;
	public String recieverID;
	
	public String senderName;
	public String recieverName;
	
	public Client_Info msg_Client_Info;
	public Client_Info msg_Client_Info_ex;
	
	public Update_Info msg_Update_Info;
	
	public Msg()
	{
		super();
	}
	
	public Msg(Msg_Type msg_Type)
	{
		super();
		this.msg_Type = msg_Type;
	}
}
