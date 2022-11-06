package by.Iren137.quizer.tasks;

import by.Iren137.quizer.quiz.Result;

import java.util.Random;

import static by.Iren137.quizer.Constants.accuracy;
import static java.lang.Math.abs;

public class EquationTask implements Task {

    int num;
    double x;
    boolean is_x_first;
    Operator operator;
    int answer;

    public EquationTask(int minNumber,
                        int maxNumber,
                        boolean generateSum,
                        boolean generateDifference,
                        boolean generateMultiplication,
                        boolean generateDivision) {
        final Random random = new Random();
        num = random.nextInt() % (maxNumber - minNumber + 1) + minNumber;
        answer = random.nextInt() % (maxNumber - minNumber + 1) + minNumber;
        is_x_first = random.nextBoolean();
        int mod = 0;
        if (generateSum && generateDifference && generateMultiplication && generateDivision) {
            mod = 4;
        }
        if ((generateSum && generateDifference && generateMultiplication) || (generateSum && generateDifference &&
                generateDivision) || (generateDifference && generateMultiplication && generateDivision)) {
            mod = 3;
        }
        if ((generateSum && generateDifference) || (generateSum && generateDivision) ||
                (generateSum && generateMultiplication) || (generateDifference && generateDivision) ||
                (generateDifference && generateMultiplication) || (generateDivision && generateMultiplication)) {
            mod = 2;
        }
        if (!generateSum || !generateDifference || !generateMultiplication || !generateDivision) {
            mod = 1;
        }
        if (mod == 0) {
            throw new IllegalArgumentException("No operators");
        } else {
            int k = random.nextInt() % mod;
            if (generateSum) {
                k--;
                if (k == -1) {
                    operator = Operator.PLUS;
                }
            }
            if (generateDifference) {
                k--;
                if (k == -1) {
                    operator = Operator.MINUS;
                }
            }
            if (generateMultiplication) {
                k--;
                if (k == -1) {
                    operator = Operator.MUL;
                }
            }
            if (generateDivision) {
                k--;
                if (k == -1) {
                    operator = Operator.DIV;
                }
            }
        }
        if (operator == Operator.PLUS) {
            x = answer - num;
        }
        if (operator == Operator.MINUS) {
            if (is_x_first) {
                x = answer + num;
            } else {
                x = num - answer;
            }
        }
        if (operator == Operator.MUL) {
            x = answer * 1.0 / num;
        }
        if (operator == Operator.DIV) {
            if (is_x_first) {
                x = answer * num;
            } else {
                x = num * 1.0 / answer;
            }
        }
    }

    @Override
    public String getText() {
        String out = "";
        if (is_x_first) {
            out += "x";
        } else {
            out += num;
        }
        switch (operator) {
            case MINUS: {
                out += " - ";
                break;
            }
            case PLUS: {
                out += " + ";
                break;
            }
            case MUL: {
                out += " * ";
                break;
            }
            case DIV: {
                out += " / ";
                break;
            }
        }
        if (!is_x_first) {
            out += "x";
        } else {
            out += num;
        }
        out += " = ";
        out += answer;
        return out;
    }

    @Override
    public Result validate(String answer) {
        double value;
        try {
            value = Double.parseDouble(answer);
            if (abs(value - x) < accuracy) {
                return Result.OK;
            } else {
                return Result.WRONG;
            }
        } catch (NumberFormatException e) {
            return Result.INCORRECT_INPUT;
        }
    }
}
