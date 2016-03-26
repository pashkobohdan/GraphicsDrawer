package library.doubleFunctions;

import java.util.LinkedList;
import java.util.List;

public class Dividers {

    public static int getMinDivider(double number) {
        String numberString = number + "";

        if (numberString.contains("E")) {
            return Integer.parseInt(numberString.substring(numberString.indexOf("E") + 1));
        }


        if (numberString.contains(".")) {
            if (number > 1) {
                return numberString.substring(0, numberString.indexOf('.')).length() - 1;
            } else {
                int result = -1;
                int i = 2;
                while (i < numberString.length() && numberString.charAt(i) == '0') {
                    i++;
                    result--;
                }
                return result;
            }
        } else {
            return (numberString).length() - 1;
        }
    }

    public static double get125PowerMinDivider(double min) {
        int minDivider = getMinDivider(min);
        double minPower;

        minPower = Math.pow(10.0, minDivider);

        return minPower >= min ? minPower :
                2.0 * minPower >= min ? 2.0 * minPower :
                        5.0 * minPower >= min ? 5.0 * minPower :
                                10.0 * minPower >= min ? 10.0 * minPower :
                                        20.0 * minPower >= min ? 20.0 * minPower : 50.0 * minPower;

    }

    public static List<Double> getListOfDividers(double leftEdge, double rightEdge, double minLength) {
        List<Double> result = new LinkedList<>();
        double step = get125PowerMinDivider(minLength);

        for (double i = (int) (leftEdge / step + 1) * step; i <= rightEdge; i += step) {
            result.add(doubleToNormalValue(i));
        }

        return result;
    }

    public static List<Double> getListOfDividersFromCount(double leftEdge, double rightEdge, int maxCount) {
        return getListOfDividers(leftEdge, rightEdge, (rightEdge - leftEdge) / maxCount);
    }

    public static double doubleToNormalValue(double value) {
        String valueString = value + "";
        if (!valueString.contains(".")) {
            return value;
        }

        int dotPosition = valueString.indexOf('.');
        if (valueString.substring(dotPosition + 1, valueString.length()).length() < 7) {
            return value;
        }

        if (valueString.contains("E-")) {
            return Double.parseDouble(doubleToNormalValue(Double.parseDouble(valueString.substring(0, valueString.indexOf("E-")))) + valueString.substring(valueString.indexOf("E")));
        }

        if (valueString.substring(dotPosition + 1, dotPosition + 5).equals("9999")) {
            return Double.parseDouble(valueString.substring(0, dotPosition - 1) +
                    (Integer.parseInt(valueString.charAt(dotPosition - 1) + "") + 1) + ".0");
        }

        int index = dotPosition + 1;
        while (index + 5 < valueString.length()) {
            if (valueString.substring(index + 1, index + 5).equals("0000")) {
                return Double.parseDouble(valueString.substring(0, index + 1));
            }

            if (valueString.substring(index + 1, index + 5).equals("9999")) {
                return Double.parseDouble(valueString.substring(0, index) + (Integer.parseInt(valueString.charAt(index) + "") + 1));
            }
            index++;
        }
        return value;
    }
}
