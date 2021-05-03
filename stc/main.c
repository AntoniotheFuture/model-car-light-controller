//****************************************************
//****************************************************
//���ƿ���������
//****************************************************
//****************************************************
//Author ��С��
#include "main.h"

//****************************************************
//������
//****************************************************
//�͵�ƽ��ʾͨ·
unsigned int twinkleOn = 0;//��˸��־��0.5s
unsigned int diOn = 0;//�����������1s
unsigned int lightMode = 0;//��������ģʽ��0=�رգ�1=ʾ���ƣ�2=ǰ�յ�
unsigned int fogMode = 0;//�������ģʽ��0=�رգ�1=ǰ��ƣ�2=ǰ�����
unsigned int bigLight = 0;//��ƿ�����־,0=�ر�,1=����
unsigned int turnLightOn = 0; //ת���־��0=�ر�,1=��ת��2=��ת
bit breakOn = 0;//ɲ��
bit backOn = 0;//����
bit dfOn = 0;//˫��
bit BuzzerOn = 0;//������״̬�����̳��赥�����ƣ�


//****************************************************
//MS��ʱ����(12M�����²���)
//****************************************************
void Delay_ms(unsigned int n)
{
	unsigned int  i,j;
	for(i=0;i<n;i++)
		for(j=0;j<110;j++);
}

					#define uchar unsigned char
/******************************************************************/
/* �����жϳ���*/
/******************************************************************/
void UART_SER () interrupt 4
{
	unsigned int n; 	//������ʱ����
	uchar receive_data;
	if(RI) 		//�ж��ǽ����жϲ���
	{
		RI=0; 	//��־λ����
		n=SBUF; //���뻺������ֵ
	    receive_data=SBUF;
		if(receive_data == '1'){
			//L_Ex5 = 0;
		}
		switch(n)
		{
			case 0:	lightMode = 0;	break;	//�رյƹ�
			case 1:	lightMode = 1;	break;	//ʾ����
			case 2: lightMode = 2;	break;	//ǰ�յ�
			case 3: fogMode = 0;	break;	//�ر����
			case 4: fogMode = 1;  break; //��ǰ���
			case 5: fogMode = 2; break; //��ǰ�����
			case 6: bigLight = 0; break; //�رմ��
			case 7: bigLight = 1; break; //�򿪴��
			case 8: breakOn = 0; break; //�ر�ɲ����
			case 9: breakOn = 1; break; //��ɲ����
			case 10: backOn = 0; break; //�رյ���
			case 11: backOn = 1; break; //�򿪵���
			case 12: turnLightOn = 0;break; //�ر�ת���
			case 13: turnLightOn = 1;break; //��ת
			case 14: turnLightOn = 2;break; //��ת
			case 15: dfOn = 0;break; //�ر�˫��
			case 16: dfOn = 1;break; //��˫��
			case 17: BuzzerOn = 0;break; //�رշ�����
			case 18: BuzzerOn = 1;break; //�򿪷�����
			case 19: L_Ex0 = 1;break; //�رն����0
			case 20: L_Ex0 = 0;break; //�򿪶����0
			case 21: L_Ex1 = 1;break;
			case 22: L_Ex1 = 0;break;
			case 23: L_Ex2 = 1;break;
			case 24: L_Ex2 = 0;break;
			case 25: L_Ex3 = 1;break;
			case 26: L_Ex3 = 0;break;
			case 27: L_Ex4 = 1;break;
			case 28: L_Ex4 = 0;break;
			case 29: L_Ex5 = 1;break;
			case 30: L_Ex5 = 0;break;
		}
		
		//�жϵƹ�
		switch(lightMode)
		{
		  	case 0:
				L_Back = 1;
				L_Head = 1;
				break;
			case 1:
				L_Back = 0;
				L_Head = 1;
				break;
			case 2:
				L_Back = 0;
				L_Head = 0;
				break;
		}
		switch(fogMode)
		{
		  	case 0:
				L_Front_Fog = 1;
				L_Back_Fog = 1;
				break;
			case 1:
				L_Front_Fog = 0;
				L_Back_Fog = 1;
				break;
			case 2:
				L_Front_Fog = 0;
				L_Back_Fog = 0;
				break;
		}
		L_Head_Big = ~bigLight;
		if(breakOn || backOn){
		   L_Break = 0;
		
		}else{
		 	L_Break = 1;	
		}
		L_Re = ~backOn;
	}
}

//������ʼ��
void BTinit(void)
{
   
  SCON = 0x50; 	// SCON: ģʽ1, 8-bit UART, ʹ�ܽ��� 
	TMOD |= 0x20;
	TH1=0xfd; 		//������9600 ��ֵ
	TL1=0xfd;
	TR1= 1;
	EA = 1;	    //�����ж�
	ES= 1; 		//�򿪴����ж�
}

void main()
{
	L_Back = 1;
	L_Head = 1;
	L_Head_Big = 1;
	L_Left = 1;
	L_Right = 1;
	L_Break = 1;
	L_Re = 1;
	L_Front_Fog = 1;
	L_Back_Fog = 1;
	L_Ex0 = 1;
	L_Ex1 = 1;
	L_Ex2 = 1;
	L_Ex3 = 1;
	L_Ex4 = 1;
	L_Ex5 = 1;
	L_Buzzer = 1;
	BTinit();
	while(1)
	{
		//�ж�˫��
		if(dfOn)
		{
		   	L_Left = twinkleOn;
			L_Right = twinkleOn;

		}else
		{
		   	L_Left = 1;
			L_Right = 1;
			switch(turnLightOn){
				case 1:
					L_Left = twinkleOn;
					break;
				case 2:
					L_Right = twinkleOn;
					break;
			}

		}
		//�жϷ�����
		if(backOn || BuzzerOn){
			L_Buzzer = diOn > 1;
		}
		Delay_ms(500);		 //��ʱ0.5s		
		twinkleOn = ~twinkleOn;
		diOn ++;
		if(diOn >= 3){
			diOn = 0;
		}
	}
}
