package by.Iren137.quizer.task_generators;

import by.Iren137.quizer.tasks.EquationTask;

public class EquationTaskGenerator implements TaskGenerator {
    int minNumberEq;
    int maxNumberEq;
    boolean generateSumEq;
    boolean generateDifferenceEq;
    boolean generateMultiplicationEq;
    boolean generateDivisionEq;

    /**
     * @param minNumber              минимальное число
     * @param maxNumber              максимальное число
     * @param generateSum            разрешить генерацию с оператором +
     * @param generateDifference     разрешить генерацию с оператором -
     * @param generateMultiplication разрешить генерацию с оператором *
     * @param generateDivision       разрешить генерацию с оператором /
     */
    public EquationTaskGenerator(
            int minNumber,
            int maxNumber,
            boolean generateSum,
            boolean generateDifference,
            boolean generateMultiplication,
            boolean generateDivision
    ) {
        minNumberEq = minNumber;
        maxNumberEq = maxNumber;
        generateSumEq = generateSum;
        generateDifferenceEq = generateDifference;
        generateMultiplicationEq = generateMultiplication;
        generateDivisionEq = generateDivision;
    }

    /**
     * return задание типа {@link EquationTask}
     */
    @Override
    public EquationTask generate() {
        return new EquationTask(this.minNumberEq, this.maxNumberEq, this.generateSumEq, this.generateDifferenceEq,
                this.generateMultiplicationEq, this.generateDivisionEq);
    }
}
