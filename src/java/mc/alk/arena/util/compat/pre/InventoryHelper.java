package mc.alk.arena.util.compat.pre;

import mc.alk.arena.util.compat.IInventoryHelper;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.awt.*;
import java.util.List;

public class InventoryHelper implements IInventoryHelper{
	@Override
	public void setColor(ItemStack itemStack, Color color) {/* do nothing */}

	@Override
	public void addCustomEffect(ItemStack itemStack, PotionEffect effect) {/* do nothing */}

	@Override
	public void removeCustomEffect(ItemStack itemStack, PotionEffectType effectType) {/* do nothing */}

	@Override
	public void setLore(ItemStack itemStack, List<String> lore) {/* do nothing */}

	@Override
	public void setDisplayName(ItemStack itemStack, String displayName) {/* do nothing */}

	@Override
	public void setOwnerName(ItemStack itemStack, String ownerName) {/* do nothing */}

	@Override
	public Color getColor(ItemStack itemStack) {return null;}

	@Override
	public List<PotionEffect> getCustomEffects(ItemStack itemStack) {return null;}

	@Override
	public List<String> getLore(ItemStack itemStack) {return null;}

	@Override
	public String getDisplayName(ItemStack itemStack) {return null;}

	@Override
	public String getOwnerName(ItemStack itemStack) {return null;}

	@Override
	public String getCommonNameByEnchantment(Enchantment enchantment) {return enchantment.getName();}

	@Override
	public Enchantment getEnchantmentByCommonName(String itemName) {return null;}

	@Override
	public boolean isEnderChest(InventoryType type) {return false;}
}
