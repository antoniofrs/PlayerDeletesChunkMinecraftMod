package it.antoniofrs;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.UUID;

public class ChunkEventHandler {

    private final HashMap<UUID, ChunkPos> playerChunkMap = new HashMap<>();

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {

        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) return;

        ServerPlayer player = (ServerPlayer) event.player;
        ChunkPos current = new ChunkPos(player.blockPosition());
        UUID uuid = player.getUUID();

        ChunkPos previous = playerChunkMap.get(uuid);
        if (previous != null && !previous.equals(current)) {
            ServerLevel world = (ServerLevel) player.level();
            clearChunk(world, previous);
        }
        playerChunkMap.put(uuid, current);
    }

    private void clearChunk(ServerLevel world, ChunkPos chunkPos) {
        int minX = chunkPos.getMinBlockX();
        int minZ = chunkPos.getMinBlockZ();
        int maxX = chunkPos.getMaxBlockX();
        int maxZ = chunkPos.getMaxBlockZ();
        int minY = -64;
        int maxY = 320;

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int y = minY; y < maxY; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                }
            }
        }

        System.out.println("[PlayerDeletesChunk] Chunk " + chunkPos + " svuotato.");
    }
}
