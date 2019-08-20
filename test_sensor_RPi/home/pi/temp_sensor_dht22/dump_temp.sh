#!/bin/bash


sqlite3 temperatures.sqlite3 "select * from reading order by ts desc limit 10"
