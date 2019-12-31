# notebot-api 1.0.0

## Usage
To use the `notebot-api` in your project, simply add the JitPack repository to your build file:
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

And add `in.jord:notebot-api:1.0.0` as a dependency:
```groovy
dependencies {
    implementation "in.jord:notebot-api:1.0.0"
}
```

Then, create a [NoteBot](https://github.com/Jordin/notebot-api/blob/master/src/main/java/in/jord/notebot/api/NoteBot.java) instance with your desired instrument set, for example:
```java
NoteBot<MinecraftInstrument> notebot = new Notebot<>(MinecraftInstrument.class);
NoteBot<MinecraftInstrument> notebot = new MinecraftNoteBot();
```

Songs can be programmatically created using [Note](https://github.com/Jordin/notebot-api/blob/master/src/main/java/in/jord/notebot/api/action/Note.java) and [Rest](https://github.com/Jordin/notebot-api/blob/master/src/main/java/in/jord/notebot/api/action/Rest.java) actions.
For example:
```java
List<NoteBotAction<MinecraftInstrument>> actions = new ArrayList<>();
actions.add(Note.of(MinecraftInstrument.BIT, 0));
actions.add(Note.of(MinecraftInstrument.BIT, 1));

actions.add(Rest.of(10));
actions.add(Note.of(MinecraftInstrument.FLUTE, 5));
actions.add(Note.of(MinecraftInstrument.GUITAR, 6));
```

With a [NoteBot](https://github.com/Jordin/notebot-api/blob/master/src/main/java/in/jord/notebot/api/NoteBot.java) instance, songs may be read and written in the following ways:

```java
NoteBotSong<?> song = noteBot.read(file);
NoteBotSong<?> song = noteBot.read("Song Name", customInputStream);

noteBot.write(file, song);
noteBot.write(file, actions);
```

Complete examples can be found in the [NoteBotTest](https://github.com/Jordin/notebot-api/blob/master/src/test/java/in/jord/notebot/api/NoteBotTest.java).

## Song Format

notebot-api uses a byte-aligned binary format to store song data, where all data types are assumed to be unsigned. 
Adhering to the standard file format ensures wide-spread compatibility and the ability to convert to different file formats in the future.
Currently, two NoteBot actions are defined: `note` and `rest`. These actions are encoded in the file in the following format:
```
X??? ???? ...
```
where `X` is the `action-id`  

Note: Actions are currently either `8-bit` or `16-bit`.

NoteBot songs are encoded as a finite stream of actions.

---

### Note
The `note` action is a `16-bit` action given an `action-id` of `0`. This leads to the following encoding format:
```
0--- IIII ---N NNNN
```
where `I` denotes the `instrument-id`  
and `N` denotes the `note-id`  
and `-` denotes an unused bit (`0`)

Note: the `note` action contains 6 unused bits. These bits will either be used in a future `1.X.Y` version for data such as `track-ids`, or will be ignored during a conversion to a future `2.0.0` version. 
As such, these bits should not be used.

---

### Rest
The `rest` action is an `8-bit` action given an `action-id` of `1`. This leads to the following encoding format:
```
1DDD DDDD
```
where `D` denotes the `duration` of the rest

Note: the rest duration must be between `0` and `127` time quanta, inclusively. If a rest duration longer than `127` time quanta is desired, consecutive `rest` actions must be used.
This design choice was dictated by the typical notebot environment, where `20` time quanta occur in `1` second, leading to a sufficiently large maximum rest duration of `6.35 second`. 
If a rest duration shorter than `0` time quanta is desired, please contact your time machine manufacturer.

## Example Song Files

A catalogue of songs encoded in this format can be found [here](https://github.com/Jordin/notebot-songs/tree/notebot-1.0.0) 
with a direct download [here](https://github.com/Jordin/notebot-songs/archive/notebot-1.0.0.zip). 
These songs are free to use, distribute, and modify, with one exception: do not sell them. 
