
#define CELL_COUNT 49
int resetBtnPin = 2;
int minValues[CELL_COUNT];
int rowPins [] = { 6, 7, 8, 9 };
int colPins [] = { 10,11,12,13};
int metaMsk[7][4] = {
  {
    0, 0, 0, 0    }
  ,
  {
    1, 0, 0, 0    }
  ,
  {
    0, 1, 0, 0    }
  ,
  {
    1, 1, 0, 0    }
  ,
  {
    0, 0, 1, 0    }
  ,
  {
    1, 0, 1, 0    }
  ,
  {
    0, 1, 1, 0    }
};

int r0 = 0;
int r1 = 0;
int r2 = 0;
int r3 = 0;

int c0 = 0;
int c1 = 0;
int c2 = 0;
int c3 = 0;

int matrixValues[CELL_COUNT];

int COM1 = 4;
int COM2 = A0;

void setup() {
  for (int i = 0; i < 4; i++) {
    pinMode(rowPins[i], OUTPUT);
    pinMode(colPins[i], OUTPUT);
  }
  
  // enable output mux
  pinMode(COM1, OUTPUT);
  digitalWrite(COM1, HIGH);
  
  // enable input mux
  pinMode(COM2, INPUT);
  
  Serial.begin(9600);
}

void loop() {
  
  // Reset button for mapping
  int value = digitalRead(resetBtnPin);
  if (value == HIGH) {
    // New minimum values are set
    for (int j = 0; j < CELL_COUNT; j++) {
      minValues[j] = matrixValues[j];
    }
  }
  
  int counter = 0;
  for (int row = 0; row < 7; row++) {
 
    // Set multiplexer address bits
    for (int i = 0 ; i < 4; i++ ) {
      digitalWrite(rowPins[i], metaMsk[row][i]);
    }
    
    for (int col = 0; col < 7; col++) {
      
      // Set multiplexer address bits
      for (int j = 0 ; j < 4; j++ ) {
         digitalWrite(colPins[j], metaMsk[col][j]);
      }      
      matrixValues[counter] = analogRead(COM2);
      counter++;
    }     
  }

    
  // Map and print to Processing 
  for (int j = 0; j < CELL_COUNT; j++) {
    int mappedReading = map(matrixValues[j], minValues[j], 1023, 0, 255);
    Serial.print(mappedReading, DEC);
    if (j < CELL_COUNT-1) Serial.print(",");  
  }
  Serial.println();
  delay(10);   
   
}
