package by.Iren137.quizer.task_generators;

import by.Iren137.quizer.tasks.Task;
import by.Iren137.quizer.tasks.TextTask;

import java.util.ArrayList;

public class TextTaskGenerator implements TaskGenerator {
    ArrayList<TextTask> textTasks = new ArrayList<>();
    int current = 0;

    public void Add(String text, String answer) {
        textTasks.add(new TextTask(text, answer));
    }

    @Override
    public Task generate() {
        Task out = this.textTasks.get(current);
        current++;
        return out;
    }
}
