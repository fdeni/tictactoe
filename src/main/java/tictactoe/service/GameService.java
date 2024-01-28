package tictactoe.service;

import org.springframework.ui.Model;
import tictactoe.entity.User;
import tictactoe.entity.Game;

public interface GameService {
    Game create(User appUser, boolean playerGoFirst, int arraySize);

    Game getLastGame(User appUser);

    void takeTurnRandom(Game game, int arraySize);

    void takeTurn(Game game, String tileId, int arraySize);

    void setModelGameAttributes(Model model, Game game);

    Game handleGameActions(User appUser, String tileId, boolean newGame, boolean playerGoFirst, int arraySize);
}
