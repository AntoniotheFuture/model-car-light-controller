#ifndef __MAIN_H__
#define __MAIN_H__


#include <reg52.h>

//�󶨳���(STC12C5A60S2)
sbit L_Back = P0^0;//ʾ����
sbit L_Head = P0^1;//С��
sbit L_Head_Big = P0^2;//���
sbit L_Left = P0^3;//��ת��
sbit L_Right = P0^4;//��ת��
sbit L_Break = P0^5;//ɲ����
sbit L_Re = P0^6;//������
sbit L_Front_Fog = P0^7;//ǰ���
sbit L_Back_Fog = P2^7;//�����
sbit L_Ex0 = P2^6;//�����0
sbit L_Ex1 = P2^5;//�����1
sbit L_Ex2 = P2^4;//�����2
sbit L_Ex3 = P2^3;//�����3
sbit L_Ex4 = P2^2;//�����4
sbit L_Ex5 = P2^1;//�����5
sbit L_Buzzer = P2^0;//������


//�������߱�������
extern void Delay_ms(unsigned int n);
extern void BTinit();

#endif