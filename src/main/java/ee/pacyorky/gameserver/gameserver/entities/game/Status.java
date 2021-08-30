package ee.pacyorky.gameserver.gameserver.entities.game;

public enum Status {
    WAITING("Waiting", 1L),
    STARTED("Started", 2L),
    FINISHED("Finished", 3L),
    CANCELLED("Cancelled", 4L);
    private final String name;
    private final Long id;

    Status(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public static Status getById(Long id) {
        for (Status status : Status.values()) {
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
