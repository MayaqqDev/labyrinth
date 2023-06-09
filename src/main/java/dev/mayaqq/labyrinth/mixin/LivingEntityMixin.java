package dev.mayaqq.labyrinth.mixin;

import dev.mayaqq.labyrinth.items.base.LabyrinthHammerItem;
import dev.mayaqq.labyrinth.items.base.LabyrinthWarHammerItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "disablesShield", at = @At("HEAD"), cancellable = true)
    private void disableShield(CallbackInfoReturnable<Boolean> cir) {
        boolean shouldDisable = false;
        Item mainHandItem = ((LivingEntity) (Object) this).getMainHandStack().getItem();
        if (mainHandItem instanceof AxeItem || mainHandItem instanceof LabyrinthHammerItem || mainHandItem instanceof LabyrinthWarHammerItem) {
            shouldDisable = true;
        }
        cir.setReturnValue(shouldDisable);
    }
}
