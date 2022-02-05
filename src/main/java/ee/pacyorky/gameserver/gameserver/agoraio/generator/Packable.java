package ee.pacyorky.gameserver.gameserver.agoraio.generator;

interface Packable {
    ByteBuf marshal(ByteBuf out);
}
