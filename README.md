SOUNDXY2
========
Copyright (c) 2012 Anna Xambó <anna.xambo@open.ac.uk>

Introduction
------------

**SoundXY2** is a musical tabletop interface implemented for real-time performance of sound samples and effects using stereo spatialisation. SoundXY2 is suitable for duos performing in collaboration using two speakers. It provides auditory information of the location of tangible objects on a tabletop surface. It can be useful to support both individual and group awareness of users actions on the tabletop surface with no need of using headphones. This work precedes SoundXY4, which implements sound samples and effects using ambisonics spatialisation, suitable for small groups of between two and four-five people (http://github.com/axambo/soundxy4).

See a video demo here:

http://vimeo.com/55298584


Application Start
-----------------

Before starting the SoundXY2 application, you will need to start your tabletop hardware setup or alternatively a software simulator. This version has been tested with a hardware setup based on the diffused illumination (DI) technique, assuming that i) below the tabletop surface there is an IR camera with appropriate IR illumination and a video projector, and ii) the tabletop surface material is both translucent enough to allow the IR camera to track the markers of the objects, and opaque enough to allow the projection to be clearly displayed. 

For more information: 

http://wiki.nuigroup.com/Diffused_Illumination

Make sure to install a computer vision engine that implements the TUIO protocol (here we are using reacTIVision: http://reactivision.sourceforge.net). Confirm that an IR camera is connected to reacTIVision before launching the engine, and also that in the file `reacTIVision.xml` the number of port is changed to `57120` to send TUIO messages to SuperCollider: `<tuio host="127.0.0.1" port="57120" />`. Alternatively start the TUIO Simulator typing `java -jar TUIOSimulator.jar -port 57120` from the command line to send TUIO messages to SuperCollider.

reacTIVision provides a set of fiducial markers with unique IDs. It is recommended to have a number of physical objects with these markers attached.

The SoundXY2 application is based on the SuperCollider programming language.
 
http://supercollider.sourceforge.net

Drag SoundXY2 folder to the Extensions folder of SuperCollider: `/Users/{username}/Library/Application Support/SuperCollider/Extensions` (in Mac)

You need to have installed the SETO Quark for the recognition of tangible objects.

http://sourceforge.net/p/quarks/code/HEAD/tree/seto/

You will also need to download the sounds and point to the corresponding folder in the main class file `XY2.sc`.

Restart SuperCollider.


Usage
-----

You can start the application by typing `XY2.new` on the SuperCollider IDE. The spatialisation mode is activated by default. It is also possible to launch the program with no spatialisation by typing `XY2.new(0)`. 
 
SoundXY2 tracks the identity (ID), position and orientation of tangible objects tagged with fiducial markers and maps them to sound players. In this version, there is a subset of 12 different sound samples that are mapped to 12 fiducials (IDs from 0 to 11), 6 objects for each of the users distributed between even and odd fiducial ID numbers. Sounds are played in loop. There are also two additional objects (IDs 20 and 21), one for each of the two players, that trigger the active sounds of that player from an auxiliary monitor positioned at the back of each musician (similar to the monitors used when performing on stage).
 
SoundXY2 lacks visual feedback for the tangible objects on the tabletop surface.
 
It is possible to change a parameter for each sound player object by rotating clockwise (volume up) or counterclockwise (volume down).


License
-------

SoundXY2 is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version. See [COPYING](COPYING) for the license text.


References
----------
- This code is inspired by WaveTable, a tangible and multi-touch tabletop waveform editor developed by Gerard Roma who led the software design and Anna Xambó who led the hardware design in 2008. Read our paper here: http://www.nime.org/proceedings/2008/nime2008_249.pdf

- The stereo spatialisation approach has been adapted from Adam Jansch's code examples (Day 10 and 12): http://www.adamjansch.co.uk/sc-a-day/

- This application runs with SuperCollider: http://supercollider.sourceforge.net

- This application uses the SETO Quark for the recognition of objects, developed by Till Bovermann: http://tuio.lfsaw.de/seto.shtml

- The computer vision engine used in this work is reacTIVision, developed by Martin Kaltenbrunner: http://reactivision.sourceforge.net

- All sounds (see full list below) can be found in the open sound database Freesound.org, developed by the MTG-UPF: http://freesound.org


Acknowledgements
----------------

I am very thankful to Gerard Roma for his help and feedback. 


Sound credits
-------------

**Sounds from FreeSound.org**

- 20090621.recipe_00.wav by dobroide: http://www.freesound.org/people/dobroide/sounds/74545/ (used in the promotional video)
- c major bass.wav by marvman: http://www.freesound.org/people/marvman/sounds/36238/ (used in the promotional video)
- djembe_loop_13.wav by junggle: http://www.freesound.org/people/junggle/sounds/27359/ (used in the experiment)
- guitar harmonics by kaponja: http://www.freesound.org/people/kaponja/sounds/54234/ (used in the experiment)
- Incoming transmission-download complete (Female Robot)2.wav by Timbre: http://www.freesound.org/people/Timbre/sounds/92000/ (used in the promotional video)
- muted_d_5th.wav by sleep: http://www.freesound.org/people/sleep/sounds/1400/ (used in the promotional video)

**Sounds from SuperCollider**

- a11wlk01-44_1.aiff (used in the experiment and in the promotional video)
- a11wlk01.wav (used in the experiment and in the promotional video)

