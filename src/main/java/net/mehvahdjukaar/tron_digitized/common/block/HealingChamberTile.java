package net.mehvahdjukaar.tron_digitized.common.block;

import net.mehvahdjukaar.tron_digitized.common.entity.IHealableEntity;
import net.mehvahdjukaar.tron_digitized.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;

public class HealingChamberTile extends BlockEntity {

    public float fade = 0;
    public float prevFade = 0;

    private long soundCounter = 0;

    public HealingChamberTile(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModRegistry.HEALING_CHAMBER_TILE.get(), pWorldPosition, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putFloat("fade", fade);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.fade = pTag.getFloat("fade");
        this.prevFade = this.fade;

    }

    private static final AABB aabb = Shapes.block().toAabbs().get(0).inflate(0.15, 0.5, 0.15);

    public static void tick(Level level, BlockPos pos, BlockState state, HealingChamberTile tile) {
        boolean on = state.getValue(HealingChamberBlock.ENABLED);
        long time = level.getGameTime();
        if (level.isClientSide) {
            tile.prevFade = tile.fade;
            if (on) {
                if (tile.fade < 1)
                    tile.fade = Math.min(tile.fade + 0.04f, 1);
            } else {
                if (tile.fade > 0)
                    tile.fade = Math.max(tile.fade - 0.1f, 0);
            }
        } else if (on) {
            if (tile.soundCounter % (1 * 20) == 0L) {
                level.playSound(null, pos, ModRegistry.HEALING_CHAMBER_SOUND.get(), SoundSource.BLOCKS, 2, 1.0F);
            }
            tile.soundCounter++;
        }


        if (time % 5 == 0L) {
            var list = level.getEntitiesOfClass(LivingEntity.class, aabb.move(pos.getX(), pos.getY() + 1.3, pos.getZ()), EntitySelector.ENTITY_STILL_ALIVE);
            boolean shouldBeOn = !list.isEmpty();


            if (level.isClientSide) {
                for (var e : list) {
                    if (e instanceof IHealableEntity iHealable) {
                        iHealable.setInHealingChamber(true);
                    }
                }


            } else {


                if (shouldBeOn != on) {
                    level.setBlockAndUpdate(pos, state.setValue(HealingChamberBlock.ENABLED, shouldBeOn));
                    if (!shouldBeOn) tile.soundCounter = 0;
                }
                for (var e : list) {
                    e.heal(0.5f);
                }
            }
        }


    }
}
