package mc.alk.arena.objects.regions;

import mc.alk.arena.objects.YamlSerializable;
import org.battleplugins.arenaregenutil.ArenaRegenController;
import org.battleplugins.arenaregenutil.RegenPlugin;
import org.battleplugins.arenaregenutil.region.ArenaRegion;
import org.bukkit.World;

import java.util.Map;

public class PylamoRegion implements ArenaRegion, YamlSerializable {

    private String id;

    public PylamoRegion() {}

    public PylamoRegion(String id){
        this.id = id;
    }

    @Override
    public Object yamlToObject(Map<String,Object> map, String value) {
        id = value;
        return new PylamoRegion(id);
    }

    @Override
    public Object objectToYaml() {
        return id;
    }

    @Override
    public String getID() {
        return id;
    }

    // Pylamo doesn't need a world to paste
    @Override
    public World getWorld() {
        return null;
    }

    @Override
    public boolean isValid(){
        return id != null && ArenaRegenController.hasRegenPlugin(RegenPlugin.PYLAMO_RESTORATION);
    }
}
