package by.Iren137.quizer.tasks.math_tasks;

import by.Iren137.quizer.exceptions.EmptyArgumentsException;
import by.Iren137.quizer.quiz.enums.Operations;
import by.Iren137.quizer.quiz.enums.Result;
import by.Iren137.quizer.tasks.Task;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Random;

public class EquationTask extends AbstractMathTask {
    private final double x;
    private final boolean is_x_first;

    public EquationTask(double minNumber, double maxNumber, EnumSet<Operations> operations, int precision1) {
        super(minNumber, maxNumber, operations, precision1);

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

    public String getAnswer() {
        return this.getAnswer(x);
    }

    @Override
    public String getText() {
        String out = "";
        if (is_x_first) {
            out += "x";
        } else {
            if (this.getPrecision() == 0) {
                out += (int) num1;
            } else {
                out += num1;
            }
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
            if (this.getPrecision() == 0) {
                out += (int) num1;
            } else {
                out += num1;
            }
        }
        out += " = ";
        if (this.getPrecision() == 0) {
            out += (int) num2;
        } else {
            out += num2;
        }
        out += '\n';
        out += "x = ";
        return out;
    }

    @Override
    public Result validate(String answer) {
        return this.validate(answer, x);
    }

    public static class Generator extends AbstractMathTask.Generator {
        ArrayList<EquationTask> tasks = new ArrayList<>();
        int current = 0;

        public Generator() {
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
