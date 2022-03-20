package net.mehvahdjukaar.tron_digitized.common.entity;

import net.mehvahdjukaar.tron_digitized.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class ScreenEntity extends HangingEntity implements IEntityAdditionalSpawnData {

    private float prevAnimation = 0;
    private float animation = 0;
    private boolean on = false;

    public ScreenEntity(EntityType<ScreenEntity> screenEntityEntityType, Level level) {
        super(screenEntityEntityType, level);
    }

    public ScreenEntity(Level level, BlockPos pos, Direction direction) {
        super(ModRegistry.SCREEN.get(), level, pos);
        this.setDirection(direction);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeByte((byte) this.direction.get2DDataValue());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.direction = Direction.from2DDataValue(additionalData.readByte());
        this.setDirection(this.direction);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putBoolean("On", this.on);
        pCompound.putByte("Facing", (byte) this.direction.get2DDataValue());
        super.addAdditionalSaveData(pCompound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.on = pCompound.getBoolean("On");
        this.direction = Direction.from2DDataValue(pCompound.getByte("Facing"));
        this.setDirection(this.direction);
    }

    @Override
    public int getWidth() {
        return 7*16;
    }

    @Override
    public int getHeight() {
        return 7*16;
    }

    @Override
    public void dropItem(@Nullable Entity pBrokenEntity) {
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            this.playSound(SoundEvents.GLASS_BREAK, 1.0F, 1.0F);
            if (pBrokenEntity instanceof Player player) {
                if (player.getAbilities().instabuild) {
                    return;
                }
            }
            this.spawnAtLocation(getPickResult());
        }
    }

    @Override
    public void playPlacementSound() {
        this.playSound(SoundEvents.GLASS_PLACE, 1.0F, 1.0F);
    }

    /**
     * Sets the location and Yaw/Pitch of an entity in the world
     */
    public void moveTo(double pX, double pY, double pZ, float pYaw, float pPitch) {
        this.setPos(pX, pY, pZ);
    }

    /**
     * Sets a target for the client to interpolate towards over the next few ticks
     */
    public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport) {
        BlockPos blockpos = this.pos.offset(pX - this.getX(), pY - this.getY(), pZ - this.getZ());
        this.setPos((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ());
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ModRegistry.SCREEN_ITEM.get());
    }

    @Override
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        this.on = !this.on;
        this.playSound(SoundEvents.BEACON_ACTIVATE, 0.5F, 0.5F);
        return InteractionResult.sidedSuccess(pPlayer.level.isClientSide);
    }

    @Override
    public void tick() {
        this.prevAnimation = this.animation;
        if (on) {
            if (this.animation < 1)
                this.animation = Math.min(this.animation + 0.0245f, 1);
        } else {
            if (this.animation > 0)
                this.animation = Math.max(this.animation - 0.0245f, 0);
        }
    }

    public float getAnimation(float partialTicks){
        return Mth.lerp(partialTicks, this.prevAnimation, this.animation);
    }


}
