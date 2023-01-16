
# AdaShip-Assignment

The challenge is to program a clone of the classic ‘Battleship’
game. AdaShip is a two-player, turn based game of sea warfare.
You and an opponent each place a number of ships on your own board,
and you then alternate turns "firing" torpedoes at each other’s
ships. The game is won when one player has destroyed/sunk all
of the other player’s ships.


## Screenshots

![ss1](https://user-images.githubusercontent.com/20933329/212502975-bf584236-a14e-4135-92c5-9b313bb27063.png)
![screenshot ship](https://user-images.githubusercontent.com/20933329/212502986-77cf68c6-0361-40f5-8ad0-70e9181d987f.png)
![ss3](https://user-images.githubusercontent.com/20933329/212502988-c97c08e4-4a4a-4b55-acb2-adb827052d82.png)
![ss4](https://user-images.githubusercontent.com/20933329/212502993-819b4093-0251-40e6-9673-d6ba3b39f1bf.png)
![ss5](https://user-images.githubusercontent.com/20933329/212502994-062110c8-2be8-4da0-8592-dbf549707421.png)
![ss6](https://user-images.githubusercontent.com/20933329/212503055-e911c88e-8f47-437d-9838-a51de2bd1f2f.png)

## Decomposition of the overall problem according to the requirements

The MVP should support the following features:

- [x]  There should be 2 boards :a shipboard and a targetBoard.The first one should contain deployed ships and the latter  is for the outcome the targeted coordinates and the outcome of the fire (HIT or MISS).
- [x]  Columns should be clearly indicated with letters and rows with numbers.
- [x]  To reference a cell in a table  coordinate system should be used.There should be a letter followed by a number. For example A1,B8,D9
- [x]  Before games starts, players are supposed to place their ships on the table.
- [x]  Ships can be placed horizontally or vertically only  on the shipboard.
- [x]  The ships have different sizes  and proper deployment requires that all the ships are placed inside the board.
- [x]  Ships must not overlap and neighbouring cells must be left empty. By neighbours  I mean diagonal as well as orthogonal nearest cells.
- [x]  The default size of the grid is 10 x 10.
- [x]  By default, there are five types of ships:
  >
      1. Carrier - Length 5
      2. Battleship - Length 4
      3. Destroyer - Length 3
      4. Submarine - Length 3
      5. Patrol Boat - Length 2
- [x]  It should be a two player game where one of them is a computer.
- [x]  Game is played in turns. Turn is ended with a button click. During a turn a player fires a torpedo by using a coordinate.
- [x]  Opponent indicates of the fire result. (Hit or Miss)
- [x]  All called fired torpedos should be recorded by a player.
- [x]  The game lasts as long as one of the players looses all its ships, hence becoming a looser but only a winner is announced.
- [x]  Providing a detailed readme.md file.
- [x]  Game configuration should be read from a file. It describes the length , name of the ship used in the game as well the board size.
- [x]  There should be a  user interface, which allows to choose between play and quit
- [x]  Once a  player correctly positions a ship on the board, the occupied  coordinates should be displayed on the screen
- [x]  Indication of the placed and non-placed ship.
- [x]  Auto placement function of the non-placed ships.
- [ ]  Auto placement function of all ships.
- [ ]  Quit game function
- [x]  Reset shipboard function
- [x]  If computer is a player it also sets up its own shipboard
- [x]  Support for rational number of the ships  read from a file
- [x]  There should be a correct information displayed for the player during the game play.
- [x]  During a game play there should be a player's own and up to date shipboard and target booard displayed
- [ ]  Improved ‘computer player’ targeting problem
- [ ]  Improved game play “hidden mines” variation: search and targeting algorithm
- [x]  Add functionality allowing the current ‘player’ (player orcomputer) to ‘fire’ one torpedo per their remaining ships)
- [ ]  Hidden mines functionality. If Opponent hit the mine  that location and 9 surrounding it "explode"

My own requirements:
- [x] Players must use only a mouse or a touchscreen.
## Development strategy

After getting familiar with a project brief I decided to remake the game a little bit. So my aim was to create the required game but
I did not want the players to touch the keyboard. All the functionality must be done with the mouse or finger only.
Instead of writing the coordinate of the shipboard, I just press that cell with my mouse click or a finger touch  and torpedo should fire.
Instead of writing the coordinates of my ship placement during the set-up phase, I just grab the ship with my mouse click and
drag it accross the board to the desired place. Initially  that functionality was my main goal.

To achieve that mention user interaction I chose LibGdx framework, because I had some experience with it earlier on ,
it uses java language which is main programming language at my work. But the main reason for the framework was the support for Scene2D.
It is a perfect solution for developing such a game.

Scene2d is a 2D scene graph for building applications and UIs using a hierarchy of actors.
It provides the following features:
- Rotation and scale of a group is applied to all child actors. Rotation is needed for the ships. The idea is the user can  rotate a ship with a right mouse click.
- Simplified 2D drawing via Texture. Each actor draws in its own un-rotated and unscaled coordinate system where 0,0 is the bottom left corner of the actor.
  This feature is going to be heavily used across the application. The Actors have a texture class as a depenedency.

  In my game  an image with  a colour and a size of 1 by 1 pixel   is created  programmatically
  and a texture loads that pixel of colour into GPU memory. During the set-up phase not placed ships are 'in training' mode  and have brown colour.Once a player properly drops
  a ship on the shipboard, it changes a colour into green, hence becomes 'in deployment' mode. It is an interactive helpful feature ,the player gets a response from the system.
  This feedback was also helpful during the test - phase. Becuase a colour change of the ship is completely independent from the logic of the game I could test
  my ship placement algorithms by observing its transition.  During a play- phase ships change  color as well. Once it is hit by an opponent the part of the ship which occupies the targetted coordinate becomes  
  red  and ship transions to 'damaged mode'. Opponent knows if the ship is in'sunk' mode because the coordinates turn into dark blue.

- Hit detection of rotated and scaled actors. Each actor determines if it is hit using its own un-rotated and unscaled coordinate system.

  That feature I use to determine the coordinates of the ship on the shipboard during a set-up phase when a player position the ship on the shipboard.The ship is built from the ShipUnit object and
  a Shipboard is built from GridUnit object. GridUnit and ShipUnit have the same size and base class: UnitActor, which has dependency on Texture ( image with a color).Origin of
  the ShipUnit is a point around which the ship rotates, it is in the middle of their size. For example a 4 length ship has 4 ShipUnits and 4 origins.
  When the ship is dropped,  the origin of each ShipUnit is used by HitDetection to determine which grids of the Shipboard are covered by the ship. Hence, I am getting coordinates of the deployed ship
  which are saved in the shipboard  object.
  Another usage of the hit detection is during auto-fire. The coordinate Of the shipboard is randomly chosen which then is feeded to the hit detection  function to determine the random cell
  which is going to be targeted. And the last useage of the hit detection is  during the autoplacement of the ship.

- Routing of input and other events to the appropriate actor. The event system is flexible, enabling parent actors to handle events before or after children.
  The main classes of the  Scene2D are Actors ,Groups and a Stage. Stage is an InputProcessor. When it receives input events, it fires them on the appropriate actors.
  Hit detection decides which actor it is. Stage keeps track of all actors and  a  convenient method of the stage , or any group is findActor.
  This method helps to reduce the verbosity of the code. I did not have to carry over a reference of the actor to  other obects. I just needed a name of the desired actor to get a reference back.
  And setting a name on the actor is easy just setName.


My overall aproach   followed   divide-and-conquer aproach. I kept  breaking  down a problem into  more sub-problems
of the same or related type  until these became simple enough to be solved directly. So I decomposed the whole project
into key epic style tasks.
#### 1. Initial setup ####
Because of using the frmework I had to follow the documentation how to set up my initial code. It involves using
a LibGDX Project Generator Tool a software which need to be installed.

![tool](https://user-images.githubusercontent.com/20933329/212529884-5a3feaea-383d-46d3-a2db-5fda0aa61561.png)

I checked in only Desktop as the supported platform. The game is going to be developed for the Windows operating system.
If I change my mind later on  I can alter the gradle settings file.  Libgdx uses as the built tool and  dependency management    Gradle.
Freetype is a development  library which renders text into bitmap. Thanks to this library  I announce the  winner
with a big eye-catching font at the end of the game.

#### 2. Creating visuals and basic model  classes ####

Basic model classes are impleneted. Board, which is goin to store the placement of the ships. The class is going to  have a data-base functionlity.
I needed to have an answer  on  the question if  if a cell is ocupied or is still free. Then If it

#### 3. Implementing  user interaction with ships ####
Functionality  like: picking a ship with a  mouse click,  dragging a ship across the board, dropping a ship in a desired location  when the mouse button is released.
Also rotation functionality of the ships was introduced at this point. With a right click of the button player can rotate  a ship by 90 degrees.
#### 3. Enhancing user experience -ship alignment ####
Functionality  like: Alligning a ship with table cells. A ship will stay at the position where a user dropped it. Almost everytime it does not align with the cells.
To make it look good that alligning functionality was requiered.
#### 4. Connecting model with visuals.  ####
Set-up phase logic of the game can determine if player properly set-up the ship
#### 5 Adapting model to reading a set-up file.  ####
Introducing a GameConfig object as a singleton. It creates a board with  the size read from the file. It also initializes
2   objects of type player:player 1 and player 2. The application will often refer to these objects through GamePlayConfig singleton class. Using a singleton I make sure
I will always have the same references of the player, opponent and board object. On top of that I register properties of the ships : name and length in a boat object, which is accessed through sinleton as well as a list.



## Authors

- Marcin Wasilewski (https://www.github.com/kasandrop)


## UML

![model 110](https://user-images.githubusercontent.com/20933329/212606061-5d0b47e8-a816-4287-bca1-9c52a73b239b.png)
![Untitled Diagram drawio](https://user-images.githubusercontent.com/20933329/212605915-90d7ed5b-274f-42b1-84ee-7022979469dc.png)

![visuals 111](https://user-images.githubusercontent.com/20933329/212606245-27e56896-0408-4097-b063-24784b3eec21.png)