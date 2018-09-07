package hello.repository;

import hello.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value = "SELECT 1 FROM User u WHERE u.openId = :openId ")
    Long isUserExist(@Param("openId") String openId);

    @Query(value = "SELECT u.avatarUrl FROM User u WHERE u.openId= :openId ")
    String getAvatarUrl(@Param("openId") String openId);

    @Query(value = "SELECT u.nickName FROM User u WHERE u.openId= :openId ")
    String getNickName(@Param("openId") String openId);

    Optional<User> getUserByOpenId(String openId);

    @Query(value = "SELECT u.money FROM User u WHERE u.openId= :openId ")
    Double getMoneyByOpenId(@Param("openId")String openId);

    @Modifying
    @Query(value = "UPDATE User u SET u.times= u.times + 1 WHERE u.openId= :openId ")
    void updateTimes(@Param("openId")String openId);

    @Modifying
    @Query(value = "UPDATE User u SET u.money= u.money + 0.1 WHERE u.openId= :openId ")
    void updateMoney(@Param("openId")String openId);

    @Modifying
    @Query(value = "UPDATE User u SET u.money= :money WHERE u.openId= :openId ")
    void withdraw(@Param("openId")String openId, @Param("money") Double money);
}
