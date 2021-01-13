package ee.pacyorky.gameserver.gameserver.services.impl;

import ee.pacyorky.gameserver.gameserver.entities.Player;
import ee.pacyorky.gameserver.gameserver.repositories.PlayerRepository;
import ee.pacyorky.gameserver.gameserver.services.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;


    @Override
    public Player getOrCreatePlayer(String id) {
        return playerRepository.findById(id).orElse(playerRepository.save(Player.builder().id(id).build()));
    }

    @Override
    public Player savePlayer(Player player) {
        return playerRepository.saveAndFlush(player);
    }
}
