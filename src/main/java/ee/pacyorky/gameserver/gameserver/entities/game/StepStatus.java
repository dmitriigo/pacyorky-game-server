package ee.pacyorky.gameserver.gameserver.entities.game;

public enum StepStatus {

    STARTED("Started", 1L),
    WAITING_DICE("Waiting Dice", 2L),
    WAITING_CARD("Waiting Card", 3L),
    WAITING_VOTE("Waiting Vote", 4L),
    FINISHED("Finished", 5L);

    private final String name;
    private final Long id;

    StepStatus(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public static StepStatus getById(Long id) {
        for (StepStatus status : StepStatus.values()) {
            if (status.id.equals(id)) {
                return status;
            }
        }
        return null;
    }

    public Long getId() {
        return this.id;
    }
}
