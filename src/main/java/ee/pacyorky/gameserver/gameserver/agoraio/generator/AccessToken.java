package ee.pacyorky.gameserver.gameserver.agoraio.generator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

@SuppressWarnings("ALL")
class AccessToken {
    enum Privileges {
        K_JOIN_CHANNEL(1),
        K_PUBLISH_AUDIO_STREAM(2),
        K_PUBLISH_VIDEO_STREAM(3),
        K_PUBLISH_DATA_STREAM(4),
        
        // For RTM only
        K_RTM_LOGIN(1000);
        
        short intValue;
        
        Privileges(int value) {
            intValue = (short) value;
        }
    }
    
    private static final String VER = "006";
    
    String appId;
    String appCertificate;
    String channelName;
    String uid;
    byte[] signature;
    byte[] messageRawContent;
    int crcChannelName;
    int crcUid;
    PrivilegeMessage message;
    int expireTimestamp;
    
    AccessToken(String appId, String appCertificate, String channelName, String uid) {
        this.appId = appId;
        this.appCertificate = appCertificate;
        this.channelName = channelName;
        this.uid = uid;
        this.crcChannelName = 0;
        this.crcUid = 0;
        this.message = new PrivilegeMessage();
    }
    
    String build() throws NoSuchAlgorithmException, InvalidKeyException {
        if (!RTCUtils.isUUID(appId)) {
            return "";
        }
        
        if (!RTCUtils.isUUID(appCertificate)) {
            return "";
        }
        
        messageRawContent = RTCUtils.pack(message);
        signature = generateSignature(appCertificate,
                appId, channelName, uid, messageRawContent);
        crcChannelName = RTCUtils.crc32(channelName);
        crcUid = RTCUtils.crc32(uid);
        
        PackContent packContent = new PackContent(signature, crcChannelName, crcUid, messageRawContent);
        byte[] content = RTCUtils.pack(packContent);
        return getVersion() + this.appId + RTCUtils.base64Encode(content);
    }
    
    void addPrivilege(Privileges privilege, int expireTimestamp) {
        message.messages.put(privilege.intValue, expireTimestamp);
    }
    
    static String getVersion() {
        return VER;
    }
    
    static byte[] generateSignature(String appCertificate,
                                    String appID, String channelName, String uid, byte[] message) throws NoSuchAlgorithmException, InvalidKeyException {
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(appID.getBytes());
            baos.write(channelName.getBytes());
            baos.write(uid.getBytes());
            baos.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RTCUtils.hmacSign(appCertificate, baos.toByteArray());
    }
    
    class PrivilegeMessage implements PackableEx {
        int salt;
        int ts;
        TreeMap<Short, Integer> messages;
        
        PrivilegeMessage() {
            salt = RTCUtils.randomInt();
            ts = RTCUtils.getTimestamp() + 24 * 3600;
            messages = new TreeMap<>();
        }
        
        @Override
        public ByteBuf marshal(ByteBuf out) {
            return out.put(salt).put(ts).putIntMap(messages);
        }
        
        @Override
        public void unmarshal(ByteBuf in) {
            salt = in.readInt();
            ts = in.readInt();
            messages = in.readIntMap();
        }
    }
    
    class PackContent implements PackableEx {
        byte[] signature;
        int crcChannelName;
        int crcUid;
        byte[] rawMessage;
        
        PackContent(byte[] signature, int crcChannelName, int crcUid, byte[] rawMessage) {
            this.signature = signature;
            this.crcChannelName = crcChannelName;
            this.crcUid = crcUid;
            this.rawMessage = rawMessage;
        }
        
        @Override
        public ByteBuf marshal(ByteBuf out) {
            return out.put(signature).put(crcChannelName).put(crcUid).put(rawMessage);
        }
        
        @Override
        public void unmarshal(ByteBuf in) {
            signature = in.readBytes();
            crcChannelName = in.readInt();
            crcUid = in.readInt();
            rawMessage = in.readBytes();
        }
    }
}
