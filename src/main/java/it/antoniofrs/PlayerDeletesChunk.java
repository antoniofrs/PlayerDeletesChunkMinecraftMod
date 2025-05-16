package it.antoniofrs;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(PlayerDeletesChunk.MOD_ID)
public class PlayerDeletesChunk {
    public static final String MOD_ID = "player_deletes_chunks";

    public PlayerDeletesChunk() {
        MinecraftForge.EVENT_BUS.register(new ChunkEventHandler());
    }
}