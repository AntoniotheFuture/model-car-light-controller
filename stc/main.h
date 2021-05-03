#ifndef __MAIN_H__
#define __MAIN_H__


#include <reg52.h>

//绑定车灯(STC12C5A60S2)
sbit L_Back = P0^0;//示廓灯
sbit L_Head = P0^1;//小灯
sbit L_Head_Big = P0^2;//大灯
sbit L_Left = P0^3;//左转灯
sbit L_Right = P0^4;//右转灯
sbit L_Break = P0^5;//刹车灯
sbit L_Re = P0^6;//倒车灯
sbit L_Front_Fog = P0^7;//前雾灯
sbit L_Back_Fog = P2^7;//后雾灯
sbit L_Ex0 = P2^6;//额外灯0
sbit L_Ex1 = P2^5;//额外灯1
sbit L_Ex2 = P2^4;//额外灯2
sbit L_Ex3 = P2^3;//额外灯3
sbit L_Ex4 = P2^2;//额外灯4
sbit L_Ex5 = P2^1;//额外灯5
sbit L_Buzzer = P2^0;//蜂鸣器


//函数或者变量声明
extern void Delay_ms(unsigned int n);
extern void BTinit();

#endif