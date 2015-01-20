### Unnamed Platformer  
A 2D platform game with a built-in level editor.  
Programmed in Java with LWJGL and Swing.

![Screenshot](scr/scr0019.png "Screenshot")

### Features
- Ability to create, edit, and save levels
- Variable jump height
- Breakable boxes
- Springs
- Hazards (spikes, magma)
- Water (slow region)
- Electric field (fast region)
- Windowed/fullscreen
- In-game control configuration

### Technical Info
- Large levels with many objects stay fast by using iterative spatial hashing
- Saved levels are stored as a serialized list of game objects with various properties (object type, texture, start location, size)

### Plans
- Add more gameplay mechanics
- Improve UI appearance and functionality

### Misc
Graphics and sound are all CC0-licensed. Most are from opengameart.org.  
License information for library dependencies can be found in /docs/licenses/
