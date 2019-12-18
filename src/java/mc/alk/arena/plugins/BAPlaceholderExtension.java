package mc.alk.arena.plugins;

import mc.alk.arena.BattleArena;
import mc.alk.arena.competition.Competition;
import mc.alk.arena.competition.match.Match;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.util.MessageUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

/**
 * Placeholder expansion for PlaceholderAPI
 *
 * @author Redned
 */
public class BAPlaceholderExtension extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "BA";
    }

    @Override
    public String getAuthor() {
        return "BattlePlugins";
    }

    @Override
    public String getVersion() {
        return BattleArena.getSelf().getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (player == null || !player.isOnline())
            return "";

        ArenaPlayer arenaPlayer = BattleArena.toArenaPlayer(player);
        if (arenaPlayer.getCompetition() == null)
            return "";

        Competition competition = arenaPlayer.getCompetition();
        switch (params) {
            case "active_game":
                return competition.getParams().getName();
            case "active_game_prefix":
                return competition.getParams().getPrefix();
            case "active_game_players":
                return String.valueOf(competition.getPlayers().size());
            case "active_game_teams":
                return String.valueOf(competition.getTeams().size());
            case "active_game_team":
                return MessageUtil.decolorChat(competition.getTeam(arenaPlayer).getScoreboardDisplayName());
            case "active_game_team_color":
                return competition.getTeam(arenaPlayer).getTeamChatColor().toString();
            case "active_game_lives_left":
                return String.valueOf(arenaPlayer.getMetaData().getLivesLeft());
            case "active_game_time_left":
                if (arenaPlayer.getCompetition() instanceof Match) {
                    Match match = (Match) competition;
                    if (match.getMatchCountdown() == null)
                        break;

                    return String.valueOf((int) (match.getMatchCountdown().getTimeRemaining() / 1000));
                }
                break;
            case "active_game_players_left":
                if (arenaPlayer.getCompetition() instanceof Match) {
                    Match match = (Match) competition;
                    return String.valueOf(match.getAlivePlayers().size());
                }
                break;
            case "active_game_teams_left":
                if (arenaPlayer.getCompetition() instanceof Match) {
                    Match match = (Match) arenaPlayer.getCompetition();
                    return String.valueOf(match.getAliveTeams().size());
                }
                break;
            case "active_game_arena":
                if (arenaPlayer.getCompetition() instanceof Match) {
                    Match match = (Match) arenaPlayer.getCompetition();
                    return match.getArena().getName();
                }
                break;
        }
        return null;
    }
}
