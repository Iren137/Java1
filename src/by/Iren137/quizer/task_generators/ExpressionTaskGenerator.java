package by.Iren137.quizer.task_generators;

import by.Iren137.quizer.tasks.ExpressionTask;

public class ExpressionTaskGenerator implements TaskGenerator {
    int minNumberEx;
    int maxNumberEx;
    boolean generateSumEx;
    boolean generateDifferenceEx;
    boolean generateMultiplicationEx;
    boolean generateDivisionEx;

    /**
     * @param minNumber              минимальное число
     * @param maxNumber              максимальное число
     * @param generateSum            разрешить генерацию с оператором +
     * @param generateDifference     разрешить генерацию с оператором -
     * @param generateMultiplication разрешить генерацию с оператором *
     * @param generateDivision       разрешить генерацию с оператором /
     */
    public ExpressionTaskGenerator(
            int minNumber,
            int maxNumber,
            boolean generateSum,
            boolean generateDifference,
            boolean generateMultiplication,
            boolean generateDivision
    ) {
        minNumberEx = minNumber;
        maxNumberEx = maxNumber;
        generateSumEx = generateSum;
        generateDifferenceEx = generateDifference;
        generateMultiplicationEx = generateMultiplication;
        generateDivisionEx = generateDivision;
    }

    /**
     * return задание типа {@link ExpressionTask}
     */
    public ExpressionTask generate() {
        return new ExpressionTask(this.minNumberEx, this.maxNumberEx, this.generateSumEx, this.generateDivisionEx,
                this.generateMultiplicationEx, this.generateDivisionEx);
    }
}
