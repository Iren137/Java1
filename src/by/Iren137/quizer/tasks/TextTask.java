package by.Iren137.quizer.tasks;

import by.Iren137.quizer.quiz.Result;

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
        this.text = text_in + "\n";
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
        if (answer.equals(text)) {
            return Result.OK;
        } else {
            return Result.WRONG;
        }
    }
}
