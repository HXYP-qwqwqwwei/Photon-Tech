package photontech.block.kinetic.motor.dc_brush;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import photontech.block.kinetic.FullAxleTile;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.utils.helperfunctions.AxisHelper;
import photontech.utils.helperfunctions.MutableInt;

public class DCBrushTilePartB extends FullAxleTile {
    private final Vector3d[] Bm = new Vector3d[4];
    private final MutableInt BCount = new MutableInt(0);
    private Vector3d Bs = new Vector3d(0, 0, 0);


    public DCBrushTilePartB(long initInertia) {
        super(PtTileEntities.DC_BRUSH_TILE_PART_B.get(), initInertia, true);
        this.setColdDown(5);
    }

    @Override
    public void tick() {
        super.tick();
        if (level != null && !level.isClientSide()) {

            if (inColdDown()) return;
            // 获取周围的磁场信息
            for (Direction direction : AxisHelper.getVerticalDirections(this.getAxis())) {
                BlockPos fromPos = this.worldPosition.relative(direction);
                TileEntity tile = level.getBlockEntity(fromPos);
                if (tile == null) continue;
                tile.getCapability(PtCapabilities.MAGNET, direction.getOpposite()).ifPresent(magnet -> {
                    Bm[BCount.value++] = magnet.getB(fromPos, this.worldPosition);
                });
            }
            Bs = new Vector3d(0, 0, 0);
            for (int i = 0; i < BCount.value; ++i) {
                Bs = Bs.add(Bm[i]);
            }
            BCount.value = 0;
        }
    }

    public double getR() {
        return 1.0;
    }

    public double getB(Direction.Axis axis) {
        switch (axis) {
            case X: return Bs.x;
            case Y: return Bs.y;
            default: return Bs.z;
        }
    }

    public double getK(Direction.Axis axis) {
        return this.getB(axis) * this.getWireLength();
    }

    public double getWireLength() {
        return 10;
    }
}
