
const int 
  LED_PIN = 13,
  VIB_PIN = 6;
  
// Holds the values read from the serial port.
char inputtype[4];
char inputrepeat[4];
char inputvalue[6];

// The current values  
int type = 1;
int value = 0;
int repeat = 0;

// supported methods
const int
  NONE = -1,
  LED_ON = 0,
  LED_OFF = 1,
  LED_BLINK = 2,
  VIBRATE = 3,
  DATA = 4,
  VIBRATE_ON = 5,
  VIBRATE_OFF = 6;

void setup() {
  Serial.begin(115200);
  Serial.flush();
  pinMode(LED_PIN, OUTPUT); 
}

void loop() {
  
  if (Serial.available() >= 10) {
    // type is of the form %03d - a 3 digit int with leading zeros
    inputtype[0] = Serial.read();
    inputtype[1] = Serial.read();
    inputtype[2] = Serial.read();
    inputtype[3] = 0;
    // repeat is of the form %03d - a 3 digit int with leading zeros
    inputrepeat[0] = Serial.read();
    inputrepeat[1] = Serial.read();
    inputrepeat[2] = Serial.read();
    inputrepeat[3] = 0;
    // value is of the form %04d - a 4 digit int with leading zeros
    inputvalue[0] = Serial.read();
    inputvalue[1] = Serial.read();
    inputvalue[2] = Serial.read();
    inputvalue[3] = Serial.read();
    inputvalue[4] = 0;
    inputvalue[5] = 0;
    
    // convert input to integers.
    type = atoi(inputtype);
    repeat= atoi(inputrepeat);
    value= atoi(inputvalue);
    
    // For debugging purposes send back what was read.
    Serial.print("Received: ");
    Serial.print(type, DEC);
    Serial.print(", ");
    Serial.print(repeat, DEC);
    Serial.print(", ");
    Serial.println(value, DEC);
  }
  
  switch (type) {
    case VIBRATE_ON:
      analogWrite(VIB_PIN, 255);
      break;
    case VIBRATE_OFF:
      analogWrite(VIB_PIN, 0);
      break;
    case DATA: // used to indicate touch. just blink rapidly
      digitalWrite(LED_PIN, HIGH);
      analogWrite(VIB_PIN, 255);
      delay(50);
      digitalWrite(LED_PIN, LOW);
      analogWrite(VIB_PIN, 0);
      type = NONE;
      break;
    case LED_OFF:
      digitalWrite(LED_PIN, LOW);
      break;
    case LED_ON:
      digitalWrite(LED_PIN, HIGH);
      break;
    case LED_BLINK:
      // blink an LED for "repeat" times in "value" intervals.
      if (repeat == 0) repeat = 1;
      for (int i = 0; i < repeat; i++) {
        digitalWrite(LED_PIN, HIGH);
        delay(value/2);
        digitalWrite(LED_PIN, LOW);
        delay(value/2); 
      }
      type = NONE; 
      break;
    case VIBRATE:
      // vibrate "repeat" times in "value" intervals.
      if (repeat == 0) repeat = 1;
      for (int i = 0; i < repeat; i++) {
        analogWrite(VIB_PIN, 255);
        delay(value/2);
        analogWrite(VIB_PIN, 0);
        delay(value/2); 
      }
      type = NONE; 
      break;
    default:
      digitalWrite(LED_PIN, LOW); 
      break;
  } 
}
