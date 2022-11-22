package by.Iren137.quizer.tasks.math_tasks;

import by.Iren137.quizer.exceptions.EmptyArgumentsException;
import by.Iren137.quizer.quiz.enums.Operations;
import by.Iren137.quizer.quiz.enums.Result;
import by.Iren137.quizer.tasks.Operator;
import by.Iren137.quizer.tasks.Task;

import java.util.ArrayList;
import java.util.EnumSet;

import static java.lang.Math.abs;

public class ExpressionTask extends AbstractMathTask {
    private final double answer;

    public ExpressionTask(double minNumber, double maxNumber, EnumSet<Operations> operations, int precision1) {
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
        double answer = 0;
        switch (operator) {
            case PLUS -> answer = this.getNumber1() + this.getNumber2();
            case MINUS -> answer = this.getNumber1() - this.getNumber2();
            case MUL -> answer = this.getNumber1() * this.getNumber2();
            case DIV -> {
                if (this.getNumber2() == 0) {
                    throw new IllegalArgumentException("You've been caught by bad random. Division to 0!");
                }
                answer = this.getNumber1() / this.getNumber2();
            }
            case ERROR -> throw new EmptyArgumentsException();
        }
        this.answer = answer;
    }

    @Override
    public String getText() {
        String out = "";
        out += num1;
        switch (this.getOperator()) {
            case MINUS -> out += " - ";
            case PLUS -> out += " + ";
            case MUL -> out += " * ";
            case DIV -> out += " / ";
        }
        out += num2;
        out += " = ";
        return out;
    }

    @Override
    public Result validate(String input_answer) {
        double value;
        double accuracy = 1.0;
        for (int i = 0; i < precision; i++) {
            accuracy /= 10;
        }
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

    public static class Generator extends AbstractMathTask.Generator {
        ArrayList<ExpressionTask> tasks = new ArrayList<>();
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
            this.tasks.add(new ExpressionTask(minNumber, maxNumber, operations, precision));
        }

        /**
         * return задание типа {@link EquationTask}
         */
        public Task generate() {
            ExpressionTask out = this.tasks.get(current);
            current++;
            return out;
        }
    }
}
