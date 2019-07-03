
# A simple temperature and RHumidity sensor with a Raspberry PI
Alan Ward (C) 2019



This setup has been tested on a Rasberry Pi model 3B+ (with WiFi), 
and operating system Raspbian GNU/Linux 9 .

## Additional hardware and software

The temperature and relative humidity sensor used is the widely-accepted 
DHT-22 component, available in packaged form from many different manufacturers.
 
Glue between the hardware and software script written in Python is 
provided by Adafruit_Python_DHT, originally written by Tony DiCola for 
Adafruit Industries, distributed under the MIT Licence. A copy of this software 
is included in this repository with no modifications to the original 
code, for ease of use. The original code was available, at the time of writing, 
from Github repository:

https://github.com/adafruit/Adafruit_Python_DHT 


## Installing the software

Please be aware the Adafruit software will need some extra Pythonic 
bits and pieces. Documentation is, unfortunately, a bit brief. So, 
at the very least you will need to:

sudo aptitude update
sudo aptitude install build-essential python-dev python-openssl
sudo aptitude install python-pip 
cd Adafruit_Python_DHT/
sudo setup.py install
cd ..
 

This software needs some bits to be compiled for your system, and 
installed into the correct (system) directories. Just copying the 
source files to the /home/pi directory *will not be sufficient*.


## Connections

Connections to the DHT22 model are very easy, and consist only of 
three wires:

- Vcc -- connected to the RPi GPIO pin 1 (3V3): leftmost pin on lower row
- Ground -- connected, for example, to RPi GPIO pin 6: third from left on upper row
- Data -- connected to RPi GPIO pin 7 (GPIO4): fourth from left on lower row

Please see adjoined images for details.

![DHT22 side](images/'2019-06-24 10.46.51.jpg' "Connection, DHT22 side")

![RPi side](images/'2019-06-24 10.47.20.jpg' "Connection, RPi side")


