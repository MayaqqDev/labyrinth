package dev.mayaqq.labyrinth.mixin;

import dev.mayaqq.labyrinth.extensions.ServerPlayerEntityExtension;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements ServerPlayerEntityExtension {
    private static long pvpCooldown = 0;
    @Inject(method = "damage", at = @At("HEAD"))
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (!player.isInvulnerableTo(source)) {
            setPvpCooldown(System.currentTimeMillis());
        }
    }

    @Override
    public long getPvpCooldown() {
        return pvpCooldown;
    }

    @Override
    public void setPvpCooldown(long value) {
        pvpCooldown = value;
    }

    @Override
    public long isOnPvpCooldown() {
        return System.currentTimeMillis() - getPvpCooldown();
    }
}
