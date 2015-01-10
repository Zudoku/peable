Into Park
========

###Install instructions###


Download JME SDK 3.0 .[here](http://hub.jmonkeyengine.org/downloads/)
Clone this project and import it to the SDK.
Download the following libraries that are needed to compile the game.

- [Guava](https://code.google.com/p/guava-libraries/)
- [Guice](https://code.google.com/p/google-guice/)
- [JGraphT](http://jgrapht.org/)
- [Gson](https://code.google.com/p/google-gson/)
- [JUnit](https://github.com/junit-team/junit/wiki/Download-and-Install)
- [Mockito](https://code.google.com/p/mockito/)

Import the libraries to the project.

Good to go!

###What is Into Park?###
Game called IntoPark. It is a theme park creation game/simulator currently in development. Written in Java utilizing JMonkey Engine 3.
Read more [here](http://arttu.me/)

####Things that need to be done####

All the subgoals have a score of 1-5 of how badly it needs to be done. 1 being the most important. This list is probably not going to make alot of sense,
so asking me what it means is recommended so that you don't waste your time.

- UI needs to be refined. All the UI art is currently not very good / temporary placeholders. [2]
- Simple Save-Editor [5]
- Functional Starting screen [3]
- Save KeyBinds [2]
- Load KeyBinds [2]
- Achievements [4]
- Save Terrain colors [3]
- User friendly controls [3]
- Smooth camera [3]
- Ability to change KeyBinds and settings [2]
- Unique preset parks [3]
- Ability to do semi-custom parks(change park settings) [3]
- Competing against AI parks [4]
- Tutorial [4]
- Random events [3]
- Different types of terrain [4]
- Different colors of roads [4]
- Roads can contain lamps and benches [3]
- Guests vary sizes and colors [2]
- Guests have emotions and act according to them [3]
- Rare special guests [4]
- Ability to build decorations [3]
- Rotate decorations [4]
- Lower and raise them [4]
- Demolish decorations [4]
- Demolish roads [2]
- Demolish explosions/smoke [4]
- Day/Night cycle [4]
- Lights to rides so it looks cool in night [4]
- Emotion indicators? [4]
- Models for Shops, Rides,NPC's, decorations. [1]
- Think of better names for shops and rides. [3]
- Window asking if you want to demolish building before demolishing it. [3]
- Populate guest/shop/ride UI's. [2]

###Architectural TODO-list###

- Refactor listenCreate***Event's to be on right location. (Move them to right child-managers).
- Change LoadPaths class to load filepaths from a separate file.
- Redo window opening & closing interface

###Known Bugs###

- Creating a new map crashes/malfunctions game.
- Decorations are a mess. (should be completely re-done).
- When placing shops, sometimes you need to click twice.
- Some models are not facing the right way.
- Roads can be built through terrain.
- Ride price change slider has floating point rounding error.
- Rides don't cost money.
- Terrain appears to be blue when game is started.
- Placing objects out of map can cause it to crash.
- Terrain editor drag sometimes frees.
- Guests walk underground when climbing up roads.