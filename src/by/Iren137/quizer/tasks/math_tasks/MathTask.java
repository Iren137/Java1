package by.Iren137.quizer.tasks.math_tasks;

import by.Iren137.quizer.tasks.Task;

public interface MathTask extends Task {
    interface Generator extends Task.Generator {
        double getMinNumber();

        double getMaxNumber();

        /**
         * @return разница между максимальным и минимальным возможным числом
         */
        default double getDiffNumber() {
            return getMaxNumber() - getMinNumber();
        }
    }
}
