Calvin Mak/ cmak@mymail.mines.edu
Assignment 2 - Global Thermonuclear War
The Program has 4 "screens". It starts on the welcome screen (fragment).
There is a menu or provided buttons that navigate to the three pages: Playing the game, Game History, and preferences. The final "Exit" Button closes the app.

There is also a top menu bar that provides the same functionality.
The Play Game screen is a TicTacToe game. The starting tile-player is random. 
Games are saved into the database and viewed in history.

In the Preferences screen, users can choose 1 player mode against 2 AI settings or 2 player mode.
The back arrow will automatically save the settings.
The Clear button will clear all the data in the database.
The history displays all the past games played. There is a filtering icon along with menu options on the top. The filtering icons can filter history based on the selections.
SharedPreferences is used to internally store the player preferences in a map.

To run the code, simply run the app on an android or emulator.

Current bugs: If you click a filtering option and not the checkbox, it will filter but it will not show the checkbox as selected.

1) The hardest part of the navigation was sharing information between fragments.
2) While using the database to store preferences would help in persistence, there is no reason to utilize a full database for one setting of preferences. It would simply be one updated entry. It is also way more work than necessary.

I applied the filtering XC.