package com.spaceforce.util.ui;

import com.spaceforce.game.Game;
import com.spaceforce.obj.Interaction;
import com.spaceforce.obj.Item;
import com.spaceforce.obj.Location;
import com.spaceforce.player.Player;
import com.spaceforce.util.fileParsing.GameMap;
import com.spaceforce.util.fileParsing.JsonImporter;

import java.util.Scanner;

public class UserInterface {

    private UserInterface() {
    }

    // consider System.console().readLine() to take user input - it doesn't echo the input which may be good for style's sake
    static Scanner userInput = new Scanner(System.in);

    public boolean specialUse(Item item) {
        switch (item.name) {
            case "Garbage":
        }
        return false;
    }

    public static void beginInput() {
        String userRequest;
        do{
            userRequest = userInput.nextLine().toUpperCase(); // in what case can userRequest be null? what happens if it's an empty string?
        }while(!GameMap.isInitialized() && !userRequest.equalsIgnoreCase("start"));


        //Game map will need to be initialized first, otherwise runtime exception occurs

            switch (userRequest) {
                case "START":
                    GameMap.init();
                    break;
                case "HELP":
                    Game.help();
                    break;
                case "SAVE":
                    Game.save();
                    break;
                case "INVENTORY":
                    View.renderText(Player.getInventory().toString());
                    break;
                case "LOOK":
                    GameMap.currentLocation.look();
                    break;
                case "EXIT":
                    Game.exit();
                    return; // if the switch doesn't have a return somewhere the ide complains, probably because of the infinite loop.
                default: {
                    userRequest = CommandParser.parse(userRequest);
                    Interaction requestTarget = null;
                    String requestAction = null;
                    //if()
                    if (CommandParser.getTarget(userRequest) != null) {
                        requestTarget = CommandParser.getTarget(userRequest);
                    }
                    if (CommandParser.getAction(userRequest) != null) {
                        requestAction = CommandParser.getAction(userRequest);
                    }
                    if (requestAction != null && requestTarget != null) {
                        if (requestAction.equals("PICKUP") && requestTarget.getClass().getSimpleName().equalsIgnoreCase("Item")) {
//                        GameMap.currentLocation.items
                            Game.grabItem((Item) requestTarget);
                        } else if (requestAction.equals("DROP") && Player.checkInventory((Item) requestTarget)) {
                            Player.removeItem((Item) requestTarget);
                        } else if (requestAction.equals("GO")) {
                            //this was in the original if. we can reimplement it later, depending on how we approach locations
                            //&& GameMap.currentLocation.checkExit(((Location) requestTarget).name)

                            View.renderText("Going to " + requestTarget.getName());
                            GameMap.currentLocation = (Location) requestTarget;
                            View.renderText(GameMap.currentLocation.introMsg);

                        } else if (requestAction.equals("USE")) {
                            for (Item item : Player.getInventory()) {
                                if (requestTarget.getName().equalsIgnoreCase(item.getName())) {
                                    item.use();
                                }
                            }
                        } else if (requestAction.equals("TALK")) {
                            for (var npc : GameMap.currentLocation.npcs) {
                                if (requestTarget.getName().equalsIgnoreCase(npc.getName())) {
                                    View.renderText(npc.talkMsg);
                                }
                            }
                        } else {
                            requestTarget.interact(requestAction);
                        }

                    } else if (requestAction != null) {
                        //This can be refactored into using a .txt file instead. hardcoding to make sure it works first
                        String message;
                        switch (requestAction) {
                            case "PICKUP":
                                message = "I can't pick that up.";
                                break;
                            case "DROP":
                                message = "Can't drop something I don't have.";
                                break;
                            case "GO":
                                message = "I can't go there right now.";
                                break;
                            case "USE":
                                message = "Can't use something I don't have.";
                                break;
                            case "TALK":
                                message = "Talk to who??";
                                break;
                            default:
                                message = "Are you speaking another language? I don't understand you.";
                        }
                        message += "\n If you're confused, you can try typing help to see what you can do.";
                        View.renderText(message);

//                        System.out.println(requestAction);
//                        System.out.println(requestTarget);
//                        View.renderText("Action cannot be completed");
//                        Game.help();
                    }
                }
            }

//                    ActionSubject subject;
//
//                    if (Player.inventory.contains(noun)){
//                            Item subject = Player.inventory.getItem()
//
//                    }
//                     else if (currentLocal.items.contains(subject) || currentLocal.npcs.contains(subject)){
//                            subject = currentLocal.getSubject()
//                      }
//
//                    if (subject.equals(null)){
//                         View.renderText("This subject is not in sight")
//                         continue;
//                    }
//                    else if (verb.equals(pickup) && subject.grabbable){
//                         subject.interact(pickup)
//                         location.remove(subject)
//                         Player.addInventory(subject)
//                         continue
//                    }
//                     else {
//                          noun.interact(verb);
//                     }
    }
}