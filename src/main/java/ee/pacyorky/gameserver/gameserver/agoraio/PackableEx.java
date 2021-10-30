package ee.pacyorky.gameserver.gameserver.agoraio;

interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
