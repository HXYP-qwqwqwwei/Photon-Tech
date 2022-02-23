package photontech.utils.capability.kinetic;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import photontech.init.PtCapabilities;
import photontech.utils.capability.ISaveLoad;
import photontech.utils.helper.AxisHelper;

import javax.annotation.Nonnull;

public class PtMutableRotateBody implements IMutableBody {
    @Nonnull
    protected IRotateBody body;
    protected Direction.Axis axis;
    protected int length = 0;
    protected BlockPos beginPos;

    protected PtMutableRotateBody(@Nonnull IRotateBody body, Direction.Axis axis) {
        this.body = body;
        this.axis = axis;
    }

    public static IMutableBody of(@Nonnull IRotateBody body, Direction.Axis axis) {
        return new PtMutableRotateBody(body, axis);
    }

//    public static IMutableBody create(IWorld level, BlockPos position, Direction.Axis axis, long inertia) {
//        if (inertia <= 0) return EMPTY;
//        Direction positiveSide = AxisHelper.getAxisPositiveDirection(axis);
//        Direction negativeSide = positiveSide.getOpposite();
//        TileEntity posTile = level.getBlockEntity(position.relative(positiveSide));
//        TileEntity negTile = level.getBlockEntity(position.relative(negativeSide));
//        IMutableBody posBody = EMPTY;
//        IMutableBody negBody = EMPTY;
//        boolean posConnect = false;
//        boolean negConnect = false;
//        // 计算最大长度
//        int combinedLength = 1;
//        if (posTile != null) posBody = posTile.getCapability(PtCapabilities.RIGID_BODY).orElse(EMPTY);
//        if (negTile != null) negBody = negTile.getCapability(PtCapabilities.RIGID_BODY).orElse(EMPTY);
//        if (posBody.getAxis() == axis) {
//            combinedLength += posBody.getLength();
//            posConnect = true;
//        }
//        if (negBody.getAxis() == axis) {
//            combinedLength += negBody.getLength();
//            negConnect = true;
//        }
//        if (combinedLength > MAX_LENGTH) {
//            return EMPTY;
//        }
//        IMutableBody ret = of(PtRotateBody.create(inertia), axis);
//        if (posConnect) {
//
//        }
//        return ret;
//    }

    @Override
    public IRotateBody get() {
        while (this.body instanceof IMutableBody) {
            this.body = ((IMutableBody) this.body).get();
        }
        return this.body;
    }

    @Override
    public void set(IRotateBody newBody) {
        this.body = newBody;
    }

    @Override
    public float getOmega() {
        return body.getOmega();
    }

    @Override
    public void setOmega(float omega) {
        body.setOmega(omega);
    }

    @Override
    public int getKinetic() {
        return body.getKinetic();
    }

    @Override
    public long getInertia() {
        return body.getInertia();
    }

    @Override
    public float getAngle() {
        return body.getAngle();
    }

    @Override
    public void setAngle(float angle) {
        body.setAngle(angle);
    }

    @Override
    public void addP(float p) {
        body.addP(p);
    }

    @Override
    public void updateAngle() {
        body.updateAngle();
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        return body.save(nbt);
    }

    @Override
    public void load(CompoundNBT nbt) {
        body.load(nbt);
    }

    @Override
    public void reverse() {
        body.reverse();
    }

    @Override
    public void setInertia(long inertia) {
        body.setInertia(inertia);
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public Direction.Axis getAxis() {
        return axis;
    }

    @Override
    public void setAxis(Direction.Axis axis) {
        this.axis = axis;
    }

    @Override
    public boolean isEmpty() {
        return this == EMPTY;
    }
}
