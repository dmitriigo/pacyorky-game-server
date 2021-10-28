package ee.pacyorky.gameserver.gameserver.agario;

interface Packable {
    ByteBuf marshal(ByteBuf out);
}
