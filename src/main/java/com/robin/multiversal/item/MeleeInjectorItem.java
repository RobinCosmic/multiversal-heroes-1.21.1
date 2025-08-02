package com.robin.multiversal.item;

import com.robin.multiversal.powers.MeleeHero;
import com.robin.multiversal.powers.PowerManager;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MeleeInjectorItem extends InjectorItem {

    public MeleeInjectorItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);

        // Create a new NBT compound and put the "PowerType" tag as "Melee"
        NbtCompound nbt = new NbtCompound();
        nbt.putString(POWER_TAG, "Melee");

        // Wrap it inside a NbtComponent and set it to CUSTOM_DATA on the item stack
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));

        return stack;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        // Melee injector does NOT glow by default
        return false;
    }
}