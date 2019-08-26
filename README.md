
# Projecte collab

## Project description

The aim of this project is to build a modular system to monitor various 
parameters within a given building. The main types of module will be:

- Sensor modules, that are the hardware part of the system. Each sensor will 
report one or more parameters at a given location. Some examples of data types 
are temperature (TEMP) and relative humidity (RHUM).

- Database modules. Each database must be capable of maintaining a local 
copy of all types of data, and making this data available to other modules 
over various connections. Examples include, but are not limited to: HTTP and 
RSYNC.

- Data manipulation and analysis modules. These will perform tasks such as 
data mining.

- Data access and representation modules, which access raw or treated data 
and represent it on screen for human users. Examples could range from 
simple desktop applications to web interfaces and mobile apps.


## The software

*test_sensor_RPi* is a project using a DHT-22 digital temperature 
and relative humidity sensor connected to a Raspberry Pi single-
chip computer. A crontab job is set up to fire a script written in 
Python (by AdaFruit) to obtain the data from the sensor. They can 
then be use as seen fit, in this example logged to an SQLite database 
file. 

*access_data_graph-python* is a simple Python program for the desktop that 
accesses temperature and humidity data from a store in our companion repository 
*projecte_collab_dades* and presents the curves onscreen. 
 
*access_data_graph-android* is a complete Android application that 
accesses temperature and humidty data from a store in our companion repository 
*projecte_collab_dades*. Source code and APK file are available.

*access_local_data-php* is a server script in PHP that accesses a 
local copy of data files from the *data* subdirectory and plots 
results in a web page.
