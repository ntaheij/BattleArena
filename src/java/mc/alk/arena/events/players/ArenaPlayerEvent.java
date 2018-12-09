package mc.alk.arena.events.players;

import mc.alk.arena.competition.Competition;
import mc.alk.arena.events.CompetitionEvent;
import mc.alk.arena.objects.ArenaPlayer;

public class ArenaPlayerEvent extends CompetitionEvent{
	final ArenaPlayer player;

	public ArenaPlayerEvent(ArenaPlayer player){
		this.player = player;
	}
        
        public ArenaPlayerEvent(Competition competition, ArenaPlayer player) {
            super(competition);
            this.player = player;
        }

	public ArenaPlayer getPlayer(){
		return player;
	}
}
