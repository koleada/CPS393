import java.util.function.*;
import java.util.*;

public class Examples2 {
    public static void main(String[] args) {
        /*
         Write a predicate for each of the following
         */
        
        /**
         To check if a list of strings is empty or not.
         */
        Predicate<List<String>> isEmpty = list -> list.isEmpty();
        
        /**
         What type can you use so all the lines below work?
         Hint: Look at the Java hierarchy.
         */
        
        List<String> strs = Arrays.asList("hello", "a", "ABC");
        System.out.println(isEmpty.test(strs));
        System.out.println(isEmpty.test(new ArrayList<>()));
        System.out.println(isEmpty.test(new LinkedList<>()));

        /**
         Check if a user is an admin or not.
         Test by processing the whole queue
         */
        Predicate<User> isAdmin = user -> user.role().equals("admin");
        
        Queue<User> users = new LinkedList<>();
        users.add(new User("mary", "jane", 0));
        users.add(new User("john", "doe", 1));
        users.add(new User("barbara", "jean", 1));
        users.add(new User("max", "smith", 0));
        
        users.forEach(user -> System.out.println(user + " is admin? " + isAdmin.test(user)));

        /**
         Check if a user's last name is at least 5 characters long
         */
        Predicate<User> isLastNameLong = user -> user.last().length() >= 5;
        
        /**
         Check if a user's first name starts with "ma"
         */
        Predicate<User> startsWithMa = user -> user.first().toLowerCase().startsWith("ma");
        
        /**
         Check if an integer is between 0 and 100
         */
        Predicate<Integer> isBetween0And100 = num -> num >= 0 && num <= 100;
        
        /**
         Check if a user's first name contains any characters that are not letters
         Hint: use {} for multiple lines on the left hand side of ->
         */
        Predicate<User> hasNonLetterInFirstName = user -> {
            String firstName = user.first();
            return !firstName.chars().allMatch(Character::isLetter);
        };

        /*
         Write a Consumer function for each
         */
        
        /**
         Multiply every integer of a list by 2
         Hint: use {} for multiple lines on the left hand side of the arrow
         */
        Consumer<List<Integer>> doubleList = list -> {
            for (int i = 0; i < list.size(); i++) {
                list.set(i, list.get(i) * 2);
            }
        };
       
        List<Integer> values = new ArrayList<>(Arrays.asList(2, 56, 72, 56, 57, 248));
        System.out.println("Before doubling: " + values);	
        doubleList.accept(values);
        System.out.println("After doubling: " + values);
        
        /**
         Reverse a list of integers
         */
        Consumer<List<Integer>> reverseList = list -> Collections.reverse(list);

        reverseList.accept(values);
        System.out.println("After reversing: " + values);
        
        /*
         Write a supplier to generate a random int in [0,100]
         */
        Supplier<Integer> randomIntSupplier = () -> new Random().nextInt(101);
        System.out.println("Random number: " + randomIntSupplier.get());
        
        /*
         Write a function for each
         */
                
        /**
         Compute the square of a number
         */
        Function<Integer, Integer> square = x -> x * x;
        System.out.println("Square of 5: " + square.apply(5));
        
        /**
         Create an uppercase version of a string
         */
        Function<String, String> toUpperCase = String::toUpperCase;
        System.out.println("Uppercase 'hello': " + toUpperCase.apply("hello"));
        
        /**
         Get the role of a user
         */
        Function<User, String> getRole = User::role;
        System.out.println("Role of first user: " + getRole.apply(users.peek()));
        
        /**
         Generate a queue of users from a list
         */
        Function<List<User>, Queue<User>> generateQueue = LinkedList::new;
        Queue<User> userQueue = generateQueue.apply(users.stream().toList());
        userQueue.forEach(System.out::println);
    }
}

class User {
    private final String[] roles = {"admin", "student", "staff", "faculty"};
    private String first, last, role;

    public User(String f, String l, int r) {
        first = f;
        last = l;
        role = roles[r];
    }

    public String toString() {
        return first + " " + last + " " + role;
    }

    public String role() {
        return role;
    }

    public String last() {
        return last;
    }

    public String first() {
        return first;
    }
}