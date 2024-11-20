package lab11;

import org.junit.*;
import java.sql.*;
import static org.junit.Assert.*;
import lab11.Project.*;

public class ProjectTest {

    private static Connection connection;

    @Before
    static void setUp() throws Exception {
        
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "kole");
    }

    @After
    static void tearDown() throws Exception {
       
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void testGetMostPopularCaloricGoal() throws Exception {
       
        int mostPopularCaloricGoal = Project.getMostPopularCaloricGoal(connection);

        
        assertEquals(2000, mostPopularCaloricGoal);
    }

    @Test
    void testDisplayTop10ProteinFoods() {
        
        String output = Project.displayTop10ProteinFoods(connection);

        // Expected result string
        String expectedOutput = """
                Top 10 Foods by Protein Content:
                -------------------------------
                steak - 50g protein
                chicken - 45g protein
                protein powder - 40g protein
                salmon - 38g protein
                tilapia - 35g protein
                greek yogurt - 30g protein
                egg whites - 25g protein
                shrimp - 20g protein
                milk - 18g protein
                """;

        
        assertEquals(expectedOutput, output);
    }

    @Test
    void testInsertMicroNutrientData() throws Exception {
        
        Project.insertMicroNutrientData(connection);

        // Query the MicroNutrients table to verify row count
        String query = "SELECT COUNT(*) AS total FROM MicroNutrients";
        try (var statement = connection.prepareStatement(query);
             var resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                int rowCount = resultSet.getInt("total");

                // Assert that 3 rows were inserted (change based on how many we insert in our function)
                assertEquals(3, rowCount);
            } else {
                System.out.println("Failed to get data from table");
            }
        }
    }

    @Test
    void testGetMostPopularCaloricGoalFail() throws Exception {
        
        int mostPopularCaloricGoal = Project.getMostPopularCaloricGoal(connection);
        assertEquals(2500, mostPopularCaloricGoal);
    }

    @Test
    void testDisplayTop10ProteinFoodsFail() {
       
        String output = Project.displayTop10ProteinFoods(connection);

        String incorrectOutput = """
                Top 10 Foods by Protein Content:
                -------------------------------
                apple - 1g protein
                banana - 2g protein
                orange - 1g protein
                watermelon - 0g protein
                grape - 0g protein
                """;

        assertEquals(incorrectOutput, output);
    }

    @Test
    void testInsertMicroNutrientDataFail() throws Exception {
        
        Project.insertMicroNutrientData(connection);

        
        String query = "SELECT COUNT(*) AS total FROM MicroNutrients";
        try (var statement = connection.prepareStatement(query);
             var resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                int rowCount = resultSet.getInt("total");
                assertEquals(0, rowCount);
            } else {
                System.out.println("Failed to retrieve row count from MicroNutrients table.");
            }
        }
    }

    @Test
    void testGetMostPopularCaloricGoalAnotherFail() throws Exception {
        
        int mostPopularCaloricGoal = Project.getMostPopularCaloricGoal(connection);

        assertEquals(-1, mostPopularCaloricGoal);
    }
}
