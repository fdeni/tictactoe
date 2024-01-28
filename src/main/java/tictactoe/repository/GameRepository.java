package tictactoe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tictactoe.entity.Game;
import tictactoe.entity.User;

@Repository
@Transactional(readOnly = true)
public interface GameRepository extends JpaRepository<Game, Long> {

    Game findFirstByAppUserOrderByIdDesc(User appUser);

    @Modifying
    @Transactional
    @Query("DELETE FROM Game WHERE appUser = :appUser")
    void deleteUserGames(@Param("appUser") User appUser);

}
