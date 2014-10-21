package mc.alk.arena.executors;

import mc.alk.arena.objects.arenas.Arena;
import mc.alk.arena.objects.scoreboard.ArenaScoreboard;
import mc.alk.arena.objects.scoreboard.ScoreboardFactory;
import org.bukkit.command.CommandSender;

/**
 * Command executor used to modify the Scoreboard of an arena game. <br/><br/>
 * 
 * <pre>
 * Command syntax examples:
 * /sb add|inc|undo
 * /sb inc {arena} {player}
 * /sb add {arena} {player} {amount}
 * /sb undo
 * </pre>
 * 
 * @author Nikolai
 */
public class ScoreboardExecutor {
    
    
    /**
     * Increment by one.
     */
    @MCCommand(cmds={"increment", "increase", "inc"}, op=true)
    public boolean increment(CommandSender sender, Arena arena, String name) {
        return add(sender, arena, name, 1);
    }
    
    /**
     * Add X amount.
     */
    @MCCommand(cmds={"add"}, op=true)
    public boolean add(CommandSender sender, Arena arena, String name, Integer x) {
        ArenaScoreboard sb = ScoreboardFactory.getScoreboard(arena.getName());
        sender.sendMessage("Command not yet implemented");
        return true;
    }
    
    /**
     * Undo the last '/sb' command by the sender. <br/><br/>
     * 
     * This is going to be a fun method to implement (no sarcasm).
     */
    @MCCommand(cmds={"undo"}, op=true)
    public boolean undo(CommandSender sender) {
        sender.sendMessage("Command not yet implemented");
        return true;
    }
}
