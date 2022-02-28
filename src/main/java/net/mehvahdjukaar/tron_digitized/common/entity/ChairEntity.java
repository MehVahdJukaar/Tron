package net.mehvahdjukaar.tron_digitized.common.entity;

import com.mojang.math.Vector3f;
import net.mehvahdjukaar.tron_digitized.common.block.ChairBlock;
import net.mehvahdjukaar.tron_digitized.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.List;

public class ChairEntity extends Entity implements IEntityAdditionalSpawnData {

    private float chairHeight;
    private int seats;
    private BlockState state = ModRegistry.BLACK_CHAIR.get().defaultBlockState();

    public ChairEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.chairHeight = 0;
        this.seats = 1;
    }
    public ChairEntity(Level worldIn, float chairHeight, int seats) {
        super(ModRegistry.CHAIR_ENTITY.get(), worldIn);
        this.chairHeight = chairHeight;
        this.seats = seats;
    }

    public ChairEntity(Level worldIn, float chairHeight) {
        this(worldIn, chairHeight, 1);
    }

    @Override
    public void tick() {

        super.tick();
        List<Entity> passengers = this.getPassengers();
        boolean dead = passengers.isEmpty();
        BlockPos pos = this.blockPosition();
        this.state = this.level.getBlockState(pos);
        if (!dead && !(state.getBlock() instanceof ChairBlock)) {
            PistonMovingBlockEntity piston = null;
            boolean didOffset = false;
            BlockEntity tile = this.level.getBlockEntity(pos);
            if (tile instanceof PistonMovingBlockEntity pis && pis.getMovedState().getBlock() instanceof ChairBlock) {
                piston = pis;
            } else {
                for (Direction d : Direction.values()) {
                    BlockPos offPos = pos.relative(d);
                    tile = this.level.getBlockEntity(offPos);
                    if (tile instanceof PistonMovingBlockEntity pi && pi.getMovedState().getBlock() instanceof ChairBlock) {
                        piston = pi;
                        break;
                    }
                }
            }

            if (piston != null) {
                Direction dir = piston.getMovementDirection();
                this.move(MoverType.PISTON, new Vec3((double)((float)dir.getStepX()) * 0.33D, (double)((float)dir.getStepY()) * 0.33D, (double)((float)dir.getStepZ()) * 0.33D));
                didOffset = true;
            }

            dead = !didOffset;
        }

        if (dead && !this.level.isClientSide) {
            this.removeAfterChangingDimensions();
            if (state.getBlock() instanceof ChairBlock) {
                this.level.setBlockAndUpdate(pos, state.setValue(ChairBlock.SAT_IN, false));
            }
        }
    }

    public double getPassengersRidingOffset() {
        return chairHeight;
    }

    @Override
    public void positionRider(Entity pPassenger) {
        if (this.hasPassenger(pPassenger)) {
            var passengers = this.getPassengers();
            int size = passengers.size();
            int i = this.getPassengers().indexOf(pPassenger);

            double d0 = this.getY() + this.getPassengersRidingOffset() + pPassenger.getMyRidingOffset();

            Vector3f step;
            float distance = 1;
            float f = ((size-1)*distance)/2f;
            if (this.state.hasProperty(ChairBlock.FACING)) {
                step = this.state.getValue(ChairBlock.FACING).getClockWise().step();
            }else step = Vector3f.ZERO;
            var oldStep = step.copy();
            oldStep.mul(-i*distance);
            step.mul(f);
            step.add(oldStep);

            pPassenger.setPos(this.getX() + step.x(), d0, this.getZ()+step.z());
        }
    }

    @Override
    protected boolean canAddPassenger(Entity pPassenger) {
        return this.getPassengers().size()<seats;
    }

    protected void defineSynchedData() {
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        this.chairHeight = compound.getFloat("chairHeight");
        this.seats = compound.getInt("seats");
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putFloat("chairHeight",this.chairHeight);
        compound.putInt("seats",this.seats);
    }

    @Nonnull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeFloat(this.chairHeight);
        buffer.writeInt(this.seats);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.chairHeight = additionalData.readFloat();
        this.seats = additionalData.readInt();
    }
}
