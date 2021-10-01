package mc.alk.arena;

import java.io.File;
import java.lang.reflect.Field;
import junit.framework.TestCase;
import mc.alk.arena.BattleArena;
import mc.alk.arena.Defaults;
import mc.alk.arena.controllers.BattleArenaController;
import mc.alk.arena.controllers.BukkitServer;
import mc.alk.arena.controllers.ParamController;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.MatchParams;
import mc.alk.arena.objects.arenas.Arena;
import mc.alk.arena.objects.arenas.ArenaType;
import mc.alk.arena.objects.messaging.AnnouncementOptions;
import mc.alk.arena.serializers.ArenaSerializer;
import mc.alk.arena.serializers.BAClassesSerializer;
import mc.alk.arena.serializers.BAConfigSerializer;
import mc.alk.arena.serializers.MessageSerializer;
import mc.alk.arena.util.MessageUtil;
import mc.alk.mc.MCServer;
import mc.alk.tests.testbukkit.TestBukkitPlayer;
import mc.alk.tests.testbukkit.TestBukkitServer;
import mc.alk.tests.testbukkit.TestMCBukkitServer;
import org.bukkit.entity.Player;
import mc.alk.arena.objects.TestPlugin;
import mc.alk.arena.util.Helper;

public class BATest extends TestCase {
    TestPlugin plugin = null;
    BattleArenaController bac;
    private static final BAConfigSerializer baConfigSerializer = new BAConfigSerializer();
    BattleArena ba = new BattleArena();
    ArenaPlayer[] ap = new ArenaPlayer[10];
    public static String dir = "../arena/BattleArena";
    public static String cdir = dir + "/test_files/competitions";

    @Override
    protected void setUp() throws Exception {
        Defaults.DEBUG_MSGS = true;
        Defaults.TESTSERVER = true;
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        plugin = new TestPlugin();
        ArenaType.register("arena", Arena.class, plugin);
        BukkitServer.setServer(new TestBukkitServer());
        plugin.onEnable();

        /// Set test server
        MCServer.setInstance(new TestMCBukkitServer());
        baConfigSerializer.setConfig(new File(dir + "/test_files/config.yml"));
        baConfigSerializer.loadDefaults();
        MatchParams mp = ParamController.getMatchParamCopy(Defaults.DEFAULT_CONFIG_NAME);
        assertNotNull(mp);
        for (int i = 0; i < ap.length; i++) {
            ap[i] = createArenaPlayer("p" + i);
        }

        /// load classes
        BAClassesSerializer classesSerializer = new BAClassesSerializer();
        classesSerializer.setConfig(new File(dir + "/test_files/classes.yml"));
        classesSerializer.loadAll();
        /// Controller
        bac = new BattleArenaController(null);
        Field field = BattleArena.class.getDeclaredField("arenaController");
        field.setAccessible(true);
        field.set(null, bac);

        /// Messages
        MessageSerializer ms = new MessageSerializer("default", null);
        ms.setConfig(new File(dir + "/default_files/messages.yml"));
        MessageSerializer.setDefaultConfig(ms);
        AnnouncementOptions an = new AnnouncementOptions();
        AnnouncementOptions.setDefaultOptions(an);

        mp = Helper.loadParams(dir + "/test_files/competitions/ArenaConfig.yml", plugin, "Arena");
        assertNotNull(mp);

        /// Arenas
        ArenaSerializer as = new ArenaSerializer(plugin, new File(dir + "/test_files/arenas.yml"));
        ArenaSerializer.setBAC(bac);
        as.loadArenas(plugin);
    }

    public static ArenaPlayer createArenaPlayer(String name){
        Player p1 = new TestBukkitPlayer(name);
        return BattleArena.toArenaPlayer(p1);
    }

    public static void msg(String msg) {
        System.out.println(MessageUtil.decolorChat(msg));
    }

    protected void delay(long millis) {
        try {Thread.sleep(millis);}catch(Exception e){}
    }
    protected MatchParams loadParams(String type, String configName) throws Exception {
        return Helper.loadParams(cdir+"/"+configName, plugin, type);
    }
    
    public void testPerformed() {
        assertTrue(true);
    }

}
