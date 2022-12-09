package by.Iren137.quizer.task_generators;

import by.Iren137.quizer.exceptions.EmptyGenerators;
import by.Iren137.quizer.tasks.Task;

import java.util.*;

import static java.lang.Math.abs;

public class PoolTaskGenerator implements Task.Generator {
    boolean isAllowDuplicate;
    List<Task> tasks = new ArrayList<>();

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
        this.tasks = removeDuplication(Arrays.stream(tasks).toList());
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
        this.tasks = removeDuplication(tasks.stream().toList());
    }

    public static ArrayList<Task> removeDuplication(List<Task> taskArrayList) {
        ArrayList<Task> tasksArray = new ArrayList<>(taskArrayList);
        for (int i = 0; i < tasksArray.size(); i++) {
            for (int j = i + 1; j < tasksArray.size(); j++) {
                if (tasksArray.get(i).equals(tasksArray.get(j))) {
                    tasksArray.remove(tasksArray.get(j));
                    j = i;
                }
            }
        }
        return tasksArray;
    }

    /**
     * @return случайная задача из списка
     */
    @Override
    public Task generate() {
        if (this.tasks.size() > 0) {
            final Random random = new Random();
            int num = abs(random.nextInt()) % this.tasks.size();
            Task out = this.tasks.get(num);
            if (!this.isAllowDuplicate) {
                this.tasks.remove(num);
            }
            return out;
        } else {
            throw new IllegalArgumentException("Tasks are empty");
        }
    }

    public int size() {
        return this.tasks.size();
    }
}
