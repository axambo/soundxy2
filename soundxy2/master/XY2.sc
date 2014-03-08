/*

	SoundXY2 - a musical tabletop interface for stereo spatialisation

	Author:
    Copyright (c) 2012 Anna Xambó <anna.xambo@open.ac.uk>
	Computing Department
	Faculty of Maths, Computing and Technology
	The Open University
	Milton Keynes, UK

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.

*/

XY2 {

	classvar <>scserver;
	classvar <>mode; // 0 => Normal; 1 => Spatial

   *new {arg mod=1;
        ^super.new.init (mod);
    }

	init {arg mod;
		mode = mod;
		this.initSCServer;
	}

	initSCServer {
		postln("loading server...");
		scserver=Server.local;
		scserver.waitForBoot({
			this.initSynthDefs;
			SoundXYController.new;
		});
	}

	initSynthDefs{

		SynthDef(\OutputAux, {
			|outAuxBus = 3, inBus|
				var out;
			    out = In.ar(inBus, 1);
			    Out.ar(outAuxBus, out * 0.2);
		}).send(scserver);

		SynthDef(\outputNormal, {
				|outBus = 0, inBus|
			    var out;
			    out = In.ar(inBus, 2);
			    Out.ar(outBus, out * 0.4);
		}).send(scserver);

		SynthDef(\playBuf, {
			|outBus = 0, freqB = 1, vol = 0.5, gate = 0, outAuxBus = 2, volAux = 0.0, bufnum = 0|
			var source, out;
			source = PlayBuf.ar(1, bufnum, BufRateScale.kr(bufnum)*freqB, loop: 1, doneAction: 2);
			out = EnvGen.kr(Env.adsr, gate) * source;
			Out.ar(outBus, out!2);
			Out.ar(outAuxBus, out * volAux);
		}).send(scserver);

		SynthDef(\playBuf2, {
			|outBus = 0, freqB = 1, vol = 0.5, gate = 0, pan = 0.0, lp_freq = 0.5, delay_time = 0.0, outAuxBus = 2, volAux = 0.0, bufnum = 0|
			var source, out, outSpat, outDist;
			source = PlayBuf.ar(1, bufnum, BufRateScale.kr(bufnum)*freqB, loop: 1, doneAction: 2);
			outSpat = Pan2.ar(source, (pan * 2.0) - 1.0);
			outDist = DelayC.ar(LPF.ar(outSpat, ((1 - lp_freq) * 19600) + 400), 1.0, Ramp.kr(delay_time, 0.05), 1.0);
			out = EnvGen.kr(Env.adsr, gate) * outDist;
			Out.ar(outBus, out);
			Out.ar(outAuxBus, out * volAux);
		}).send(scserver);

		SynthDef(\sinesFM, {
			|outBus = 0, freq = 440, mfreq=20, vol = 0.5, gate = 0, outAuxBus = 2, volAux = 0.0|
			var source, out;
			source = SinOsc.ar(freq + SinOsc.ar(mfreq, 0, 100));
			out = EnvGen.kr(Env.adsr, gate) * source;
			Out.ar(outBus, out!2);
			Out.ar(outAuxBus, out * volAux);
		}).send(scserver);

		SynthDef(\sinesFM2, {
			|outBus = 0, freq = 440, mfreq=20, vol = 0.5, gate = 0, pan = 0.0, lp_freq = 0.5, delay_time = 0.0, outAuxBus = 2, volAux = 0.0|
			var source, out, outSpat, outDist;
			source = SinOsc.ar(freq + SinOsc.ar(mfreq, 0, 100));
			outSpat = Pan2.ar(source, (pan * 2.0) - 1.0);
			outDist = DelayC.ar(LPF.ar(outSpat, ((1 - lp_freq) * 19600) + 400), 1.0, Ramp.kr(delay_time, 0.05), 1.0);
			out = EnvGen.kr(Env.adsr, gate) * outDist;
			Out.ar(outBus, out);
			Out.ar(outAuxBus, out * volAux);
		}).send(scserver);

		SynthDef(\moogPulse, {
		|outBus = 0, freq = 110, vol = 0.5, gate = 0, outAuxBus = 2, volAux = 0.0|
			var source, out;
			source = MoogFF.ar(Pulse.ar(110), freq);
			out = EnvGen.kr(Env.adsr, gate) * source;
			Out.ar(outBus, out!2);
			Out.ar(outAuxBus, out * volAux);
	}).send(scserver);

		SynthDef(\moogPulse2, {
		|outBus = 0, freq = 110, vol = 0.5, gate = 0, pan = 0.0, lp_freq = 0.5, delay_time = 0.0, outAuxBus = 2, volAux = 0.0|
			var source, out, outSpat, outDist;
			source = MoogFF.ar(Pulse.ar(110), freq);
			outSpat = Pan2.ar(source, (pan * 2.0) - 1.0);
			outDist = DelayC.ar(LPF.ar(outSpat, ((1 - lp_freq) * 19600) + 400), 1.0, Ramp.kr(delay_time, 0.05), 1.0);
			out = EnvGen.kr(Env.adsr, gate) * outDist;
	    	Out.ar(outBus, out);
			Out.ar(outAuxBus, out * volAux);
	}).send(scserver);

		SynthDef(\saw, {
		|outBus = 0, freq = 440, gate = 0, outAuxBus = 2, volAux = 0.0|
		    var source, out;
		    source = RLPF.ar(Saw.ar(freq, 0.1), freq*20, 0.05);
			out = EnvGen.kr(Env.adsr, gate) * source;
	        Out.ar(outBus, out!2);
			Out.ar(outAuxBus, out * volAux);
	}).send(scserver);

		SynthDef(\saw2, {
		|outBus = 0, freq = 440, gate = 0, pan = 0.0, lp_freq = 0.5, delay_time = 0.0, outAuxBus = 2, volAux = 0.0|
			var source, out, outSpat, outDist;
		    source = RLPF.ar(Saw.ar(freq, 0.1), freq*20, 0.05);
			outSpat = Pan2.ar(source, (pan * 2.0) - 1.0);
			outDist = DelayC.ar(LPF.ar(outSpat, ((1 - lp_freq) * 19600) + 400), 1.0, Ramp.kr(delay_time, 0.05), 1.0);
			out = EnvGen.kr(Env.adsr, gate) * outDist;
	        Out.ar(outBus, out);
			Out.ar(outAuxBus, out * volAux);
	}).send(scserver);

		SynthDef(\sin, {
			|outBus = 0, freq = 440, vol = 0.5, gate = 0, outAuxBus = 2, volAux = 0.0|
			    var source, out;
				source = SinOsc.ar(freq, 0, vol);
				out = EnvGen.kr(Env.adsr, gate) * source;
		        Out.ar(outBus, out!2);
				Out.ar(outAuxBus, out * volAux);
		}).send(scserver);

		SynthDef(\sin2, {
			|outBus = 0, freq = 440, vol = 0.5, gate = 0, pan = 0.0, lp_freq = 0.5, delay_time = 0.0, outAuxBus = 2, volAux = 0.0|
				var source, out, outSpat, outDist;
				source = SinOsc.ar(freq, 0, vol);
				outSpat = Pan2.ar(source, (pan * 2.0) - 1.0);
				outDist = DelayC.ar(LPF.ar(outSpat, ((1 - lp_freq) * 19600) + 400), 1.0, Ramp.kr(delay_time, 0.05), 1.0);
				out = EnvGen.kr(Env.adsr, gate) * outDist;
			    Out.ar(outBus, out);
				Out.ar(outAuxBus, out * volAux);
		}).send(scserver);

		SynthDef(\pulse, {
			|outBus = 0, freq = 440, gate = 0, outAuxBus = 2, volAux = 0.0|
			    var source, out;
			    source = RLPF.ar(Pulse.ar(freq, 0.5, 0.1), freq*40, 0.05);
				out = EnvGen.kr(Env.adsr, gate) * source;
		        Out.ar(outBus, out!2);
				Out.ar(outAuxBus, out * volAux);
		}).send(scserver);

		SynthDef(\pulse2, {
			|outBus = 0, freq = 440, gate = 0, pan = 0.0, lp_freq = 0.5, delay_time = 0.0, outAuxBus = 2, volAux = 0.0|
				var source, out, outSpat, outDist;
			    source = RLPF.ar(Pulse.ar(freq, 0.5, 0.1), freq*40, 0.05);
				outSpat = Pan2.ar(source, (pan * 2.0) - 1.0);
				outDist = DelayC.ar(LPF.ar(outSpat, ((1 - lp_freq) * 19600) + 400), 1.0, Ramp.kr(delay_time, 0.05), 1.0);
				out = EnvGen.kr(Env.adsr, gate) * outDist;
		        Out.ar(outBus, out);
				Out.ar(outAuxBus, out * volAux);
		}).send(scserver);

		SynthDef(\simple_noise, {
			|outBus = 0, vol = 0.1, gate = 0, outAuxBus = 2, volAux = 0.0|
				var source, out;
				source = WhiteNoise.ar(vol/2);
				out = EnvGen.kr(Env.adsr, gate) * source;
				Out.ar(outBus, out!2);
		        Out.ar(outAuxBus, out * volAux);
		}).send(scserver);

		SynthDef(\simple_noise2, {
			|outBus = 0, vol = 0.3, gate = 0, pan = 0.0, lp_freq = 0.5, delay_time = 0.0, outAuxBus = 2, volAux = 0.0|
				var source, out, outSpat, outDist;
				source = WhiteNoise.ar(vol/2);
				outSpat = Pan2.ar(source, (pan * 2.0) - 1.0);
				outDist = DelayC.ar(LPF.ar(outSpat, ((1 - lp_freq) * 19600) + 400), 1.0, Ramp.kr(delay_time, 0.05), 1.0);
				out = EnvGen.kr(Env.adsr, gate) * outDist;
				Out.ar(outBus, out);
				Out.ar(outAuxBus, out * volAux);
		}).send(scserver);

		SynthDef(\blip, {
			|outBus = 0, freq = 5, vol = 0.5, gate = 0, outAuxBus = 2, volAux = 0.0|
			    var source, out;
				source = Blip.ar(Blip.kr(4, freq, 500, 60), 59, 0.5);
				out = EnvGen.kr(Env.adsr, gate) * source;
		        Out.ar(outBus, out!2);
				Out.ar(outAuxBus, out * volAux);
		}).send(scserver);

		SynthDef(\blip2, {
			|outBus = 0, freq = 440, vol = 0.5, gate = 0, pan = 0.0, lp_freq = 0.5, delay_time = 0.0, outAuxBus = 2, volAux = 0.0|
				var source, out, outSpat, outDist;
				source = Blip.ar(Blip.kr(freq, 5, 500, 60), 59, 0.5);
				outSpat = Pan2.ar(source, (pan * 2.0) - 1.0);
				outDist = DelayC.ar(LPF.ar(outSpat, ((1 - lp_freq) * 19600) + 400), 1.0, Ramp.kr(delay_time, 0.05), 1.0);
				out = EnvGen.kr(Env.adsr, gate) * outDist;
		        Out.ar(outBus, out);
				Out.ar(outAuxBus, out * volAux);
		}).send(scserver);

		SynthDef(\pinkNoise, {
			|outBus = 0, gate = 0, rhythm = 1, outAuxBus = 2, volAux = 0.0|
				var source, out;
				source = Decay2.ar(Impulse.ar(1+rhythm*4, 0.25), 0.01, 0.2, PinkNoise.ar);
				out = EnvGen.kr(Env.adsr, gate) * source;
		        Out.ar(outBus, out!2);
				Out.ar(outAuxBus, out * volAux);
		}).send(scserver);

		SynthDef(\pinkNoise2, {
			|outBus = 0, gate = 0, rhythm = 1, pan = 0.0, lp_freq = 0.5, delay_time = 0.0, outAuxBus = 2, volAux = 0.0|
				var source, out, outSpat, outDist;
				source = Decay2.ar(Impulse.ar(1+rhythm*4, 0.25), 0.01, 0.2, PinkNoise.ar);
				outSpat = Pan2.ar(source, (pan * 2.0) - 1.0);
				outDist = DelayC.ar(LPF.ar(outSpat, ((1 - lp_freq) * 19600) + 400), 1.0, Ramp.kr(delay_time, 0.05), 1.0);
				out = EnvGen.kr(Env.adsr, gate) * outDist;
		        Out.ar(outBus, out);
				Out.ar(outAuxBus, out * volAux);
		}).send(scserver);

	}

}

SoundXYController {

	var <>seto;
	var <>synths;
	var <>file;
	classvar <>buf, <>buf2, <>buf3, <>buf4;
	classvar <>bus, <>busPlayer1, <>busPlayer2;
	classvar <>synthgroup, <>fxgroup, <>wrapupgroup;
	classvar <>flagAux1 = 0, <>flagAux2 = 0, <>counterAux1 = 0, <>counterAux2 = 0;

   *new {
        ^super.new.init;
    }

	init {
		var outsynths, outaux1, outaux2;

		bus = Bus.audio(XY2.scserver,2);
		busPlayer1 = Bus.audio(XY2.scserver,2);
		busPlayer2 = Bus.audio(XY2.scserver,2);

		synthgroup = Group.tail(XY2.scserver);
		fxgroup = Group.tail(XY2.scserver);
		wrapupgroup = Group.tail(XY2.scserver);

		outsynths = Synth.head(wrapupgroup, \outputNormal, [\inBus, bus]);
		outaux1 = Synth.tail(wrapupgroup, \OutputAux, [\inBus, busPlayer1, \outAuxBus, 2]);
		outaux2 = Synth.tail(wrapupgroup, \OutputAux, [\inBus, busPlayer2, \outAuxBus, 3]);

		synths = IdentityDictionary(know: true);

		// EXPERIMENT: SAME SET OBJECTS
		/*
		buf = Buffer.read(Server.default,"/waves/a11wlk01-44_1.aiff");
		buf2 = Buffer.read(Server.default,"/waves/a11wlk01-44_1.aiff");
		buf3 = Buffer.read(Server.default,"/waves/54234__kaponja__guitar-harmonics.wav");
		buf4 = Buffer.read(Server.default,"/waves/54234__kaponja__guitar-harmonics.wav");

		switch (XY2.mode,
			0, {
				synths.put(0,Synth(\sin));
				synths.put(1,Synth(\sin));
				synths.put(2,Synth(\simple_noise));
				synths.put(3,Synth(\simple_noise));
				synths.put(4,Synth(\pinkNoise));
				synths.put(5,Synth(\pinkNoise));
				synths.put(6,Synth(\moogPulse));
				synths.put(7,Synth(\moogPulse));
				synths.put(8,Synth(\playBuf, [\bufnum,buf.bufnum]));
				synths.put(9,Synth(\playBuf, [\bufnum,buf2.bufnum]));
				synths.put(10,Synth(\playBuf, [\bufnum,buf3.bufnum]));
				synths.put(11,Synth(\playBuf, [\bufnum,buf4.bufnum]));
				},
			1, {
				synths.put(0,Synth(\sin2));
				synths.put(1,Synth(\sin2));
				synths.put(2,Synth(\simple_noise2));
				synths.put(3,Synth(\simple_noise2));
				synths.put(4,Synth(\pinkNoise2));
				synths.put(5,Synth(\pinkNoise2));
				synths.put(6,Synth(\moogPulse2));
				synths.put(7,Synth(\moogPulse2));
				synths.put(8,Synth(\playBuf2, [\bufnum,buf.bufnum]));
				synths.put(9,Synth(\playBuf2, [\bufnum,buf2.bufnum]));
				synths.put(10,Synth(\playBuf2, [\bufnum,buf3.bufnum]));
				synths.put(11,Synth(\playBuf2, [\bufnum,buf4.bufnum]));
				});

		*/

		// EXPERIMENT: DIFFERENT SET OBJECTS
		/*
		buf = Buffer.read(Server.default,"/waves/a11wlk01-44_1.aiff");
		buf2 = Buffer.read(Server.default,"/waves/a11wlk01.wav");
		buf3 = Buffer.read(Server.default,"/waves/54234__kaponja__guitar-harmonics.wav");
		buf4 = Buffer.read(Server.default,"/waves/27359__junggle__djembe-loop-13.wav");

				switch (XY2.mode,
			0, {
				synths.put(0,Synth(\sin));
				synths.put(1,Synth(\pulse));
				synths.put(2,Synth(\simple_noise));
				synths.put(3,Synth(\blip));
				synths.put(4,Synth(\pinkNoise));
				synths.put(5,Synth(\saw));
				synths.put(6,Synth(\moogPulse));
				synths.put(7,Synth(\sinesFM));
				synths.put(8,Synth(\playBuf, [\bufnum,buf.bufnum]));
				synths.put(9,Synth(\playBuf, [\bufnum,buf2.bufnum]));
				synths.put(10,Synth(\playBuf, [\bufnum,buf3.bufnum]));
				synths.put(11,Synth(\playBuf, [\bufnum,buf4.bufnum]));
				},
			1, {
				synths.put(0,Synth(\sin2));
				synths.put(1,Synth(\pulse2));
				synths.put(2,Synth(\simple_noise2));
				synths.put(3,Synth(\blip2));
				synths.put(4,Synth(\pinkNoise2));
				synths.put(5,Synth(\saw2));
				synths.put(6,Synth(\moogPulse2));
				synths.put(7,Synth(\sinesFM2));
				synths.put(8,Synth(\playBuf2, [\bufnum,buf.bufnum]));
				synths.put(9,Synth(\playBuf2, [\bufnum,buf2.bufnum]));
				synths.put(10,Synth(\playBuf2, [\bufnum,buf3.bufnum]));
				synths.put(11,Synth(\playBuf2, [\bufnum,buf4.bufnum]));
				});
		*/

		// VIDEO DEMO

		buf = Buffer.read(Server.default,"/waves/92000__timbre__incoming-transmission-download-complete-female-robot-2.wav");
		buf2 = Buffer.read(Server.default,"/waves/74530__dobroide__20090621-recipe-00.wav");
		buf3 = Buffer.read(Server.default,"/waves/1400__sleep__muted-d-5th.wav");
		buf4 = Buffer.read(Server.default,"/waves/36238__marvman__c-major-bass.wav");

		switch (XY2.mode,
			0, {
				synths.put(0,Synth(\sin));
				synths.put(1,Synth(\pulse));
				synths.put(2,Synth(\simple_noise));
				synths.put(3,Synth(\blip));
				synths.put(4,Synth(\pinkNoise));
				synths.put(5,Synth(\saw));
				synths.put(6,Synth(\moogPulse));
				synths.put(7,Synth(\sinesFM));
				synths.put(8,Synth(\playBuf, [\bufnum,buf.bufnum]));
				synths.put(9,Synth(\playBuf, [\bufnum,buf2.bufnum]));
				synths.put(10,Synth(\playBuf, [\bufnum,buf3.bufnum]));
				synths.put(11,Synth(\playBuf, [\bufnum,buf4.bufnum]));
				},
			1, {
				synths.put(0,Synth(\sin2));
				synths.put(1,Synth(\pulse2));
				synths.put(2,Synth(\simple_noise2));
				synths.put(3,Synth(\blip2));
				synths.put(4,Synth(\pinkNoise2));
				synths.put(5,Synth(\saw2));
				synths.put(6,Synth(\moogPulse2));
				synths.put(7,Synth(\sinesFM2));
				synths.put(8,Synth(\playBuf2, [\bufnum,buf.bufnum]));
				synths.put(9,Synth(\playBuf2, [\bufnum,buf2.bufnum]));
				synths.put(10,Synth(\playBuf2, [\bufnum,buf3.bufnum]));
				synths.put(11,Synth(\playBuf2, [\bufnum,buf4.bufnum]));
				});


		file = File("testTUIO", "w");
		file.write("Mode: % \n".postf(XY2.mode));

		this.listen;
	}

	listen{

		var sound_speed = 340.29;					// Speed of sound at sea level in m/s (from Google Calculator)
		var max_distance = 20;						// Distance in metres
		var max_time = max_distance / sound_speed; 	// Maximum delay time at max_distance and sound_speed

			SETODictObj.setAction([0,1,2,3,4,5,6,7,8,9,10,11], // Objects type: Sound generators (param: rotation)
				{|me|
					var normRot, freqv, posx, posy, posy2;
					var distance, distance2;
					var rel_delay_time = 0.0;			// The time source is delayed due to distance from listener
					var rel_delay_time2 = 0.0;
					file.write("% set % % % % %\n".postf(me.tStamp, me.classID, me.id, me.pos[0], me.pos[1], me.rotEuler[0]));

					posx = me.pos[0];
					posy = me.pos[1];
					posy2 = 1 - me.pos[1];
					normRot = me.rotEuler[0]/2pi; // rot from 0 to 1
					freqv = this.convertValueToFreq(normRot);
					// Calculate direct distance from bottom centre to position object (hypot)
					distance = ((posx - 0.5).squared.abs + posy.squared).sqrt;
					distance2 = 1-((posx - 0.5).squared.abs + posy.squared).sqrt;
					// Calculate delay time from source
					rel_delay_time = (distance * max_distance) / sound_speed;
					rel_delay_time2 = (distance2 * max_distance) / sound_speed;
					synths[me.classID].set(\outBus, bus, \freq, freqv); // For me.classID == 0 & 1
					if(XY2.mode == 1, {synths[me.classID].set(\pan, posx)});
					if((XY2.mode == 1) && (me.classID % 2 == 0),
						{synths[me.classID].set(\lp_freq, posy, \delay_time, rel_delay_time)},
						{synths[me.classID].set(\lp_freq, posy2, \delay_time, rel_delay_time2)}
						);

				    if(me.classID == 1,                                  // if different set
						{freqv = this.convertValueToFreq(normRot, 500);  // if different set
						synths[me.classID].set(\freq, freqv)}            // if different set
						);                                               // if different set
					if(me.classID == 2,
						{synths[me.classID].set(\vol, normRot)}
						);
					if(me.classID == 3,
						//{synths[me.classID].set(\vol, normRot)}        // if same set
					    {freqv = this.convertValueToFreq(normRot, 8);    // if different set
						synths[me.classID].set(\freq, freqv)}            // if different set
						);
					if(me.classID == 4,
						{synths[me.classID].set(\rhythm, normRot)}
						);
						if(me.classID == 5,
						//{synths[me.classID].set(\rhythm, normRot)}     // if same set
					    {freqv = this.convertValueToFreq(normRot, 250);  // if different set
						synths[me.classID].set(\freq, freqv)}            // if different set
						);
					if(me.classID == 6,
						{freqv = this.convertValueToFreq(normRot, 800);
						synths[me.classID].set(\freq, freqv)}
						);
					if(me.classID == 7,
						//{freqv = this.convertValueToFreq(normRot, 800); // if same set
					    {freqv = this.convertValueToFreq(normRot, 100);   // if different set
						synths[me.classID].set(\freq, freqv)}
						);
					if(me.classID == 8,
						{synths[me.classID].set(\freqB, normRot);}
						);
					if(me.classID == 9,
						{synths[me.classID].set(\freqB, normRot);}
						);

					if(me.classID == 10,
						{synths[me.classID].set(\freqB, normRot);}
						);
					if(me.classID == 11,
						{synths[me.classID].set(\freqB, normRot);}
						);

				    if(me.classID % 2 == 0 && flagAux1 == 1,
						{synths[me.classID].set(\volAux, 1.0, \outAuxBus, busPlayer1);
							}
						);
					if(me.classID % 2 == 0 && flagAux1 == 0,
						{synths[me.classID].set(\volAux, 0.0, \outAuxBus, busPlayer1);}
						);

					if(me.classID % 2 != 0 && flagAux2 == 1,
						{synths[me.classID].set(\volAux, 1.0, \outAuxBus, busPlayer2);}
						);
					if(me.classID % 2 != 0 && flagAux2 == 0,
						{synths[me.classID].set(\volAux, 0.0, \outAuxBus, busPlayer2);}
						);

					synths[me.classID].set(\gate, 1);
					},
				{|me|
					file.write("% del % % % % %\n".postf(me.tStamp, me.classID, me.id, me.pos[0], me.pos[1], me.rotEuler[0]));
					synths[me.classID].set(\gate, 0);
					}
				);
				SETODictObj.setAction([20],
					{|me|
						"aux1 on".postln;
						flagAux1 = 1;
						},
					{|me|
						counterAux1 = counterAux1 + 1;
						file.write("% set counterAux1: % times\n".postf(me.tStamp, counterAux1));
						SystemClock.sched(3, { flagAux1 = 0; "aux1 off".postln;});
						}
				);
				SETODictObj.setAction([21],
					{|me|
						"aux2 on".postln;
						flagAux2 = 1;
						},
					{|me|
						counterAux2 = counterAux2 + 1;
						file.write("% set counterAux2: % times\n".postf(me.tStamp, counterAux2));
						SystemClock.sched(3, { flagAux2 = 0; "aux2 off".postln;});
						}
				);
				SETODictObj.setAction([23],
					{|me|
						"start".postln;
						},
					{|me|
						file.write("% set start %\n".postf(me.tStamp, me.classID));
						}
				);


		seto = SETO_OSCServer('2Dobj', nil, SETODictObj);
		seto.start;

	}

	convertValueToFreq {arg normValue, maxValue=2000;
		^20+(log(normValue+1)*maxValue);
	}

	stop {
		seto.stop;
	}

	cmdPeriod {
		file.close;
		[synthgroup, fxgroup, wrapupgroup, bus, busPlayer1, busPlayer2].do({ arg x; x.free });
		currentEnvironment.clear;

	}

}

