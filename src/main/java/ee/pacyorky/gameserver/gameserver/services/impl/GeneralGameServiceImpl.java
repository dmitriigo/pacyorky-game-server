package ee.pacyorky.gameserver.gameserver.services.impl;

import ee.pacyorky.gameserver.gameserver.config.AppProperties;
import ee.pacyorky.gameserver.gameserver.entities.Card;
import ee.pacyorky.gameserver.gameserver.entities.CardType;
import ee.pacyorky.gameserver.gameserver.entities.Game;
import ee.pacyorky.gameserver.gameserver.entities.Player;
import ee.pacyorky.gameserver.gameserver.services.EventDayService;
import ee.pacyorky.gameserver.gameserver.services.GameManager;
import ee.pacyorky.gameserver.gameserver.services.GeneralGameService;
import ee.pacyorky.gameserver.gameserver.services.PlayerService;
import ee.pacyorky.gameserver.gameserver.services.impl.GameExecutors.GameStartExecutor;
import ee.pacyorky.gameserver.gameserver.services.impl.GameExecutors.GameStepExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;

@Service
public class GeneralGameServiceImpl implements GeneralGameService {

    private final PlayerService playerService;
    private final EventDayService eventDayService;
    private final ExecutorService executorService;
    private final Map<Long, Future<?>> steps = new HashMap<>();
    private final Map<Long, Future<?>> games = new HashMap<>();

    @Autowired
    public GeneralGameServiceImpl(PlayerService playerService, EventDayService eventDayService, AppProperties properties) {
        this.playerService = playerService;
        this.eventDayService = eventDayService;
        this.executorService = Executors.newFixedThreadPool(properties.getMaxGames());
    }

    public static void initPlayersCards(Player player, Game game) {
        Map<CardType, List<Card>> cardTypeListMap = game.getAllDecks();

        for (Map.Entry<CardType, List<Card>> cardTypeListEntry : cardTypeListMap.entrySet()) {
            List<Card> playersCards = player.getCardsByType(cardTypeListEntry.getKey());
            for (int i = playersCards.size(); i < 2; i++) {
                Card card = cardTypeListEntry.getValue().stream().findFirst().orElseThrow();
                player.getDeck().add(card);
                cardTypeListEntry.getValue().remove(card);
            }
        }
    }

    private BiConsumer<Long, GameManager> afterStartCallback() {
        return this::nextStep;
    }

    @Override
    public void nextStep(Long gameId, GameManager gameManager) {
        var feature = executorService.submit(new GameStepExecutor(gameManager, playerService, eventDayService, gameId));
        steps.put(gameId, feature);
    }

    @Override
    public void setUpStart(Game savedGame, GameManager gameManager) {
        var feature = executorService.submit(new GameStartExecutor(gameManager, playerService, eventDayService, savedGame.getId(), afterStartCallback()));
        games.put(savedGame.getId(), feature);
    }
}
