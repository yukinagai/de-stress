#include <Wire.h>

#define LEDPIN	(10)
#define IN1PIN  (6)
#define IN2PIN  (7)

int I2CTEMPADDR = 0x48;
int countTemperture=0;

char recv[10];

void setup()
{
  pinMode( LEDPIN, OUTPUT );
  pinMode(IN1PIN,OUTPUT);
  pinMode(IN2PIN,OUTPUT);
  Serial.begin(115200);
  Wire.begin(); 
}



void MotorDrive( int iIn1Pin, int iIn2Pin, int iMotor )
{
  if( -5 < iMotor
   &&  5 > iMotor )
  {
    //digitalWrite(iIn1Pin, HIGH);
    //digitalWrite(iIn2Pin, HIGH);
    digitalWrite(iIn1Pin, LOW);
    digitalWrite(iIn2Pin, LOW);
  }
  else if( 0 < iMotor )
  {
    analogWrite(iIn1Pin, iMotor);
    analogWrite(iIn2Pin, 0);
  }
  else
  {
    analogWrite(iIn1Pin, 0);
    analogWrite(iIn2Pin, -iMotor);
  }
}

void getTemp(){
  //Serial.println("get temp");
  countTemperture++;
  if(countTemperture==100){
    countTemperture=0;
    uint16_t val;
    float tmp;
    int ival;
   
    Wire.requestFrom(I2CTEMPADDR, 2);       // S.C発行,CB送信
    val = (uint16_t)Wire.read() << 8;   // データの読み出し(上位)
    val |= Wire.read();                 // データの読み出し(下位)
    val >>= 3;                          // 13bit化
    ival = (int)val;                    // 整数化
   
    if(val & (0x8000 >> 3)) {         // 符号判定
      ival = ival  - 8192;            // 負数のとき
    }
   
    tmp = (float)ival / 16.0;         // 摂氏温度換算
    Serial.print("temperature/");
    Serial.println(tmp,1);     // xx.xx 温度値をシリアル送信
  }
}

int iMotor = 0;
void loop()
{
  while (Serial.available()) {
      iMotor = Serial.read();  
      Serial.print("get");
      Serial.println(iMotor);  
  }
  MotorDrive(IN1PIN,IN2PIN,iMotor);

  getTemp();
  //delay(5000);
  delay(50);
}

