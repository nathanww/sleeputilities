SleepCue is written using MIT app inventor. SleepCue_public.apk is the compiled application whereas SleepCue_public.aia is the App Inventor source.

# Use
To run an experiment all your users will need to install **the sleepCue app** and **your experiment files**. The app is installed as a regular Android APK, either from
here or from the Play store at: *pending* Your experiment consists of a file called sleepConfig.txt which specifies which stimuli play when, and the sound files themselves.

Participants will be prompted to load an experiment when first starting the app (see below).O nce the experiment is loaded the app will request that participants set the volume of white noise and cue sounds (using a "calibration sound" specified in the experiment). Once the sounds are set, the participant can tap "Ready for sleep" to begin the protocol.

If a data server is specified in the experiment file, participants can also use the "send data" button on the first screen to send log files back to the experimenter.

## Installing an experiment

There are two ways to install an experiment:
* Manually copy a sleepConfig.txt file and your sounds onto a phone. If you do this you should place all the files in the "root" of the device (not in a folder)
* Deploy from a web server. In this case you put all your files in a zip archive (again in the root, not in a folder!) and host it online (GitHub works well for this). When the sleepCue app is opened on the phone, it will ask for a URL to the experiment .zi file and download it. This option is good for remotely deploying experiments without needing people to come into your lab.

You can test wither method using the stimulusfiles.zip file in this repository.

## Creating an experiment
Experiments consist of:
* Sound stimulus files
* An experiment script in the sleepConfig.txt file.

### Syntax of the sleepConfig file
Each line of the sleepConfig file corresponds to a different parameter. These are:

**Experiment/subject ID** This will set the name of the generated log files

**Onset time(minutes)** After the participant enters sleep mode, how long to wait before starting to play sounds

**Duration (minutes)** After sounds start, the length of time they will continue playing

**Inter stimulus interval (seconds):** How long to wait between each sound

**Calibration sound** Before starting sleep mode this d to let participants set the volume appropriately.

**Data destination URL** If this line contains a URL with http or https, the app will send all log files to a web server as an HTTP POST 
when the users clicks the "send data" button. It can be left blank to disable this function

**All subsequent lines** Filenames of the sounds to play, in order.

In place of a sound file, you can also use the special keywork "silent". This tells the phone to not play anything (essentially doubling the inter stimulus interval before the next sound)

## Sounds
Sounds are sound files in any format the Android device can play. This actually varies a bit by device but MP3 files are usually safe.
