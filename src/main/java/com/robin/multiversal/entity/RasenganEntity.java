package com.robin.multiversal.entity;

import com.boundless.registry.EntityRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/*public class RasenganEntity extends PersistentProjectileEntity {

    public RasenganEntity(EntityType<? extends RasenganEntity> entityType, World world) {
        super(entityType, world);
        this.setNoGravity(true);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    public RasenganEntity(World world, LivingEntity owner) {
        super(EntityRegistry.RASENGAN_ENTITY, world);
        this.setOwner(owner);
        this.setNoGravity(true);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    @Override
    public void tick() {
        super.tick();

        if (!(getOwner() instanceof LivingEntity owner) || !owner.isAlive()) {
            this.discard();
            return;
        }

        // Position Rasengan at owner's right hand
        Vec3d handOffset = new Vec3d(0.4, 0.9, 0.0); // x: right, y: hand height, z: front/back
        Vec3d rotatedOffset = handOffset.rotateY((float) -Math.toRadians(owner.getYaw()));
        Vec3d newPos = owner.getPos().add(rotatedOffset);

        this.setPosition(newPos);
        this.setVelocity(Vec3d.ZERO); // Prevent drift
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return Items.BLUE_DYE.getDefaultStack(); // Optional visual representation
    }
}*/
