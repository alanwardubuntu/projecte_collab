
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


## Connections

Connections to the DHT22 model are very easy, and consist only of 
three wires:

- Data
- Vcc
- Ground

Please see adjoined images for details.

![DHT22 side](images/'2019-06-24 10.46.51.jpg' "Connection, DHT22 side")

![RPi side](images/'2019-06-24 10.47.20.jpg' "Connection, RPi side")


