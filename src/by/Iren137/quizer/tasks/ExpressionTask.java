package by.Iren137.quizer.tasks;

import by.Iren137.quizer.quiz.Result;

import java.util.Random;

import static by.Iren137.quizer.Constants.accuracy;
import static java.lang.Math.abs;

public class ExpressionTask implements Task {
    int num1;
    int num2;
    Operator operator;
    double answer;

    public ExpressionTask(int minNumber,
                          int maxNumber,
                          boolean generateSum,
                          boolean generateDifference,
                          boolean generateMultiplication,
                          boolean generateDivision) {
        final Random random = new Random();
        num1 = random.nextInt() % (maxNumber - minNumber + 1) + minNumber;
        num2 = random.nextInt() % (maxNumber - minNumber + 1) + minNumber;
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
        switch (operator) {
            case PLUS -> answer = num1 + num2;
            case MINUS -> answer = num1 - num2;
            case MUL -> answer = num1 * num2;
            case DIV -> answer = num1 * 1.0 / num2;
        }
    }

    @Override
    public String getText() {
        String out = "";
        out += num1;
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
        out += num2;
        out += " = ";
        return out;
    }

    @Override
    public Result validate(String input_answer) {
        double value;
        try {
            value = Double.parseDouble(input_answer);
            if (abs(value - answer) < accuracy) {
                return Result.OK;
            } else {
                return Result.WRONG;
            }
        } catch (NumberFormatException e) {
            return Result.INCORRECT_INPUT;
        }
    }
}
