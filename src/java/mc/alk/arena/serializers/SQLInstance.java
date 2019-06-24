package mc.alk.arena.serializers;

import mc.alk.v1r9.serializers.SQLSerializer;

public class SQLInstance extends SQLSerializer {

    public static String URL = "localhost";
    public static String PORT = "3306";
    public static String USERNAME = "root";
    public static String PASSWORD = "";

    public static final String TABLE_PREFIX = "ba_";
    public static final String ACTIVE_GAMES_TABLE_SUFFIX = "_active_games";
    public static final String ACTIVE_EVENTS_TABLE_SUFFIX = "_active_events";

    public static final String SERVER = "Server"; // The server name
    public static final String GAME = "Game"; // The game
    public static final String ARENA = "ArenaName"; // The arena name
    public static final String QUEUED_PLAYERS = "QueuedPlayers"; // The players queued
    public static final String MAX_PLAYERS = "MaxPlayers"; // The maximum amount of players that can join
    public static final String MATCH_STATE = "MatchState"; // The match state of the arena
    public static final String PREREQUISITES = "Prerequisites"; // The prerequisites of the arena
    public static final String ENABLED = "Enabled"; // If the arena is enabled

    public String DB = "BattleArena";

    private String activeGamesTable;
    private String activeEventsTable;

    private String tableName;

    private String createActiveGamesTable, createActiveEventsTable;
    private String insertActiveGamesTable, insertActiveEventsTable;

    private String getGameServer;
    private String getGameGame;
    private String getGameArena;
    private String getGameQueuedPlayers;
    private String getGameMatchState;
    private String getGamePrerequisites;
    private String getGameMaxPlayers;
    private String getGameEnabled;

    private String getEventServer;
    private String getEventGame;
    private String getEventArena;
    private String getEventQueuedPlayers;
    private String getEventMatchState;
    private String getEventPrerequisites;
    private String getEventMaxPlayers;
    private String getEventEnabled;

    public SQLInstance() {

    }

    public void setTable(String tableName) {
        this.tableName = tableName;

        this.activeGamesTable = TABLE_PREFIX + tableName + ACTIVE_GAMES_TABLE_SUFFIX;
        this.activeEventsTable = TABLE_PREFIX + tableName + ACTIVE_EVENTS_TABLE_SUFFIX;
    }

    public String getTable(){
        return tableName;
    }

    @Override
    public boolean init() {
        super.init();

        createActiveGamesTable = "CREATE TABLE IF NOT EXISTS " + activeGamesTable
                + " (" + SERVER + " VARCHAR(100), " + GAME + " VARCHAR(100),"
                + ARENA + " VARCHAR(100), " + QUEUED_PLAYERS + " VARCHAR(40000), "
                + MATCH_STATE + "VARCHAR(100), " + PREREQUISITES + " VARCHAR(1000), "
                + MAX_PLAYERS + " VARCHAR(100), " + ENABLED + " VARCHAR(100))";

        createActiveEventsTable = "CREATE TABLE IF NOT EXISTS " + activeEventsTable
                + " (" + SERVER + " VARCHAR(100), " + GAME + " VARCHAR(100),"
                + ARENA + " VARCHAR(100), " + QUEUED_PLAYERS + " VARCHAR(40000), "
                + MATCH_STATE + "VARCHAR(100), " + PREREQUISITES + " VARCHAR(1000), "
                + MAX_PLAYERS + " VARCHAR(100), " + ENABLED + " VARCHAR(100))";

        insertActiveGamesTable = "INSERT INTO " + activeGamesTable + " VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE "
                + SERVER + " = VALUES(" + SERVER + "), " + GAME + " = VALUES(" + GAME + "), "
                + ARENA + " = VALUES(" + ARENA + "), " + QUEUED_PLAYERS + " = VALUES(" + QUEUED_PLAYERS + "), "
                + MATCH_STATE + " = VALUES(" + MATCH_STATE + "), " + PREREQUISITES + " = VALUES(" + PREREQUISITES + "), "
                + MAX_PLAYERS + " = VALUES(" + MAX_PLAYERS + "), " + ENABLED + " = VALUES(" + ENABLED + ")";

        insertActiveEventsTable = "INSERT INTO " + activeEventsTable + " VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE "
                + SERVER + " = VALUES(" + SERVER + "), " + GAME + " = VALUES(" + GAME + "), "
                + ARENA + " = VALUES(" + ARENA + "), " + QUEUED_PLAYERS + " = VALUES(" + QUEUED_PLAYERS + "), "
                + MATCH_STATE + " = VALUES(" + MATCH_STATE + "), " + PREREQUISITES + " = VALUES(" + PREREQUISITES + "), "
                + MAX_PLAYERS + " = VALUES(" + MAX_PLAYERS + "), " + ENABLED + " = VALUES(" + ENABLED + ")";

        getGameServer = "SELECT * FROM " + activeGamesTable + " WHERE " + SERVER + " = ?";
        getGameGame = "SELECT * FROM " + activeGamesTable + " WHERE " + GAME + " = ?";
        getGameArena = "SELECT * FROM " + activeGamesTable + " WHERE " + ARENA + " = ?";
        getGameQueuedPlayers = "SELECT * FROM " + activeGamesTable + " WHERE " + QUEUED_PLAYERS + " = ?";
        getGameMatchState = "SELECT * FROM " + activeGamesTable + " WHERE " + MATCH_STATE + " = ?";
        getGamePrerequisites = "SELECT * FROM " + activeGamesTable + " WHERE " + PREREQUISITES + " = ?";
        getGameMaxPlayers = "SELECT * FROM " + activeGamesTable + " WHERE " + MAX_PLAYERS + " = ?";
        getGameMaxPlayers = "SELECT * FROM " + activeGamesTable + " WHERE " + ENABLED + " = ?";

        getEventServer = "SELECT * FROM " + activeEventsTable + " WHERE " + SERVER + " = ?";
        getEventGame = "SELECT * FROM " + activeEventsTable + " WHERE " + GAME + " = ?";
        getEventArena = "SELECT * FROM " + activeEventsTable + " WHERE " + ARENA + " = ?";
        getEventQueuedPlayers = "SELECT * FROM " + activeEventsTable + " WHERE " + QUEUED_PLAYERS + " = ?";
        getEventMatchState = "SELECT * FROM " + activeEventsTable + " WHERE " + MATCH_STATE + " = ?";
        getEventPrerequisites = "SELECT * FROM " + activeEventsTable + " WHERE " + PREREQUISITES + " = ?";
        getEventMaxPlayers = "SELECT * FROM " + activeEventsTable + " WHERE " + MAX_PLAYERS + " = ?";
        getGameMaxPlayers = "SELECT * FROM " + activeEventsTable + " WHERE " + ENABLED + " = ?";

        return true;
    }
}
