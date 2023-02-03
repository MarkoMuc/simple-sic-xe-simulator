### Simple SIC/XE simulator
This is a simple SIC/XE simulator as per standard defined in **System Software: An Introduction to Systems Programming by *Leland Beck***.
Directory objs holds some precompiled .obj files, to test on.
## Overview
Simulation options : Step or auto, print Memory dump and sleep time between instructions

Functions:
- 256 devices (including STDIN, STDOUT, STDERR)
- All 4 formats of instructions
- Majority of instructions are implemented
- Word and Byte data formats (float is not implemented)

## Run 
Directory CompiledJar holds compiled sic.jar, implemented and compiled in JDK17.

Command to run the simulator:

    java -jar sic.jar [PATH to .obj file]
