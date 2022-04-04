package net.mehvahdjukaar.tron_digitized.common.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public interface ICustomBed {

    Vec3 getBedOffset(BlockState state);
}
