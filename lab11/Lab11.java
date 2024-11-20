import java.util.*;
import java.util.stream.*;
import java.util.function.Function;

public class Lab11
{
    public static void main(String[] args)
	{
        List<String> fruit = Arrays.asList("cherry","banana","berry","apple","cherry","kiwi","fig","date","lemon","honeydew","cherry","elderberry","apple","banana","grape");
        
		// Collect elements into a Set
        Set<String> fruitSet = fruit.stream()
                                   .collect(Collectors.toSet());
		
        // Collect the fruit into groups based on their first character
        Map<Character, List<String>> fruitByFirstChar = fruit.stream()
                                                             .collect(Collectors.groupingBy(s -> s.charAt(0)));

		// Group fruit by the length
        Map<Integer, List<String>> fruitByLength = fruit.stream()
                                                        .collect(Collectors.groupingBy(String::length));
     
		// Collect the fruit that has "erry" in it
        List<String> fruitWithErry = fruit.stream()
                                          .filter(s -> s.contains("erry"))
                                          .collect(Collectors.toList());
		
		// Collect the fruit that has 5 or fewer symbols
        List<String> shortFruits = fruit.stream()
                                        .filter(s -> s.length() <= 5)
                                        .collect(Collectors.toList());
		
		// Find the total number of symbols in all the fruit stored
        int totalSymbols = fruit.stream()
                                .mapToInt(String::length)
                                .sum();
		
		
		List<Integer> data = Arrays.asList(87, 23, 45, 100, 6, 78, 92, 44, 13, 56, 34, 99, 82, 19, 1012, 78, 45, 90, 23, 56, 78, 100, 3, 43, 67, 89, 21, 34, 10);

        // Partition data based on if >=50
        Map<Boolean, List<Integer>> partitionedData = data.stream()
                                                          .collect(Collectors.partitioningBy(n -> n >= 50));
		
		// Divide data into groups based on the remainder when divided by 7
        Map<Integer, List<Integer>> groupedByRemainder = data.stream()
                                                             .collect(Collectors.groupingBy(n -> n % 7));
		
		// Find the sum of the data
        int sumOfData = data.stream()
                            .mapToInt(Integer::intValue)
                            .sum();
		
		// Collect the unique values
        Set<Integer> uniqueValues = data.stream()
                                        .collect(Collectors.toSet());
		
        // Compute the cube of each value
        List<Integer> cubes = data.stream()
                                  .map(n -> n * n * n)
                                  .collect(Collectors.toList());

		// Find the sum of the cubes of each value
        int sumOfCubes = data.stream()
                             .mapToInt(n -> n * n * n)
                             .sum();
		
		// Increase the value of each element by 5
        List<Integer> increasedValues = data.stream()
                                            .map(n -> n + 5)
                                            .collect(Collectors.toList());
   }
}
