#!/bin/bash

cd /home/pi

# model DHT22
# GPIO 4 (= pin 7)
python Adafruit_Python_DHT/examples/LogDHT.py 22 4
