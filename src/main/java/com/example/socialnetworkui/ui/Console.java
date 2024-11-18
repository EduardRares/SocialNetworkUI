package com.example.socialnetworkui.ui;

import com.example.socialnetworkui.domain.Friendship;
import com.example.socialnetworkui.domain.User;
import com.example.socialnetworkui.domain.validators.ValidationException;
import com.example.socialnetworkui.service.FriendshipService;
import com.example.socialnetworkui.service.UserService;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;

public class Console {
    private final UserService userService;
    private final FriendshipService friendshipService;
    public Console(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        //Consumer<Friendship> consumer = userService::importFriendship;
        //friendshipService.findAll().forEach(consumer);
    }
    public void run() {
        Scanner myInput = new Scanner( System.in );
        boolean ok = true;
        while (ok) {
            optiuni();
            System.out.print( "Option: " );
            int op;
            try {
                op = myInput.nextInt();
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid option");
                break;
            }
            switch (op) {
                case 1: {
                    System.out.print( "FirstName: " );
                    String firstName = myInput.next();
                    System.out.print( "LastName: " );
                    String lastName = myInput.next();
                    System.out.print( "Email: " );
                    String email = myInput.next();
                    System.out.print( "password: " );
                    String password = myInput.next();
                    try {
                        User u = new User(firstName, lastName, email, password);
                        userService.addUser(u);
                    } catch (ValidationException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 2: {
                    System.out.print( "ID: " );
                    String id = myInput.next();
                    long u1;
                    try {
                        u1 = Long.parseLong(id);
                        Optional<User> user = userService.deleteUser(u1);
                        friendshipService.deleteAllFriendships(user.get().getId());
                    } catch (NumberFormatException e) {
                        System.out.println("Introduceti un ID valid");
                        }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 3: {
                    System.out.print( "ID1: " );
                    String id1 = myInput.next();
                    System.out.print( "ID2: " );
                    String id2 = myInput.next();
                    Long f1, f2;
                    try {
                        f1 = Long.parseLong(id1);
                        f2 = Long.parseLong(id2);
                        Friendship f = friendshipService.addFriendship(f1, f2);
                    } catch (NumberFormatException e) {
                        System.out.println("Introduceti un ID valid");
                    }
                    catch (NoSuchElementException e) {
                        System.out.println(e.getMessage());
                    }

                    break;
                }
                case 4: {
                    System.out.print( "ID1: " );
                    String id1 = myInput.next();
                    System.out.print( "ID2: " );
                    String id2 = myInput.next();
                    long f1, f2;
                    try {
                        f1 = Long.parseLong(id1);
                        f2 = Long.parseLong(id2);
                        Friendship f = friendshipService.deleteFriendship(f1, f2);
                    } catch (NoSuchElementException e) {
                        System.out.println("Nu exista relatia de prietenie mentionata");
                    }
                    break;
                }
                case 5: {
                    System.out.println(friendshipService.noComunity());
                    break;
                }
                case 6: {
                    System.out.print("[ ");
                    Consumer<String> consumer = u -> {
                        System.out.print(u + " ");
                    };
                    friendshipService.mostSocialComunity().forEach(consumer);
                    System.out.println("]");
                    break;
                }
                case 0: {
                    ok = false;
                    break;
                }
                default: {
                    ok = false;
                    System.out.println("Wrong option");
                    break;
                }

            }
        }
    }
    private void optiuni() {
        System.out.println("1. Add User");
        System.out.println("2. Delete User");
        System.out.println("3. Add Friendship");
        System.out.println("4. Remove Friendship");
        System.out.println("5. Number of comunities");
        System.out.println("6. Most social comunity");
        System.out.println("0. Exit");
    }
}
