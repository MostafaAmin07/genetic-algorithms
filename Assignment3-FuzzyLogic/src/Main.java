import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        FuzzyLogic fuzzyLogic = new FuzzyLogic();
        fuzzyLogic.constructProblem(input);
        fuzzyLogic.predict();
        input.close();
    }
}
