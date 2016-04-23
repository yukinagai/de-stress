#define LEDPIN	(10)
#define IN1PIN  (6)
#define IN2PIN  (7)

char recv[10];

void setup()
{
  pinMode( LEDPIN, OUTPUT );
  pinMode(IN1PIN,OUTPUT);
  pinMode(IN2PIN,OUTPUT);
  Serial.begin(115200);
  
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

int iMotor = 0;
void loop()
{
  while (Serial.available()) {
      iMotor = Serial.read();  
      Serial.print("get");
      Serial.println(iMotor);  
  }
  MotorDrive(IN1PIN,IN2PIN,iMotor);
  
  delay(50);
}

