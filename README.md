# Weedim Virtual Piano
This simple Android App was developed as project for an exam. Its aim is to both be an effective virtual piano, and to be a tool which can be combined with the Weedim Desktop app, making your life easier to build simple mono-channeled MIDIs.

# How to install Weedim Virtual Piano
This repo is the content of the generic Android Studio Project. Simply clone the whole repo and open it using Android Studio. In case you get issues about "deleting old versions of the app", simply click OK, and it should install anyways.

# How to use
First, make sure you have Weedim running on any device (in case you're not, get the repo here: https://github.com/MassimoTrionfante/Weedim). Keep in mind the IP address and the port you choose for running the Weedim Web Services.

The app's first activity has 3 buttons:
- START: starts the app right away. It's the button to press if you want to use the app as a virtual piano only;
- SETTINGS: brings you to an activity which makes you able to set the URL of the machine where you are running Weedim's Web Services. For now, this is the recommended alternative if you want to use the app together with Weedim's web interface, since Weedim doesn't run on a static URL address yet;
- EXIT: closes the app in the clean way;

Once you start, you'll see the interface below. Refer to red numbers and the list below to see which button does what:
![](https://github.com/MassimoTrionfante/Weedim_VirtualPiano/blob/master/img/preview1.png)
- 1: get back to title screen;
- 2: send recorded notes sequence to Weedim web services;
- 3: get into the settings view;
- 4: lower octaves;
- 5: length of the recorded note; for example, if it's "4th" and you record a note, that note will be saved as a 4th note;
- 6: buttons that shorten and widen note length;
- 7: undo, deletes the last recorded note;
- 8: play the recorded sequence of notes;
- 9: add a rest in the recorded sequence;
- 10: recording toggle, "ON" means it's in recording mode, "OFF" means it's in free mode;
- 11: upper octaves;
- 12: upper piano row;
- 13: lower piano row.

If you properly set up the URL of your Weedim instance, you can press on button "2" to send your sequence (if you recorded a sequence). Once you send it, a message with a red number will appear. The number in question is the "session number", it's the number you have to input in the Weedim's desktop interface: once you input it, the page will reload, starting with the notes you recorded from your mobile device!

# Known bugs
While the app does its job properly, the web interface could get randomly buggy when you use it to load recorded tapes from your phone. For more details, please refer to "Known bugs" in Weedim's repo here https://github.com/MassimoTrionfante/Weedim

# Special thanks
The project uses a driver built by billthefarmer. Link to his repo here: https://github.com/billthefarmer/mididriver
