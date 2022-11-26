package org.example;

import storage.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Cao");
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
        Collection<FileMetaData> result = null;
        Collection<String> resenje;
        Sorter sorter;
        Filter filter = null;

        String createFilesPath = "";

        String path = args[0];
        Storage storage = StorageManager.getStorage(path);
        System.out.println(storage.toString());

        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        while(!input.equals("exit")){
            String[] command = input.split(" ");

            switch (command[0]) {
                case "init": // init OLGA C:/Users/Lav/Desktop 2048 5 exe txt
                    if (command.length >= 6) {
                        int size = Integer.parseInt(command[3]);
                        int maxFiles = Integer.parseInt(command[4]);
                        String[] extensions = new String[command.length];
                        int i = 0;
                        for (String st : command)
                            extensions[i++] = st;
                        storage.initialiseDirectory(command[1], command[2], size, maxFiles, extensions);
                    } else if (command.length >= 5) {
                        int size = Integer.parseInt(command[3]);
                        int maxFiles = Integer.parseInt(command[4]);
                        storage.initialiseDirectory(command[1], command[2], size, maxFiles, "");
                    } else if (command.length >= 4) {
                        int size = Integer.parseInt(command[3]);
                        storage.initialiseDirectory(command[1], command[2], size, -1, "");
                    } else if (command.length >= 3) {
                        storage.initialiseDirectory(command[1], command[2], -1, -1, "");
                    } else {
                        System.out.println("Ne dovoljan broj argumenata");
                    }
                    createFilesPath = command[2] + "/" + command[1];
                    break;

                case "open":    // open C:/Users/Lav/Desktop/OLGA
                    if (command.length == 2){
                        storage.openDirectory(command[1]);
                        createFilesPath = command[1];
                    }else
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

                case "createFiles":         // sluzi kao precica da kreira fajlove i direktorijume da bi mogli da ih koristimo za move, rename, .itd
                    if(command.length == 1){
                        storage.create("A", "#/", 5);
                        storage.create("B", "#/", 5);
                        storage.create("B", "#/A/", 5);
                        storage.create("C", "#/A/B/", 5);
                        File file3 = new File( createFilesPath + "/A/B/1.txt");
                        File file4 = new File(createFilesPath + "/A/B/C/2.xlsx");
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

                case "delete":  //  delete #/A/B
                    if(command.length == 2){
                        storage.delete(command[1]);
                    }else
                        System.out.println("Not enough arguments");
                    input = sc.nextLine();
                    break;

                case "rename":  // rename Marko #/A/B
                    if(command.length == 3)
                        storage.rename(command[1], command[2]);
                    else
                        System.out.println("Not enough arguments");
                    input = sc.nextLine();
                    break;

                case "move":    // move #/B #/A/1.docx
                    if(command.length >= 2){
                        try {
                            String[] filesToMove = new String[command.length];
                            int i = 0;
                            for (String st : command)
                                filesToMove[i++] = st;
                            storage.moveFiles(command[1], filesToMove);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }else
                        System.out.println("Not enough arguments");
                    input = sc.nextLine();
                    break;

                case "upload":  //  upload #/B C:/Users/Lav/Desktop/D
                    if(command.length >= 2){
                        String[] filesToUpload = new String[command.length];
                        int i = 0;
                        for (String st : command)
                            filesToUpload[i++] = st;
                        storage.uploadFiles(command[1], command[2]);
                    }else
                        System.out.println("Not enough arguments");
                    input = sc.nextLine();
                    break;

                case "download":    // download C:/Users/Lav/Desktop/Proba #/A/B/C
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

                case "byteQuota":   //  byteQuota set 3072
                    if(command[1].equals("set")){
                        storage.setSizeQuota(Long.parseLong(command[2]));
                    }else if(command[1].equals("get"))
                        System.out.println(storage.getSizeQuota());
                    input = sc.nextLine();
                    break;

                case "searchFilesInDirectory":  //  searchFilesInDirectory #/A
                    if(command.length == 2){
                        result = storage.searchFilesInDirectory(command[1]);
                        for(FileMetaData f : result){
                            System.out.println(f);
                        }
                    }else
                        System.out.println("Not enough arguments");
                    input = sc.nextLine();
                    break;

                case "searchFilesInDirectoryAndBelow":  //  searchFilesInDirectoryAndBelow #/A
                    if(command.length == 2){
                        result = storage.searchFilesInDirectoryAndBelow(command[1]);
//                        for(FileMetaData f : result){
//                            System.out.println(f);
//                        }
                    }else
                        System.out.println("Not enough arguments");
                    input = sc.nextLine();
                    break;

                case "searchExtension":  //  searchExtension xlsx
                    if(command.length == 2){
                        result = storage.searchFilesWithExtension("", command[1]);
                        for(FileMetaData f : result){
                            System.out.println(f);
                        }
                    }else
                        System.out.println("Not enough arguments");
                    input = sc.nextLine();
                    break;

                case "searchSubstring":  //  searchSubstring bravar
                    if(command.length == 2){
                        result = storage.searchFilesThatContain("", command[1]);
                        for(FileMetaData f : result){
                            System.out.println(f);
                        }
                    }else
                        System.out.println("Not enough arguments");
                    input = sc.nextLine();
                    break;

                case "searchIfExist":  //  searchIfExist #/A Markobravar.docx
                    if(command.length >= 2){
                        boolean b = storage.searchIfFilesExist(command[1], command[2]);
                        String str = "";
                        if(b){
                            str = "postoji";
                        }else
                            str = "ne postoji";
                        System.out.println("Fajl " + command[2] + " " + str);
                    }else
                        System.out.println("Not enough arguments");
                    input = sc.nextLine();
                    break;

                case "searchFile":  //  searchFile Markobravar.docx
                    if(command.length == 2){
                        resenje = storage.searchFile(command[1]);
//                        for(String s : resenje){
//                            System.out.println(s);
//                        }
                    }else
                        System.out.println("Not enough arguments");
                    input = sc.nextLine();
                    break;

                case "filter":  //  filter 25 11 2022 27 11 2022 modify             filter 0 10
                    if(command.length == 8 && command[3].length()==4){
                        if(result != null){
                            if(command[3].length()==4){
                                Calendar calendar1 = null;
                                Calendar calendar2 = null;
                                if(Integer.parseInt(command[2]) == 1) {
                                    calendar1 = new GregorianCalendar(Integer.parseInt(command[3]), Calendar.JANUARY, Integer.parseInt(command[1]));
                                }else if(Integer.parseInt(command[2]) == 2) {
                                    calendar1 = new GregorianCalendar(Integer.parseInt(command[3]), Calendar.FEBRUARY, Integer.parseInt(command[1]));
                                }else if(Integer.parseInt(command[2]) == 3) {
                                    calendar1 = new GregorianCalendar(Integer.parseInt(command[3]), Calendar.MARCH, Integer.parseInt(command[1]));
                                }else if(Integer.parseInt(command[2]) == 4) {
                                    calendar1 = new GregorianCalendar(Integer.parseInt(command[3]), Calendar.APRIL, Integer.parseInt(command[1]));
                                }else if(Integer.parseInt(command[2]) == 5) {
                                    calendar1 = new GregorianCalendar(Integer.parseInt(command[3]), Calendar.MAY, Integer.parseInt(command[1]));
                                }else if(Integer.parseInt(command[2]) == 6) {
                                    calendar1 = new GregorianCalendar(Integer.parseInt(command[3]), Calendar.JUNE, Integer.parseInt(command[1]));
                                }else if(Integer.parseInt(command[2]) == 7) {
                                    calendar1 = new GregorianCalendar(Integer.parseInt(command[3]), Calendar.JULY, Integer.parseInt(command[1]));
                                }else if(Integer.parseInt(command[2]) == 8) {
                                    calendar1 = new GregorianCalendar(Integer.parseInt(command[3]), Calendar.AUGUST, Integer.parseInt(command[1]));
                                }else if(Integer.parseInt(command[2]) == 9) {
                                    calendar1 = new GregorianCalendar(Integer.parseInt(command[3]), Calendar.SEPTEMBER, Integer.parseInt(command[1]));
                                }else if(Integer.parseInt(command[2]) == 10) {
                                    calendar1 = new GregorianCalendar(Integer.parseInt(command[3]), Calendar.OCTOBER, Integer.parseInt(command[1]));
                                }else if(Integer.parseInt(command[2]) == 11) {
                                    calendar1 = new GregorianCalendar(Integer.parseInt(command[3]), Calendar.NOVEMBER, Integer.parseInt(command[1]));
                                }else if(Integer.parseInt(command[2]) == 12) {
                                    calendar1 = new GregorianCalendar(Integer.parseInt(command[3]), Calendar.DECEMBER, Integer.parseInt(command[1]));
                                }

                                if(Integer.parseInt(command[5]) == 1) {
                                    calendar2 = new GregorianCalendar(Integer.parseInt(command[6]), Calendar.JANUARY, Integer.parseInt(command[4]));
                                }else if(Integer.parseInt(command[2]) == 2) {
                                    calendar2 = new GregorianCalendar(Integer.parseInt(command[6]), Calendar.FEBRUARY, Integer.parseInt(command[4]));
                                }else if(Integer.parseInt(command[2]) == 3) {
                                    calendar2 = new GregorianCalendar(Integer.parseInt(command[6]), Calendar.MARCH, Integer.parseInt(command[4]));
                                }else if(Integer.parseInt(command[2]) == 4) {
                                    calendar2 = new GregorianCalendar(Integer.parseInt(command[6]), Calendar.APRIL, Integer.parseInt(command[4]));
                                }else if(Integer.parseInt(command[2]) == 5) {
                                    calendar2 = new GregorianCalendar(Integer.parseInt(command[6]), Calendar.MAY, Integer.parseInt(command[4]));
                                }else if(Integer.parseInt(command[2]) == 6) {
                                    calendar2 = new GregorianCalendar(Integer.parseInt(command[6]), Calendar.JUNE, Integer.parseInt(command[4]));
                                }else if(Integer.parseInt(command[2]) == 7) {
                                    calendar2 = new GregorianCalendar(Integer.parseInt(command[6]), Calendar.JULY, Integer.parseInt(command[4]));
                                }else if(Integer.parseInt(command[2]) == 8) {
                                    calendar2 = new GregorianCalendar(Integer.parseInt(command[6]), Calendar.AUGUST, Integer.parseInt(command[4]));
                                }else if(Integer.parseInt(command[2]) == 9) {
                                    calendar2 = new GregorianCalendar(Integer.parseInt(command[6]), Calendar.SEPTEMBER, Integer.parseInt(command[4]));
                                }else if(Integer.parseInt(command[2]) == 10) {
                                    calendar2 = new GregorianCalendar(Integer.parseInt(command[6]), Calendar.OCTOBER, Integer.parseInt(command[4]));
                                }else if(Integer.parseInt(command[2]) == 11) {
                                    calendar2 = new GregorianCalendar(Integer.parseInt(command[6]), Calendar.NOVEMBER, Integer.parseInt(command[4]));
                                }else if(Integer.parseInt(command[2]) == 12) {
                                    calendar2 = new GregorianCalendar(Integer.parseInt(command[6]), Calendar.DECEMBER, Integer.parseInt(command[4]));
                                }

                                assert calendar1 != null;
                                Date date1 = calendar1.getTime();
                                assert calendar2 != null;
                                Date date2 = calendar2.getTime();
                                if(command[7].equalsIgnoreCase("create")){
                                    filter = new Filter(date1, date2, DateType.CREATE);
                                }else if(command[7].equalsIgnoreCase("access")){
                                    filter = new Filter(date1, date2, DateType.ACCESS);
                                }else if(command[7].equalsIgnoreCase("modify")){
                                    filter = new Filter(date1, date2, DateType.MODIFY);
                                }else
                                    filter = new Filter(date1, date2, DateType.MODIFY);

                                Collection<FileMetaData> solution = filter.applyFilter(result);
                                for(FileMetaData f : solution) {
                                    System.out.println(f);
                                }
                            }else{
                                filter = new Filter(Long.parseLong(command[1]), Long.parseLong(command[2]));
                                Collection<FileMetaData> solution = filter.applyFilter(result);
                                for(FileMetaData f : solution) {
                                    System.out.println(f);
                                }
                            }

                        }else
                            System.out.println("Prvo uradite neki od search-eva pa tek onda koristite filter");
                    }else{
                        if (result != null) {
                            filter = new Filter(Long.parseLong(command[1]), Long.parseLong(command[2]));
                            Collection<FileMetaData> solution = filter.applyFilter(result);
                            for (FileMetaData f : solution) {
                                System.out.println(f);
                            }
                        }else
                            System.out.println("Not enough arguments");
                    }
                    input = sc.nextLine();
                    break;
                case "sorter":  //  sorter true
                    if(command.length == 2){
                        if(result != null){
                            sorter = new Sorter(Boolean.parseBoolean(command[1]));
                            result = sorter.applySorter(result);
                            for(FileMetaData f : result)
                                System.out.println(f);

                        }else
                            System.out.println("Prvo uradite neki od search-eva pa tek onda koristite sorter");

                    }else
                        System.out.println("Not enough arguments");
                    input = sc.nextLine();
                    break;

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