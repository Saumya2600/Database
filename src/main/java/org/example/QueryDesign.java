package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryDesign {
    public static final String CreateTableStatementpattern = "(?i)^CREATE TABLE ([a-zA-Z0-9_]+) \\((.*?)\\);$";
    public static final String SelectStatementPattern = "^(?i)SELECT (([a-zA-Z0-9_, ]+)|\\*) FROM ([a-zA-Z0-9_]+);$";
    public static final String SelectStatementWherePattern = "^(?i)SELECT (([a-zA-Z0-9_, ]+)|\\*) FROM ([a-zA-Z0-9_]+) WHERE ([a-zA-Z0-9_]+)=('[a-zA-Z0-9_]+');?$";
    public static final String InsertStatementPattern = "^(?i)INSERT INTO ([a-zA-Z0-9_]+) VALUES \\((('[^']*'|[^,]+)(, *('[^']*'|[^,]+))*)\\);$";
    public static final String DeleteStatementWherePattern = "^(?i)DELETE FROM ([a-zA-Z0-9_]+) WHERE ([a-zA-Z0-9_]+)=('[a-zA-Z0-9_ ]+');$";
    public static final String UpdateStatementWherePattern = "^(?i)UPDATE ([a-zA-Z0-9_]+) SET ([a-zA-Z0-9_]+)=('[a-zA-Z0-9_ ]+') WHERE ([a-zA-Z0-9_]+)=('[a-zA-Z0-9_ ]+');$";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter fILE name: ");
        String fileName = scanner.nextLine();
        while (true) {


            System.out.print("Enter a SQL query: ");
            String input = scanner.nextLine();


            // eg = "CREATE TABLE Persons (PersonID int, LastName varchar(255), FirstName varchar(255), Address varchar(255), City varchar(255));";
            Pattern create_pattern = Pattern.compile(CreateTableStatementpattern);
            Matcher create_matcher = create_pattern.matcher(input);
            if (create_matcher.find()) {
                String table_name = create_matcher.group(1);
                String[] columns = create_matcher.group(2).split(",\\s*");
                System.out.println("Table: " + table_name);
                System.out.println("Columns: " + String.join(", ", columns));
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
                    writer.write("Table: " + table_name);
                    writer.newLine();
                    writer.write("Columns: " + String.join(", ", columns));
                    writer.newLine();
                } catch (IOException e) {
                    System.err.println("Failed to write output to file: " + e.getMessage());
                }
            } else {
                // eg  "SELECT * FROM Persons;";
                Pattern select_pattern = Pattern.compile(SelectStatementPattern, Pattern.MULTILINE);
                Matcher select_matcher = select_pattern.matcher(input);
                if (select_matcher.find()) {
                    System.out.println(select_matcher.groupCount());
                    String table_name = select_matcher.group(3);
                    String columns = select_matcher.group(1).equals("*") ? "all" : select_matcher.group(1);
                    System.out.println("Table: " + table_name);
                    System.out.println("Columns: " + columns);
                } else {
                    //        //eg : "SELECT FirstName, LastName FROM Persons WHERE City='SFO';";

                    Pattern select_where_pattern = Pattern.compile(SelectStatementWherePattern, Pattern.MULTILINE);
                    Matcher select_where_matcher = select_where_pattern.matcher(input);
                    if (select_where_matcher.find()) {
                        System.out.println(select_where_matcher.groupCount());
                        String table_name = select_where_matcher.group(3);
                        String columns = select_where_matcher.group(1).equals("") ? "all" : select_where_matcher.group(1);
                        String where_column = select_where_matcher.group(4);
                        String where_value = select_where_matcher.group(5);
                        System.out.println("Table: " + table_name);
                        System.out.println("Columns: " + columns);
                        System.out.println("Where column: " + where_column);
                        System.out.println("Where value: " + where_value);
                    } else {
//        //eg: "INSERT INTO Persons VALUES ('1', 'John', 'Doe', '123 Main St', 'Anytown', 'CA');";

                        Pattern insert_pattern = Pattern.compile(InsertStatementPattern);
                        Matcher insert_matcher = insert_pattern.matcher(input);
                        if (insert_matcher.find()) {
                            String table_name = insert_matcher.group(1);
                            String[] values = insert_matcher.group(2).split(",\s");
                            System.out.println("Table: " + table_name);
                            System.out.println("Values: " + String.join(", ", values));
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
                                writer.write("Table: " + table_name);
                                writer.newLine();
                                writer.write("Values: " + String.join(", ", values));
                                writer.newLine();
                            } catch (IOException e) {
                                System.err.println("Failed to write output to file: " + e.getMessage());
                            }
                        } else {
                            //        // eg input = "DELETE FROM Persons WHERE LastName='Doe';";

                            Pattern delete_where_pattern = Pattern.compile(DeleteStatementWherePattern);
                            Matcher delete_where_matcher = delete_where_pattern.matcher(input);
                            if (delete_where_matcher.find()) {
                                String table_name = delete_where_matcher.group(1);
                                String where_column = delete_where_matcher.group(2);
                                String where_value = delete_where_matcher.group(3);
                                System.out.println("Table: " + table_name);
                                System.out.println("Where column: " + where_column);
                                System.out.println("Where value: " + where_value);
                            } else {

//                                eg:  "UPDATE Customers SET ContactName='Alfred Schmidt' WHERE CustomerID='1';";

                                Pattern update_where_pattern = Pattern.compile(UpdateStatementWherePattern);
                                Matcher update_where_matcher = update_where_pattern.matcher(input);
                                if (update_where_matcher.find()) {
                                    String table_name = update_where_matcher.group(1);
                                    String set_column = update_where_matcher.group(2);
                                    String set_value = update_where_matcher.group(3);
                                    String where_column = update_where_matcher.group(4);
                                    String where_value = update_where_matcher.group(5);
                                    System.out.println("Table: " + table_name);
                                    System.out.println("Set column: " + set_column);
                                    System.out.println("Set value: " + set_value);
                                    System.out.println("Where column: " + where_column);
                                    System.out.println("Where value: " + where_value);
                                } else {
                                    System.out.println("Invalid SQL query");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}