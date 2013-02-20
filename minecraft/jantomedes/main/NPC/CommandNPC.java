package jantomedes.main.NPC;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraft.world.GameRules;

public class CommandNPC extends CommandBase
{
    public String getCommandName()
    {
        return "npc";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "Glówna komenda sterujaca eNPeCekami. Autorstwo: Jantomedes";
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
    	MinecraftServer server = ModLoader.getMinecraftServerInstance();
		EntityPlayer player = server.getConfigurationManager().getPlayerForUsername(par1ICommandSender.getCommandSenderName());
		EntityNPC entityNPC = new EntityNPC(player.worldObj);
		entityNPC.initSettings(EnumNPCTypes.TALKER, null);
		entityNPC.posX = player.posX;
		entityNPC.posY = player.posY;
		entityNPC.posZ = player.posZ;
		player.worldObj.spawnEntityInWorld(entityNPC);
    	/*if(par2ArrayOfStr[0] == "help"){
    		if(par2ArrayOfStr.length==1){
    			par1ICommandSender.sendChatToPlayer("Dostepne opcje:");
    			par1ICommandSender.sendChatToPlayer("character - tworzy, usuwa, lub zarzadza eNPeCekami");
    			par1ICommandSender.sendChatToPlayer("group - tworzy, usuwa, lub zarzadza grupami eNPeCeków");
    		}
    		if(par2ArrayOfStr.length==2){
    			if(par2ArrayOfStr[1] == "character"){
    				par1ICommandSender.sendChatToPlayer("Dostepne opcje:");
        			par1ICommandSender.sendChatToPlayer("remove - usuwa eNPeCeka");
        			par1ICommandSender.sendChatToPlayer("create - tworzy nowego eNPeCeka");
        			par1ICommandSender.sendChatToPlayer("order - rozkazuje cos eNPeCekowi");
        			par1ICommandSender.sendChatToPlayer("setgroup - ustawia dana grupe eNPeCekowi");
        			par1ICommandSender.sendChatToPlayer("settype - tworzy nowego eNPeCeka");
    			}
    			if(par2ArrayOfStr[1] == "group"){
    				//TODO
    			}
    			else{
    				par1ICommandSender.sendChatToPlayer("Niepoprawne u¿ycie /npc help");
    			}
    			if(par2ArrayOfStr[0] == "character" && par2ArrayOfStr[1] == "create" && par2ArrayOfStr[2] == "&test1"){
    				MinecraftServer server = ModLoader.getMinecraftServerInstance();
    				EntityPlayer player = server.getConfigurationManager().getPlayerForUsername(par1ICommandSender.getCommandSenderName());
    				EntityNPC entityNPC = new EntityNPC(player.worldObj, EnumNPCTypes.TALKER, null);
    				entityNPC.posX = player.posX;
    				entityNPC.posY = player.posY;
    				entityNPC.posZ = player.posZ;
    				player.worldObj.spawnEntityInWorld(entityNPC);
    			}
    			
    			if(par2ArrayOfStr[0] == "character" && par2ArrayOfStr[1] == "create" && par2ArrayOfStr[2] == "&test2"){
    				MinecraftServer server = ModLoader.getMinecraftServerInstance();
    				EntityPlayer player = server.getConfigurationManager().getPlayerForUsername(par1ICommandSender.getCommandSenderName());
    				EntityNPC entityNPC = new EntityNPC(player.worldObj, EnumNPCTypes.TALKER, null);
    				entityNPC.posX = player.posX;
    				entityNPC.posY = player.posY;
    				entityNPC.posZ = player.posZ;
    				player.worldObj.spawnEntityInWorld(entityNPC);
    			}
    		}
    	}*/
    	
    	
    	
    	
    	
    	
        /*String var6;

        if (par2ArrayOfStr.length == 2)
        {
            var6 = par2ArrayOfStr[0];
            String var7 = par2ArrayOfStr[1];
            GameRules var8 = this.getGameRules();

            if (var8.hasRule(var6))
            {
                var8.setOrCreateGameRule(var6, var7);
                notifyAdmins(par1ICommandSender, "commands.gamerule.success", new Object[0]);
            }
            else
            {
                notifyAdmins(par1ICommandSender, "commands.gamerule.norule", new Object[] {var6});
            }
        }
        else if (par2ArrayOfStr.length == 1)
        {
            var6 = par2ArrayOfStr[0];
            GameRules var4 = this.getGameRules();

            if (var4.hasRule(var6))
            {
                String var5 = var4.getGameRuleStringValue(var6);
                par1ICommandSender.sendChatToPlayer(var6 + " = " + var5);
            }
            else
            {
                notifyAdmins(par1ICommandSender, "commands.gamerule.norule", new Object[] {var6});
            }
        }
        else if (par2ArrayOfStr.length == 0)
        {
            GameRules var3 = this.getGameRules();
            par1ICommandSender.sendChatToPlayer(joinNiceString(var3.getRules()));
        }
        else
        {
            throw new WrongUsageException("commands.gamerule.usage", new Object[0]);
        }*/
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return par2ArrayOfStr.length == 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, this.getGameRules().getRules()) : (par2ArrayOfStr.length == 2 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"true", "false"}): null);
    }

    /**
     * Return the game rule set this command should be able to manipulate.
     */
    private GameRules getGameRules()
    {
        return MinecraftServer.getServer().worldServerForDimension(0).getGameRules();
    }
}
