package com.supermartijn642.simplemagnets.packets.demagnetization_coil;

import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.core.network.TileEntityBasePacket;
import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import net.minecraft.util.math.BlockPos;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class PacketIncreaseYRange extends TileEntityBasePacket<DemagnetizationCoilTile> {

    public PacketIncreaseYRange(BlockPos pos){
        super(pos);
    }

    public PacketIncreaseYRange(){
    }

    @Override
    protected void handle(DemagnetizationCoilTile tile, PacketContext context){
        tile.setRangeY(tile.rangeY + 1);
    }
}
