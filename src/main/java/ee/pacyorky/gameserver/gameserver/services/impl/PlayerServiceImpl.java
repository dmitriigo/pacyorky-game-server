package ee.pacyorky.gameserver.gameserver.services.impl;

import ee.pacyorky.gameserver.gameserver.entities.Game;
import ee.pacyorky.gameserver.gameserver.entities.Player;
import ee.pacyorky.gameserver.gameserver.repositories.GameRepository;
import ee.pacyorky.gameserver.gameserver.repositories.PlayerRepository;
import ee.pacyorky.gameserver.gameserver.services.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;


    @Override
    public Player getOrCreatePlayer(String id) {
        Player player = playerRepository.findById(id).orElse(null);
        if (player == null) player = playerRepository.save(Player.builder().id(id).build());
        return player;
    }

    @Override
    public Player savePlayer(Player player) {
        return playerRepository.saveAndFlush(player);
    }

    @Override
    public void clearPlayers() {
        List<Game> all = gameRepository.findAll();
        for (Game game : all) {
            game.setCurrentPlayer(null);
            Set<Player> players = game.getPlayers();
            for (Player player : players) {
                game.removePlayer(player.getId());
            }
            gameRepository.save(game);
        }
        playerRepository.deleteAll();
    }
}
