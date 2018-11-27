import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

public class FuzzySet {
    public enum Type {
        Triangle,
        Trapezoidal
    }

    private String name;
    private Type type;
    private ArrayList<Double> rangePoints;

    public FuzzySet() {
        rangePoints = new ArrayList<>();
    }

    public FuzzySet(String name, Type type, ArrayList<Double> rangePoints) {
        this.name = name;
        this.type = type;
        this.rangePoints = rangePoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ArrayList<Double> getRangePoints() {
        return rangePoints;
    }

    public void setRangePoints(ArrayList<Double> rangePoints) {
        this.rangePoints = rangePoints;
    }

    public Double intersectionPoint(Double value) {
        Double y = 0.0, x = value;
        for (int i = 1; i < rangePoints.size(); i++) {
            if (rangePoints.get(i - 1) <= x && x <= rangePoints.get(i)) {
                y = calculateY(rangePoints.get(i - 1), mapRangeToY(i - 1), rangePoints.get(i), mapRangeToY(i), x);
                break;
            }
        }
        return y;
    }

    private Double mapRangeToY(int i) {
        if (type.equals(Type.Triangle)) {
            return Double.valueOf(i % 2);
        } else if (type.equals(Type.Trapezoidal)) {
            if (i == 0 || i == 3) {
                return 0.0;
            } else if (i == 1 || i == 2) {
                return 1.0;
            } else {
                return -1.0;
            }
        } else {
            return -1.0;
        }
    }

    private Double calculateY(Double x1, Double y1, Double x2, Double y2, Double x3) {
        Double m = 0.0, b, y;
        if (!x1.equals(x2)) {
            m = (y2 - y1) / (x2 - x1);
        }
        b = y1 - m*x1;
        y = m * x3 + b;
//        System.out.println(x1+" "+y1+" "+x2+" "+y2+" "+x3+" "+y+" "+m+" "+b);
        return y;
    }

    public Double calculateCentroid(){
        Double cX=0.0, cY = 0.0, A = 0.0;
        for (int i=0;i<rangePoints.size()-1;i++){
            A += (rangePoints.get(i)*mapRangeToY(i+1) - rangePoints.get(i+1)*mapRangeToY(i));
            cX += ((rangePoints.get(i)+rangePoints.get(i+1))*(rangePoints.get(i)*mapRangeToY(i+1) - rangePoints.get(i+1)*mapRangeToY(i)));
            cY += ((mapRangeToY(i)+mapRangeToY(i+1))*(rangePoints.get(i)*mapRangeToY(i+1) - rangePoints.get(i+1)*mapRangeToY(i)));
        }
        A/=2.0;
        cX /= (6*A);
        cY /= (6*A);
        return cX;
    }

    public void constructFuzzySet(Scanner input) {
//        System.out.println("Fuzzy set Name");
        name = input.next();
        String setType;
        do {
//            System.out.println("Fuzzy set type (\"trapezoidal\" or \"triangle\")");
            setType = input.next();
        } while (!setType.toLowerCase().equals("trapezoidal") && !setType.toLowerCase().equals("triangle"));
        if (setType.equals("trapezoidal")) {
            type = Type.Trapezoidal;
//            System.out.println("Enter Trapezoidal ranges");
            for (int i = 0; i < 4; i++) {
//                System.out.println("i-> "+i);
                rangePoints.add(input.nextDouble());
            }
//            System.out.println("Ranges Done");
        } else {
            type = Type.Triangle;
//            System.out.println("Enter Triangle Ranges");
            for (int i = 0; i < 3; i++) {
//                System.out.println("i-> "+i);
                rangePoints.add(input.nextDouble());
            }
//            System.out.println("Ranges Done");
        }
    }

//    @Override
//    public String toString() {
//        String toReturn = name + "\n\t";
//        return super.toString();
//    }
}
