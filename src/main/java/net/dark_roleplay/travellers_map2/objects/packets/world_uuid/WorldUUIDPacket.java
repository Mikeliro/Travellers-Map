package net.dark_roleplay.travellers_map2.objects.packets.world_uuid;

import java.util.UUID;

public class WorldUUIDPacket {

    private UUID worldUUID = null;

    public UUID getWorldUUID() {
        return worldUUID;
    }

    public WorldUUIDPacket setWorldUUID(UUID worldUUID) {
        this.worldUUID = worldUUID;
        return this;
    }
}
