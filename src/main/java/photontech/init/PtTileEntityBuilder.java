package photontech.init;


import javax.annotation.Nullable;

import com.jozufozu.flywheel.backend.instancing.InstancedRenderRegistry;
import com.jozufozu.flywheel.backend.instancing.tile.ITileInstanceFactory;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.builders.TileEntityBuilder;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class PtTileEntityBuilder<T extends TileEntity, P> extends TileEntityBuilder<T, P> {

    @Nullable
    private NonNullSupplier<ITileInstanceFactory<? super T>> instanceFactory;

    public static <T extends TileEntity, P> TileEntityBuilder<T, P> create(AbstractRegistrate<?> owner, P parent,
                                                                           String name, BuilderCallback callback, NonNullFunction<TileEntityType<T>, ? extends T> factory) {
        return new PtTileEntityBuilder<>(owner, parent, name, callback, factory);
    }

    protected PtTileEntityBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback,
                                  NonNullFunction<TileEntityType<T>, ? extends T> factory) {
        super(owner, parent, name, callback, factory);
    }

    public PtTileEntityBuilder<T, P> instance(NonNullSupplier<ITileInstanceFactory<? super T>> instanceFactory) {
        if (this.instanceFactory == null) {
            DistExecutor.runWhenOn(Dist.CLIENT, () -> this::registerInstance);
        }

        this.instanceFactory = instanceFactory;

        return this;
    }

    protected void registerInstance() {
        OneTimeEventReceiver.addModListener(FMLClientSetupEvent.class, $ -> {
            NonNullSupplier<ITileInstanceFactory<? super T>> instanceFactory = this.instanceFactory;
            if (instanceFactory != null) {
                InstancedRenderRegistry.getInstance().register(getEntry(), instanceFactory.get());
            }

        });
    }
}
