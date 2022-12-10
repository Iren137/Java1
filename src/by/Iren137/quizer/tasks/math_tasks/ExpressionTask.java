package by.Iren137.quizer.tasks.math_tasks;

import by.Iren137.quizer.exceptions.EmptyArgumentsException;
import by.Iren137.quizer.quiz.enums.Operations;
import by.Iren137.quizer.quiz.enums.Result;
import by.Iren137.quizer.tasks.Task;

import java.util.ArrayList;
import java.util.EnumSet;

public class ExpressionTask extends AbstractMathTask {
    private final double answer;

    public ExpressionTask(double minNumber, double maxNumber, EnumSet<Operations> operations, int precision) {
        super(minNumber, maxNumber, operations, precision);

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

    public String getAnswer() {
        return this.getAnswer(answer);
    }

    @Override
    public String getText() {
        String out = "";
        if (this.getPrecision() == 0) {
            out += (int) num1;
        } else {
            out += num1;
        }
        switch (this.getOperator()) {
            case MINUS -> out += " - ";
            case PLUS -> out += " + ";
            case MUL -> out += " * ";
            case DIV -> out += " / ";
        }
        if (this.getPrecision() == 0) {
            out += (int) num2;
        } else {
            out += num2;
        }
        out += " = ";
        return out;
    }

    @Override
    public Result validate(String input_answer) {
        return this.validate(input_answer, answer);
    }

    public static class Generator extends AbstractMathTask.Generator {
        ArrayList<ExpressionTask> tasks = new ArrayList<>();
        int current = 0;

        public Generator() {
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
