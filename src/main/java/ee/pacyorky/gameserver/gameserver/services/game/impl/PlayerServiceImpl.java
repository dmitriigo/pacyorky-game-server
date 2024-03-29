package ee.pacyorky.gameserver.gameserver.services.game.impl;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import ee.pacyorky.gameserver.gameserver.entities.game.Game;
import ee.pacyorky.gameserver.gameserver.entities.game.Player;
import ee.pacyorky.gameserver.gameserver.repositories.GameRepository;
import ee.pacyorky.gameserver.gameserver.repositories.PlayerRepository;
import ee.pacyorky.gameserver.gameserver.services.game.PlayerService;
import lombok.AllArgsConstructor;

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
            game.setStep(null);
            Set<Player> players = game.getPlayers();
            for (Player player : players) {
                game.removePlayer(player.getId());
            }
            gameRepository.save(game);
        }
        playerRepository.deleteAll();
    }
}
