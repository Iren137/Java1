package by.Iren137.quizer.tasks.math_tasks;

import by.Iren137.quizer.exceptions.EmptyArgumentsException;
import by.Iren137.quizer.quiz.Operations;
import by.Iren137.quizer.quiz.Result;
import by.Iren137.quizer.tasks.Operator;
import by.Iren137.quizer.tasks.Task;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Random;

import static java.lang.Math.abs;

public class EquationTask extends AbstractMathTask {
    private final double x;
    private final boolean is_x_first;

    public EquationTask(double minNumber, double maxNumber, EnumSet<Operations> operations, int precision1) {
        super(minNumber, maxNumber, operations, precision1);
        if (operations == null) {
            throw new IllegalArgumentException("Operation is null");
        }
        if (maxNumber == 0 && this.getOperator() == Operator.DIV) {
            throw new IllegalArgumentException("The expression is \"a / 0 = b\"");
        }
        Operator operator = this.getOperator();
        if (minNumber == maxNumber && minNumber == 0 && (operator == Operator.DIV || operator == Operator.MUL)) {
            throw new IllegalArgumentException("You've been caught by bad random. LOOSER!");
        }
        final Random random = new Random();
        this.is_x_first = random.nextBoolean();
        double x = 0;
        switch (operator) {
            case PLUS -> x = this.getNumber2() - this.getNumber1();
            case MINUS -> {
                if (is_x_first) {
                    x = this.getNumber2() + this.getNumber1();
                } else {
                    x = this.getNumber1() - this.getNumber2();
                }
            }
            case MUL -> {
                if (this.getNumber2() == 0) {
                    throw new IllegalArgumentException("You've been caught by bad random. Division to 0!");
                }
                x = this.getNumber2() / this.getNumber1();
            }
            case DIV -> {
                if (this.is_x_first) {
                    x = this.getNumber2() * this.getNumber1();
                } else {
                    if (this.getNumber2() == 0) {
                        throw new IllegalArgumentException("You've been caught by bad random. Division to 0!");
                    }
                    x = this.getNumber2() / this.getNumber1();
                }
            }
            case ERROR -> throw new EmptyArgumentsException();
        }
        this.x = x;
    }

    @Override
    public String getText() {
        String out = "";
        if (is_x_first) {
            out += "x";
        } else {
            out += this.getNumber1();
        }
        switch (operator) {
            case MINUS -> out += " - ";
            case PLUS -> out += " + ";
            case MUL -> out += " * ";
            case DIV -> out += " / ";
        }
        if (!is_x_first) {
            out += "x";
        } else {
            out += this.getNumber1();
        }
        out += " = ";
        out += this.getNumber2();
        out += '\n';
        out += "x = ";
        return out;
    }

    @Override
    public Result validate(String answer) {
        double value;
        try {
            value = Double.parseDouble(answer);
            double accuracy = 1.0;
            for (int i = 0; i < precision; i++) {
                accuracy /= 10;
            }
            if (abs(value - x) < accuracy) {
                return Result.OK;
            } else {
                return Result.WRONG;
            }
        } catch (NumberFormatException e) {
            return Result.INCORRECT_INPUT;
        }
    }

    public static class Generator extends AbstractMathTask.Generator {
        ArrayList<EquationTask> tasks = new ArrayList<>();
        int current = 0;

        /**
         * @param minNumber минимальное число
         * @param maxNumber максимальное число
         *                  //     * @param generateSum            разрешить генерацию с оператором +
         *                  //     * @param generateDifference     разрешить генерацию с оператором -
         *                  //     * @param generateMultiplication разрешить генерацию с оператором *
         *                  //     * @param generateDivision       разрешить генерацию с оператором /
         */
        public Generator(double minNumber, double maxNumber, EnumSet<Operations> operations, int precision) {
            super(minNumber, maxNumber, operations, precision);
        }

        public void Add(double minNumber, double maxNumber, EnumSet<Operations> operations, int precision) {
            this.tasks.add(new EquationTask(minNumber, maxNumber, operations, precision));
        }

        /**
         * return задание типа {@link EquationTask}
         */
        public Task generate() {
            EquationTask out = this.tasks.get(current);
            current++;
            return out;
        }
    }
}
