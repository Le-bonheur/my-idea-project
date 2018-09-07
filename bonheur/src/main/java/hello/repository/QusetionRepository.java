package hello.repository;

import hello.entity.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface QusetionRepository extends CrudRepository<Question, Long> {

    List<Question> getQuestionsByThemeId(Long themeId);
}
