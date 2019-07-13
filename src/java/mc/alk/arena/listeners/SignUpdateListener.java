package mc.alk.arena.listeners;

import mc.alk.arena.BattleArena;
import mc.alk.arena.events.matches.MatchFinishedEvent;
import mc.alk.arena.events.matches.MatchStartEvent;
import mc.alk.arena.events.players.ArenaPlayerEnterQueueEvent;
import mc.alk.arena.events.players.ArenaPlayerLeaveQueueEvent;
import mc.alk.arena.objects.arenas.Arena;
import mc.alk.arena.objects.signs.ArenaCommandSign;
import mc.alk.arena.util.MapOfTreeSet;
import mc.alk.arena.util.SignUtil;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Comparator;

public class SignUpdateListener implements Listener {

    MapOfTreeSet<String,ArenaCommandSign> arenaSigns =
            new MapOfTreeSet<String, ArenaCommandSign>(ArenaCommandSign.class, new Comparator<ArenaCommandSign>() {
                @Override
                public int compare(ArenaCommandSign o1, ArenaCommandSign o2) {
                    return o1.hashCode() - o2.hashCode();
                }
            });

    public void updateAllSigns() {
        for (String str : BattleArena.getBAController().getArenas().keySet()) {
            Arena arena = BattleArena.getBAController().getArenas().get(str);
            updateSign(arena, "Open");
        }
    }

    public void updateSign(Arena arena, String state) {
        ArenaCommandSign[] commandSigns = arenaSigns.getSafe(arena.getName());
        if (commandSigns == null || commandSigns.length == 0)
            return;

        for (ArenaCommandSign commandSign : commandSigns) {
            Sign sign = commandSign.getSign();

            // There is potential the sign was destroyed, so lets check
            if (sign == null)
                continue;

            String[] lines = sign.getLines();
            if (SignUtil.isJoinSign(lines)) {
                String[] formattedLines = SignUtil.getFormattedLines(commandSign.getMatchParams(), state, arena.getName(), BattleArena.getSelf().getBASignSerializer().getJoinSignFormat(state.toLowerCase()));
                for (int i = 0; i < formattedLines.length; i++) {
                    sign.setLine(i, formattedLines[i]);
                }
            }

            if (SignUtil.isLeaveSign(lines)) {
                String[] formattedLines = SignUtil.getFormattedLines(commandSign.getMatchParams(), state, arena.getName(), BattleArena.getSelf().getBASignSerializer().getLeaveSignFormat(state.toLowerCase()));
                for (int i = 0; i < formattedLines.length; i++) {
                    sign.setLine(i, formattedLines[i]);
                }
            }

            sign.update();
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onMatchStartEvent(MatchStartEvent event) {
        updateSign(event.getMatch().getArena(), "Active");
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onMatchFinishedEvent(MatchFinishedEvent event){
        updateSign(event.getMatch().getArena(), "Open");
    }

    @EventHandler
    public void onArenaPlayerEnterQueueEvent(ArenaPlayerEnterQueueEvent event){
        if (event.getArena() == null)
            return;

        updateSign(event.getArena(), "Open");
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onArenaPlayerLeaveQueueEvent(ArenaPlayerLeaveQueueEvent event){
        if (event.getArena() == null)
            return;

        updateSign(event.getArena(), "Open");
    }

    public void addSign(ArenaCommandSign acs) {
        if (acs.getSign() == null || acs.getJoinOptions() == null){
            return;
        }

        Arena a = acs.getArena();
        if (a == null)
            return;

        arenaSigns.add(a.getName(), acs);
    }

    public MapOfTreeSet<String, ArenaCommandSign> getStatusSigns() {
        return arenaSigns;
    }

}
