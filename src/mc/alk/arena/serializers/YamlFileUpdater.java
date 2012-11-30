package mc.alk.arena.serializers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import mc.alk.arena.BattleArena;
import mc.alk.arena.util.Log;
import mc.alk.plugin.updater.Version;

import org.bukkit.configuration.file.FileConfiguration;

public class YamlFileUpdater {
	BufferedReader br = null;
	BufferedWriter fw =null;
	File tempFile = null;
	File configFile = null;

	public void updateMessageSerializer(MessageSerializer ms) {
		FileConfiguration fc = ms.getConfig();

		Version version = new Version(fc.getString("version","0"));
		File dir = BattleArena.getSelf().getDataFolder();
		/// configVersion: 1.2, move over to new messages.yml
		/// this will delete their previous messages.yml
		if (version.compareTo(1.2)<0){
			File backupdir = new File(dir+"/backups");
			if (!backupdir.exists()){
				backupdir.mkdir();}
			File msgdir = new File(dir+"/messages");
			if (!msgdir.renameTo(new File(dir+"/backups/messages1.1"))){
				Log.warn("Couldn't rename the messages yml");
			}
			File messageFile = new File(dir+"/messages.yml");
			messageFile.renameTo(new File(dir+"/backups/messages.1.1.yml"));
			Log.warn("Updating to messages.yml version 1.2");
			Log.warn("If you had custom changes to messages you will have to redo them");
			Log.warn("But the old messages are saved as backups/messages.1.1.yml");
			Log.warn("You can override specific match/event messages inside the messages folder");
			move("/default_files/messages.yml",dir+"/messages.yml");
			ms.setConfig(new File(dir+"/messages.yml"));
		}
	}

	public static void updateAllConfig(ConfigSerializer cc) {
		Version version = new Version(cc.getString("configVersion","0"));
		YamlFileUpdater yfu = new YamlFileUpdater();
		yfu.configFile = cc.getFile();
		if (version.compareTo("2.0")<0){
			yfu.to2Point0(cc.getConfig(), version);}
	}

	public static void updateBaseConfig(BAConfigSerializer bacs) {
		File tempFile = null;
		FileConfiguration fc = bacs.getConfig();
		Version version = new Version(fc.getString("configVersion","0"));
		YamlFileUpdater yfu = new YamlFileUpdater();
		yfu.configFile = bacs.getFile();
		/// configVersion: 1.1, move over to classes.yml
		if (version.compareTo(1.1) <0){
			yfu.to1Point1(bacs, bacs.getConfig(), bacs.getFile(), tempFile, version);}
		if (version.compareTo(1.2)<0){
			yfu.to1Point2(bacs, bacs.getConfig(), version);}
		if (version.compareTo(1.3)<0){
			yfu.to1Point3(bacs, bacs.getConfig(), version);}
		if (version.compareTo("1.3.5")<0){
			yfu.to1Point35(bacs, bacs.getConfig(), version);}
		if (version.compareTo(1.4)<0){
			yfu.to1Point4(bacs, bacs.getConfig(), version);}
		if (version.compareTo("1.4.5")<0){
			yfu.to1Point45(bacs, bacs.getConfig(), version);}
		if (version.compareTo("1.5")<0){
			yfu.to1Point5(bacs, bacs.getConfig(), version);}
		if (version.compareTo("1.5.5")<0){
			yfu.to1Point55(bacs, bacs.getConfig(), version);}
		if (version.compareTo("1.6")<0){
			yfu.to1Point6(bacs, bacs.getConfig(), version);}
		if (version.compareTo("1.6.5")<0){
			yfu.to1Point65(bacs, bacs.getConfig(), version);}
		if (version.compareTo("1.7")<0){
			yfu.to1Point7(bacs, bacs.getConfig(), version);}
		if (version.compareTo("1.7.3")<0){
			yfu.to1Point73(bacs, bacs.getConfig(), version);}
		if (version.compareTo("2.0")<0){
			yfu.to2Point0(bacs.getConfig(), version);
			bacs.setConfig(new File(BattleArena.getSelf().getDataFolder()+"/config.yml"));
		}

	}

	private void to1Point1(BAConfigSerializer bacs, FileConfiguration fc, File f, File tempFile, Version version) {
		Log.warn("BattleArena updating config to 1.1");
		Log.warn("Classes are now located in the classes.yml");
		Boolean configStillHasClasses = fc.contains("classes");
		File classesFile = new File(BattleArena.getSelf().getDataFolder()+"/classes.yml");
		BufferedWriter cfw =null;

		String line =null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedWriter fw =null;
		try {
			tempFile = new File(BattleArena.getSelf().getDataFolder()+"/temp.yml");
			fw = new BufferedWriter(new FileWriter(tempFile));
			cfw = new BufferedWriter(new FileWriter(classesFile));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			boolean inClassSection = false;
			if (version.compareTo(0)==0){
				fw.write("configVersion: 1.1\n");
			}
			while ((line = br.readLine()) != null){
				if (line.contains("configVersion")){
					fw.write("configVersion: 1.1\n");
					if (!configStillHasClasses) /// we are finished if no classes exist
						break;
				} else if (line.matches("classes:") || line.matches(".*You can add new classes here.*")){
					inClassSection = true;
					cfw.write(line+"\n");
				} else if (inClassSection && line.matches("## default Match Options.*")){
					inClassSection = false;
					fw.write(line +"\n");
				} else if (inClassSection) {
					cfw.write(line+"\n");
				} else {
					fw.write(line+"\n");
				}
			}
			fw.close();
			cfw.close();
			tempFile.renameTo(f.getAbsoluteFile());
			bacs.setConfig(new File(BattleArena.getSelf().getDataFolder()+"/config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void to1Point2(BAConfigSerializer bacs, FileConfiguration fc, Version version) {
		Log.warn("BattleArena updating config to 1.2");
		Log.warn("You will have to remake any changes you made to defaultOptions");
		if (!openFiles())
			return;
		String line =null;
		try {
			boolean updatedDefaultSection = false;
			boolean inDefaultSection = false;
			while ((line = br.readLine()) != null){
				//				System.out.println((line.matches("defaultMatchOptions:.*") || line.matches("## default Match Options.*")) + " "+line);
				if (line.contains("configVersion")){
					fw.write("configVersion: 1.2\n");
				} else if (!updatedDefaultSection && (line.matches("defaultMatchOptions:.*") || line.matches("## default Match Options.*"))){
					updatedDefaultSection = true;
					inDefaultSection = true;
					fw.write("## default Options (these can be overridden by each match/event type)\n");
					fw.write("defaultOptions:\n");
					fw.write("    ### Match Options\n");
					fw.write("    secondsTillMatch: 3 ## Time between onPrestart and onStart\n");
					fw.write("    secondsToLoot: 5 ## Time after winning to run around and collect loot\n");
					fw.write("    matchTime: 120 ## How long do timed matches last, (in seconds)\n");
					fw.write("    matchUpdateInterval: 30 ## For timed matched, how long between sending players match updates\n");
					fw.write("\n");
					fw.write("    ### Event Options\n");
					fw.write("    eventCountdownTime: 180 ## How long before announcing an automated event and its start\n");
					fw.write("    eventCountdownInterval: 60 ## How often will it announce a reminder that its open and you can join\n");
					fw.write("\n");
					fw.write("    ### Match/Event Announcements\n");
					fw.write("    ## these only affect the broadcasts to the server or channel, not the messages the fighting players receive\n");
					fw.write("    ## announce : announce these messages \n");
					fw.write("    ## dontannounce : dont announce these messages\n");
					fw.write("    ## server : use herochat with the channel specified\n");
					fw.write("    ## herochat=<channel> : use herochat with the channel specified\n");
					fw.write("    announcements:\n");
					fw.write("        onPreStart: [ announce, server ]  ## match going to happen soon, example 'P1[p1Elo] vs P2[p2elo]'\n");
					fw.write("        onStart: [ dontannounce ]  ## match starting\n");
					fw.write("        onVictory:  [ announce, server ] ## match has been won, exmaple 'P1[p1elo] has defeated P2[p2elo]'\n");
					fw.write("\n");
					fw.write("    ### Default event Announcements\n");
					fw.write("    eventAnnouncements:\n");
					fw.write("        onOpen: [ announce, server ]  ## event is now open\n");
					fw.write("        onStart: [ announce, server ]  ## event is starting\n");
					fw.write("        onVictory:  [ announce, server ] ## event has been won\n");
					fw.write("\n");
				} else if (inDefaultSection && (line.matches("### Prerequisites.*") || line.matches("arena:.*"))){
					inDefaultSection = false;
					fw.write(line +"\n");
				} else if (inDefaultSection){
					/// dont print
				} else if ((line.matches(".*prefix.*FFA.*") || line.matches(".*prefix.*DeathMatch.*"))){
					fw.write(line +"\n");
					fw.write("    announcements: ### Override the match victory announcement as the event has one too\n");
					fw.write("        onVictory:  [ dontannounce ]\n");
				} else {
					fw.write(line+"\n");
				}
			}
			fw.close();
			tempFile.renameTo(configFile.getAbsoluteFile());
			bacs.setConfig(new File(BattleArena.getSelf().getDataFolder()+"/config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void to1Point3(BAConfigSerializer bacs, FileConfiguration fc, Version version) {
		Log.warn("BattleArena updating config to 1.3");
		if (!openFiles())
			return;
		String line =null;
		try {
			boolean updatedDefaultSection = false;
			while ((line = br.readLine()) != null){
				//				System.out.println((line.matches(".*Event Announcements.*") +"   " + line));
				if (line.contains("configVersion")){
					fw.write("configVersion: 1.3\n");
				} else if (!updatedDefaultSection && (line.matches(".*Event Announcements.*"))){
					updatedDefaultSection = true;
					fw.write("    ### Duel Options\n");
					fw.write("    allowRatedDuels: false\n");
					fw.write("    # after a player rejects a duel, how long before they can be challenged again\n");
					fw.write("    challengeInterval: 1800 # (seconds) 1800 = 30minutes\n");
					fw.write("\n");
					fw.write(line+"\n");
				} else {
					fw.write(line+"\n");
				}
			}
			fw.close();
			tempFile.renameTo(configFile.getAbsoluteFile());
			bacs.setConfig(new File(BattleArena.getSelf().getDataFolder()+"/config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void to1Point35(BAConfigSerializer bacs, FileConfiguration fc, Version version) {
		Log.warn("BattleArena updating config to 1.3.5");
		if (!openFiles())
			return;
		String line =null;
		try {
			boolean updatedDefaultSection = false;
			while ((line = br.readLine()) != null){
				//				System.out.println((line.matches(".*Event Announcements.*") +"   " + line));
				if (line.contains("configVersion")){
					fw.write("configVersion: 1.3.5\n");
				} else if (!updatedDefaultSection && (line.matches(".*challengeInterval.*"))){
					fw.write(line +"\n");
					fw.write("\n");
					fw.write("    ### Scheduled Event Options\n");
					fw.write("    ### Valid options [startContinuous, startNext]\n");
					fw.write("    onServerStart: []");
					fw.write("\n");
				} else {
					fw.write(line+"\n");
				}
			}
			fw.close();
			tempFile.renameTo(configFile.getAbsoluteFile());
			bacs.setConfig(new File(BattleArena.getSelf().getDataFolder()+"/config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void to1Point4(BAConfigSerializer bacs, FileConfiguration fc, Version version) {
		Log.warn("BattleArena updating config to 1.4");

		if (!openFiles())
			return;
		String line =null;
		try {
			boolean updatedDefaultSection = false;
			while ((line = br.readLine()) != null){
				//				System.out.println((line.matches(".*Event Announcements.*") +"   " + line));
				if (line.contains("configVersion")){
					fw.write("configVersion: 1.4\n");
				} else if (!updatedDefaultSection && (line.matches(".*moneyName:.*"))){
					fw.write(line +"\n");
					fw.write("\n");
					fw.write("### Misc Options\n");
					fw.write("# some servers like to teleport people into the floor, you can adjust the Y offset of the teleport\n");
					fw.write("# to make them teleport higher by default, 1.0 = 1 block\n");
					fw.write("teleportYOffset: 1.0\n");
					fw.write("\n");
					fw.write("# which player commands should be disabled when they enter an arena\n");
					fw.write("disabledCommands: [home, spawn, payhome, warp, watch, sethome, ma]\n");
					fw.write("\n");
				} else {
					fw.write(line+"\n");
				}
			}
			fw.close();
			tempFile.renameTo(configFile.getAbsoluteFile());
			bacs.setConfig(new File(BattleArena.getSelf().getDataFolder()+"/config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void to1Point45(BAConfigSerializer bacs, FileConfiguration fc, Version version) {
		Log.warn("BattleArena updating config to 1.4.5");
		if (!openFiles())
			return;
		String line =null;
		try {
			boolean updatedDefaultSection = false;
			while ((line = br.readLine()) != null){
				//				System.out.println((line.matches(".*Event Announcements.*") +"   " + line));
				if (line.contains("configVersion")){
					fw.write("configVersion: 1.4.5\n\n");
					fw.write("# Auto Update the BattleArena plugin (only works for unix/linux/mac)\n");
					fw.write("# Updates will be retrieved from the latest plugin on the bukkit site\n");
					fw.write("autoUpdate: true\n");
					fw.write("\n");
				} else if (!updatedDefaultSection && (line.matches(".*teleportYOffset.*"))){
					fw.write(line +"\n");
					fw.write("\n");
					fw.write("# When a player joins an arena and their inventory is stored\n");
					fw.write("# how many old inventories should be saved\n");
					fw.write("# put in 0 if you don't want this option\n");
					fw.write("numberSavedInventories: 5\n");
					fw.write("\n");
				} else {
					fw.write(line+"\n");
				}
			}
			fw.close();
			tempFile.renameTo(configFile.getAbsoluteFile());
			bacs.setConfig(new File(BattleArena.getSelf().getDataFolder()+"/config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void to1Point5(BAConfigSerializer bacs, FileConfiguration fc, Version version) {
		Log.warn("BattleArena updating config to 1.5");
		if (!openFiles())
			return;
		String line =null;
		try {
			boolean updatedDefaultSection = false;
			while ((line = br.readLine()) != null){
				//				System.out.println((line.matches(".*Event Announcements.*") +"   " + line));
				if (line.contains("configVersion")){
					fw.write("configVersion: 1.5\n\n");
				} else if (!updatedDefaultSection && (line.matches(".*disabledCommands:.*"))){
					fw.write(line +"\n");
					fw.write("\n");
					fw.write("# If set to true, items that are usually not stackable will be stacked when\n");
					fw.write("# a player is given items.  Examples: 64 mushroom soup, or 64 snow_ball, instead of 1 or 16\n");
					fw.write("ignoreMaxStackSize: false\n");
					fw.write("\n");
				} else {
					fw.write(line+"\n");
				}
			}
			fw.close();
			tempFile.renameTo(configFile.getAbsoluteFile());
			bacs.setConfig(new File(BattleArena.getSelf().getDataFolder()+"/config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {br.close();} catch (Exception e) {}
			try {fw.close();} catch (Exception e) {}
		}
	}

	private void to1Point55(BAConfigSerializer bacs, FileConfiguration fc, Version version) {
		Log.warn("BattleArena updating config to 1.5.5");
		if (!openFiles())
			return;
		String line =null;
		try {
			boolean updatedDefaultSection = false;
			while ((line = br.readLine()) != null){
				//				System.out.println((line.matches(".*Event Announcements.*") +"   " + line));
				if (line.contains("configVersion")){
					fw.write("configVersion: 1.5.5\n\n");
				} else if (!updatedDefaultSection && (line.matches(".*eventCountdownInterval:.*"))){
					fw.write(line +"\n");
					fw.write("    ## If true, when a player joins and an event that can be opened, it will be\n");
					fw.write("    ## silently opened and the player will join\n");
					fw.write("    allowPlayerCreation: true \n");
					fw.write("\n");
				} else {
					fw.write(line+"\n");
				}
			}
			fw.close();
			tempFile.renameTo(configFile.getAbsoluteFile());
			bacs.setConfig(new File(BattleArena.getSelf().getDataFolder()+"/config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {br.close();} catch (Exception e) {}
			try {fw.close();} catch (Exception e) {}
		}
	}

	private void to1Point6(BAConfigSerializer bacs, FileConfiguration fc, Version version) {
		Log.warn("BattleArena updating config to 1.6");
		if (!openFiles())
			return;
		String line =null;
		try {
			boolean updatedDefaultSection = false;
			while ((line = br.readLine()) != null){
				//				System.out.println((line.matches(".*Event Announcements.*") +"   " + line));
				if (line.contains("configVersion")){
					fw.write("configVersion: 1.6\n\n");
				} else if (!updatedDefaultSection && (line.matches(".*ignoreMaxStackSize:.*"))){
					fw.write(line +"\n\n");
					fw.write("# If true if a player joins a match which has 2 arenas. 1v1 and 1v1v1v1. Then 1v1 will happen first\n");
					fw.write("# afterwards the 1v1v1v1 is guaranteed to be the next arena used.\n");
					fw.write("# if false.  if after the 1v1 is used, and the match ends, the 1v1 can be used again before the 1v1v1v1\n");
					fw.write("useArenasOnlyInOrder: false\n\n");
				} else {
					fw.write(line+"\n");
				}
			}
			fw.close();
			tempFile.renameTo(configFile.getAbsoluteFile());
			bacs.setConfig(new File(BattleArena.getSelf().getDataFolder()+"/config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {br.close();} catch (Exception e) {}
			try {fw.close();} catch (Exception e) {}
		}
	}

	private void to1Point65(ConfigSerializer bacs, FileConfiguration fc, Version version) {
		Log.warn("BattleArena updating config to 1.6.5");
		if (!openFiles())
			return;
		String line =null;
		try {
			if (version.compareTo(0)==0){
				fw.write("configVersion: 1.6.5\n");
			}

			boolean updatedDefaultSection = false;
			while ((line = br.readLine()) != null){
				//				System.out.println((line.matches(".*Event Announcements.*") +"   " + line));
				if (line.contains("configVersion")){
					fw.write("configVersion: 1.6.5\n\n");
				} else if (!updatedDefaultSection && (line.matches(".*matchUpdateInterval:.*"))){
					fw.write(line +"\n\n");
					fw.write("    ## when set to true when a player joins a queue the match will attempt to \n");
					fw.write("    ## start after the forceStartTime regardless if the minimum amount of people\n");
					fw.write("    ## have joined.  Example: say 2 teams of 4 people each is needed, if after\n");
					fw.write("    ## the forceStartTime is exceeded only 2 teams of 1 person is needed to start.\n");
					fw.write("    matchEnableForceStart: false\n");
					fw.write("    matchForceStartTime: 180\n");
				} else {
					fw.write(line+"\n");
				}
			}
			fw.close();
			tempFile.renameTo(configFile.getAbsoluteFile());
			bacs.setConfig(new File(BattleArena.getSelf().getDataFolder()+"/config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {br.close();} catch (Exception e) {}
			try {fw.close();} catch (Exception e) {}
		}
	}

	private void to1Point7(ConfigSerializer bacs, FileConfiguration fc, Version version) {
		Log.warn("BattleArena updating config to 1.7");
		if (!openFiles())
			return;
		String line =null;
		try {
			boolean updatedDefaultSection = false;
			while ((line = br.readLine()) != null){
				//				System.out.println((line.matches(".*Event Announcements.*") +"   " + line));
				if (line.contains("configVersion")){
					fw.write("configVersion: 1.7\n\n");
				} else if (!updatedDefaultSection && (line.matches(".*enableForceStart:.*"))){
					line = line.replace("enableForceStart", "matchEnableForceStart");
					fw.write(line+"\n");
				} else if (!updatedDefaultSection && (line.matches(".*forceStartTime:.*"))){
					line = line.replace("forceStartTime", "matchForceStartTime");
					fw.write(line+"\n");
				} else {
					fw.write(line+"\n");
				}
			}
			fw.close();
			tempFile.renameTo(configFile.getAbsoluteFile());
			bacs.setConfig(new File(BattleArena.getSelf().getDataFolder()+"/config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {br.close();} catch (Exception e) {}
			try {fw.close();} catch (Exception e) {}
		}
	}

	private void to1Point73(ConfigSerializer bacs, FileConfiguration fc, Version version) {
		Log.warn("BattleArena updating config to 1.7.3");
		if (!openFiles())
			return;
		String line =null;
		try {
			boolean updatedDefaultSection = false;
			while ((line = br.readLine()) != null){
				//				System.out.println((line.matches(".*Event Announcements.*") +"   " + line));
				if (line.contains("configVersion")){
					fw.write("configVersion: 1.7.3\n\n");
				} else if (!updatedDefaultSection && (line.matches(".*useArenasOnlyInOrder:.*"))){
					fw.write(line+"\n\n");
					fw.write("### Option sets allow you to give an easy to remember name for a group of options\n");
					fw.write("# you can add as many of your own as you want\n");
					fw.write("# there are two hidden defaults. storeAll, and restoreAll that can not be overridden\n");
					fw.write("# storeAll: with options [storeExperience, storeGamemode, storeHealth, storeHunger, storeItems, storeHeroclass, storeMagic, clearExperience, clearInventory, deEnchant]\n");
					fw.write("# restoreAll: with options [restoreExperience, restoreGamemode, restoreHealth, restoreHunger, restoreItems, restoreHeroclass, restoreMagic, clearInventory, deEnchant]\n");
					fw.write("optionSets:\n");
					fw.write("  storeAll1: \n");
					fw.write("      options: [storeExperience, storeGamemode, storeHealth, storeHunger, storeItems, storeHeroclass, storeMagic, clearExperience, clearInventory, deEnchant]\n");
					fw.write("  restoreAll1:\n");
					fw.write("      options: [restoreExperience, restoreGamemode, restoreHealth, restoreHunger, restoreItems, restoreHeroclass, restoreMagic, clearInventory, deEnchant]\n");
				} else {
					fw.write(line+"\n");
				}
			}
			fw.close();
			tempFile.renameTo(configFile.getAbsoluteFile());
			bacs.setConfig(new File(BattleArena.getSelf().getDataFolder()+"/config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {br.close();} catch (Exception e) {}
			try {fw.close();} catch (Exception e) {}
		}
	}

	private void to2Point0(FileConfiguration fc, Version version) {
		Log.warn("BattleArena updating "+fc.getName() +" to 2.0");
		if (!openFiles())
			return;
		String line =null;
		try {
			if (version.compareTo(0)==0){
				fw.write("configVersion: 2.0\n");
			}
			boolean updatedDefaultSection = false;
			boolean lineRightAfterPreReqs = false;
			while ((line = br.readLine()) != null){
				//				System.out.println((line.matches(".*Event Announcements.*") +"   " + line));
				if (line.contains("configVersion")){
					fw.write("configVersion: 2.0\n\n");
				} else if (line.matches(".*preReqs:.*")){
					lineRightAfterPreReqs = true;
					line = line.replace("enableForceStart", "matchEnableForceStart");
					fw.write(line+"\n");
				} else if (!updatedDefaultSection && (line.matches(".*forceStartTime:.*"))){
					line = line.replace("forceStartTime", "matchForceStartTime");
					fw.write(line+"\n");
				} else if (lineRightAfterPreReqs && line.matches(".*options:.*")) {
					lineRightAfterPreReqs = false;
					if (line.matches(".*options:.*\\[\\s*clearInventory\\s*\\].*")){
						Log.debug("SIWTCHING    " + line);
						fw.write("        options: []\n");
					} else if (line.matches(".*options:.*clearInventory\\s*,.*")){
						Log.debug("SIWTCHING    " + line);
						line = line.replaceAll("clearInventory\\s*,", "");
						fw.write(line+"\n");
					} else if (line.matches(".*options:.*,\\s*clearInventory.*")){
						Log.debug("SIWTCHING    " + line);
						line = line.replaceAll(",\\s*clearInventory", "");
						fw.write(line+"\n");
					}
					fw.write("    onEnter:\n");
					fw.write("        options: [storeAll]\n");
					fw.write("    onLeave:\n");
					fw.write("        options: [restoreAll]\n");
				} else {
					lineRightAfterPreReqs = false;
					fw.write(line+"\n");
				}
			}
			fw.close();
			tempFile.renameTo(configFile.getAbsoluteFile());
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {br.close();} catch (Exception e) {}
			try {fw.close();} catch (Exception e) {}
		}
	}
	private boolean openFiles() {
		try {
			br = new BufferedReader(new FileReader(configFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		try {
			tempFile = new File(BattleArena.getSelf().getDataFolder()+"/temp.yml");
			fw = new BufferedWriter(new FileWriter(tempFile));
		} catch (IOException e) {
			e.printStackTrace();
			try{br.close();}catch (Exception e2){}
			return false;
		}
		return true;
	}

	public File move(String default_file, String config_file) {
		File file = new File(config_file);
		try{
			InputStream inputStream = getClass().getResourceAsStream(default_file);
			OutputStream out=new FileOutputStream(config_file);
			byte buf[]=new byte[1024];
			int len;
			while((len=inputStream.read(buf))>0){
				out.write(buf,0,len);}
			out.close();
			inputStream.close();
		} catch (Exception e){
		}
		return file;
	}
}
