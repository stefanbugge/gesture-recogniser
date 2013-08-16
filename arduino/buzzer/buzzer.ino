
const int
  VIBRATION_PIN_1 = 8;

const int
  OFF = 0,
  ACCEPT = 1;
  
int vibrationMode = OFF;
  
char vibrateInput[2];

void setup() {
  Serial.begin(57600);
  Serial.flush();

  pinMode(VIBRATION_PIN_1, OUTPUT);
  
}

void loop() {
  
  if (Serial.available() >= 1) {

    vibrateInput[0] = Serial.read();
    vibrateInput[1] = 0;
    
    vibrationMode = atoi(vibrateInput);
    
    Serial.print("Received: ");
    Serial.println(vibrationMode, DEC);
  }
  
  unsigned long counter = millis();
  
  switch (vibrationMode) {
    case OFF:
      analogWrite(VIBRATION_PIN_1, 0);
      break;
    
    case ACCEPT:
      {
        int vibrationStep = counter % 1760;
        if (0 <= vibrationStep && vibrationStep < 800) {
          // vibrate(800, 200);
          analogWrite(VIBRATION_PIN_1, 200);
        } else if (800 <= vibrationStep && vibrationStep < 860) {
          // vibrateBreak(60);
          analogWrite(VIBRATION_PIN_1, 0);
        } else if (860 <= vibrationStep && vibrationStep < 1360) {
          // vibrate(500, 180);
          analogWrite(VIBRATION_PIN_1, 180);
        } else if (1360 <= vibrationStep && vibrationStep < 1760) {
          // vibrateBreak(400);
          analogWrite(VIBRATION_PIN_1, 0);
          vibrationMode = OFF;
        }
      }
      break;
    default:
      analogWrite(VIBRATION_PIN_1, 0);
      break;    
  }
}
