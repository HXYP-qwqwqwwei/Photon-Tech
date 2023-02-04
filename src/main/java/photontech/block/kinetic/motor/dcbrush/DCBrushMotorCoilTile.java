package photontech.block.kinetic.motor.dcbrush;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import photontech.block.kinetic.FullAxleTile;
import photontech.block.kinetic.ResistType;
import photontech.init.PtTileEntities;
import photontech.utils.helper.fuctions.AxisHelper;
import photontech.utils.helper.fuctions.PtPhysics;

public class DCBrushMotorCoilTile extends FullAxleTile {
    protected double sumMagnetFlux;
    protected Direction.Axis brushAxis;


    public DCBrushMotorCoilTile(long initInertia) {
        super(PtTileEntities.DC_BRUSH_TILE_PART_B.get(), initInertia, true, ResistType.AXLE);
        this.setColdDown(5);
    }

    @Override
    public void tick() {
        super.tick();
        if (level != null && !level.isClientSide()) {

            if (inColdDown()) return;
            this.sumMagnetFlux = 0;
            // 获取周围的磁场信息
            for (Direction side : AxisHelper.getVerticalDirections(this.getAxis())) {
                BlockPos magnetPos = this.worldPosition.relative(side);
                TileEntity tile = level.getBlockEntity(magnetPos);

                double flux = PtPhysics.getMagnetFlux(tile, side.getOpposite());

                // 正交的磁场只贡献1/4的通量
                this.sumMagnetFlux += brushAxis == side.getAxis() ? flux : flux * 0.25;
            }
        }
    }

    public double getR() {
        return 0.1;
    }

    public double getSumMagnetFlux() {
        return sumMagnetFlux;
    }

    /**
     * 电机常数
     */
    public double getKt() {
        return this.getSumMagnetFlux() * this.getWireLength();
    }

    public double getWireLength() {
        return 50;
    }

    public void setBrushAxis(Direction.Axis brushAxis) {
        this.brushAxis = brushAxis;
    }
}
