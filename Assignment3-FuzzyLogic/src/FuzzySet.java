import java.util.ArrayList;

public class FuzzySet {
    public enum Type {
        Triangle,
        Trapezoidal
    }

    private String name;
    private Type type;
    private ArrayList<Double> rangePoints;

    public FuzzySet() {
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
            if (rangePoints.get(i-1) <= x && x <= rangePoints.get(i)) {
                y = calculateY(rangePoints.get(i-1), mapRangeToY(i-1), rangePoints.get(i), mapRangeToY(i), x);
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

    private Double calculateY(Double x1, Double y1, Double x2, Double y2, Double x3){
        Double m = 0.0, b = y1, y = 0.0;
        if(!x1.equals(x2)){
            m = (y2-y1)/(x2-x1);
        }
        else {
            b = 1.0;
        }
        y = m*x3 + b;
//        System.out.println(x1+" "+y1+" "+x2+" "+y2+" "+x3+" "+y+" "+m+" "+b);
        return y;
    }
}
