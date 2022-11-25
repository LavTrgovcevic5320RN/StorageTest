package org.example;

import storage.FileMetaData;
import storage.Storage;
import storage.StorageManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
                Class.forName("org.example.GoogleDriveImplementation");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        Collection<FileMetaData> result;

        String path = args[0];
        Storage storage = StorageManager.getStorage(path);
        System.out.println(storage.toString());

        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        while(!input.equals("exit")){
            String[] command = input.split(" ");

            switch (command[0]){
                case "init": // init OLGA C:/Users/Lav/Desktop 2048 5 exe
                    if(command.length >= 6){
                        int size = Integer.parseInt(command[3]);
                        int maxFiles = Integer.parseInt(command[4]);
                        storage.initialiseDirectory(command[1], command[2], size, maxFiles, command[5]);
                    }else if (command.length >= 5){
                        int size = Integer.parseInt(command[3]);
                        int maxFiles = Integer.parseInt(command[4]);
                        storage.initialiseDirectory(command[1], command[2], size, maxFiles, "");
                    }else if (command.length >= 4){
                        int size = Integer.parseInt(command[3]);
                        storage.initialiseDirectory(command[1], command[2], size, -1, "");
                    }else if (command.length >= 3){
                        storage.initialiseDirectory(command[1], command[2], -1, -1, "");
                    }else{
                        System.out.println("Ne dovoljan broj argumenata");
                    }
                    break;

                case "open":    // open C:/Users/Lav/Desktop//OLGA
                    if(command.length == 2)
                        storage.openDirectory(command[1]);
                    else
                        System.out.println("Not enough arguments");
                    input = sc.nextLine();
                    break;

                case "create":
                    if(command.length == 4){        // create A #/ 5
                        storage.create(command[1], command[2], Integer.parseInt(command[3]));
                    }else if(command.length == 3){  // create A #/
                        storage.create(command[1], command[2]);
                    }else
                        System.out.println("Not enough arguments");
                    input = sc.nextLine();
                    break;

                case "createFiles":
                    if(command.length == 2){
                        storage.create("B", "#/A/", 5);
                        storage.create("C", "#/A/B/", 5);
                        File file3 = new File("C:/Users/Lav/Desktop/Adasdasd/SK/A/B/1.txt");
                        File file4 = new File("C:/Users/Lav/Desktop/Adasdasd/SK/A/B/C/2.xlsx");
                        try {
                            file3.createNewFile();
                            file4.createNewFile();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }else {
                        System.out.println("Not enough arguments");
                    }
                    input = sc.nextLine();
                    break;

                case "delete":
                    if(command.length == 2){
                        storage.delete(command[1]);
                    }else
                        System.out.println("Not enough arguments");
                    input = sc.nextLine();
                    break;

                case "rename":  // rename Marko C:\\Users\\Lav\\Desktop\\Adasdasd\\SK\\A
                    if(command.length == 3)
                        storage.rename(command[1], command[2]);
                    else
                        System.out.println("Not enough arguments");
                    input = sc.nextLine();
                    break;

                case "move":    // move #/B #/A/1.docx
                    if(command.length >= 2){
                        try {
                            storage.moveFiles(command[1], command[2]);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }else
                        System.out.println("Not enough arguments");
                    input = sc.nextLine();
                    break;

                case "upload":
                    if(command.length >= 2){
                        storage.uploadFiles(command[1], command[2]);
                    }else
                        System.out.println("Not enough arguments");
                    input = sc.nextLine();
                    break;

                case "download":    // download C:/Users/Lav/Desktop/Proba #/A
                    if(command.length >= 2){
                        storage.download(command[1], command[2]);
                    }else
                        System.out.println("Not enough arguments");
                    input = sc.nextLine();
                    break;

                case "setMaxFiles": // set-max-files path max-files
                    if(command.length == 3){
                        storage.setMaxFiles(command[1], Integer.parseInt(command[2]));
                    }else
                        System.out.println("Not enough arguments");
                    input = sc.nextLine();
                    break;

                case "getStorageSize":
                    System.out.println(storage.getStorageByteSize());
                    input = sc.nextLine();
                    break;

                case "byteQuota":
                    if(command[1].equals("set")){
                        storage.setSizeQuota(Long.parseLong(command[2]));
                    }else if(command[1].equals("get"))
                        System.out.println(storage.getSizeQuota());
                    input = sc.nextLine();
                    break;

                case "":
                case "help":
                    System.out.println("...");
                    printfCommands();
                    input = sc.nextLine();
                    break;
                default:
                    printfCommands();
                    input = sc.nextLine();
                    break;
            }


        }

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