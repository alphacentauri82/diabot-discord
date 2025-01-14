package com.dongtronic.diabot.commands.nightscout

import com.dongtronic.diabot.commands.DiabotCommand
import com.dongtronic.diabot.data.NightscoutDAO
import com.dongtronic.diabot.util.NicknameUtils
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import org.slf4j.LoggerFactory

class NightscoutPublicCommand(category: Command.Category, parent: Command?) : DiabotCommand(category, parent) {

    private val logger = LoggerFactory.getLogger(NightscoutPublicCommand::class.java)

    init {
        this.name = "public"
        this.help = "Make your Nightscout data public or private"
        this.guildOnly = true
        this.ownerCommand = false
        this.aliases = arrayOf("pub", "p")
        this.category = category
        this.examples = arrayOf(this.parent!!.name + " public on")
    }

    override fun execute(event: CommandEvent) {
        val args = event.args.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if(args.isEmpty()) {
            // toggle visibility if no arguments are provided
            val newVisibility = !NightscoutDAO.getInstance().isNightscoutPublic(event.author)
            NightscoutDAO.getInstance().setNightscoutPublic(event.author, newVisibility)
            reply(event, newVisibility)
            return
        }

        val mode = args[0].toUpperCase()

        if (mode == "TRUE" || mode == "T" || mode == "YES" || mode == "Y" || mode == "ON") {
            NightscoutDAO.getInstance().setNightscoutPublic(event.author, true)
            reply(event, true)
        } else {
            NightscoutDAO.getInstance().setNightscoutPublic(event.author, false)
            reply(event, false)
        }
    }

    fun reply(event: CommandEvent, public: Boolean) {
        val authorNick = NicknameUtils.determineAuthorDisplayName(event)
        val visibility = if (public) "public" else "private"
        event.reply("Nightscout data for $authorNick set to $visibility")
    }
}
