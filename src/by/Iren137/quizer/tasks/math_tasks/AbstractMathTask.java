package by.Iren137.quizer.tasks.math_tasks;

import by.Iren137.quizer.exceptions.EmptyArgumentsException;
import by.Iren137.quizer.quiz.enums.Operations;
import by.Iren137.quizer.quiz.enums.Result;
import by.Iren137.quizer.tasks.Operator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.EnumSet;
import java.util.Random;

import static java.lang.Math.abs;

public abstract class AbstractMathTask implements MathTask {
    protected final double num1;
    protected final double num2;
    protected final Operator operator;
    protected final int precision;

    public AbstractMathTask(double number1, double number2, EnumSet<Operations> operations, int precision) {
        if (operations == null) {
            throw new IllegalArgumentException("Operation is null");
        }
        if (number2 == 0 && this.getOperator() == Operator.DIV) {
            throw new IllegalArgumentException("The expression is \"a / 0 = b\"");
        }
        Operator operator = this.getOperator();
        if (number1 == number2 && number1 == 0 && (operator == Operator.DIV || operator == Operator.MUL)) {
            throw new IllegalArgumentException("You've been caught by bad random. LOOSER!");
        }
        this.precision = precision;
        final Random random = new Random();
        BigDecimal result = new BigDecimal(abs(random.nextDouble()) % (number2 - number1 + 1) + number1);
        num1 = result.setScale(precision, RoundingMode.UP).doubleValue();
        result = new BigDecimal(abs(random.nextDouble()) % (number2 - number1 + 1) + number1);
        num2 = result.setScale(precision, RoundingMode.DOWN).doubleValue();
        if (operations.size() == 0) {
            throw new EmptyArgumentsException();
        }
        int k = abs(random.nextInt()) % operations.size();
        Operations operation = operations.toArray(new Operations[0])[k];
        Operator temp_operator = Operator.ERROR;
        switch (operation) {
            case generateSum -> temp_operator = Operator.PLUS;
            case generateDifference -> temp_operator = Operator.MINUS;
            case generateMultiplication -> temp_operator = Operator.MUL;
            case generateDivision -> temp_operator = Operator.DIV;
        }
        this.operator = temp_operator;
    }

    public String getAnswer(double answer) {
        BigDecimal result = new BigDecimal(answer);
        result = result.setScale(precision, RoundingMode.HALF_UP);
        NumberFormat nf = new DecimalFormat("#.######");
        return nf.format(result.doubleValue());
    }

    public Result validate(String input_answer, double answer) {
        try {
            if (abs(Double.parseDouble(input_answer) - answer) < this.countAccuracy()) {
                return Result.OK;
            } else {
                return Result.WRONG;
            }
        } catch (NumberFormatException e) {
            return Result.INCORRECT_INPUT;
        }
    }

    Operator getOperator() {
        return this.operator;
    }

    public double getNumber1() {
        return this.num1;
    }


    public double getNumber2() {
        return this.num2;
    }

    public int getPrecision() {
        return this.precision;
    }

    public double countAccuracy() {
        double accuracy = 1.0;
        for (int i = 0; i < precision; i++) {
            accuracy /= 10;
        }
        return accuracy;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        AbstractMathTask task = (AbstractMathTask) obj;
        return ((this.num1 == task.num1) && (this.num2 == task.num2) && (this.operator == task.operator)
                && (this.precision == task.precision));
    }

    public static abstract class Generator implements MathTask.Generator {
        public Generator() {
        }
    }
}
