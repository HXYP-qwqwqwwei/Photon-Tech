package photontech.block.light.mirror;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import photontech.utils.client.render.MachineTileRenderer;
import photontech.utils.client.render.SuperByteBuffer;

import javax.annotation.Nonnull;

import static net.minecraft.state.properties.BlockStateProperties.FACING;
import static photontech.utils.PtConstants.MODELS.MIRROR_FRAME;
import static photontech.utils.PtConstants.MODELS.MIRROR_SUPPORTER;

public class MirrorFrameTER extends MachineTileRenderer<MirrorFrameTile> {

    public MirrorFrameTER(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(@Nonnull MirrorFrameTile mirrorTile, float v, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer typeBuffer, int combinedLightIn, int combinedOverlayIn) {

        Vector3d pointing = mirrorTile.getMirrorNormalVector();
        Direction baseFacing = mirrorTile.getBlockState().getValue(FACING);

        // init model
        BlockState state = mirrorTile.getBlockState();
        SuperByteBuffer supporter = bufferFromResourceLocation(state, MIRROR_SUPPORTER);
        SuperByteBuffer goldenFrame = bufferFromResourceLocation(state, MIRROR_FRAME);
        boolean hasMirror = !mirrorTile.getMirrorItemStack().isEmpty();
        SuperByteBuffer mirror = bufferFromItemStack(state, mirrorTile.getMirrorItemStack());

        rotateBuffer(supporter, Direction.Axis.Y, -HALF_PI);
        rotateBuffer(goldenFrame, Direction.Axis.Y, -HALF_PI);
        rotateBuffer(mirror, Direction.Axis.Y, -HALF_PI);


        double xSupportR = 0, ySupportR = 0, zSupportR = 0;
        double xFrameR = 0, yFrameR = 0, zFrameR = 0;

        // init rotation angle
        switch (baseFacing) {
            case DOWN:
                ySupportR = 0.5 * PI - MathHelper.atan2(pointing.z, pointing.x);
                xFrameR = -Math.atan(pointing.y / MathHelper.sqrt(this.sumOfSquare(pointing.x, pointing.z)));
                break;

            case UP:
                zSupportR = PI;
                ySupportR = - 0.5 * Math.PI - MathHelper.atan2(pointing.z, pointing.x);
                xFrameR = -Math.atan(pointing.y / MathHelper.sqrt(this.sumOfSquare(pointing.x, pointing.z)));
                yFrameR = PI;
                zFrameR = -PI;
                break;

            case NORTH:
                xSupportR = 0.5 * PI;
                ySupportR = -Math.atan2(pointing.x ,pointing.y);
                xFrameR = PI + Math.atan2(pointing.z, MathHelper.sqrt(this.sumOfSquare(pointing.x, pointing.y)));
                break;

            case SOUTH:
                xSupportR = -0.5 * PI;
                ySupportR = Math.atan2(pointing.x ,pointing.y);
                xFrameR = Math.atan2(pointing.z, MathHelper.sqrt(this.sumOfSquare(pointing.x, pointing.y)));
                break;

            case EAST:
                zSupportR = 0.5 * PI;
                xSupportR = -Math.atan2(pointing.y, pointing.z);
                xFrameR = Math.atan2(pointing.x, MathHelper.sqrt(this.sumOfSquare(pointing.y, pointing.z)));
                break;

            case WEST:
                zSupportR = -0.5 * PI;
                xSupportR = -Math.atan2(pointing.y, pointing.z);
                xFrameR = -Math.atan2(pointing.x, MathHelper.sqrt(this.sumOfSquare(pointing.y, pointing.z)));
                break;
        }


        for (RenderType type : RenderType.chunkBufferLayers()) {
            if (RenderTypeLookup.canRenderInLayer(state, type)) {
                // render support
                matrixStack.translate(.5, .5, .5);
                matrixStack.mulPose(Vector3f.XP.rotation((float) xSupportR));
                matrixStack.mulPose(Vector3f.YP.rotation((float) ySupportR));
                matrixStack.mulPose(Vector3f.ZP.rotation((float) zSupportR));
                matrixStack.translate(-.5, -.5, -.5);
                supporter.renderInto(matrixStack, typeBuffer.getBuffer(type));


                // render frame
                matrixStack.translate(.5, .53125, .5);
                matrixStack.mulPose(Vector3f.XP.rotation((float) xFrameR));
                matrixStack.mulPose(Vector3f.YP.rotation((float) yFrameR));
                matrixStack.mulPose(Vector3f.ZP.rotation((float) zFrameR));
                matrixStack.translate(-.5, -.53125, -.5);
                goldenFrame.renderInto(matrixStack, typeBuffer.getBuffer(type));
                if (hasMirror) mirror.renderInto(matrixStack, typeBuffer.getBuffer(type));
            }
        }

    }

    private double sumOfSquare(double a, double b) {
        return a*a + b*b;
    }

}
