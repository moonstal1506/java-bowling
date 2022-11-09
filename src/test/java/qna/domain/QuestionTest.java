package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("로그인 사용자와 질문한 사람이 같은 경우 삭제 가능하다.")
    @Test
    void deleteByLoginUser() {
        List<DeleteHistory> deleteHistories = Q1.delete(JAVAJIGI);
        assertThat(deleteHistories).hasSize(1);
        assertThat(Q1.isDeleted()).isTrue();
    }

    @DisplayName("로그인 사용자와 질문한 사람이 다른 경우 삭제할 때 예외가 발생한다.")
    @Test
    void deleteByAnotherUser() {
        assertThatThrownBy(() -> {
            Q1.delete(SANJIGI);
        }).isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("질문자와 답변글의 모든 답변자 같은 경우 삭제가 가능하다.")
    @Test
    void deleteQnAByEqualUser() {
        Q1.addAnswer(A1);
        Q1.delete(JAVAJIGI);

        assertThat(Q1.isDeleted()).isTrue();
        assertThat(A1.isDeleted()).isTrue();
    }

    @DisplayName("질문자와 답변글의 모든 답변자 다를 경우 예외가 발생한다.")
    @Test
    void deleteQnAByDifferentUser() {
        Q1.addAnswer(A2);

        assertThatThrownBy(() -> {
            Q1.delete(JAVAJIGI);
        }).isInstanceOf(CannotDeleteException.class);
    }

}
