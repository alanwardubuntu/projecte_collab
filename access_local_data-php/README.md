# Access local data through PHP

Since most web browsers implement a strict policy on Cross-Origin 
Resource Sharing (CORS), it is difficult to access a respository 
located on different server from that which hosts our PHP code. 
For this reason, **a local copy of data files is required**, to be placed 
in subdirectory *data* of wherever the PHP file is.

This PHP code has been tested on an Apache server version 2.4.25 
and PHP version 7.0+49, on Debian Stretch.
