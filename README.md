# Virtual Drum Machine

## An app for musicians and anyone interested in making new sounds!

This will be an application in which users can place various drum sounds in sequences and then loop the beat which 
results. Some examples of drum sounds I will include, all of which come with the basic MIDI standard are:
- Kick drum 
- Open and closed hi-hat
- Snare drum
- Ride cymbal
- Timpani

And many more. The app is intended for pianists, guitarists and other musicians who want to create backing tracks while 
they play, as well as any other people who are interested in making simple beats and rhythms. This project is of 
interest to me because I am a musician, and I find it useful to have drum accompaniment to keep 
me on rhythm, to fill out the sound and to help with songwriting. I also really enjoy playing with rhythms and I will 
find it fun to create new patterns on the app.

## User Stories
- As a user, I want to be able to add an instrument with its notes to be played to the track
- As a user, I want to be able to delete an instrument from the track
- As a user, I want to be able to modify the notes being played each instrument
- As a user, I want to be able to change the voice of each instrument (change the MIDI sound being played)
- As a user, I want to be able to change the tempo of the beat
- As a user, I want to be able to play the beat and hear it loop repeatedly
- As a user, I want to have the option to save my work
- As a user, I want to have the option to load a previously saved beat

## Instructions for Grader
- To add a new instrument to the track, press the "Add Instrument" button at the bottom of the screen
- To change the instrument's voice, click the "Change Instrument" button next to the row for a given instrument
- To change which notes an instrument plays on, click on the JToggleButtons - on means sound, off means no sound
- To play the beat, press "Play"
- The visual component of the project is the image of the drum machine and drum kit to the right hand side of the screen
- To save the file, press "Save" at the bottom
- To load the most recent saved file, press "Load" at the bottom

## Phase 4: Task 2
- NOTE: because of some idiosyncracies about how MIDI sequencers work in Java, my design does not add the Instrument objects to the DrumTrackList until the play button is pressed. That's why in the EventLog, the events in which the instruments are created and their features are changed happen before we actually add it to the track list: the instrument is created first, and only once the play button is pressed is it added to the TrackList so that it may be converted into a MIDI file.

Thu Apr 13 16:26:42 PDT 2022  
New instrument created  
Thu Apr 13 16:26:43 PDT 2023  
Set instrument number to 54  
Thu Apr 13 16:26:45 PDT 2023  
Set instrument notes to x-------  
Thu Apr 13 16:26:45 PDT 2023  
Set instrument notes to x-x-----  
Thu Apr 13 16:26:45 PDT 2023  
Set instrument notes to x-x-x---  
Thu Apr 13 16:26:46 PDT 2023  
Set instrument notes to x-x-x-x-  
Thu Apr 13 16:26:47 PDT 2023  
New instrument created  
Thu Apr 13 16:26:48 PDT 2023  
Set instrument number to 55  
Thu Apr 13 16:26:49 PDT 2023  
Set instrument notes to -x------  
Thu Apr 13 16:26:50 PDT 2023  
Set instrument notes to -x-x----  
Thu Apr 13 16:26:50 PDT 2023  
Set instrument notes to -x-x-x--  
Thu Apr 13 16:26:51 PDT 2023  
Set instrument notes to -x-x-x-x  
Thu Apr 13 16:26:52 PDT 2023  
Added instrument number 54 playing x-x-x-x- to the track list  
Thu Apr 13 16:26:52 PDT 2023  
Added instrument number 55 playing -x-x-x-x to the track list  
Thu Apr 13 16:26:52 PDT 2023  
Playing...  
Thu Apr 13 16:26:54 PDT 2023  
Stopped playing  
Thu Apr 13 16:26:55 PDT 2023  
Saved file to JSON  

## Phase 4 Task 3
The aforementioned idiosyncrasy about the MIDI library means that my UI adds each of the instruments associated with 
ToggleButtonRows to the DrumTrackList only *after* the play button is pressed. This is far from an ideal design. For one
thing, it required me to create a new DrumTrackList very time the play button is pressed, and then loop through each 
ToggleButtonRow to add the instrument associated with that row to the new list. This results in unnecessary duplication, 
as instruments which may already have been present in the old DrumTrackList still need to be added once again to the new 
one before it can be played. This design also resulted in a bug in which a track couldn't be saved unless it was played 
first, and the only way I could resolve this bug was by implementing the same loop through the ToggleButtonRows in the 
save function, resulting in duplicated code .

The design would be vastly improved if the instrument was added to the DrumTrackList at the same time as it was created, 
and then this instrument was edited in real time as the user pressed the toggle buttons in the ToggleButtonRow 
associated with it. There must be a way to implement this given the MIDI library, but despite my trying several times
to get it to work, I kept running into errors. If I had more time, I would like to fix this design flaw.