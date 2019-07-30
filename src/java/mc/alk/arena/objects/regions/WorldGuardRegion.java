package mc.alk.arena.objects.regions;

import mc.alk.arena.objects.YamlSerializable;

import org.battleplugins.arenaregenutil.region.ArenaRegion;
import org.battleplugins.worldguardutil.controllers.WorldGuardController;
import org.battleplugins.worldguardutil.region.ProtectedArenaRegion;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Map;

public class WorldGuardRegion extends ProtectedArenaRegion implements ArenaRegion, YamlSerializable {

    public WorldGuardRegion() { }

    public WorldGuardRegion(String id, World world) {
        super(id, world);
    }

    @Override
    public Object yamlToObject(Map<String, Object> map, String value) {
        if (value == null) {
            return null;
        }
        String split[] = value.split(",");
        world = Bukkit.getWorld(split[0]);
        id = split[1];
        return new WorldGuardRegion(id, world);
    }

    @Override
    public Object objectToYaml() {
        return world.getName() + "," + id;
    }

    @Override
    public boolean isValid() {
        return id != null && world != null
                && WorldGuardController.hasWorldGuard()
                && WorldGuardController.hasRegion(world, id);
    }
}
