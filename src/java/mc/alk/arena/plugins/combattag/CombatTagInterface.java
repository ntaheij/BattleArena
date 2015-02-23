package mc.alk.arena.plugins.combattag;

import org.bukkit.entity.Player;

/**
 * 
 * @author Europia79
 */
public interface CombatTagInterface {
    
    public abstract boolean isInCombat(Player player);
    public abstract void untag(Player player);
    
}
