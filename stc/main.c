//****************************************************
//****************************************************
//车灯控制主函数
//****************************************************
//****************************************************
//Author 梁小蜗
#include "main.h"

//****************************************************
//主函数
//****************************************************
//低电平表示通路
unsigned int twinkleOn = 0;//闪烁标志，0.5s
unsigned int diOn = 0;//蜂鸣器间隔，1s
unsigned int lightMode = 0;//车辆开灯模式，0=关闭，1=示廓灯，2=前照灯
unsigned int fogMode = 0;//车辆雾灯模式，0=关闭，1=前雾灯，2=前后雾灯
unsigned int bigLight = 0;//大灯开启标志,0=关闭,1=开启
unsigned int turnLightOn = 0; //转向标志，0=关闭,1=左转，2=右转
bit breakOn = 0;//刹车
bit backOn = 0;//倒车
bit dfOn = 0;//双闪
bit BuzzerOn = 0;//蜂鸣器状态（工程车需单独控制）


//****************************************************
//MS延时函数(12M晶振下测试)
//****************************************************
void Delay_ms(unsigned int n)
{
	unsigned int  i,j;
	for(i=0;i<n;i++)
		for(j=0;j<110;j++);
}

					#define uchar unsigned char
/******************************************************************/
/* 串口中断程序*/
/******************************************************************/
void UART_SER () interrupt 4
{
	unsigned int n; 	//定义临时变量
	uchar receive_data;
	if(RI) 		//判断是接收中断产生
	{
		RI=0; 	//标志位清零
		n=SBUF; //读入缓冲区的值
	    receive_data=SBUF;
		if(receive_data == '1'){
			//L_Ex5 = 0;
		}
		switch(n)
		{
			case 0:	lightMode = 0;	break;	//关闭灯光
			case 1:	lightMode = 1;	break;	//示廓灯
			case 2: lightMode = 2;	break;	//前照灯
			case 3: fogMode = 0;	break;	//关闭雾灯
			case 4: fogMode = 1;  break; //打开前雾灯
			case 5: fogMode = 2; break; //打开前后雾灯
			case 6: bigLight = 0; break; //关闭大灯
			case 7: bigLight = 1; break; //打开大灯
			case 8: breakOn = 0; break; //关闭刹车灯
			case 9: breakOn = 1; break; //打开刹车灯
			case 10: backOn = 0; break; //关闭倒车
			case 11: backOn = 1; break; //打开倒车
			case 12: turnLightOn = 0;break; //关闭转向灯
			case 13: turnLightOn = 1;break; //左转
			case 14: turnLightOn = 2;break; //右转
			case 15: dfOn = 0;break; //关闭双闪
			case 16: dfOn = 1;break; //打开双闪
			case 17: BuzzerOn = 0;break; //关闭蜂鸣器
			case 18: BuzzerOn = 1;break; //打开蜂鸣器
			case 19: L_Ex0 = 1;break; //关闭额外灯0
			case 20: L_Ex0 = 0;break; //打开额外灯0
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
		
		//判断灯光
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

//蓝牙初始化
void BTinit(void)
{
   
  SCON = 0x50; 	// SCON: 模式1, 8-bit UART, 使能接收 
	TMOD |= 0x20;
	TH1=0xfd; 		//波特率9600 初值
	TL1=0xfd;
	TR1= 1;
	EA = 1;	    //开总中断
	ES= 1; 		//打开串口中断
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
		//判断双闪
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
		//判断蜂鸣器
		if(backOn || BuzzerOn){
			L_Buzzer = diOn > 1;
		}
		Delay_ms(500);		 //延时0.5s		
		twinkleOn = ~twinkleOn;
		diOn ++;
		if(diOn >= 3){
			diOn = 0;
		}
	}
}
