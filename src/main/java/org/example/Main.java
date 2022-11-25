package org.example;

import storage.Storage;
import storage.StorageManager;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args[0].equals("local")){
            try {
                Class.forName("LocalImplementation");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        else if(args[0].equals("drive")){
            try {
                Class.forName("GoogleDriveImplementation");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        String path = args[0];
        Storage storage = StorageManager.getStorage(path);
        System.out.println(storage.toString());
//
//        Scanner sc = new Scanner(System.in);
//        String input = sc.nextLine();

//        while(!input.equals("exit")){
//            String[] command = input.split(" ");
//
//            switch (command[0]){
//                case "createRoot:
//                    storage.
//                    break;
//                case "help":
//                    printfCommands();
//                    input = "exit";
//                    break;
//
//                default:
//                    printfCommands();
//                    break;
//            }
//
//
//        }

        System.out.println("Zavrsio rad sa storage-om");
    }

    static void printfCommands(){
        System.out.println("Create directory: dir name path");
        System.out.println("Initialise directory: init path name size maxFiles ");
        System.out.println("create path name maxSize");
        System.out.println("set maxFiles path maxFiles");
        System.out.println("upload local-path storage");
        System.out.println("...");
    }
}