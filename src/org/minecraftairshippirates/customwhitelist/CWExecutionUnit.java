package org.minecraftairshippirates.customwhitelist;

import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

/**
 * This class is to house all necessary components for executing a CW function
 */
public class CWExecutionUnit{
	public static final int TYPE_ADD_USER_BY_NAME = 0,
		TYPE_ADD_USER_BY_UUID = 1,
		TYPE_REMOVE_USER_BY_NAME = 2,
		TYPE_REMOVE_USER_BY_UUID = 3,
		TYPE_LIST_WITHOUT_RESOLVE = 4,
		TYPE_LIST_WITH_RESOLVE = 5,
		TYPE_CHECK_USER_BY_NAME = 6,
		TYPE_CEHCK_USER_BY_UUID = 7;
	private static final int[] VALID_TYPES = {0, 1, 2, 3, 4, 5, 6, 7};
	
	private final CustomWhitelistPlugin cwp;
	private final int type;
	private final CommandSender sender;
	private final String[] subCmdArgs, subCmdOpts;
	
	CWExecutionUnit(CustomWhitelistPlugin newCWP, int newType, CommandSender newSender, String[] newSubCmdArgs, String[] newSubCmdOpts) throws InvalidCWEUTypeException{
		boolean typeIsValid = false;
		for(int i : VALID_TYPES){ // For every element in VALID_TYPES
			if(i == newType){ // If the current element matches newType
				typeIsValid = true; // Mark it as valid
				break; // Then exit the loop
			}
		}
		if(!typeIsValid){ // If the type is not valid
			throw new InvalidCWEUTypeException();
		}
		cwp = newCWP;
		type = newType;
		sender = newSender;
		subCmdArgs = newSubCmdArgs;
		subCmdOpts = newSubCmdOpts;
	}
	
	public void process(){
		if(type == TYPE_ADD_USER_BY_NAME){
			try{ // Try to get the UUID
				String stuuid = UUIDFetcher.getUUID(subCmdArgs[0]);
				try{
					UUID uuid = UUID.fromString(stuuid);
					OfflinePlayer ofp = cwp.getServer().getOfflinePlayer(uuid);
					if(ofp.isWhitelisted()){ // If the player is already on the whitelist
						sender.sendMessage('\"' + subCmdArgs[0] + "\" is already on the whitelist.");
					}
					else{ // They're to be added
						ofp.setWhitelisted(true);
						sender.sendMessage('\"' + subCmdArgs[0] + "\" was added to the whitelist.");
					}
				}
				catch(IllegalArgumentException iaex){
					sender.sendMessage(ChatColor.RED + "There was an exception preprocessing trying to add a user by name, see the log for details.");
					cwp.getLogger().warning("There was an exception preprocessing trying to add a user by name: " + subCmdArgs[0]);
					iaex.printStackTrace();
				}
			}
			catch(UUIDNotFoundException unfe){
				sender.sendMessage(ChatColor.RED.toString() + '\"' + subCmdArgs[0] + "\" was not found and could not be added to the whitelist.");
			}
		}
		else if(type == TYPE_ADD_USER_BY_UUID){
			try{
				UUID uuid = UUID.fromString(subCmdArgs[0]);
				OfflinePlayer ofp = cwp.getServer().getOfflinePlayer(uuid);
				if(ofp.isWhitelisted()){ // If the player is already on the whitelist
					sender.sendMessage('\"' + subCmdArgs[0] + "\" is already on the whitelist.");
				}
				else{ // They're to be added
					ofp.setWhitelisted(true);
					sender.sendMessage('\"' + subCmdArgs[0] + "\" was added to the whitelist.");
				}
			}
			catch(IllegalArgumentException iaex){
				sender.sendMessage(ChatColor.RED + "There was an exception addding a user by uuid, see the log for details.");
				cwp.getLogger().warning("There was an exception addding a user by uuid: " + subCmdArgs[0]);
				iaex.printStackTrace();
			}
		}
		// TODO Add processing blocks for the rest of the types
	}
}
