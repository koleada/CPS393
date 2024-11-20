package lab11;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Project {

   

    public static void main (String[] args) throws SQLException {
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "kole");
        int mostPopularCaloricGoal = getMostPopularCaloricGoal(conn);
        System.out.println("The most popular caloric goal is: " + mostPopularCaloricGoal + " calories.");
        
        displayTop10ProteinFoods(conn);
        
        conn.close();  
        
    }

    // get the most popular caloric goal 
    public static int getMostPopularCaloricGoal(Connection connection) throws SQLException {
        String query = """
                SELECT goal_calories, COUNT(goal_calories) AS frequency
                FROM Goals
                GROUP BY goal_calories
                ORDER BY frequency DESC
                LIMIT 1;
                """;
        
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getInt("goal_calories");
            } else {
                throw new SQLException("No data found in Goals table.");
            }
        }
    }

    public static String displayTop10ProteinFoods(Connection connection) {
        StringBuilder output = new StringBuilder("Top 10 Foods by Protein Content:\n");
        output.append("-------------------------------\n");
    
        String query = """
                SELECT food_name, protein
                FROM foods
                ORDER BY protein DESC
                LIMIT 10;
                """;
    
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
    
            while (resultSet.next()) {
                String foodName = resultSet.getString("food_name");
                int protein = resultSet.getInt("protein");
                output.append(foodName).append(" - ").append(protein).append("g protein\n");
            }
        } catch (SQLException e) {
            output.append("Error: ").append(e.getMessage());
        }
    
        return output.toString();
    }
    

    // insert data into the MicroNutrients Table
    public static void insertMicroNutrientData(Connection connection) {
        String insertQuery = """
                INSERT INTO MicroNutrients (vitamin_a, vitamin_c, calcium, iron, potassium, magnesium, food_id)
                VALUES (?, ?, ?, ?, ?, ?, ?);
                """;
        
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            // Insert first record
            statement.setInt(1, 500);   // vitamin_a
            statement.setInt(2, 60);    // vitamin_c
            statement.setInt(3, 120);   // calcium
            statement.setInt(4, 18);    // iron
            statement.setInt(5, 400);   // potassium
            statement.setInt(6, 30);    // magnesium
            statement.setInt(7, 1);     // food_id (example ID)

            statement.addBatch();

            // Insert second record
            statement.setInt(1, 300);
            statement.setInt(2, 80);
            statement.setInt(3, 100);
            statement.setInt(4, 10);
            statement.setInt(5, 350);
            statement.setInt(6, 25);
            statement.setInt(7, 2);

            statement.addBatch();

            // Insert third record
            statement.setInt(1, 450);
            statement.setInt(2, 90);
            statement.setInt(3, 130);
            statement.setInt(4, 20);
            statement.setInt(5, 420);
            statement.setInt(6, 35);
            statement.setInt(7, 3);

            statement.addBatch();

            // Execute all inserts as a batch
            int[] results = statement.executeBatch();

            System.out.println("Inserted " + results.length + " rows into MicroNutrients table.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}