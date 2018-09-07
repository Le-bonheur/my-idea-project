package hello.repository;

import hello.entity.Theme;
import hello.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ThemeRepository extends CrudRepository<Theme, Long> {

//    @Query(value = "SELECT t.theme FROM Theme t WHERE t.themeId= :themeId ")
//    String getThemeByThemeId(@Param("themeId") Long themeId);

    Theme getThemeByThemeId(Long themeId);

}
