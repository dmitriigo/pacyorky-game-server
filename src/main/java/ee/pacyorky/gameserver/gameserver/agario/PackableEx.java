package ee.pacyorky.gameserver.gameserver.agario;

interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
