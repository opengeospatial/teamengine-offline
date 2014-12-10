TEAM ENGINE OFFLINE
author: Luis Bermudez, Richard Martell, Chris Coppock
TE Version 4.0.5
date: July 25th, 2014
questions:http://cite.opengeospatial.org/forum

Team Engine - Offline Repository

This repository provides information, scripts, test results and other artifacts to run (TEAM ENGINE)[https://github.com/opengeospatial/teamengine/] offline.
Team Engine - Offline Build Instructions

1 Ensure network adapters are disabled on the workstation

Verify using the command prompt by typing this command:
C:\> Ipconfig – the IP listed should be an internally used one, and the IP address range should be either 10.0.0.0 – 10.255.255.255, 172.16.0.0 – 172.31.255.255, or 192.168.0.0 – 192.168.255.255
2 Locate and Install Software

(Please note that the numbers contained in parentheses directly correlate to the Team Engine 4.0.5 Instruction Set)

Locate and open the directory labeled: ‘Offline_4.0.5_Install’
Open the sub-folder labeled: ‘pre-req_SW’
The software packages in this folder should consist of:
Apache-maven-3.2.1-bin.zip
Apache-tomcat-7.0.52-windows-x64.zip
Jdk-7u60-x64.exe
Git-1.9.4-preview20140611
3 Install the software in the following order:

Jdk-7u60-windows-x64.exe (3.1) 
If presented with Windows Smart Screen error, click on ‘Run Anyway’
Apache-maven-3.2.1-bin.zip (3.2)
Git-1.9.4-preview20140611 (3.3)
Apache-tomcat.7.0.52-windows-x64.zip (3.5)
4 Modify the system environment variables (3.6)

The major difference in online versus offline installation of the system variables, is the non-inclusion of the SVN client and of the associated system environment variable

Restart workstation
Test the commands found in (3.8)
5 Testing Tomcat

Utilizing the installation instructions from (3.9), run tomcat to verify it works properly
6 Copying Directories

Copy .m2 folder from ‘Offline_4.0.5_Install/repo’ into the “C:\Users\’User Name’\” directory. If folder already exists, after backing up original folder, replace with bundled folder.
Copy the ‘Offline_4.0.5_Install\repo’ folder into the ‘C:\’ directory. (4.6.1)
Resume executing instructions from (5.0 – 6.2.2)
Copy the scripts directory from the ‘Offline_4.0.5_Install\repo\svn’ directory into the ‘C:\TE_BASE\’ directory, replacing any existing ‘scripts’ directory
Skip directions from 7.0 – 7.5
7 Completing installation of tests and running tests in command prompt

In the command prompt, change directory to ‘c:\repo\svn\ets-resources\14.04.16\’ and follow these instructions:
7.6.4 – 7.7, and 7.9 - 7.11.3.4 (skipping 7.8 - 7.8.3.3)
Use ‘\Offline_4.0.5_Install\repo\Test_Data\test_ogrlibkml’ for test 7.11.2.3
Use ‘\Offline_4.0.5_Install\repo\Test_Data\gml321.xml’ for test 7.11.3.3
8 Creating Tomcat Catalina WebApp

Continue with directions for (8.0 – 8.2.6)
Skip instructions for (8.3 – 8.3.4)
Copy teamengine.war file from ‘\Offline_4.0.5_Install\repo’ into ‘c:\CATALINA_BASE\webapps’ (8.3.6)
9 Running Team Engine

Continue with directions (8.5 – 8.6) and complete installation
