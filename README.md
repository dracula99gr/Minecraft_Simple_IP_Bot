# Minecraft_Simple_IP_Bot
A simple solution that lets your buddies know about your Minecraft Server IP. :)

This simple Discord Bot is intergrated as a plug-in to your modded Minecraft dedicated server. It updates the IP in case of network loss or server reboot.
The update message is fully customizable to your preference via the ``config.yml`` file. (Keep in mind that if you choose to customize it, you need to include the predifined keywords, so they can be replaced with the correct information: ``%server_ip%``, ``%port%``, ``%currentdatetime%``.)
The bot can also update its' message if you want to avoid spam, simply provide the message ID to it. 


NOTE: Your IP needs to be public and the port of your choice needs to be forwarded for people to be able to connect to your server. (This plug-in is inteded for those who do not have a static IP but their IP is public). You also need to create a bot application/account on discord, configure the needed permissions, get your bot token and invite it to your discord server, all that via Discord's developer portal https://discord.com/developers/applications.
The following discord permissions are a must as far as I remember: ``Read Message History``, ``Send Messages``, ``Send Messages in Threads``, ``View Channels``.



## Installation
1. Download the plugin jar from the latest Github Release.
2. Navigate to the ``plugins`` folder in your servers root directory.
3. Copy the plugin jar to the ``plugins`` folder.
4. Start the server and wait until the configuration files of the plugin are generated.
5. Stop the server.
6. Navigate to the ``plugins`` folder and open folder ``Minecraft_Simple_IP_Bot``.
7. Open config.yml & fill the required information (BOT_TOKEN, CHANNEL_ID, Port).
8. Restart the server.
9. The bot will send your public IP and it will also be logged in your server's console.
10. Enjoy!


![image](https://github.com/user-attachments/assets/fc8b3368-ee95-493b-be42-9fc996f9d4e8)
