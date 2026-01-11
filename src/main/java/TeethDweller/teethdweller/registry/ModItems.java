package TeethDweller.teethdweller.registry;

import TeethDweller.teethdweller.TeethDweller;
import TeethDweller.teethdweller.item.ElectricToothbrushItem;
import TeethDweller.teethdweller.item.ManualToothbrushItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItems {
    
    public static final Item MANUAL_TOOTHBRUSH = register("manual_toothbrush", 
        ManualToothbrushItem::new, 
        new Item.Settings().maxCount(1));
    
    public static final Item ELECTRIC_TOOTHBRUSH = register("electric_toothbrush", 
        ElectricToothbrushItem::new, 
        new Item.Settings().maxCount(1));
    
    public static final Item TOOTHPASTE = register("toothpaste", 
        Item::new, 
        new Item.Settings().maxCount(64));
    
    public static final Item BRUSH_HEAD = register("brush_head", 
        Item::new, 
        new Item.Settings().maxCount(64));
    
    private static Item register(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(TeethDweller.MOD_ID, name));
        return Registry.register(Registries.ITEM, key, factory.apply(settings.registryKey(key)));
    }
    
    public static void initialize() {
        TeethDweller.LOGGER.info("Registering items for " + TeethDweller.MOD_ID);
        
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(MANUAL_TOOTHBRUSH);
            entries.add(ELECTRIC_TOOTHBRUSH);
        });
        
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(TOOTHPASTE);
            entries.add(BRUSH_HEAD);
        });
    }
}

