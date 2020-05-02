package net.dark_roleplay.travellers_map.objects.data;

import net.dark_roleplay.travellers_map.TravellersMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.UUID;

public class WorldIdentifierData extends WorldSavedData {
    private static final String DATA_NAME = TravellersMap.MODID + "_world_id";

    private UUID worldUUID = null;

    public static WorldIdentifierData getWorldIdentifier(ServerWorld world){
        DimensionSavedDataManager storage = world.getSavedData();
        return storage.getOrCreate(WorldIdentifierData::new, DATA_NAME);
    }

    public WorldIdentifierData() {
        super(DATA_NAME);
    }

    @Override
    public void read(CompoundNBT nbt) {
        this.worldUUID = nbt.getUniqueId("world_uuid");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putUniqueId("world_uuid", this.worldUUID);
        return compound;
    }

    public UUID getWorldUUID(){
        if(this.worldUUID == null){
            this.worldUUID = UUID.randomUUID();
            this.markDirty();
        }
        return this.worldUUID;
    }
}
