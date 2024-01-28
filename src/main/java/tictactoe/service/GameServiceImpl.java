package tictactoe.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import tictactoe.entity.Game;
import tictactoe.entity.Game.GameState;
import tictactoe.entity.Game.PlayerNumber;
import tictactoe.entity.Game.PlayerType;
import tictactoe.repository.GameRepository;
import tictactoe.entity.User;
import tictactoe.util.BoardTileEnum;
import tictactoe.util.BoardUtil;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    private static final String DEFAULT_TILE_ID = "1-1";

    @Autowired
    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    @Transactional
    public Game create(User appUser, boolean playerGoFirst, int arraySize) {
        gameRepository.deleteUserGames(appUser);

        Game game = new Game();
        game.setAppUser(appUser);
        game.setState(GameState.IN_PROGRESS);
        game.setNextMove(PlayerNumber.PLAYER_1);

        if (playerGoFirst) {
            game.setPlayer1Type(PlayerType.HUMAN);
            game.setPlayer2Type(PlayerType.COMPUTER);
        } else {
            game.setPlayer1Type(PlayerType.COMPUTER);
            game.setPlayer2Type(PlayerType.HUMAN);
        }

        game.setRows(BoardUtil.createEmptyBoard(arraySize));

        gameRepository.save(game);

        return game;
    }

    @Override
    public Game getLastGame(User appUser) {
        return gameRepository.findFirstByAppUserOrderByIdDesc(appUser);
    }

    @Override
    public void takeTurnRandom(Game game, int arraySize) {
        String tileId = BoardUtil.getRandomAvailableTile(game.getRows());
        if (tileId != null) {
            takeTurn(game, tileId, arraySize);
        }
    }

    @Override
    public void takeTurn(Game game, String tileId, int arraySize) {
        if (game.getState() != GameState.IN_PROGRESS) {
            return;
        }

        String[] indices = tileId.split("-");
        if (indices.length != 2) {
            return;
        }

        BoardTileEnum tile;
        if (game.getNextMove() == PlayerNumber.PLAYER_1) {
            tile = BoardTileEnum.X;
            game.setNextMove(PlayerNumber.PLAYER_2);
        } else {
            tile = BoardTileEnum.O;
            game.setNextMove(PlayerNumber.PLAYER_1);
        }

        int rowIndex = Integer.parseInt(indices[0]);
        int columnIndex = Integer.parseInt(indices[1]);
        game.getRows().get(rowIndex).set(columnIndex, tile.toString());

        GameState state = checkWinner(game.getRows(), arraySize);
        game.setState(state);
        if (state != GameState.IN_PROGRESS) {
            game.setNextMove(null);
        }

        gameRepository.save(game);
    }

    @Override
    public void setModelGameAttributes(Model model, Game game) {
        boolean playerGoFirst = game.getPlayer1Type() == PlayerType.HUMAN;

        String playerStatus;
        switch (game.getState()) {
            case PLAYER_1_WIN:
                playerStatus = playerGoFirst ? "WON" : "LOST";
                break;
            case PLAYER_2_WIN:
                playerStatus = playerGoFirst ? "LOST" : "WON";
                break;
            case DRAW:
                playerStatus = "DRAW";
                break;
            case IN_PROGRESS:
            default:
                playerStatus = "IN_PROGRESS";
                break;
        }

        model.addAttribute("playerGoFirst", playerGoFirst);
        model.addAttribute("playStatus", playerStatus);
        model.addAttribute("board", game.getRows());

    }

    @Override
    public Game handleGameActions(User appUser, String tileId, boolean newGame, boolean playerGoFirst, int arraySize) {
        if (newGame) {
            return handleNewGame(appUser, playerGoFirst, arraySize);
        } else {
            return handleExistingGame(appUser, tileId, arraySize);
        }
    }

    private Game handleNewGame(User appUser, boolean playerGoFirst, int arraySize) {
        Game game = create(appUser, playerGoFirst, arraySize);
        if (!playerGoFirst) {
            takeTurn(game, DEFAULT_TILE_ID, arraySize);
        }
        return game;
    }

    private Game handleExistingGame(User appUser, String tileId, int arraySize) {
        Game game = getLastGame(appUser);
        takeTurn(game, tileId, arraySize);
        takeTurnRandom(game, arraySize);
        return game;
    }

    private GameState checkWinner(List<List<String>> rows, int arraySize) {
        for (List<String> line : BoardUtil.getAllLines(rows, arraySize)) {
            String firstTile = line.get(0);
            if (firstTile.isEmpty()) {
                continue;
            }

            if (line.stream().allMatch(tile -> tile.equals(firstTile))) {
                return firstTile.equals(BoardTileEnum.X.toString()) ? GameState.PLAYER_1_WIN : GameState.PLAYER_2_WIN;
            }
        }

        for (List<String> row : rows) {
            if (row.stream().anyMatch(String::isEmpty)) {
                return GameState.IN_PROGRESS;
            }
        }

        return GameState.DRAW;
    }
}
