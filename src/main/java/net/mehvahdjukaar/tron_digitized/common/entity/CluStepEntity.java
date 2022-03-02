package net.mehvahdjukaar.tron_digitized.common.entity;

import net.mehvahdjukaar.tron_digitized.common.block.CluStairsBlock;
import net.mehvahdjukaar.tron_digitized.init.ModRegistry;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class CluStepEntity extends Entity {

    private static final float RADIUS = 1.36f;
    private static final int STEPS = 20;
    private static final int HEIGHT = 7;
    private static final float OPENING_SPEED = 0.01f;

    private static final EntityDataAccessor<Integer> STEP_ORDINAL = SynchedEntityData.defineId(CluStepEntity.class, EntityDataSerializers.INT);

    private static final Vec3[] offsets = Util.make(new Vec3[STEPS], v -> {
        for (int i = 0; i < v.length; i++) {
            float angle = (float) ((i / ((float)v.length)) * 2 * Math.PI);
            v[i] = new Vec3(RADIUS * Mth.sin(angle), angle, RADIUS * Mth.cos(angle));
        }

    });

    public float openingProgress = 0;
    public float prevOpeningProgress = 0;

    public CluStepEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.blocksBuilding = true;
    }

    public CluStepEntity(Level level, Vec3 pos, BlockState state, int ordinal) {
        this(ModRegistry.CLU_STEP_ENTITY.get(), level);
        this.setPos(pos);
        this.setOrdinal(ordinal);
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        //if(this.getOrdinal() == 0)return MAIN_DIMENSIONS;
        return super.getDimensions(pPose);
    }

    public static void create(Level pLevel, BlockPos pPos, BlockState pState) {
        var main = new CluStepEntity(pLevel, Vec3.atCenterOf(pPos), pState, 0);
        pLevel.addFreshEntity(main);

        for (int i = 0; i < offsets.length; i++) {
            var v = offsets[i];
            var step = new CluStepEntity(pLevel, main.position().add(v.x, pPos.getY() + 0.5, v.z), pState, i + 1);
            step.setYRot(-(float) (180 * v.y / Math.PI));
            step.startRiding(main);
            pLevel.addFreshEntity(step);
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.setOrdinal(pCompound.getInt("Ordinal"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putInt("Ordinal", this.getOrdinal());
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(STEP_ORDINAL, 0);
    }

    @Override
    public double getMyRidingOffset() {
        return 0;
    }

    @Override
    protected float getEyeHeight(Pose pPose, EntityDimensions pSize) {
        return pSize.height * 0.5F;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public void setYRot(float pYRot) {
        if (yRotO < 0) {
            int a = 1;
        }
        super.setYRot(pYRot);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick() {
        //master
        if (this.getOrdinal() == 0) {
            BlockState state = this.getBlockStateOn();
            if (state.getBlock() != ModRegistry.CLU_STAIRS.get()) {
                this.discard();
            } else {
                this.prevOpeningProgress = this.openingProgress;
                this.moveDist = 0;
                if (state.getValue(CluStairsBlock.ENABLED)) {

                    if (this.openingProgress < 1) {
                        this.moveDist = -OPENING_SPEED;
                        this.openingProgress = Math.min(this.openingProgress - moveDist, 1);
                    }
                } else {
                    if (this.openingProgress > 0) {
                        this.moveDist = OPENING_SPEED;
                        this.openingProgress = Math.max(this.openingProgress - moveDist, 0);
                    }
                }
            }
            this.getPassengers().forEach(c -> c.moveDist = this.moveDist);
        } else {
            if (!this.isPassenger()) {
                this.discard();
            } else {
                this.moveCollidedEntities();
            }
        }
    }

    public void setOrdinal(int pDamage) {
        this.entityData.set(STEP_ORDINAL, pDamage);
    }

    public int getOrdinal() {
        return this.entityData.get(STEP_ORDINAL);
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return ModRegistry.CLU_STAIRS.get().asItem().getDefaultInstance();
    }

    @Override
    public void remove(Entity.RemovalReason pReason) {
        if (!this.level.isClientSide && pReason.shouldDestroy()) {
            if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {

            }
        }
        super.remove(pReason);
    }

    @Override
    protected boolean canAddPassenger(Entity pPassenger) {
        return this.getPassengers().size() < STEPS;
    }

    @Override
    public void positionRider(Entity pPassenger) {
        if (this.hasPassenger(pPassenger)) {
            int i = this.getPassengers().indexOf(pPassenger);
            var v = offsets[i];
            float y = -Math.min(openingProgress, (i / (float) STEPS));
            pPassenger.setPos(this.position().add(v.x, y * (float) HEIGHT, v.z));
            pPassenger.moveDist = this.moveDist;
        }
    }

    private void moveCollidedEntities() {
        if (this.moveDist > 0) {
            AABB aabb = this.makeBoundingBox().move(0, moveDist*HEIGHT+0.001, 0);
            List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, aabb);
            for (LivingEntity entity : list) {
                if (entity.getPistonPushReaction() != PushReaction.IGNORE) {
                    entity.move(MoverType.SHULKER_BOX, new Vec3(0, aabb.maxY-entity.getY(), 0));
                }
            }
        }
    }

    public AABB getProgressDeltaAabb(float yDir, float prev, float op) {
        double d0 = Math.max(prev, op);
        double d1 = Math.min(prev, op);
        return this.makeBoundingBox().move(0, yDir, 0);
    }
}
