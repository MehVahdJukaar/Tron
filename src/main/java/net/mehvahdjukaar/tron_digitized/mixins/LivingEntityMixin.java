package net.mehvahdjukaar.tron_digitized.mixins;

import net.mehvahdjukaar.tron_digitized.common.block.HealingChamberBlock;
import net.mehvahdjukaar.tron_digitized.common.block.ICustomBed;
import net.mehvahdjukaar.tron_digitized.common.entity.IHealableEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IHealableEntity {

    private static final int H_OFFSET = 6;

    private int inHealingChamberTime = 0;

    private int prevAnimationTime = 0;
    private int animationTime = 0;

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void aiStep(CallbackInfo ci) {
        if (this.isInHealingChamber()) {
            if (!(this.getBlockStateOn().getBlock() instanceof HealingChamberBlock)) {
                this.prevAnimationTime = this.animationTime;
                this.animationTime = Math.max(0, animationTime - 1);
                this.inHealingChamberTime = Math.max(0, inHealingChamberTime - 1);
            } else {
                this.prevAnimationTime = this.animationTime;
                this.animationTime = Math.min(H_OFFSET, animationTime + 1);
                this.inHealingChamberTime = Math.min(H_OFFSET, inHealingChamberTime + 1);
            }
        }
    }

    @Override
    public boolean isInHealingChamber() {
        return inHealingChamberTime > 0;
    }

    public float getHealingFade(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevAnimationTime, this.animationTime) / (float) H_OFFSET;
    }


    @Override
    public void setInHealingChamber(boolean inHealingChamber) {
        this.inHealingChamberTime = inHealingChamber ? Math.max(this.inHealingChamberTime, H_OFFSET) : 0;
    }


    @Inject(method = "setPosToBed", at = @At("TAIL"))
    private void setPosToBed(BlockPos pos, CallbackInfo ci) {
        if (this.level != null) {
            BlockState state = this.level.getBlockState(pos);
            if (state.getBlock() instanceof ICustomBed bed){
                Vec3 v = bed.getBedOffset(state);
                this.setPos((double) pos.getX() + 0.5D + v.x,
                        (double) pos.getY() + v.y,
                        (double) pos.getZ() + 0.5D + v.z);
            }
        }
    }
}