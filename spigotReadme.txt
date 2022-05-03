[IMG]https://i.imgur.com/C4Ef8fT.png[/IMG]
[B][SIZE=7][COLOR=#000000][U]WriterBot[/U][/COLOR][/SIZE][/B]
 [SIZE=5][B]Description[/B][/SIZE]
[SIZE=4][COLOR=#006600]A simple paste bot written in java.
This bot uploads all kinds of text files sent in the channels to a paste service and provides you with the link of the paste.[/COLOR][/SIZE]

[SIZE=5][COLOR=#ff0000][B]The bot is globally hosted on [URL]https://railway.app[/URL]. But since its open source you can compile it and host it on your own.[/B]
[/COLOR]
[COLOR=#0080ff]Don't wanna deal with hosting the bot?
We got you covered, you can invite the globally hosted WriterBot from the link below.

If You don't want to host the bot on a spigot instance then you can directly run the jar with the required environment variables. Head over to my [URL='https://github.com/MrF1yn/WriterBot#readme'][B][U]Github[/U][/B][/URL] for more info.
[/COLOR][/SIZE]
[SIZE=6][B][U][COLOR=#00b300]Invite The Bot[/COLOR][COLOR=#00b359]:[/COLOR][/U][/B][/SIZE]
  [URL='https://writerbot.vectlabs.xyz'][SIZE=5][B]https://writerbot.vectlabs.xyz[/B][/SIZE][/URL]
[IMG]https://i.imgur.com/iQmbKEe.png[/IMG]

[SIZE=7][COLOR=#0000ff][B]For Support related to the bot join my [URL='https://discord.vectlabs.xyz']discord[/URL] server.[/B][/COLOR][/SIZE]
[URL='https://discord.vectlabs.xyz'][IMG]https://discord.com/api/guilds/928525879087362050/widget.png?style=banner4[/IMG] [/URL]

[SIZE=6][COLOR=#404040][B][U]Config:[/U][/B][/COLOR][/SIZE]
The bot needs certain values set in config.yml to work.

DC_BOT_TOKEN - The discord bot token.
PASTEGG_API_KEY - The api key of your account at [URL]https://paste.gg[/URL].
DB_HOST - The host url of the mysql database.
DB_DATABASE - The database name of the mysql database.
DB_USERNAME - The username of the mysql database.
DB_PASSWORD - The password url of the mysql database.
DB_TABLE - The table name url of the mysql database(it will create one with the name).
DB_PORT - The port of the mysql database.

[B][U][SIZE=6][COLOR=#404040]Commands(Discord Only) [/COLOR][/SIZE][/U][/B]
  [B][SIZE=5][COLOR=#404040]/activatedapi [NameOfThePasteService][/COLOR][/SIZE][/B]
   Choose your prefered paste service for your discord server.
    [SIZE=4][COLOR=#404040][B][U]AvailableOptions:[/U][/B][/COLOR][/SIZE]
    Paste.gg - [URL]https://paste.gg[/URL]
    HelpChat (default) - [URL]https://paste.helpch.at[/URL]
    HasteBin-byMD5 - [URL]https://paste.md-5.net[/URL]
    Official-HasteBin - [URL]https://www.toptal.com/developers/hastebin/[/URL]

  [SIZE=5][COLOR=#404040][B]/failsilently [Boolean][/B][/COLOR][/SIZE]
   [SIZE=4][COLOR=#404040][B][U]AvailableOptions:[/U][/B][/COLOR][/SIZE]
    True - The bot will not display an error message when it fails to create      a paste.
    False - The bot will display an error message when it fails to create a        paste.

[SIZE=5][COLOR=#404040][B]  /autodelete [Boolean][/B][/COLOR][/SIZE]
    [SIZE=4][COLOR=#404040][B]AvailableOptions[/B][/COLOR][/SIZE]:
    True - The bot will delete the attachment after it successfully uploads it      to a paste service.\
    False (default) - The bot will not delete the attachment after it      successfully uploads it to a paste service.