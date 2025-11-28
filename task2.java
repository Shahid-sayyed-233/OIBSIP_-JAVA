import java.util.Scanner;
import java.util.Random;

public class task2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();

        int maxAttempts = 5; 
        int numberToGuess = rand.nextInt(100) + 1;
        boolean guessedCorrectly = false;

        System.out.println("Welcome to Guess the Number!");
        System.out.println("I have chosen a number between 1 and 100.");
        System.out.println("You have only " + maxAttempts + " attempts to guess it!");

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            System.out.print("Attempt " + attempt + ": Enter your guess: ");
            int userGuess = sc.nextInt();

            if (userGuess == numberToGuess) {
                System.out.println(" Correct! You guessed the number in " + attempt + " attempts.");
                guessedCorrectly = true;
                break;
            } else if (userGuess < numberToGuess) {
                System.out.println("Too low!");
            } else {
                System.out.println("Too high!");
            }
        }

        if (!guessedCorrectly) {
            System.out.println(" Out of attempts! The number was " + numberToGuess);
        }

        sc.close();
    }
}