//package by.Iren137.quizer.task_generators;
//
//import by.Iren137.quizer.quiz.Operations;
//import by.Iren137.quizer.tasks.math_tasks.EquationTask;
//
//import java.util.ArrayList;
//import java.util.EnumSet;
//
//public class EquationTaskGenerator implements TaskGenerator {
//    ArrayList<EquationTask> tasks = new ArrayList<>();
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
//    public EquationTaskGenerator(double minNumber, double maxNumber, EnumSet<Operations> operations, int precision) {
//        this.tasks.add(new EquationTask(minNumber, maxNumber, operations, precision));
//    }
//
//    public void Add(double minNumber, double maxNumber, EnumSet<Operations> operations, int precision) {
//        this.tasks.add(new EquationTask(minNumber, maxNumber, operations, precision));
//    }
//
//    public EquationTaskGenerator() {
//    }
//
//    /**
//     * return задание типа {@link EquationTask}
//     */
//    public EquationTask generate() {
//        EquationTask out = tasks.get(current);
//        current++;
//        return out;
//    }
//}
