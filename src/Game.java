import java.util.Random;
import java.util.Scanner;

class Game {
    static int dungeonRows = 4;
    static int dungeonColumns = 4;
    static int lesserMonsters = 2;
    static int dragon = 1;

    public static void main(String[] args) {
        Scanner inputUser = new Scanner(System.in);
        char userSelection;
        // This array is the actual 2D grid
        int [][] visited = new int [dungeonRows][dungeonColumns];
        visited[0][0] = 0;
        boolean gameOver = false, isValid = false;
        // Array of coordinates where the user currently is
        int [] currentCoordinates = {0, 0};
        int hp = 10;
        // The previous coordinates, in case user wants to run away
        int [] lastCoordinates = new int [2];
        boolean swordAquired = false;
        boolean shieldAquired = false;
        printTitle();
        printIntroduction();
        // Gets the coordinates of the shield, sword and dragon
        int[][] monstersLocation = getMonstersLocation();

        while (!gameOver) { //While the game is not over
            prettyPrint( visited, currentCoordinates[0], currentCoordinates[1]);
            // Menu for moving in the dungeon options
            displayMenuMoving(currentCoordinates, hp, swordAquired, shieldAquired);
            userSelection = inputUser.next().charAt(0);
            boolean isValidSelection = checkValidSelection(userSelection);
            if (isValidSelection) {
                // Checking if it in bounds
                boolean isInBounds = isInBound (userSelection, currentCoordinates, visited);
                if (isInBounds) {
                    switch (userSelection) {
                        case 'l':
                            // In case the user wants to go back, the last coordinates are preserved
                            lastCoordinates[0] = currentCoordinates[0];
                            lastCoordinates[1] = currentCoordinates[1];
                            // Changes the coordinates, so that the user can move
                            currentCoordinates[1] = currentCoordinates[1] - 1;
                            break;
                        case 'r':
                            lastCoordinates[0] = currentCoordinates[0];
                            lastCoordinates[1] = currentCoordinates[1];
                            currentCoordinates[1] = currentCoordinates[1] + 1;
                            break;
                        case 'u':
                            lastCoordinates[0] = currentCoordinates[0];
                            lastCoordinates[1] = currentCoordinates[1];
                            currentCoordinates[0] = currentCoordinates[0] - 1;
                            break;
                        case 'd':
                            lastCoordinates[0] = currentCoordinates[0];
                            lastCoordinates[1] = currentCoordinates[1];
                            currentCoordinates[0] = currentCoordinates[0] + 1;
                            break;
                    }
                    // HERE IS WHERE MY CODE SHOULD BE B/C VALID OPTION AND IN BOUNDS
                    // Room with monster guarding the shield based on the getMonstersLocation method
                    if (currentCoordinates[0] == monstersLocation[0][0] && currentCoordinates[1] == monstersLocation[0][1]) {
                        System.out.println("The mythical shield is located in this room!\nA monster is guarding it.");
                        userSelection = 'a';
                        while (userSelection == 'a' && hp > 0) {    // Takes care of when the user enters do nothing over and over
                            displayMenuEncounter();
                            // The user enters a character
                            userSelection = inputUser.next().charAt(0);
                            if (checkValidEncounter(userSelection)) {
                                switch (userSelection) {
                                    case 'a':
                                        hp--; // decrease health points by one.
                                        System.out.println("You have chosen to do nothing.\nThe monster hit you.\nYour remaining health points are: " + hp);
                                        break;
                                    case 'b':
                                        hp--;
                                        System.out.println("You have chosen to hit the monster.\nThe monster hit you.\nYou have defeated the monster and acquired the shield!");
                                        shieldAquired = true;
                                        visited[ currentCoordinates[0] ][ currentCoordinates[1] ] = 2; // For pretty print
                                        break;
                                    case 'c':
                                        System.out.println("You have chosen to run away.");
                                        // Going back to the previous room
                                        currentCoordinates[0] = lastCoordinates[0];
                                        currentCoordinates[1] = lastCoordinates[1];
                                        break;
                                }
                            } // end ifValidEncounter
                        } // End while
                    }
                    // Room with monster guarding the sword
                    else if (currentCoordinates[0] == monstersLocation[1][0] && currentCoordinates[1] == monstersLocation[1][1]) {
                        System.out.println("The mythical sword is located in this room!\nA monster is guarding it.");
                        userSelection = 'a';
                        while (userSelection == 'a' && hp > 0) {  // for when the user enters do nothing over and over
                            displayMenuEncounter();
                            userSelection = inputUser.next().charAt(0);
                            if (checkValidEncounter(userSelection)) {
                                switch (userSelection) {
                                    case 'a':
                                        hp--;
                                        System.out.println("You have chosen to do nothing.\nThe monster hit you.\nYour remaining health points are: " + hp);
                                        break;
                                    case 'b':
                                        hp--;
                                        System.out.println("You have chosen to hit the monster.\nThe monster hit you.\nYou have defeated the monster and acquired the sword!");
                                        visited[ currentCoordinates[0] ][ currentCoordinates[1] ] = 3; // For pretty print
                                        swordAquired = true;
                                        break;
                                    case 'c':
                                        System.out.println("You have chosen to run away.");
                                        // Moves back to the last coordinates
                                        currentCoordinates[0] = lastCoordinates[0];
                                        currentCoordinates[1] = lastCoordinates[1];
                                        break;
                                }
                            } // end ifValidEncounter
                        } // End while
                    }
                    // Room with dragon
                    else if (currentCoordinates[0] == monstersLocation[2][0] && currentCoordinates[1] == monstersLocation[2][1]) {
                        System.out.println("The dragon is located in this room!");
                        userSelection = 'a';
                        while (userSelection == 'a' && hp > 0) {  // for when the user enters do nothing over and over
                            displayMenuEncounter();
                            userSelection = inputUser.next().charAt(0);
                            if (checkValidEncounter(userSelection)) {
                                switch (userSelection) {
                                    // DO NOTHING
                                    case 'a':
                                        if (!swordAquired && !shieldAquired) {
                                            missingItem("sword and shield");
                                            hp = 0; // Game over since user is missing items
                                        }
                                        else if (!swordAquired) {
                                            missingItem("sword");
                                            hp = 0; // Game over
                                        }
                                        else if (!shieldAquired) {
                                            missingItem("shield");
                                            hp = 0; // Game over
                                        }
                                        else {
                                            hp -= 2; // Reduce two points if user does not do anything but has shield and sword
                                            System.out.println("You have chosen to do nothing.\nThe monster hit you.\nYour remaining health points are: " + hp);
                                        }
                                        break;
                                    // HIT DRAGON
                                    case 'b':
                                        if (!swordAquired && !shieldAquired) {
                                            missingItem("sword and shield");
                                            hp = 0;
                                        }
                                        else if (!swordAquired) {
                                            missingItem("sword");
                                            hp = 0;
                                        }
                                        else if (!shieldAquired) {
                                            missingItem("shield");
                                            hp = 0;
                                        }
                                        else {
                                            System.out.println("You have chosen to hit the dragon.\nThe dragon hit you.\nYou have defeated the dragon!");
                                            System.out.println("Congratulations, you have survived this quest!");
                                            System.out.println("You have saved the village but died in the process");
                                            hp = 0; // User dies anyway
                                        }
                                        break;
                                    // RUN AWAY
                                    case 'c':
                                        System.out.println("You have chosen to run away.");
                                        // Going back to the previous room
                                        visited[ currentCoordinates[0] ][ currentCoordinates[1] ] = 4; // For pretty print
                                        currentCoordinates[0] = lastCoordinates[0];
                                        currentCoordinates[1] = lastCoordinates[1];
                                        break;
                                }
                            } // end ifValid encounter
                        }
                    }
                    else {
                        // Do nothing, empty room
                        System.out.println("\nYou have entered an empty room, looks like there's nothing to do here\n");
                    }
                }// End if isInBounds
                else {
                    System.out.println("You are out of bounds. Try again\n");
                }
            }// end if is valid selection
            else {
                System.out.println("\nOption valid. Try again\n");
            }
            gameOver = isGameOver(hp); // Depending on the health points, it repeats or not
        }
        //Your code ENDS HERE
    }

    //Your methods START HERE
    //********************************* Print Introduction ********************************
    public static void printIntroduction() {
        System.out.println("Oh no!\nA dragon is killing our sheep and scaring our villagers, please help the knight put a stop to this nightmare!\n");
    }
    //************************ Menu for moving **************************************
    public static void displayMenuMoving (int [] coordinates, int hp, boolean swordAquired, boolean shieldAquired) {
        String shieldStatus = "no", swordStatus = "no";
        if (shieldAquired)
            shieldStatus = "yes";
        if (swordAquired)
            swordStatus = "yes";
        System.out.println("\nYou are in cell " + coordinates[0] + ", " + coordinates[1]);
        System.out.println("Your remaining health points are: " + hp);
        System.out.println("Shield acquired: " + shieldStatus + "\nSword acquired: " + swordStatus);
        System.out.println("\nUse L to move left\nUse R to move right\nUse U to move up\nUse D to move down\n");
    }
    //***************************** Menu for encountering monsters ***************************
    public static void displayMenuEncounter () {
        System.out.println("What would you like to do:\na) Do nothing\nb) Hit the enemy\nc) Run away");
    }
    //************************* Check valid Movement method ***********************************
    public static boolean checkValidSelection (char optionUser){
        //Turn to lower case first
        optionUser = makeLowerCase(optionUser);
        if (optionUser == 'l'|| optionUser == 'r'|| optionUser == 'u'|| optionUser == 'd')
            return true;
        return false;
    }
    //**************************** Check valid option Encounter *******************************
    public static boolean checkValidEncounter (char userSelection) {
        if (userSelection == 'a' || userSelection == 'b' || userSelection == 'c')
            return true;
        return false;
    }
    //******************************** Check if in bounds method *******************************
    public static boolean isInBound (char direction, int [] coordinates, int[][]dungeon) {
        if (direction == 'l' && coordinates[1] != 0) // Cannot substract one to columns if it is already 0
            return true;
        if (direction == 'r' && coordinates[1] != (dungeon[0].length - 1)) // As long as it is not at the end, user can move right
            return true;
        if (direction == 'u' && coordinates[0] != 0)
            return true;
        if (direction == 'd' && coordinates[0] != (dungeon.length - 1))
            return true;
        // If the condition is not met for any option, then it is definitely out of bounds
        return false;
    }
    //****************************** Make user selection lower case *******************************
    public static char makeLowerCase (char letter) {
        if (Character.isUpperCase(letter))
            return Character.toLowerCase(letter);
        else
            return letter;
    }
    //***************************************** isGameOver ****************************************
    public static boolean isGameOver (int hp) {
        if (hp <= 0) { // Health points of 0 is what determines wether the game is over or not
            System.out.println("\nGame Over...");
            return true;
        }
        else {
            return false;
        }
    }
    //********************************** Missing item dragon ****************************************
    public static void missingItem (String item) {
        System.out.println("You are missing the " + item + ".\nThe dragon killed you...");
    }
    //********************************* Pretty print ***********************************
    private static void prettyPrint(int[][] visited, int currentRow, int currentColumn){
        for(int i = 0; i < visited.length; i++){
            System.out.println(" ");
            for(int j = 0; j < visited[0].length; j++){
                if(i == currentRow && j == currentColumn){
                    System.out.print("\tC"); // Character
                }else if(visited[i][j] == 0){
                    System.out.print("\t?");
                }else if(visited[i][j] == 2){ // Shield
                    System.out.print("\tSH");
                }else if(visited[i][j] == 3){
                    System.out.print("\tSW"); // Sword
                }else if(visited[i][j] == 4){
                    System.out.print("\tD"); // Dragon
                }else if(visited[i][j] == 5){
                    System.out.print("\tC");
                }
            }
        }
        System.out.println(" ");
    }
    //*********************************** Print title ************************************
    public static void printTitle () {
        System.out.println("\n----------------------------------------------------------------------------------");
        System.out.println("-----------------------    D R A G O N     S L A Y E R    -------------------------");
        System.out.println("-----------------------------------------------------------------------------------");
    }
    /*
     * The following method returns a 2-D array
     * The first set of row,column is the location of the monster guarding the shield
     * The second set of row,column is the location of the monster guarding the sword
     * The third set of row,column is the location of the dragon
     * The minimum size for the dungeon must be 4x4, this is set in the dungeonColumns and dungeonRows global variables
     */
    private static int[][] getMonstersLocation(){
        if(dungeonColumns < 4 || dungeonRows < 4){
            System.out.println("Minimum size for the dungeon must be 4x4");
            return null;
        }

        int[][] monstersLocation = new int[lesserMonsters + dragon][2];
        Random rand = new Random();

        for(int i = 0; i < lesserMonsters + dragon; i++){
            int row = rand.nextInt(dungeonRows);
            int column = rand.nextInt(dungeonColumns);
            if((row == 0 && column == 0) || (i != 0 && monsterLocationDuplicate(i + 1, row, column, monstersLocation))) {
                int columnDuplicatedValue = column;
                while(column == columnDuplicatedValue || (i != 0 && monsterLocationDuplicate(i + 1, row, column, monstersLocation)))
                    column = rand.nextInt(dungeonColumns);
            }
            monstersLocation[i][0] = row;
            monstersLocation[i][1] = column;
        }
        return monstersLocation;
    }

    /*
     * Returns true if a monster is already placed in the current cell (row, column)
     */
    private static boolean monsterLocationDuplicate(int monsters, int row, int column, int[][] monstersLocation) {
        for(int i = 0; i < monsters; i++){
            if(monstersLocation[i][0] == row && monstersLocation[i][1] == column)
                return true;
        }
        return false;
    }
}
