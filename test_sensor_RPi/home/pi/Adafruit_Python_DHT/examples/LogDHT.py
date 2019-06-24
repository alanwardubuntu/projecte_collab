import datetime
import sys
import sqlite3
import time

import Adafruit_DHT


# Parse command line parameters.
sensor_args = { '11': Adafruit_DHT.DHT11,
                '22': Adafruit_DHT.DHT22,
                '2302': Adafruit_DHT.AM2302 }
if len(sys.argv) == 3 and sys.argv[1] in sensor_args:
    sensor = sensor_args[sys.argv[1]]
    pin = sys.argv[2]
else:
    print('Usage: sudo ./Adafruit_DHT.py [11|22|2302] <GPIO pin number>')
    print('Example: sudo ./Adafruit_DHT.py 2302 4 - Read from an AM2302 connected to GPIO pin #4')
    sys.exit(1)


humidity, temperature = Adafruit_DHT.read_retry(sensor, pin)

if humidity is not None and temperature is not None:
    now = datetime.datetime.now()
    ts = time.time()
    print('Temp=%f   Hum=%f    Date=%d/%d/%d   Time=%d:%d:%d' % (temperature, humidity, now.day, now.month, now.year, now.hour, now.minute, now.second))
    
    conn = sqlite3.connect("temperatures.sqlite3")
    c = conn.cursor()
    
    c.execute("INSERT INTO reading VALUES(%f, %f, %d, %d, %d, %d, %d, %d, %f)" % (temperature, humidity, now.day, now.month, now.year, now.hour, now.minute, now.second, ts))
    
    conn.commit()
    conn.close()
    
else:
    print('Failed to get reading. Try again!')
    sys.exit(1)

