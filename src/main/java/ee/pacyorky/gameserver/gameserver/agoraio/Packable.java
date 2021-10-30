package ee.pacyorky.gameserver.gameserver.agoraio;

interface Packable {
    ByteBuf marshal(ByteBuf out);
}
