package com.robin.multiversal.item;

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

import com.robin.multiversal.powers.PowerManager;

public class InjectorItem extends Item {
    protected static final String POWER_TAG = "PowerType";

    public InjectorItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        // Get the CUSTOM_DATA NBT component for items (1.21.1+)
        NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
        NbtCompound nbt = nbtComponent.copyNbt();

        if (!world.isClient) {
            String storedPower = nbt.getString(POWER_TAG);

            if (storedPower.isEmpty()) {
                // EMPTY INJECTOR: extract player's power if any
                String playerPower = PowerManager.getCurrentPower(player);

                if (playerPower != null && !playerPower.isEmpty()) {
                    PowerManager.removePower(player);
                    nbt.putString(POWER_TAG, playerPower);

                    // Save updated NBT back into the item
                    stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));

                    player.sendMessage(Text.literal("Extracted " + playerPower + " power!"), true);
                    world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.PLAYERS, 1.0F, 0.8F);
                } else {
                    player.sendMessage(Text.literal("No power to extract!"), true);
                }
            } else {
                // FILLED INJECTOR: inject stored power into player
                if (PowerManager.hasAnyPower(player)) {
                    player.sendMessage(Text.literal("Player already has a power! Extract it first."), true);
                } else {
                    PowerManager.grantPower(player, storedPower);

                    // Clear the stored power and update item
                    nbt.putString(POWER_TAG, "");
                    stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));

                    player.sendMessage(Text.literal("Injected " + storedPower + " power!"), true);
                    world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1.0F, 1.2F);
                }
            }
        }

        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
        NbtCompound nbt = nbtComponent.copyNbt();
        return nbt.contains(POWER_TAG) && !nbt.getString(POWER_TAG).isEmpty();
    }
}