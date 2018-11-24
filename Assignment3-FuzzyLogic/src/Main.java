import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        FuzzySet test = new FuzzySet();
        test.setName("Low");
        test.setType(FuzzySet.Type.Triangle);
        test.setRangePoints(new ArrayList<>(Arrays.asList(0.0, 0.0, 50.0)));

        System.out.println(test.intersectionPoint(60.0));
    }
}
