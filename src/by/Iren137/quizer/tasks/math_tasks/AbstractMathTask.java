package by.Iren137.quizer.tasks.math_tasks;

import by.Iren137.quizer.exceptions.EmptyArgumentsException;
import by.Iren137.quizer.quiz.enums.Operations;
import by.Iren137.quizer.tasks.Operator;

import java.util.EnumSet;
import java.util.Random;

import static java.lang.Math.abs;

public abstract class AbstractMathTask implements MathTask {
    protected final double minNumber;
    protected final double maxNumber;
    protected final double num1;
    protected final double num2;
    protected final Operator operator;
    protected final int precision;

    public AbstractMathTask(double number1, double number2, EnumSet<Operations> operations, int precision) {
        Operator operator1;
        this.minNumber = number1;
        this.maxNumber = number2;
        this.precision = precision;
        final Random random = new Random();
        int shift = 1;
        for (int i = 0; i < precision; i++) {
            shift *= 10;
        }
        num1 = Math.round((abs(random.nextDouble()) % (maxNumber - minNumber + 1) + minNumber) * shift) * 1.0 / shift;
        num2 = Math.round((abs(random.nextDouble()) % (maxNumber - minNumber + 1) + minNumber) * shift) * 1.0 / shift;
        int mod = operations.size();
        if (mod == 0) {
            throw new EmptyArgumentsException();
        }
        int k = abs(random.nextInt()) % mod;
        operator1 = Operator.ERROR;
        if (operations.contains(Operations.generateSum)) {
            k--;
            if (k == -1) {
                operator1 = Operator.PLUS;
            }
        }
        if (operations.contains(Operations.generateDifference) && k != -1) {
            k--;
            if (k == -1) {
                operator1 = Operator.MINUS;
            }
        }
        if (operations.contains(Operations.generateMultiplication) && k != -1) {
            k--;
            if (k == -1) {
                operator1 = Operator.MUL;
            }
        }
        if (operations.contains(Operations.generateDivision) && k != -1) {
            operator1 = Operator.DIV;
        }
        this.operator = operator1;
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

    public static abstract class Generator implements MathTask.Generator {
        protected final double minNumber;
        protected final double maxNumber;
        protected final EnumSet<Operations> operations;
        protected final int precision;

        public Generator(double minNumber, double maxNumber, EnumSet<Operations> operations, int precision) {
            if (operations == null) {
                throw new IllegalArgumentException("Operations are null");
            }
            if (operations.isEmpty()) {
                throw new IllegalArgumentException("Operations are empty");
            }
            if (maxNumber < minNumber) {
                throw new IllegalArgumentException("Incorrect input for boundaries");
            }
            this.minNumber = minNumber;
            this.maxNumber = maxNumber;
            this.operations = operations;
            this.precision = precision;
        }

        @Override
        public double getMinNumber() {
            return minNumber;
        }

        @Override
        public double getMaxNumber() {
            return maxNumber;
        }

    }
}
