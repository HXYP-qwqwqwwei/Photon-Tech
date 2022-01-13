package photontech.block.kinetic.axle;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import photontech.init.PtCapabilities;
import photontech.utils.capability.kinetic.IMutableBody;
import photontech.utils.capability.kinetic.IRotateBody;
import photontech.utils.capability.kinetic.PtRotateBody;
import photontech.utils.capability.kinetic.PtMutableRotateBody;
import photontech.utils.helper.AxisHelper;
import photontech.utils.helper.MutableLong;
import photontech.utils.tileentity.PtMachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public class AxleTile extends PtMachineTile {

    public long selfInertia;
    Direction.Axis currentAxis = Direction.Axis.X;
    LazyOptional<IMutableBody> mainBody;
    private final int maxConnects = 16;
    private int searchDepth = 0;
    private final TileEntity[] canConnectTiles = new TileEntity[maxConnects];

    public AxleTile(TileEntityType<?> tileEntityTypeIn, long initInertia) {
        super(tileEntityTypeIn);
        this.selfInertia = initInertia;
        mainBody = LazyOptional.of(() -> PtMutableRotateBody.create(initInertia));
        this.setColdDown(5);
    }

    public void setCurrentAxis(Direction.Axis newAxis) {
        Direction.Axis oldAxis = this.currentAxis;
        this.currentAxis = newAxis;
        this.departBody(oldAxis);
    }

    @Override
    public void tick() {
        if (this.level != null) {

            // 发起合并
            if (!this.combineSearchOneStep()) {
                this.combineAll();
            }

            // 只在服务端的逻辑
            if (!level.isClientSide) {
                long time = level.getGameTime();

                mainBody.ifPresent(body -> {
                    body.updateAngle(time, 50);
                    IRotateBody.kineticTransferWithEnv(body, 1.0D);
                });

                if (this.isTerminalAxle()) {
                    level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
                }
            }
        }
    }

    private boolean isTerminalAxle() {
        assert level != null;
        TileEntity tileEntity = level.getBlockEntity(this.worldPosition.relative(AxisHelper.getAxisPositiveDirection(this.currentAxis).getOpposite()));
        if (tileEntity instanceof AxleTile) {
            return ((AxleTile) tileEntity).currentAxis != this.currentAxis;
        }
        return true;
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        nbt.putString("CurrentAxis", this.currentAxis.getName());
        this.mainBody.ifPresent(body -> nbt.put("MainBody", body.save(new CompoundNBT())));
        nbt.putLong("SelfInertia", this.selfInertia);
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        this.currentAxis = Direction.Axis.byName(nbt.getString("CurrentAxis"));
        this.mainBody.ifPresent(body -> body.load(nbt.getCompound("MainBody")));
        this.selfInertia = nbt.getLong("SelfInertia");
    }

    public float getAngle(Direction direction) {
        return this.mainBody.orElse(PtMutableRotateBody.create(0)).getAngle();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PtCapabilities.RIGID_BODY) {
            if (side != null && side.getAxis() == this.currentAxis) return this.mainBody.cast();
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }

    /**
     * depart selfBody from mainBody, called when tile remove or AXIS change.
     * this method will only check the axis positive direction.
     */
    protected void departBody(Direction.Axis oldAxis) {
        assert this.level != null;
        this.mainBody.ifPresent(body -> {
            // 记录拆分前的角速度、惯量、角度
            float oldOmega = body.getOmega();
            float oldAngle = body.getAngle();
            long oldInertia = body.getInertia();

            Direction direction = AxisHelper.getAxisPositiveDirection(oldAxis);
            BlockPos.Mutable pos = new BlockPos.Mutable(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ());

            TileEntity tile;
            IF:
            if ((tile = level.getBlockEntity(pos.move(direction))) instanceof AxleTile) {

                AxleTile axle = (AxleTile) tile;
                if (axle.currentAxis != this.currentAxis)break IF;
                tile.getCapability(PtCapabilities.RIGID_BODY, direction.getOpposite()).ifPresent(other -> {
                    // 未连接
                    if (other.get() != body.get()) return;
                    // 为自己正方向相邻的轴创建新的刚体对象，调用其searchAndCombine()，然后将自身的惯量减去分离出去的部分（包括自身）
                    other.set(PtRotateBody.create(axle.selfInertia));

                    // A little trick
                    Direction.Axis currAxis = this.currentAxis;
                    this.currentAxis = null;
                    long departedInertia = axle.searchAndCombineAll();
                    this.currentAxis = currAxis;

                    other.setOmega(oldOmega);
                    other.setAngle(oldAngle);
                    body.setInertia(oldInertia - departedInertia - this.selfInertia);
                });
            }
            // 拆分结束后，为自己新建刚体对象，并继承拆分前的角速度和角度
            body.set(PtMutableRotateBody.of(PtRotateBody.create(this.selfInertia)));
            body.setOmega(oldOmega);
            body.setAngle(oldAngle);
        });
    }

    /**
     * 搜索并合并与之相连的轴，此函数一次性完成所有满足条件的轴的搜索与合并。
     * 由于开销较大，此方法应只在调用频率较低的（例如departBody()）方法中使用
     */
    @SuppressWarnings("all")
    protected long searchAndCombineAll() {
        assert this.level != null && !this.level.isClientSide;
        this.searchDepth = 0;
        while (this.combineSearchOneStep());
        return this.combineAll();
    }

    /**
     * 仅对周围可合并的轴进行一个方块位的搜索。
     * 如果搜索到可合并的轴，则只是将其保存到canConnectTiles数组中，然后步数+1并返回true；
     * 如果自己不满足搜索条件，或者本次搜索未找到可合并的轴，或者深度已达最大，则直接返回false；
     * tick()中会检查返回值，如果为false，则会将canConnectTiles数组中前searchDepth个轴合并；
     * 否则什么都不做。
     * @return 如果搜索已经结束，返回false，此时也就意味着已经达成了合并的条件
     */
    protected boolean combineSearchOneStep() {
        assert this.level != null && !this.level.isClientSide;
        Direction direction = AxisHelper.getAxisPositiveDirection(this.currentAxis);
        // 负方向有可连接轴，则不进行搜索
        TileEntity tileEntity = level.getBlockEntity(this.worldPosition.relative(direction.getOpposite()));
        if (tileEntity instanceof AxleTile && ((AxleTile) tileEntity).currentAxis == this.currentAxis) {
            this.searchDepth = 0;
            return false;
        }
        // 搜索深度已达最大，不再继续搜索
        if (this.searchDepth >= this.maxConnects) return false;
        BlockPos pos = this.worldPosition.relative(direction, searchDepth);
        tileEntity = level.getBlockEntity(pos);
        // 正方向有轴，则将其加到候选列表，然后继续搜索
        if (tileEntity instanceof AxleTile) {
            this.canConnectTiles[searchDepth] = tileEntity;
            this.searchDepth += 1;
            return true;
        }
        // 正方向没有轴，则搜索结束
        return false;
    }

    protected long combineAll() {
        final int nAxles = this.searchDepth;
        MutableLong sumInertia = new MutableLong(this.selfInertia);
        if (nAxles > 0) {

            Direction direction = AxisHelper.getAxisPositiveDirection(this.currentAxis);
            this.mainBody.ifPresent(body -> {
                // 遍历所有可合并的轴，并让它们的Cap指向同一个刚体
                for (int k = 1; k < nAxles; ++k) {
                    TileEntity tileEntity = this.canConnectTiles[k];
                    // 合并途中发现中间已经断开，则停止遍历，并清空列表
                    if (tileEntity.isRemoved()) {
                        Arrays.fill(this.canConnectTiles, null);
                        break;
                    }

                    IMutableBody other = tileEntity.getCapability(PtCapabilities.RIGID_BODY, direction.getOpposite()).orElse(PtMutableRotateBody.create(0));
                    if (other.getInertia() == 0) break;

                    IRotateBody.kineticTransfer(body.get(), other.get());
                    other.set(body.get());
                    sumInertia.value += ((AxleTile) tileEntity).selfInertia;

                    // 设置为null以免强引用导致已不存在的Tile无法回收
                    this.canConnectTiles[k] = null;
                }
                // 设置合并后的转动惯量
                body.setInertia(sumInertia.value);
            });
            // 重置搜索深度
            this.searchDepth = 0;
        }
        return sumInertia.value;
    }

    @Override
    public void setRemoved() {
        this.departBody(this.currentAxis);
        super.setRemoved();
    }
}
