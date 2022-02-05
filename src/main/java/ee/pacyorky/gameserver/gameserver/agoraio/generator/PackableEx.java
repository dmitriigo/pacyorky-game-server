package ee.pacyorky.gameserver.gameserver.agoraio.generator;

interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
