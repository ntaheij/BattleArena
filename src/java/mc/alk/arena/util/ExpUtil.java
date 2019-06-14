package mc.alk.arena.util;

/**
 * Never use player.getTotalExperience() because it can return an incorrect
 * value because Bukkit does not update experience after spending it on
 * enchants.
 *
 * @author alkarin version: 1.4
 *
 * Levels based off of 1.3 exp formulas verified by myself.
 * Formulas used.
 * lvl <= 15 : 17*lvl;
 * 15 < lvl < 31 : 17*l + 3*(0.5*l2*(l2+1)), and 17 + 3*l2, where l2 = (l - 16)
 * lvl > 30 : 17*l + 3*(0.5*l2*(l2+1))+4*(0.5*l3*(l3+1)) and
 *            17+inc, 17 + 3*l2 +4*l3, where l2 = (l-16) and l3=(l-31)
 *
 * The forms you see in the functions are simplifications of the above
 */
/**
 * @deprecated As of BattleArena v3.9.12.0
 * Instead, use {@link mc.alk.battlebukkitlib.ExpUtil}
 */
public class ExpUtil extends mc.alk.battlebukkitlib.ExpUtil {

}
