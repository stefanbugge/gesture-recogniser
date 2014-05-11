
#define CELL_COUNT 256
// Pins
int resetBtnPin = 2;
int colPins [] = { 6, 7, 8, 9 };
int rowPins [] = { 10,11,12,13};

// Multiplexer map with binary values
int muxMap[16][4] = {
  { 0, 0, 0, 0 },
  { 1, 0, 0, 0 },
  { 0, 1, 0, 0 },
  { 1, 1, 0, 0 },
  { 0, 0, 1, 0 },
  { 1, 0, 1, 0 },
  { 0, 1, 1, 0 },
  { 1, 1, 1, 0 },
  { 0, 0, 0, 1 },
  { 1, 0, 0, 1 },
  { 0, 1, 0, 1 },
  { 1, 1, 0, 1 },
  { 0, 0, 1, 1 },
  { 1, 0, 1, 1 },
  { 0, 1, 1, 1 },
  { 1, 1, 1, 1 }
};

// array with sensor readings
int matrixValues[CELL_COUNT];
// the calibrated minimum values
int minValues[CELL_COUNT];
// Multiplexer for output values
int MuxOut = 4;
// Multiplexer for reading values
int MuxIn = A0; 

void setup() {
  for (int i = 0; i < 4; i++) {
    pinMode(rowPins[i], OUTPUT);
    pinMode(colPins[i], OUTPUT);
  }
  
  // enable output mux
  pinMode(MuxOut, OUTPUT);
  digitalWrite(MuxOut, HIGH);
  
  // enable input mux
  pinMode(MuxIn, INPUT);
  
  Serial.begin(115200);
    
  // Initial calibration
  sweep();
  sweep();
  calibrate();
}

void loop() {
  
  // Reset button for mapping
  int value = digitalRead(resetBtnPin);
  if (value == HIGH) {
    // New minimum values are set
    calibrate();
  }
  
  sweep();
    
  // Map to 8bit values and print to serial stream
  for (int j = 0; j < CELL_COUNT; j++) {
    int mappedReading = map(matrixValues[j], minValues[j], 1023, 0, 255);
    mappedReading = (mappedReading < 0) ? 0 : mappedReading;
    Serial.print(mappedReading, DEC);
    if (j < CELL_COUNT-1) Serial.print(",");  
  }
  Serial.println();
  delay(10);
}

// Set the minimum values to the latest sensor values.
void calibrate() {
  for (int j = 0; j < CELL_COUNT; j++) {
    minValues[j] = matrixValues[j];
  }  
}

// Do a sweep. Read all columns for each row. 
void sweep() {
  int counter = 0;
  for (int row = 0; row < 16; row++) {
    // Set multiplexer address bits
    for (int i = 0 ; i < 4; i++ ) {
      digitalWrite(rowPins[i], muxMap[row][i]);
    }
    
    for (int col = 0; col < 16; col++) {  
      // Set multiplexer address bits
      for (int j = 0 ; j < 4; j++ ) {
         digitalWrite(colPins[j], muxMap[col][j]);
      }      
      // read pressure values.
      matrixValues[counter] = analogRead(MuxIn);
      counter++;
    }     
  } 
}

