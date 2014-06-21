INTOPARK
========
#INSTALL#

	Things you need to develop this game.
- It is recommended for you to download JME SDK 3.0 But if you know what your doing you can find just the JARs too (I think).[here](http://hub.jmonkeyengine.org/downloads/)
- [Guava](https://code.google.com/p/guava-libraries/)
- [Guice](https://code.google.com/p/google-guice/)
- [JGraphT](http://jgrapht.org/)
- [Gson](https://code.google.com/p/google-gson/)
- [JUnit](https://github.com/junit-team/junit/wiki/Download-and-Install)
- [Mockito](https://code.google.com/p/mockito/)

#WHAT IS THIS#
This is my passion, a game called IntoPark. It is a theme park strategy game currently in developement. This is my first game.
Read more [here](http://arttu.me/)
#FEATURES#
- #UI#
	- TOP BAR []
	- Guest UI []
	- Building Selection UI []
	- Decoration Selection UI []
	- Shop Inspect UI []
	- Ride Inspect UI []
	- Terrain Editor UI []
	- Road Editor UI []
	- Settings UI []
	- Start Screen UI []
- #LOAD AND SAVE#
	- Ability to save games to files [x]
	- Ability load saved games from files [x]
	- Simple Save-Editor []
	- Functional Starting screen []
	- Save preferred settings [x]
	- Load preferred settings [x]
	- Save KeyBinds []
	- Load KeyBinds []
	- Achievements []
	- Save Terrain colors []
- #CONTROLS#
	- User friendly controls []
	- Smooth camera []
	- Ability to change KeyBinds and settings []
- #PARK#
	- Unique preset parks []
	- Ability to do semi-custom parks(change park settings) []
	- Competing against AI parks []
	- Tutorial []
	- Random events []
- #TERRAIN#
	- Terrain made of quads [x]
	- Terrain Editor [x]
	- Different types of terrain []
	- Raise and lower terrain [x]
	- Smoothing tool [x]
	- Sizeable brush on Terrain Editor [x]
- #ROADS#
	- Road Editor [x]
	- NPCs walk on road [x]
	- 2 types of road [x] (Normal road, Queue road)
	- Build road with mouse drag [x]
	- Different colors of roads []
	- Roads can contain lamps and benches []
- #GUESTS#
	- Guests spawning with different stats to park [x]
	- Guests can move along roads [x]
	- Guests vary sizes and colors [/] (yes, but not visually)
	- Guests interact with shops and rides [x]
	- Guests have emotions and act according to them []
	- Rare special guests []
- #SHOPS#
	- Buildable Shops
		- #FOOD/DRINK#
		- Cafe [x]
		- Nakk kiosk []
		- Candy Street []
		- Popcorn van []
		- Trach food []
		- Soda machine []
		- Milkshjake []
		- Chick em []
		- Meatballs []
		- Sweet fruits []
		- Wrap place []
		- Into's IceCream []
		- Pastry shop []
		- Nor ma'I food []
		- Sweet corn []
		- Chips and chicken []
		- #OTHER#
		- Balloon stand []
		- Can throw []
		- Info board []
		- News stand []
		- Joke vending maching []
		- Info kiosk []
		- Toilet []
		- Fast Joy []
		- Pick a rope []
                - Camera booth []
	- Shops sell items [x]
	- Customizable colors, shop name, product names... []
	- Shop upgrades []
	-
- #RIDES#
	- Buildable Rides
		- Archery range []
		- Chess Center []
		- Haunted house []
		- Pirate Ship []
		-
	- Rides that move [x]
	- Customizable colors, ride name, ride price... []
	- Functional enterances and exits [x]
	- Functional queue handling []
        - Inspectors need to inspect ride before it can be activated.
- #DECORATIONS#
	- Ability to build decorations []
	- Rotate them []
	- Lower and raise them []
- #DEMOLITION#
	- Demolish decorations []
	- Demolish roads []
	- Demolish rides [x]
	- Demilish shops []
- #SPECIAL EFFECTS#
    - Demolish explosions/smoke []
    - Day/Night cycle []
    - Lights to rides so it looks cool in night []
    - Emotion indicators? []


#Currently doing#

- Models for Shops, Rides, Guest.
- Refactoring guest queue to ride logic.
- Building selection/ride UI.

#THINGS TO DO LATER#

- Think of better names for shops and rides.
- Re-organize README.
- Window asking if you want to demolish building before demolishing it.
- Populate guest/shop/ride UI.
-

#KNOWN BUGS#

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


#ENCHANCING IDEAS#

- Various blockages like vegetation or rocks that you have to remove if you want to build something there