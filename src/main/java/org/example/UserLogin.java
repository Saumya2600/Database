package org.example;

import java.io.*;
import java.security.*;
import java.util.Scanner;

public class UserLogin {
    public static String getMD5Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        File file = new File("userInfo.txt");


        if (!file.exists()) {
            file.createNewFile();
        }


        Scanner scanner = new Scanner(System.in);


        int option = 0;
        while (option != 3) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.println("Enter your option:");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:

                    System.out.println("Enter your username:");
                    String username = scanner.nextLine();
                    System.out.println("Enter your password:");
                    String password = scanner.nextLine();
                    System.out.println("Enter your security question:");
                    String securityQuestion = scanner.nextLine();
                    System.out.println("Enter your security answer:");
                    String securityAnswer = scanner.nextLine();


                    BufferedReader br = new BufferedReader(new FileReader("userInfo.txt"));
                    String line;
                    boolean userExists = false;
                    while ((line = br.readLine()) != null) {
                        String[] userData = line.split(",");
                        if (userData.length >= 1 && userData[0].equals(username)) {
                            userExists = true;
                            break;
                        }
                    }
                    br.close();


                    if (userExists) {
                        System.out.println("User already exists.");
                    } else {
                        String hashedPassword = getMD5Hash(password); // Generate MD5 hash of password
                        BufferedWriter bw = new BufferedWriter(new FileWriter("userInfo.txt", true));
                        bw.write(username + "," + hashedPassword + "," + securityQuestion + "," + securityAnswer + "\n");
                        bw.close();
                        System.out.println("User registered successfully.");
                    }
                    break;
                case 2:

                    System.out.println("Enter your username:");
                    String loginUsername = scanner.nextLine();
                    System.out.println("Enter your password:");
                    String loginPassword = scanner.nextLine();


                    br = new BufferedReader(new FileReader("userInfo.txt"));
                    boolean isAuthenticated = false;

                    while ((line = br.readLine()) != null) {
                        String[] userData = line.split(",");
                        if (userData.length >= 4 && userData[0].equals(loginUsername)) {
                            String hashedLoginPassword = getMD5Hash(loginPassword);
                            if (userData[1].equals(hashedLoginPassword)) {
                                System.out.println("Enter your security question:");
                                String securityQuestionForLogin = scanner.nextLine();
                                System.out.println("Enter your security answer:");
                                String securityAnswerForLogin = scanner.nextLine();
                                if (userData[2].equals(securityQuestionForLogin) && userData[3].equals(securityAnswerForLogin)) {
                                    isAuthenticated = true;
                                    break;
                                } else {
                                    System.out.println("Invalid security question or answer.");
                                    break;
                                }
                            }
                        }
                    }

                    if (isAuthenticated) {
                        System.out.println("Login successful.");
                        System.out.println("Enter the database name you want to create:");
                        String filename = scanner.nextLine();
                        String folderPath = "C:\\Data Mgmt\\auth1\\database_ent\\";
                        File newFile = new File(folderPath + filename);
                        if (newFile.exists()) {
                            System.out.println("database already exists.");
                        } else {
                            newFile.createNewFile();
                            System.out.println("database created successfully.");
                            //Query.Query_design();
                            String[] input = new String[0];
                            QueryDesignn queryDesignn= new QueryDesignn();
                            queryDesignn.main(input);

                        }
                    } else {
                        System.out.println("Login failed");
                    }
                    break;
                case 3:
                    System.out.println("Exit ");
                    break;
                default:
                    System.out.println("Invalid option. Please enter a valid option.");
                    break;
            }
        }

    }


}

