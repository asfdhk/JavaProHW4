package Jdbc1;

import java.sql.*;
import java.util.Scanner;

public class Main {
    // CREATE DATABASE mydb;
    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/abs?serverTimezone=Europe/Kiev";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "88c06a97!!A";

    static Connection conn;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            try {
                // create connection
                conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
                initDB();

                while (true) {
                    System.out.println("1: add client");
                    System.out.println("2: delete client");
                    System.out.println("3: view clients");
                    System.out.println("4: enter apartment");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addApartments(sc);
                            break;
                        case "2":
                            deleteApartments(sc);
                            break;
                        case "3":
                            viewApartments();
                            break;
                        case "4":
                            viewApartmentByAddress(sc);
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                if (conn != null) conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return;
        }
    }

    private static void initDB() throws SQLException {
        Statement st = conn.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Apartments");
            st.execute("CREATE TABLE Apartments (id INT NOT NULL " +
                    "AUTO_INCREMENT PRIMARY KEY, district VARCHAR(20) " +
                    "NOT NULL, address VARCHAR(50) NOT NULL , area INT, rooms INT , price INT)");
        } finally {
            st.close();
        }

        /*
        try (Statement st1 = conn.createStatement()) {
            st1.execute("DROP TABLE IF EXISTS Clients");
            st1.execute("CREATE TABLE Clients (id INT NOT NULL " +
                    "AUTO_INCREMENT PRIMARY KEY, name VARCHAR(20) " +
                    "NOT NULL, age INT)");
        }
         */
    }

    private static void addApartments(Scanner sc) throws SQLException {
        System.out.print("Enter district apartment: ");
        String district = sc.nextLine();
        System.out.print("Enter address apartment : ");
        String address = sc.nextLine();
//        int age = Integer.parseInt(sAge);
        System.out.println("Enter area apartment: ");
        String sArea = sc.nextLine();
        int area = Integer.parseInt(sArea);
        System.out.println("Enter amount rooms: ");
        String sRoom = sc.nextLine();
        int rooms = Integer.parseInt(sRoom);
        System.out.println("Enter price: ");
        String sPrice = sc.nextLine();
        int price = Integer.parseInt(sPrice);

        //String sql = "INSERT INTO Clients (name, age) " +
          //      "VALUES(" + name + ", " + age + ")";

        PreparedStatement ps = conn.prepareStatement("INSERT INTO Apartments (district,address,area,rooms,price) VALUES(?, ?, ?,?,?)");
        try {
            ps.setString(1, district);
            ps.setString(2,address);
            ps.setInt(3,area);
            ps.setInt(4,rooms);
            ps.setInt(5,price);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE


        } finally {
            ps.close();
        }
    }

    private static void deleteApartments(Scanner sc) throws SQLException {
        System.out.print("Enter address apartments:");
        String address = sc.nextLine();

        PreparedStatement ps = conn.prepareStatement("DELETE FROM Apartments WHERE address = ?");
        try {
            ps.setString(1, address);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }
    /*

   -> 1 2 3
      -----
      -----
      -----

     */

    private static void viewApartments() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Apartments");
        try {
            // table of data representing a database result set,
            // ps.setFetchSize(100);
            ResultSet rs = ps.executeQuery();

            try {
                // can be used to get information about the types and properties of the columns in a ResultSet object
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            ps.close();
        }
    }

    private static void viewApartmentByAddress(Scanner sc) throws SQLException {
        System.out.println("Enter address:");
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Apartments WHERE address = ?");
        String address = sc.nextLine();
        try {
            ps.setString(1, address);
            ResultSet rs = ps.executeQuery();

            try {
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close();
            }
        } finally {
            ps.close();
        }
    }

}
