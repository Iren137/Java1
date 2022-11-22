package by.Iren137.quizer.task_generators;

import by.Iren137.quizer.exceptions.EmptyGenerators;
import by.Iren137.quizer.tasks.Task;

import java.util.*;

public class PoolTaskGenerator implements TaskGenerator {
    boolean isAllowDuplicate;
    ArrayList<Task> tasksP = new ArrayList<>();

    /**
     * Конструктор с переменным числом аргументов
     *
     * @param allowDuplicate разрешить повторения
     * @param tasks          задания, которые в конструктор передаются через запятую
     */
    public PoolTaskGenerator(
            boolean allowDuplicate,
            Task... tasks
    ) {
        if (tasks == null || tasks.length == 0) {
            throw new EmptyGenerators();
        }
        this.isAllowDuplicate = allowDuplicate;
        this.tasksP.addAll(Arrays.asList(tasks));
    }

    /**
     * Конструктор, который принимает коллекцию заданий
     *
     * @param allowDuplicate разрешить повторения
     * @param tasks          задания, которые передаются в конструктор в Collection (например, {@link LinkedList})
     */
    public PoolTaskGenerator(
            boolean allowDuplicate,
            Collection<Task> tasks
    ) {
        if (tasks == null || tasks.size() == 0) {
            throw new EmptyGenerators();
        }
        isAllowDuplicate = allowDuplicate;
        this.tasksP.addAll(tasks);
    }


    /**
     * @return случайная задача из списка
     */
    @Override
    public Task generate() {
        if (this.tasksP.size() > 0) {
            final Random random = new Random();
            int num = random.nextInt() % this.tasksP.size();
            Task out = this.tasksP.get(num);
            if (!this.isAllowDuplicate) {
                this.tasksP.remove(num);
            }
            return out;
        } else {
            throw new IllegalArgumentException("Tasks are empty");
        }
    }
}
