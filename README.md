# Weedim Virtual Piano
This simple Android App was developed as project for an exam. Its aim is to both be an effective virtual piano, and to be a tool which can be combined with the Weedim Desktop app, making your life easier to build simple mono-channeled MIDIs.

# How to install
The folder is an Android Studio Project developed on Windows. It should be fine to clone the whole repo as Android Studio project.

# How to use
First, make sure you have Weedim running on any device (in case you're not, get the repo here: https://github.com/MassimoTrionfante/Weedim). Keep in mind the IP address and the port you choose for running the Weedim Web Services.

The app's first activity has 3 buttons:
- START: starts the app right away. It's the button to press if you want to use the app as a virtual piano only;
- SETTINGS: brings you to an activity which makes you able to set the URL of the machine where you are running Weedim's Web Services. For now, this is the recommended alternative if you want to use the app together with Weedim's web interface, since Weedim doesn't run on a static URL address yet;
- EXIT: closes the app in the clean way;

Once you get in the main activity, your screen will automatically go in landscape mode, and you'll see a piano with many buttons on it.
In the blue bar above the piano, there are the following functionalities:
- The red minus and the green plus circles shorten and widen a note length. Note lengths go from 32th (shortest) to 1th (longest). Current length is displayed next to "Len:";
- The red curvy arrow performs an undo, just like in the desktop app. Works only if you recorded a notes sequence;
- The play button plays the recorded note sequence; it obviously does nothing if you didn't record anything yet;
- The red 3d box adds a rest, like in the desktop app. Rests are "notes without volume". It's used only in recording mode, and the length used will be the one displayed next to "Len:";
- The big red writing "OFF" is a toggle that lets you pass from free mode (OFF) to record mode (ON). Once it's on, any tapped note with a certain length (again, specified next to "Len:") will be saved;
- "<<" and ">>" arrows on the sides of the bar will increase and decrease the current octave (showed after "Oct:").

On the top of the screen, you'll see other 3 buttons:
- The arrow button on the leftmost simply brings you back to the title screen;
- The cog button on the rightmost simply brings you back to the settings screen, making you able to reset the URL;
- The big button in the middle is the button you press when sending your recorded tape to Weedim's database. Once you recorded something, after you're satisfied with the result, press it.

When you send a recorded tape, if everything went well, you'll see a sequence of numbers in red. That's the "session number" you have to input in Weedim's Web Interface. Once you input the correct number, the web app will load again, starting with the sequence you inputted from your phone!

# Known bugs
- For now, passing recorded tapes in the web app of Weedim works with inner structures, but you won't see notes graphically. I'm currently working on figuring out how to do so.

# Special thanks
The project uses a driver built by billthefarmer. Link to his repo here: https://github.com/billthefarmer/mididriver
