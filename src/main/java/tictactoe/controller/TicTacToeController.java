package tictactoe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tictactoe.entity.User;
import tictactoe.entity.Game;
import tictactoe.repository.UserRepository;
import tictactoe.service.GameService;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class TicTacToeController {

    @Value("${board.array.size}")
    private int arraySize;
    private final GameService gameService;

    private final UserRepository appUserRepository;

    @Autowired
    public TicTacToeController(GameService gameService, UserRepository appUserRepository) {
        this.gameService = gameService;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping
    public String index(Principal principal, Model model) {
        User appUser = getAppUser(principal);
        Game game = gameService.getLastGame(appUser);
        if (game == null) {
            game = gameService.create(appUser, true, arraySize);
        }
        gameService.setModelGameAttributes(model, game);
        return "index";
    }

    @PostMapping
    public String takeTurns(
            Model model,
            Principal principal,
            @RequestParam("tile_id") String tileId,
            @RequestParam(value = "new_game", required = false, defaultValue = "false") boolean newGame,
            @RequestParam(value = "player_go_first", required = false, defaultValue = "false") boolean playerGoFirst
    ) {
        User appUser = getAppUser(principal);
        Game game = gameService.handleGameActions(appUser, tileId, newGame, playerGoFirst, arraySize);
        gameService.setModelGameAttributes(model, game);
        return "index";
    }

    private User getAppUser(Principal principal) {
        User appUser = appUserRepository.findByUsername(principal.getName());
        if (appUser == null) {
            throw new UsernameNotFoundException("Invalid username: " + principal.getName());
        }
        return appUser;
    }
}
