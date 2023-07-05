package dev.mayaqq.labyrinth.entities;

import dev.mayaqq.labyrinth.registry.EntityRegistry;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class RidableSquidEntity extends ProjectileEntity implements PolymerEntity {
    public RidableSquidEntity(World world, double x, double y, double z, LivingEntity owner) {
        super(EntityRegistry.RIDABLE_SQUID, world);
        this.refreshPositionAndAngles(x, y, z, this.getYaw(), this.getPitch());
        this.refreshPosition();
        this.setOwner(owner);
        this.setRotation(owner.getYaw(), owner.getPitch());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age >= 50) {
            this.getWorld().addParticle(ParticleTypes.SQUID_INK, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            this.discard();
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        try {
            this.getOwner().stopRiding();

        } catch (NullPointerException ignored) {
        }
        if (!this.getWorld().isClient) {
            this.getWorld().addParticle(ParticleTypes.SQUID_INK, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            this.discard();
        }
    }

    @Override
    public boolean canHit() {
        return false;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }


    @Override
    public EntityType<?> getPolymerEntityType(ServerPlayerEntity player) {
        if (player.getUuid().toString().equals("a1732122-e22e-4edf-883c-09673eb55de8")) {
            return EntityType.GLOW_SQUID;
        } else {
            return EntityType.SQUID;
        }
    }

    @Override
    protected void initDataTracker() {

    }
}
