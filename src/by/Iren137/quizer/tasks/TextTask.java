package by.Iren137.quizer.tasks;

import by.Iren137.quizer.quiz.Result;

import java.util.ArrayList;

public class TextTask implements Task {
    private final String text;
    private final String answer;

    public TextTask(
            String text_in,
            String answer_in
    ) {
        if (text_in == null || answer_in == null) {
            throw new IllegalArgumentException("Text or/and answer is null");
        }
        this.text = text_in += '\n';
        this.answer = answer_in;
    }

    @Override
    public String getText() {
        return text;
    }


    public String getAnswer() {
        return answer;
    }

    @Override
    public Result validate(String answer) {
        if (answer == null || answer.length() == 0) {
            return Result.INCORRECT_INPUT;
        }
        if (answer.equals(this.answer)) {
            return Result.OK;
        } else {
            return Result.WRONG;
        }
    }
    public static class Generator implements Task.Generator {
        ArrayList<TextTask> textTasks = new ArrayList<>();
        int current = 0;

        public void Add(String text, String answer) {
            this.textTasks.add(new TextTask(text, answer));
        }

        @Override
        public Task generate() {
            Task out = this.textTasks.get(this.current);
            this.current++;
            return out;
        }
    }
}
