package network.codec;

import java.io.Serializable;
import java.lang.String;

import network.client.Network_Client;

public class Codec implements Serializable
{
	char morsetable[]={'1','1','1','1','1','0','1','1','1','1','0','0','1','1','1','0','0','0','1','1','0','0','0','0','1','0','0','0','0','0','1','0','0','0','0','1','1','0','0','0','1','1','1','0','0','1','1','1','1','0'};
	int teltable[]={2,1,2,2,2,3,3,1,3,2,3,3,4,1,4,2,4,3,5,1,5,2,5,3,6,1,6,2,6,3,7,1,7,2,7,3,7,4,8,1,8,2,8,3,9,1,9,2,9,3,9,4};
	char QWE_coding_table[]={'q','w','e','r','t','y','u','i','o','p','a','s','d','f','g','h','j','k','l','z','x','c','v','b','n','m'};
	char QWE_decoded_table[]={'k','x','v','m','c','n','o','p','h','q','r','s','z','y','i','j','a','d','l','e','g','w','b','u','f','t'};

	public Network_Client network_Client;
	char codepolicy[]=new char[100];
	char messages[]=new char[1000];
	
	public String Code(String codepolicy_string,String messages_string)
	{		
		codepolicy=codepolicy_string.toCharArray();
		messages=messages_string.toCharArray();
		
		String codedmessages_str=coding(codepolicy, messages, messages.length);
		
		return codedmessages_str;
	}
	
	public String Decode(String codepolicy_string,String messages_string)
	{	
		codepolicy=codepolicy_string.toCharArray();
		messages=messages_string.toCharArray();
				
		String decodedmessages_str=decoding(codepolicy,messages,messages.length);
		
		return decodedmessages_str;
	}
	
	public Codec()
	{
		String messages_string=new String("ILOVEYOUTOO");
		String key_policy=new String("edcba");
		
		if(!check_codepolicy(key_policy))
		{
			System.out.println("【编码策略不正确】");
		}
		else
		{
			String msg_coded=Code(key_policy, messages_string);
			if(!msg_coded.equals(""))
			{
				System.out.println("【编码成功】");
				System.out.println("%%%%【编码结果】%%%% : "+msg_coded);
				
				String msg_decoded=Decode(key_policy, msg_coded);
				System.out.println("%%%%【译码结果】%%%% : "+msg_decoded);
			}else
			{
				System.out.println("【编码失败】");
			}
		}
	}

	public Codec(Network_Client network_Client)
	{
		this.network_Client = network_Client;
	}

	int i,j=0,messages_length,lengthzong,codeok=0,messageok=0;
	char messages_character[]=new char[1000],temp_decoded_character[]=new char[1000];
	int codedmessages[]=new int[1000];

	public boolean check_codepolicy(String policy_str)
	{
		char [] policy_char=new char[policy_str.length()];
		policy_char=policy_str.toCharArray();
		try
		{
			int lengthzong = policy_char.length;
			int length = lengthzong;
			if (length > 15)
			{
				show_Codec_Info("编码策略过大，请重新输入");
				
				codeok = 0;
				return false;
			} else if (policy_char[length - 2] != 'b'
					|| policy_char[length - 1] != 'a')
			{
				show_Codec_Info("编码策略最后两位必须是ba，请重新输入");
				
				codeok = 0;
				return false;
			} else if (length == 2)
				codeok = 1;
			else
			{
				for (i = 0; i < lengthzong - 2; i++)
				{
					if (policy_char[i] > 'e' || policy_char[i] < 'c')
					{
						if (policy_char[i] > '1' && policy_char[i] <= '9'
								&& policy_char[i - 1] == 'd')
						{
							length--;
							codeok = 1;
						} else
						{
							codeok = 0;
							show_Codec_Info("编码策略违反规则，请重新输入");
							
							return false;
						}
					} else
						codeok = 1;
				}
				if (length > 9)
				{
					show_Codec_Info("编码策略过大，请重新输入");
					
					codeok = 0;
					return false;
				}
			}
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	String coding(char codepolicy[],char messages_character[],int messages_length)
	{
		int i,j=0,temp,num_column=2,column,num_row,lengthzong;
		char temp_messages_character[]=new char[1000];
		char temp_n[]=new char[1000];
		int temp_messages_number[]=new int[1000];
		char temp_c[]=new char[1000];
		char codedmessages[]=new char[1000];
		int check_messages_length = messages_character.length;
		if(check_messages_length>=100)
		{
			check_messages_length=99;
		}
		char [] messages_char=new char[check_messages_length];
		for (i = 0; i < check_messages_length; i++)
		{
			if (messages_character[i] > 'z' || messages_character[i] < 'A')
			{
				show_Codec_Info(messages_character[i] + ",");
				
				continue;
			} else if (messages_character[i] < 'a' && messages_character[i] > 'Z')
			{
				show_Codec_Info(messages_character[i] + ",");
				
				continue;
			} else if (messages_character[i] <= 'Z' && messages_character[i] >= 'A')
			{
				
				messages_character[i] = convert_A_To_a(messages_character[i]);
				messages_char[j] = messages_character[i];
				j++;
			} else
			{
				messages_char[j] = messages_character[i];
				j++;
			}
		}
		messages_length = j;
		if (i != j)
		{
			show_Codec_Info("为无效字符，忽略");
			
		}
		if (j == 0)
		{
			show_Codec_Info("无有效信息输入，请重新输入");
			
			return new String("");
		} else
		{
			show_Codec_Info("输入信息为：");
			
			for (i = 0; i < j; i++)
				System.out.print(messages_char[i]);
			show_Codec_Info(String.valueOf(messages_char));
			
			messageok = 1;
		}
		temp_messages_character=messages_char;
		lengthzong=codepolicy.length;
		for (j=0;j<lengthzong;j++)
		{
			if (codepolicy[j]=='a')   
			{
				for (i=0;i<messages_length*2;i++)
				{
					temp=temp_messages_number[i];
					temp_n[5*i]=morsetable[5*temp];
					temp_n[5*i+1]=morsetable[5*temp+1];	
					temp_n[5*i+2]=morsetable[5*temp+2];
					temp_n[5*i+3]=morsetable[5*temp+3];
					temp_n[5*i+4]=morsetable[5*temp+4];
				}
				for (i=0;i<messages_length*10;i++)
				{
					codedmessages[i]=temp_n[i];
				}
				show_Codec_Info("摩尔斯编码之后：");
				for (i=0;i<messages_length*10;i++)
				{
					show_Codec_Info(String.valueOf(codedmessages[i]), 0);
				}
				show_Codec_Info("");
			}
			else if (codepolicy[j]=='b')  
			{
				for (i=0;i<messages_length;i++)
				{
					temp=temp_messages_character[i]-'a';
					temp_messages_number[2*i]=teltable[2*temp];
					temp_messages_number[2*i+1]=teltable[2*temp+1];	
				}
				show_Codec_Info("手机编码之后：");
				for (i=0;i<messages_length*2;i++)
				{
					show_Codec_Info(String.valueOf(temp_messages_number[i]), 0);
				}				
				show_Codec_Info("");
			}
			else if (codepolicy[j]=='c')  
			{
				for (i=0;i<messages_length;i++)
				{
					temp=temp_messages_character[i]-'a';
					temp_messages_character[i]=QWE_coding_table[temp];
				}
				show_Codec_Info("QWE编码之后：");
				for (i=0;i<messages_length;i++)
				{
					show_Codec_Info(String.valueOf(temp_messages_character[i]), 0);
				}
				show_Codec_Info("");
			}
			else if (codepolicy[j]=='d')    
			{
				if (codepolicy[j+1]>'2'&&codepolicy[j+1]<='9')
					num_column=codepolicy[j+1]-'0';
				else 
					num_column=2;
				num_row=messages_length/num_column;
				for (column=0;column<messages_length%num_column;column++)
				{
					for (i=0;i<(num_row+1);i++)
						temp_c[i+(num_row+1)*column]=temp_messages_character[i*num_column+column];
				}
				for (column=messages_length%num_column;column<num_column;column++)
				{
					for (i=0;i<num_row;i++)
						temp_c[i+num_row*column+messages_length%num_column]=temp_messages_character[i*num_column+column];
				}
				for (i=0;i<messages_length;i++)
				{
					temp_messages_character[i]=temp_c[i];
				}
				show_Codec_Info(Integer.toString(num_column), 0);
				show_Codec_Info("栏栅栏编码之后：");
				for (i=0;i<messages_length;i++)
				{
					show_Codec_Info(String.valueOf(temp_messages_character[i]), 0);
				}
				show_Codec_Info("");
			}
			else if (codepolicy[j]=='e')             
			{

				for (i=0;i<messages_length;i++)
					temp_c[messages_length-i-1]=temp_messages_character[i];
				for (i=0;i<messages_length;i++)	
					temp_messages_character[i] = temp_c[i];
				show_Codec_Info("倒序编码之后：");
				for (i=0;i<messages_length;i++)
				{
					show_Codec_Info(String.valueOf(temp_messages_character[i]), 0);
				}
				show_Codec_Info("");
			}
		}
		char result_code[]=new char[messages_length*10];
		for(int re=0;re<messages_length*10;re++)
		{
			result_code[re]=codedmessages[re];
		}		
		return String.valueOf(result_code);
	}
	
	String decoding(char codepolicy[],char codedmessages[],int messages_length)
	{
		int i,j=0,temp,num_column=2,column,num_row,lengthzong;
		char temp_decoded_character[]=new char[1000];
		int	temp_decoded_number[]=new int[1000];
		int temp_decoded_n[]=new int[1000];
		char temp_decoded_c[]=new char[1000];
		char temp_decoded_cc[]=new char[1000];
		char temp_decoded_ca[]=new char[1000];
		int codedmessages_int[]=new int[1000];
		
		int mes_length=messages_length/10;
		for(j=0;j<messages_length;j++)
		{
			codedmessages_int[j]=codedmessages[j]-'0';
		}
		
		lengthzong=codepolicy.length;
		for (j=lengthzong-1;j>-1;j--)
		{
			if (codepolicy[j]=='a')   
			{
				for (i=0;i<mes_length*2;i++)
				{
					temp_decoded_n[i]=codedmessages_int[5*i]*16+codedmessages_int[5*i+1]*8+codedmessages_int[5*i+2]*4+codedmessages_int[5*i+3]*2+codedmessages_int[5*i+4];
					switch (temp_decoded_n[i])
					{
					case 31: temp_decoded_number[i]=0;break;
					case 15: temp_decoded_number[i]=1;break;
					case 7: temp_decoded_number[i]=2;break;
					case 3: temp_decoded_number[i]=3;break;
					case 1: temp_decoded_number[i]=4;break;
					case 0: temp_decoded_number[i]=5;break;
					case 16: temp_decoded_number[i]=6;break;
					case 24: temp_decoded_number[i]=7;break;
					case 28: temp_decoded_number[i]=8;break;
					case 30: temp_decoded_number[i]=9;break;
					}
				}
				show_Codec_Info("摩尔斯译码之后：");
				for (i=0;i<mes_length*2;i++)
				{
					show_Codec_Info(Integer.toString(temp_decoded_number[i]), 0);
					
				}
				show_Codec_Info("");
			}
			else if (codepolicy[j]=='b')  
			{
				for (i=0;i<mes_length;i++)
				{
					if (temp_decoded_number[2*i]==2)
					{
						switch(temp_decoded_number[2*i+1])
						{
						case 1: temp_decoded_character[i]='a';break;
						case 2: temp_decoded_character[i]='b';break;
						case 3: temp_decoded_character[i]='c';break;
						}
					}else if (temp_decoded_number[2*i]==3)
					{
						switch(temp_decoded_number[2*i+1])
						{
						case 1: temp_decoded_character[i]='d';break;
						case 2: temp_decoded_character[i]='e';break;
						case 3: temp_decoded_character[i]='f';break;
						}
					}else if (temp_decoded_number[2*i]==4)
					{
						switch(temp_decoded_number[2*i+1])
						{
						case 1: temp_decoded_character[i]='g';break;
						case 2: temp_decoded_character[i]='h';break;
						case 3: temp_decoded_character[i]='i';break;
						}
					}else if (temp_decoded_number[2*i]==5)
					{
						switch(temp_decoded_number[2*i+1])
						{
						case 1: temp_decoded_character[i]='j';break;
						case 2: temp_decoded_character[i]='k';break;
						case 3: temp_decoded_character[i]='l';break;
						}
					}else if (temp_decoded_number[2*i]==6)
					{
						switch(temp_decoded_number[2*i+1])
						{
						case 1: temp_decoded_character[i]='m';break;
						case 2: temp_decoded_character[i]='n';break;
						case 3: temp_decoded_character[i]='o';break;
						}
					}else if (temp_decoded_number[2*i]==7)
					{
						switch(temp_decoded_number[2*i+1])
						{
						case 1: temp_decoded_character[i]='p';break;
						case 2: temp_decoded_character[i]='q';break;
						case 3: temp_decoded_character[i]='r';break;
						case 4: temp_decoded_character[i]='s';break;
						}
					}else if (temp_decoded_number[2*i]==8)
					{
						switch(temp_decoded_number[2*i+1])
						{
						case 1: temp_decoded_character[i]='t';break;
						case 2: temp_decoded_character[i]='u';break;
						case 3: temp_decoded_character[i]='v';break;
						}
					}else if (temp_decoded_number[2*i]==9)
					{
						switch(temp_decoded_number[2*i+1])
						{
						case 1: temp_decoded_character[i]='w';break;
						case 2: temp_decoded_character[i]='x';break;
						case 3: temp_decoded_character[i]='y';break;
						case 4: temp_decoded_character[i]='z';break;
						}
					}
				}
				show_Codec_Info("手机译码之后：");
				for (i=0;i<mes_length;i++)
				{
					show_Codec_Info(String.valueOf(temp_decoded_character[i]),0);
					
				}
				show_Codec_Info("");
			}
			else if (codepolicy[j]=='c')  
			{
				for (i=0;i<mes_length;i++)
				{
					temp=temp_decoded_character[i]-'a';
					temp_decoded_c[i]=QWE_decoded_table[temp];
				}
				temp_decoded_character=temp_decoded_c;
				show_Codec_Info("QWE译码之后：");
				for (i=0;i<mes_length;i++)
				{					
					show_Codec_Info(String.valueOf(temp_decoded_character[i]), 0);
					
				}
				show_Codec_Info("");
			}
			else if (codepolicy[j]=='d')    
			{
				if (codepolicy[j+1]>'2'&&codepolicy[j+1]<='9')
					num_column=codepolicy[j+1]-'0';
				else 
					num_column=2;
				num_row=mes_length/num_column;	
				for (column=0;column<mes_length%num_column;column++)
				{
					for (i=0;i<(num_row+1);i++)
						temp_decoded_cc[i*num_column+column]=temp_decoded_character[i+(num_row+1)*column];
				}
				
				for (column=mes_length%num_column;column<num_column;column++)
				{
					for (i=0;i<num_row;i++)
						temp_decoded_cc[i*num_column+column]=temp_decoded_character[i+num_row*column+mes_length%num_column];
				}
				for (i=0;i<mes_length;i++)
				{
					temp_decoded_character[i]=temp_decoded_cc[i];
				}

				show_Codec_Info(Integer.toString(num_column)+"栏栅栏译码之后：");
				
				for (i=0;i<mes_length;i++)
				{
					show_Codec_Info(String.valueOf(temp_decoded_character[i]),0);
					
				}
				show_Codec_Info("");
			}
			else if (codepolicy[j]=='e')            
			{
				for (i=0;i<mes_length;i++)
					temp_decoded_ca[mes_length-i-1]=temp_decoded_character[i];
				for (i=0;i<mes_length;i++)	
					temp_decoded_character[i] = temp_decoded_ca[i];
				show_Codec_Info("倒序译码之后：");
				
				for (i=0;i<mes_length;i++)
				{
					show_Codec_Info(String.valueOf(temp_decoded_character[i]),0);
					
				}
				show_Codec_Info("");
			}
		}
		char result_decode[]=new char[mes_length];
		for(int re=0;re<mes_length;re++)
		{
			result_decode[re]=temp_decoded_character[re];
		}		
		return String.valueOf(result_decode);
	}
	
	public static void main(String[] args)
	{
		new Codec();
	}
	
	public void show_ASCII()
	{
		char A='A';
		char a='a';
		char Z='Z';
		char z='z';		
		System.out.println(A+a);
		System.out.println(A-a);
		System.out.println(Z+z);
		System.out.println(Z-z);
	}
	
	public char convert_A_To_a(char A)
	{
		return (char)((int)A+32);
	}
	
	public void show_Codec_Info(String string)
	{
			System.out.println(string);
			network_Client.show(string);
	}
	
	public void show_Codec_Info(String string,int mode)
	{
		System.out.print(string);
		network_Client.show_(string);
	}
}
