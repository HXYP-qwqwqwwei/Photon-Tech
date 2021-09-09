package photontech.block.mirror;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import photontech.init.PtTileEntities;
import photontech.utils.tileentity.IPhotonInstrument;
import photontech.utils.tileentity.PtMachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.Queue;

public class PtMirrorTile extends PtMachineTile implements IPhotonInstrument {

    private static final float MAX_RANGE = 32F;
    private static final Vector3d DEFAULT_VECTOR = new Vector3d(0, 0, 1);
    private static final Vector3d FROM_SKY_VEC = new Vector3d(0, -1, 0);
    private static final int MAX_STACK_SIZE = 32;

    public static final float[] ATTENUATION_RATE = {0, 0, 0.8F, 0.8F, 0.8F};


    Queue<PhotonPack> packs = new LinkedList<>();
    Queue<Vector3d> fromVectors = new LinkedList<>();

    private Vector3d mirrorNormalVector = new Vector3d(0, 0, 1).normalize();

    public PtMirrorTile() {
        super(PtTileEntities.MIRROR_TILEENTITY.get());
    }

    @Override
    public void tick() {

        if (level != null && !level.isClientSide) {

            if (this.isFullyOpenAir() && level.isDay()) {
                this.acceptPhotonPackFrom(new PhotonPack(SKY_LIGHT), FROM_SKY_VEC);
            }

            while (!packs.isEmpty()) {
                PhotonPack pack = packs.remove();
                Vector3d injectionVec = this.fromVectors.remove();
                if (pack.attenuation(ATTENUATION_RATE)) {
                    Vector3d reflectionVec = injectionVec.subtract(this.mirrorNormalVector.scale(2 * injectionVec.dot(this.mirrorNormalVector)));
                    this.radiatePhotonPackTo(pack, reflectionVec);
                }
            }
        }

    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        nbt.put("MirrorNormalVector", this.saveVectorToNBT(new CompoundNBT(), this.mirrorNormalVector));
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        this.mirrorNormalVector = this.loadVectorFromNBT(nbt.getCompound("MirrorNormalVector"));
    }

    protected CompoundNBT saveVectorToNBT(CompoundNBT vecNBT, Vector3d vec) {
        vecNBT.putDouble("x", vec.x());
        vecNBT.putDouble("y", vec.y());
        vecNBT.putDouble("z", vec.z());
        return vecNBT;
    }

    protected Vector3d loadVectorFromNBT(CompoundNBT vecNBT) {
        return new Vector3d(
                vecNBT.getDouble("x"),
                vecNBT.getDouble("y"),
                vecNBT.getDouble("z")
        );
    }

    public void setFacingVector(BlockPos pos1, BlockPos pos2) {
        Vector3d vec1 = Vector3d.atLowerCornerOf(pos1.subtract(this.worldPosition)).normalize();
        Vector3d vec2 = Vector3d.atLowerCornerOf(pos2.subtract(this.worldPosition)).normalize();
        this.mirrorNormalVector = vec1.add(vec2).normalize();
        if (this.mirrorNormalVector.distanceToSqr(Vector3d.ZERO) < 1e-5) {
            this.mirrorNormalVector = DEFAULT_VECTOR;
        }
    }


    protected BlockRayTraceResult getRayTrace(Vector3d vec) {

        // vec should be normalized
        assert level != null;
        return level.clip(new RayTraceContext(
                Vector3d.atCenterOf(this.worldPosition).add(vec),
                Vector3d.atCenterOf(this.worldPosition).add(vec.scale(MAX_RANGE)),
                RayTraceContext.BlockMode.OUTLINE,
                RayTraceContext.FluidMode.NONE,
                null
        ));
    }


    public Vector3d getMirrorNormalVector() {
        return mirrorNormalVector;
    }

    @Override
    public void acceptPhotonPackFrom(@Nonnull PhotonPack pack, @Nullable Vector3d injectionVector) {
        if (injectionVector == null) {
            return;
        }
        if (injectionVector.dot(this.mirrorNormalVector) >= -0.0) {
            return;
        }
        if (this.packs.size() >= MAX_STACK_SIZE) {
            return;
        }
        this.packs.add(pack);
        this.fromVectors.add(injectionVector);

    }

    @Override
    public void radiatePhotonPackTo(@Nonnull PhotonPack pack, @Nullable Vector3d ejectionVector) {
        if (ejectionVector == null) {
            return;
        }
        BlockRayTraceResult result = this.getRayTrace(ejectionVector);

        if (result.getType() == RayTraceResult.Type.BLOCK) {
            BlockPos pos = result.getBlockPos();

            assert level != null;
            TileEntity entity = level.getBlockEntity(pos);
            if (entity instanceof IPhotonInstrument) {
                IPhotonInstrument instrument = (IPhotonInstrument) entity;
                ejectionVector = Vector3d.atLowerCornerOf(pos.subtract(this.worldPosition)).normalize();
                instrument.acceptPhotonPackFrom(pack, ejectionVector);
            }
        }
    }
}
