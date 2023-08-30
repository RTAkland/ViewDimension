/*
 * Copyright 2023 RTAkland
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package cn.rtast.viewdimension

import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.scoreboard.ServerScoreboard
import net.minecraft.scoreboard.Team
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class ViewDimension : DedicatedServerModInitializer {

    private var currentScoreboard: ServerScoreboard? = null

    private var initializedMod = false

    override fun onInitializeServer() {
        ServerTickEvents.START_SERVER_TICK.register { tick ->
            this.currentScoreboard = tick.scoreboard
            if (!this.initializedMod) {
                this.initializeMod()
                this.initializedMod = true
            }
            if (!this.currentScoreboard!!.teamNames.contains(Dimension.TheNether.id)) {
                this.currentScoreboard!!.addTeam(Dimension.TheNether.id)
                this.currentScoreboard!!.getTeam(Dimension.TheNether.id)!!.suffix = makeText("the_nether")
            }
            if (!this.currentScoreboard!!.teamNames.contains(Dimension.TheEnd.id)) {
                this.currentScoreboard!!.addTeam(Dimension.TheEnd.id)
                this.currentScoreboard!!.getTeam(Dimension.TheEnd.id)!!.suffix = makeText("the_end")
            }
            if (!this.currentScoreboard!!.teamNames.contains(Dimension.Overworld.id)) {
                this.currentScoreboard!!.addTeam(Dimension.Overworld.id)
                this.currentScoreboard!!.getTeam(Dimension.Overworld.id)!!.suffix = makeText("overworld")
            }

            val playerList = tick.playerManager.playerList
            playerList.forEach { p ->
                val currentDimension = p.world.dimensionKey.value.path
                this.currentScoreboard!!.addPlayerToTeam(
                    p.entityName.toString(),
                    Team(this.currentScoreboard!!, currentDimension)
                )
            }
        }
    }

    private fun initializeMod() {
        for (i in Dimension.values().iterator()) {
            this.currentScoreboard!!.removeTeam(Team(this.currentScoreboard, i.id))
        }
    }

    private fun makeText(dimension: String): MutableText {
        val header = Text.literal("  <").styled { it.withBold(true).withItalic(true).withColor(Formatting.GRAY) }
        val dimensionText: Text = when (dimension) {
            "overworld" -> Text.literal("主世界")
                .styled { it.withBold(true).withItalic(true).withColor(Formatting.GREEN) }

            "the_nether" -> Text.literal("下界")
                .styled { it.withBold(true).withItalic(true).withColor(Formatting.RED) }

            else -> Text.literal("末地")
                .styled { it.withBold(true).withItalic(true).withColor(Formatting.DARK_PURPLE) }
        }
        val footer = Text.literal(">  ").styled { it.withBold(true).withItalic(true).withColor(Formatting.GRAY) }
        return header.append(dimensionText).append(footer)
    }

    enum class Dimension(val id: String) {
        TheNether("the_nether"), TheEnd("the_end"), Overworld("overworld")
    }

}
