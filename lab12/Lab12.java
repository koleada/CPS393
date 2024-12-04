import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.*;
import java.util.function.*;
import java.util.Map;
import java.util.Optional;
import java.util.LinkedList;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.io.*;

class Lab12 {
    // some documentation that can be helpful
    /*
     * https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html
     * https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html
     * https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html
     * https://docs.oracle.com/javase/8/docs/api/java/util/stream/Collectors.html#
     * groupingBy-java.util.function.Function-
     */
    public static void main(String[] args)throws IOException
	{
		/*
			the code below will create a stream of data 
			from a file. Each piece of data in the stream
			will be an entire line from the file.
		*/
		File f=new File("random_names.csv");
		BufferedReader br=new BufferedReader(new FileReader(f));
		Stream<String> data=br.lines();
		
		//A. Write code for generateObjects. 
		/*the method should use a function to convert each row of data
		into a Name object*/
		/*Hint: recall that csv are comma separated values*/
    public List<Name> generateObjects(Stream<String> line) {
        line.map(line -> line.split(",")).map(name -> new Name(name[0].trim(), name[1].trim()))
                .collect(Collectors.toList());
    }

    List<Name> names = generateObjects(data);System.out.println(names.size());System.out.println(names.get(0).first);
    // See anything wrong with the first entry? Why did this happen?

    // B. Using names, get a count of how many first names are longer than 5
    int greaterThanFive = names.stream().map(name -> name.getFirstName()).filter(firstName -> firstName.length() >= 5)
            .count();

    // C. Using names, sort the names alphabetically based on last name then first
    // name
    // i.e. "john doe", "jane smith"
    // i.e. "jane doe", "john doe"
    // Hint: you need to modify Name to be comparable in order to use sorted()
    List<Name> sorted = names.stream().sorted((n1, n2) -> {
        int lastNameComparison = n1.getLastName().compareTo(n2.getLastName());
        if (lastNameComparison != 0) {
            return lastNameComparison;
        }
        return n1.getFirstName().compareTo(n2.getFirstName());
    }).collect(Collectors.toList());

    // D. Using names, get the set of unique names
    Set<Name> unique = names.stream().collect(Collectors.toSet());System.out.println(unique.size());

    // Question: is the size the same as the original input?
    // Question: look in the file, are there duplicates?
    // Question: Why are they not ignored? How to fix it?
    // Hint: Add an equals method to Name.
    // Hint: the header is public boolean equals(Object o)
    // Question: is the size the same as the original input?
    // WHY? Uncomment the hashCode method.
    // Answer: The toSet uses equals and hashCode to distinguish objects.
    Set<Name> unique = names.stream().collect(Collectors.toSet());System.out.println(unique.size());

    // E. Using unique, sort the names
    List<Name> uniqueSorted = unique.stream()

            .sorted(Comparator.comparing(Name::getLastName).thenComparing(Name::getFirstName)) // Sort by last and first
                                                                                               // name
            .collect(Collectors.toList());

    // F. Using unique, find the maximum name based on the compareTo method
    // using .max()
    // use the documentation to help you structure the call.
    Name maxName = unique.stream().max(Name::compareTo);

    // G. Using unique, find the maximum name based on the total number of
    // characters in the first and last name combined.
    Optional<Name> maxName = unique.stream()
            .max(Comparator.comparingInt(name -> name.getFirstName().length() + name.getLastName().length()));

    // H. Write a function to find the total length of a Name
    // Use the function to find a list of all the names that are longer than 11
    List<Name> longNames = unique.stream().filter(name -> name.getTotalLength() > 11).collect(Collectors.toList());

    // I. What is most popular name length?
    // Hint: map the names to length and find the key with the most values in the
    // list
    Map<Integer, Long> lengthFrequency = unique.stream()
            .map(name -> name.getFirstName().length() + name.getLastName().length())
            .collect(Collectors.groupingBy(length -> length, Collectors.counting()));

    // Find the most popular name length
    Optional<Map.Entry<Integer, Long>> mostPopularLength = lengthFrequency.entrySet().stream()
            .max(Map.Entry.comparingByValue());

    // J. Give code to:
    // Filter out even numbers.
    // Square the remaining numbers.
    // Add 10 to the squared values.
    List<Integer> result = numbers.stream().filter(num -> num % 2 != 0).map(num -> num * num).map(num -> num + 10)
            .collect(Collectors.toList());

    // K. Give code to:
    // Filter out empty strings.
    // Convert each string to uppercase.
    // Append a suffix " processed" to each string.

    List<String> processedStrings = strings.stream().filter(str -> !str.isEmpty()) // Filter out empty strings
            .map(str -> str.toUpperCase()) // Convert to uppercase
            .map(str -> str + " processed") // Append " processed" to each string
            .collect(Collectors.toList());

    // L. Given a list of student grades as doubles:
    // Filter out students with a grade below 50.0
    // Increase each student's grade by 10 points (with a maximum of 100)
    // Convert the final grade to a letter grade:
    // A for grades 90.0 and above.
    // B for grades between 75.0 and 89.9
    // C for grades between 60.0 and 74.9
    // D for grades between 50.0 and 59.9
    // F for grades below 50.0
    List<String> letterGrades = grades.stream().filter(grade -> grade >= 50.0) // Filter out grades below 50.0
            .map(grade -> Math.min(grade + 10, 100.0)) // Increase by 10, with max 100
            .map(grade -> {
                if (grade >= 90.0)
                    return "A";
                else if (grade >= 75.0)
                    return "B";
                else if (grade >= 60.0)
                    return "C";
                else if (grade >= 50.0)
                    return "D";
                else
                    return "F"; // For completeness, although this won't be reached due to filtering
            }).collect(Collectors.toList());

    // M. Given a list of employee salaries:
    // Filter out employees earning less than $30,000.
    // Increase the salary of each employee by 5%.
    // Convert the salary to a string with a dollar sign (e.g., $35,000).
    List<String> updatedSalaries = salaries.stream().filter(salary -> salary >= 30000).map(salary -> salary * 1.05)
            .map(salary -> String.format("$%.2f", salary)).collect(Collectors.toList());

    // N. Read over and understand the following recursive function:
    Function<Integer, Integer> factorial = n -> {
        if (n <= 1)
            return 1;
        return n * factorial.apply(n - 1);
    };System.out.println("5! is "+factorial.apply(5));
    // Pretty straighfoward, now try the following:
    // a. sum array of doubles
    Function<double[], Double> sumArray = arr -> {
        return sumArrayHelper(arr, arr.length - 1);
    };

    // Recursive function to sum the elements directly
    Function<double[], Double> sumArrayHelper = arr -> {
        if (arr.length == 0) {
            return 0.0;
        }
        return arr[0] + sumArrayHelper.apply(java.util.Arrays.copyOfRange(arr, 1, arr.length));
    };

    // b. reverse a string
    Function<String, String> reverseString = str -> {
        if (str.isEmpty()) {
            return str;
        }
        return reverseString.apply(str.substring(1)) + str.charAt(0);
    };

    // c. calculate a power
    Function<Integer, Function<Integer, Integer>> power = x -> n -> {
        if (n == 0) {
            return 1;
        }
        return x * power.apply(x).apply(n - 1);
    };

    // O. Look over and understand Lab12B.java
}}

class Name {
    String first, last;

    public Name(String f, String l) {
        first = f;
        last = l;
    }

    public String toString() {
        return first + " " + last;
    }

    @Override
    public boolean equals(name1, name2){
        if (name1.hashCode() != name2.hashCode()) return false;
        else return true;

    }

    @Override
    public int compareTo(Name other) {
        int lastNameComparison = this.lastName.compareTo(other.lastName);
        if (lastNameComparison != 0) {
            return lastNameComparison;
        }
        return this.firstName.compareTo(other.firstName);
    }

    public int hashCode() {
        return 31 * first.hashCode() + last.hashCode();
    }

}
