package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.network.PacketChannel;
import com.supermartijn642.simplemagnets.gui.DemagnetizationCoilContainer;
import com.supermartijn642.simplemagnets.gui.FilteredDemagnetizationCoilContainer;
import com.supermartijn642.simplemagnets.gui.MagnetContainer;
import com.supermartijn642.simplemagnets.packets.demagnetization_coil.*;
import com.supermartijn642.simplemagnets.packets.magnet.*;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ObjectHolder;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod("simplemagnets")
public class SimpleMagnets {

    public static final PacketChannel CHANNEL = PacketChannel.create("simplemagnets");

    public static final ItemGroup GROUP = new ItemGroup("simplemagnets") {
        @Override
        public ItemStack makeIcon(){
            return new ItemStack(simple_magnet);
        }
    };

    @ObjectHolder("simplemagnets:basicmagnet")
    public static Item simple_magnet;
    @ObjectHolder("simplemagnets:advancedmagnet")
    public static Item advanced_magnet;
    @ObjectHolder("simplemagnets:basic_demagnetization_coil")
    public static Block basic_demagnetization_coil;
    @ObjectHolder("simplemagnets:advanced_demagnetization_coil")
    public static Block advanced_demagnetization_coil;

    @ObjectHolder("simplemagnets:basic_demagnetization_coil_tile")
    public static TileEntityType<?> basic_demagnetization_coil_tile;
    @ObjectHolder("simplemagnets:advanced_demagnetization_coil_tile")
    public static TileEntityType<?> advanced_demagnetization_coil_tile;

    @ObjectHolder("simplemagnets:container")
    public static ContainerType<MagnetContainer> container;
    @ObjectHolder("simplemagnets:demagnetization_coil_container")
    public static ContainerType<DemagnetizationCoilContainer> demagnetization_coil_container;
    @ObjectHolder("simplemagnets:filtered_demagnetization_coil_container")
    public static ContainerType<FilteredDemagnetizationCoilContainer> filtered_demagnetization_coil_container;

    public SimpleMagnets(){
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::interModEnqueue);

        // magnets
        CHANNEL.registerMessage(PacketToggleItems.class, PacketToggleItems::new, true);
        CHANNEL.registerMessage(PacketIncreaseItemRange.class, PacketIncreaseItemRange::new, true);
        CHANNEL.registerMessage(PacketDecreaseItemRange.class, PacketDecreaseItemRange::new, true);
        CHANNEL.registerMessage(PacketToggleXp.class, PacketToggleXp::new, true);
        CHANNEL.registerMessage(PacketIncreaseXpRange.class, PacketIncreaseXpRange::new, true);
        CHANNEL.registerMessage(PacketDecreaseXpRange.class, PacketDecreaseXpRange::new, true);
        CHANNEL.registerMessage(PacketToggleMagnetWhitelist.class, PacketToggleMagnetWhitelist::new, true);
        CHANNEL.registerMessage(PacketToggleMagnet.class, PacketToggleMagnet::new, true);
        CHANNEL.registerMessage(PacketItemInfo.class, PacketItemInfo::new, false);
        CHANNEL.registerMessage(PacketToggleMagnetMessage.class, PacketToggleMagnetMessage::new, true);
        CHANNEL.registerMessage(PacketToggleMagnetDurability.class, PacketToggleMagnetDurability::new, true);

        // demagnetization coils
        CHANNEL.registerMessage(PacketDecreaseXRange.class, PacketDecreaseXRange::new, true);
        CHANNEL.registerMessage(PacketDecreaseYRange.class, PacketDecreaseYRange::new, true);
        CHANNEL.registerMessage(PacketDecreaseZRange.class, PacketDecreaseZRange::new, true);
        CHANNEL.registerMessage(PacketIncreaseXRange.class, PacketIncreaseXRange::new, true);
        CHANNEL.registerMessage(PacketIncreaseYRange.class, PacketIncreaseYRange::new, true);
        CHANNEL.registerMessage(PacketIncreaseZRange.class, PacketIncreaseZRange::new, true);
        CHANNEL.registerMessage(PacketToggleDurability.class, PacketToggleDurability::new, true);
        CHANNEL.registerMessage(PacketToggleWhitelist.class, PacketToggleWhitelist::new, true);
    }

    public void init(FMLCommonSetupEvent e){
        DistExecutor.runWhenOn(Dist.CLIENT, () -> ClientProxy::init);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> ClientProxy::registerScreen);
    }

    public void interModEnqueue(InterModEnqueueEvent e){
        InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("charm").setSize(1).setEnabled(true));
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> e){
            e.getRegistry().register(new BasicMagnet());
            e.getRegistry().register(new AdvancedMagnet());
            e.getRegistry().register(new BlockItem(basic_demagnetization_coil, new Item.Properties().tab(GROUP)).setRegistryName(basic_demagnetization_coil.getRegistryName()));
            e.getRegistry().register(new BlockItem(advanced_demagnetization_coil, new Item.Properties().tab(GROUP)).setRegistryName(advanced_demagnetization_coil.getRegistryName()));
        }

        @SubscribeEvent
        public static void onItemBlock(final RegistryEvent.Register<Block> e){
            e.getRegistry().register(new DemagnetizationCoilBlock("basic_demagnetization_coil", SMConfig.basicCoilMaxRange, SMConfig.basicCoilFilter, DemagnetizationCoilTile.BasicDemagnetizationCoilTile::new));
            e.getRegistry().register(new DemagnetizationCoilBlock("advanced_demagnetization_coil", SMConfig.advancedCoilMaxRange, SMConfig.advancedCoilFilter, DemagnetizationCoilTile.AdvancedDemagnetizationCoilTile::new));
        }

        @SubscribeEvent
        public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> e){
            e.getRegistry().register(TileEntityType.Builder.of(DemagnetizationCoilTile.BasicDemagnetizationCoilTile::new, basic_demagnetization_coil).build(null).setRegistryName("basic_demagnetization_coil_tile"));
            e.getRegistry().register(TileEntityType.Builder.of(DemagnetizationCoilTile.AdvancedDemagnetizationCoilTile::new, advanced_demagnetization_coil).build(null).setRegistryName("advanced_demagnetization_coil_tile"));
        }

        @SubscribeEvent
        public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> e){
            e.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> new MagnetContainer(windowId, inv.player, data.readInt())).setRegistryName("container"));
            e.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> new DemagnetizationCoilContainer(windowId, inv.player, data.readBlockPos())).setRegistryName("demagnetization_coil_container"));
            e.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> new FilteredDemagnetizationCoilContainer(windowId, inv.player, data.readBlockPos())).setRegistryName("filtered_demagnetization_coil_container"));
        }
    }

}
