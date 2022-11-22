//package by.Iren137.quizer.task_generators;
//
//import by.Iren137.quizer.quiz.Operations;
//import by.Iren137.quizer.tasks.math_tasks.ExpressionTask;
//
//import java.util.ArrayList;
//import java.util.EnumSet;
//
//public class ExpressionTaskGenerator implements TaskGenerator {
//    ArrayList<ExpressionTask> tasks;
//    int current = 0;
//
//    /**
//     * @param minNumber              минимальное число
//     * @param maxNumber              максимальное число
////     * @param generateSum            разрешить генерацию с оператором +
////     * @param generateDifference     разрешить генерацию с оператором -
////     * @param generateMultiplication разрешить генерацию с оператором *
////     * @param generateDivision       разрешить генерацию с оператором /
//     */
//    public ExpressionTaskGenerator(double minNumber, double maxNumber, EnumSet<Operations> operations, int precision) {
//        this.tasks.add(new ExpressionTask(minNumber, maxNumber, operations, precision));
//        current = 0;
//    }
//
//    public void Add(double minNumber, double maxNumber, EnumSet<Operations> operations, int precision) {
//        this.tasks.add(new ExpressionTask(minNumber, maxNumber, operations, precision));
//    }
//
//    public ExpressionTaskGenerator() {
//    }
//
//    /**
//     * return задание типа {@link ExpressionTask}
//     */
//    public ExpressionTask generate() {
//        ExpressionTask out = tasks.get(current);
//        current++;
//        return out;
//    }
//}
