![Logo](https://i.imgur.com/C4Ef8fT.png)
# WriterBot
## Description
A simple paste bot written in java.\
This bot uploads all kinds of text files sent in the channels to https://paste.gg and provides you with the link of the paste.

The bot is globally hosted on https://railway.app. But since its open source you can compile it and host it on your own.
# Invite The Bot https://writerbot.vectlabs.xyz
[![IMG](https://i.imgur.com/iQmbKEe.png)](https://writerbot.vectlabs.xyz)

>For Support related to the bot join my discord server.

[![Discord](https://discord.com/api/guilds/928525879087362050/widget.png?style=banner4)](https://discord.vectlabs.xyz)
# Environment Variables
The bot needs certain environment variables to work.\
\
DC_BOT_TOKEN - The discord bot token.\
PASTEGG_API_KEY - The api key of your account at https://paste.gg.
DB_HOST - The host url of the mysql database.\
DB_DATABASE - The database name of the mysql database.\
DB_USERNAME - The username of the mysql database.\
DB_PASSWORD - The password url of the mysql database.\
DB_TABLE - The table name url of the mysql database(it will create one with the name).\
DB_PORT - The port of the mysql database.

# Commands
## /activatedapi [NameOfThePasteService]
Choose your prefered paste service for your discord server.
### AvailableOptions:
Paste.gg - https://paste.gg \
HelpChat (default) - https://paste.helpch.at \
HasteBin-byMD5 - https://paste.md-5.net \
Official-HasteBin - https://www.toptal.com/developers/hastebin/

## /failsilently [Boolean]
### AvailableOptions:
True - The bot will not display an error message when it fails to create a paste.\
False - The bot will display an error message when it fails to create a paste.

## /autodelete [Boolean]
### AvailableOptions:
True - The bot will delete the attachment after it successfully uploads it to a paste service.\
False (default) - The bot will not delete the attachment after it successfully uploads it to a paste service.

