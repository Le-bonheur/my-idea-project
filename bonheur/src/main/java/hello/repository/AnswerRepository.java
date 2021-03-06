package hello.repository;

import hello.entity.Answer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface AnswerRepository extends CrudRepository<Answer, Long> {

    List<Answer> getAnswersByQuestionId(Long questionId);

}
