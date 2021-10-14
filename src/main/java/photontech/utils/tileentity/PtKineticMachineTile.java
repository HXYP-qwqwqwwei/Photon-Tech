package photontech.utils.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import photontech.init.PtCapabilities;
import photontech.utils.capability.kinetic.IRigidBody;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public abstract class PtKineticMachineTile extends PtMachineTile{

    protected List<LazyOptional<IRigidBody>> rigidBodies;

    public PtKineticMachineTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        rigidBodies = NonNullList.withSize(6, LazyOptional.empty());
    }

    protected LazyOptional<IRigidBody> getRigid(@Nullable Direction side) {
        return side == null ? LazyOptional.empty() : rigidBodies.get(side.ordinal());
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        return super.save(nbt);
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PtCapabilities.RIGID_BODY) {
            return getRigid(side).cast();
        }
        return super.getCapability(cap, side);
    }
}
