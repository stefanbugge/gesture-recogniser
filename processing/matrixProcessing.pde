/*
 Code based on Tom Igoe's Serial Graphing Sketch
 >> http://wiki.processing.org/w/Tom_Igoe_Interview
 
 Reads 4 analog inputs and visualizes them by drawing a 2x2 grid,
 using grayscale shading of each square to represent sensor value.
 >> http://www.kobakant.at/DIY/?cat=347
 */

import processing.serial.*;

Serial myPort;  // The serial port
int maxNumberOfSensors = 49;     
float[] sensorValue = new float[maxNumberOfSensors];  // global variable for storing mapped sensor values
float[] previousValue = new float[maxNumberOfSensors];  // array of previous values
int rectSize = 100;
int rowCount = 7;
int colCount = 7;

void setup () { 
  size(700, 700);  // set up the window to whatever size you want
  println(Serial.list());  // List all the available serial ports
  String portName = Serial.list()[0];
  myPort = new Serial(this, portName, 9600);
  myPort.clear();
  myPort.bufferUntil('\n');  // don't generate a serialEvent() until you get a newline (\n) byte
  background(255);    // set inital background
  smooth();  // turn on antialiasing
  rectMode(CORNER);
}

void draw () {
  int count = 0;
  for (int col = 0 ; col < rowCount; col++ ) {
     for (int row = 0 ;row < colCount; row++ ) {
        fill(sensorValue[count]);
        rect(col*rectSize, row*rectSize, rectSize, rectSize);
        count ++;  
     } 
  } 
}


void serialEvent (Serial myPort) {
  String inString = myPort.readStringUntil('\n');  // get the ASCII string

  if (inString != null) {  // if it's not empty
    inString = trim(inString);  // trim off any whitespace
    int incomingValues[] = int(split(inString, ","));  // convert to an array of ints

    if (incomingValues.length <= maxNumberOfSensors && incomingValues.length > 0) {
      for (int i = 0; i < incomingValues.length; i++) {
        // map the incoming values (0 to  1023) to an appropriate gray-scale range (0-255):
        sensorValue[i] = map(incomingValues[i], 0, 1023, 0, 255);  
      }
    }
  }
}
