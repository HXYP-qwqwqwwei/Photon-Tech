package photontech.block.kinetic.motor.dc_brush;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import org.apache.logging.log4j.LogManager;
import photontech.block.kinetic.axle.AxleTile;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.utils.capability.magnet.IMagnet;
import photontech.utils.helper.AxisHelper;
import photontech.utils.helper.MutableInt;
import photontech.utils.helper.MutableLong;

public class DCBrushTilePartB extends AxleTile {
    private final Vector3d[] Bm = new Vector3d[4];
    private final MutableInt BCount = new MutableInt(0);
    private Vector3d Bsum = new Vector3d(0, 0, 0);


    public DCBrushTilePartB(long initInertia) {
        super(PtTileEntities.DC_BRUSH_TILE_PART_B.get(), initInertia);
        this.setColdDown(5);
    }

    @Override
    public void tick() {
        if (level != null && !level.isClientSide()) {
            super.tick();

            if (inColdDown()) return;
            // 获取周围的磁场信息
            for (Direction direction : AxisHelper.getVerticalDirections(this.currentAxis)) {
                BlockPos fromPos = this.worldPosition.relative(direction);
                TileEntity tile = level.getBlockEntity(fromPos);
                if (tile == null) continue;
                tile.getCapability(PtCapabilities.MAGNET, direction.getOpposite()).ifPresent(magnet -> {
                    Bm[BCount.value++] = magnet.getB(fromPos, this.worldPosition);
                });
            }
            Bsum = new Vector3d(0, 0, 0);
            for (int i = 0; i < BCount.value; ++i) {
                Bsum = Bsum.add(Bm[i]);
            }
            BCount.value = 0;
        }
    }

    public double getR() {
        return 1.0;
    }

    public double getB(Direction.Axis axis) {
        switch (axis) {
            case X: return Bsum.x;
            case Y: return Bsum.y;
            default: return Bsum.z;
        }
    }

    public double getWireLength() {
        return 10;
    }
}
