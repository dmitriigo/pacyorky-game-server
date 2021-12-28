package ee.pacyorky.gameserver.gameserver.agoraio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("ALL")
class ByteBuf {
    ByteBuffer buffer = ByteBuffer.allocate(1024).order(ByteOrder.LITTLE_ENDIAN);
    
    ByteBuf() {
    }
    
    ByteBuf(byte[] bytes) {
        this.buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
    }
    
    byte[] asBytes() {
        byte[] out = new byte[buffer.position()];
        buffer.rewind();
        buffer.get(out, 0, out.length);
        return out;
    }
    
    // packUint16
    ByteBuf put(short v) {
        buffer.putShort(v);
        return this;
    }
    
    ByteBuf put(byte[] v) {
        put((short) v.length);
        buffer.put(v);
        return this;
    }
    
    // packUint32
    ByteBuf put(int v) {
        buffer.putInt(v);
        return this;
    }
    
    ByteBuf put(long v) {
        buffer.putLong(v);
        return this;
    }
    
    ByteBuf put(String v) {
        return put(v.getBytes());
    }
    
    ByteBuf put(TreeMap<Short, String> extra) {
        put((short) extra.size());
        
        for (Map.Entry<Short, String> pair : extra.entrySet()) {
            put(pair.getKey());
            put(pair.getValue());
        }
        
        return this;
    }
    
    ByteBuf putIntMap(TreeMap<Short, Integer> extra) {
        put((short) extra.size());
        
        for (Map.Entry<Short, Integer> pair : extra.entrySet()) {
            put(pair.getKey());
            put(pair.getValue());
        }
        
        return this;
    }
    
    short readShort() {
        return buffer.getShort();
    }
    
    
    int readInt() {
        return buffer.getInt();
    }
    
    byte[] readBytes() {
        short length = readShort();
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return bytes;
    }
    
    String readString() {
        byte[] bytes = readBytes();
        return new String(bytes);
    }
    
    TreeMap<Short, String> readMap() {
        TreeMap<Short, String> map = new TreeMap<>();
        
        short length = readShort();
        
        for (short i = 0; i < length; ++i) {
            short k = readShort();
            String v = readString();
            map.put(k, v);
        }
        
        return map;
    }
    
    TreeMap<Short, Integer> readIntMap() {
        TreeMap<Short, Integer> map = new TreeMap<>();
        
        short length = readShort();
        
        for (short i = 0; i < length; ++i) {
            short k = readShort();
            Integer v = readInt();
            map.put(k, v);
        }
        
        return map;
    }
}
