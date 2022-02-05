package ee.pacyorky.gameserver.gameserver.services.security;

import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import ee.pacyorky.gameserver.gameserver.agoraio.client.AgoraClient;
import ee.pacyorky.gameserver.gameserver.dtos.ReportDto;
import ee.pacyorky.gameserver.gameserver.entities.game.Status;
import ee.pacyorky.gameserver.gameserver.entities.security.BanInfo;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalExceptionCode;
import ee.pacyorky.gameserver.gameserver.repositories.BanInfoRepository;
import ee.pacyorky.gameserver.gameserver.repositories.dao.GameDao;
import ee.pacyorky.gameserver.gameserver.services.game.PlayerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class GameSecurityService {
    
    private final PlayerService playerService;
    private final GameDao gameDao;
    private final AgoraClient agoraClient;
    private final BanInfoRepository banInfoRepository;
    
    public void attachAgoraId(Long agoraId, String id) {
        var player = playerService.getOrCreatePlayer(id);
        player.setAgoraId(agoraId);
        playerService.savePlayer(player);
    }
    
    public void banPlayer(String moderatorId, String badPlayerId) {
        managePlayerInternal(moderatorId, badPlayerId, badPlayer -> banInternal(badPlayerId, false));
    }
    
    private void managePlayerInternal(String moderatorId, String targetPlayer, Consumer<String> targetPlayerConsumer) {
        var game = gameDao.getGame(moderatorId).orElseThrow(() -> new GlobalException("Player not in game", GlobalExceptionCode.PLAYER_NOT_IN_GAME));
        var badPlayerGame = gameDao.getGame(targetPlayer).orElseThrow(() -> new GlobalException("Target player not in game", GlobalExceptionCode.PLAYER_NOT_IN_GAME));
        if (!game.getId().equals(badPlayerGame.getId())) {
            throw new GlobalException("Games are different", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (game.getOwner() == null) {
            throw new GlobalException("Game not have owner", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (moderatorId.equals(targetPlayer)) {
            throw new GlobalException("Players are same", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (!game.getOwner().getId().equals(moderatorId)) {
            throw new GlobalException("Player is not owner of this game", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        targetPlayerConsumer.accept(targetPlayer);
    }
    
    public void banInternal(String playerId, boolean force) {
        var player = playerService.getOrCreatePlayer(playerId);
        var game = gameDao.getGame(playerId);
        game.ifPresent(game1 -> {
            game1.removePlayer(playerId);
            if (game1.getOwner() != null && game1.getOwner().getId().equals(playerId)) {
                game1.setOwner(null);
                game1.finish(Status.CANCELLED);
            }
            gameDao.saveGame(game1);
        });
        player.getBanInfo().newBan();
        if (force) {
            player.getBanInfo().setPermanentlyBanned(true);
        }
        playerService.savePlayer(player);
        agoraClient.ban(player);
    }
    
    public void switchOwner(String moderatorId, String newOwnerId) {
        managePlayerInternal(moderatorId, newOwnerId, (newOwner) -> {
            var game = gameDao.getGame(newOwner).orElseThrow();
            game.setOwner(playerService.getOrCreatePlayer(newOwner));
            gameDao.saveGame(game);
        });
    }
    
    public Optional<BanInfo> getBanInfoByPlayerId(String playerId) {
        return banInfoRepository.getBanInfoByPlayerId(playerId);
    }
    
    public void renewIp(BanInfo info, String remoteAddr, String playerId) {
        info.setIp(remoteAddr);
        banInfoRepository.save(info);
        if (info.isPermanentlyBanned()) {
            agoraClient.ban(playerService.getOrCreatePlayer(playerId));
        }
    }
    
    public void report(String id, ReportDto reportDto) {
        log.error("Message from {}, Text: {}, Target: {}", id, reportDto.getMessage(), reportDto.getTargetId());
    }
}
