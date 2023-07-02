package dev.mayaqq.labyrinth.extensions;

public interface ServerPlayerEntityExtension {
    public long getPvpCooldown();
    public void setPvpCooldown(long value);
    public long isOnPvpCooldown();
}
