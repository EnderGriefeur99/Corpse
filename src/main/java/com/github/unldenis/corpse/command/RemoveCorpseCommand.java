/*
 * Corpse - Dead bodies in bukkit
 * Copyright (C) unldenis <https://github.com/unldenis>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.unldenis.corpse.command;

import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.unldenis.corpse.manager.CorpsePool;

public class RemoveCorpseCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player))
      return false;
    Player p = (Player) sender;
    if (!p.hasPermission("corpses.remove"))
      return false;
    if (args.length == 0)
      return false;
    try {
      double radius = Math.pow(Double.parseDouble(args[0]), 2);
      CorpsePool pool = CorpsePool.getInstance();
      AtomicInteger count = new AtomicInteger(0);
      pool.getCorpses().stream().filter(corpse -> corpse.getLocation().distanceSquared(p.getLocation()) <= radius)
          .forEach(corpse -> {
            pool.remove(corpse.getId());
            count.incrementAndGet();
          });
      p.sendMessage("(" + count.get() + ") " + ChatColor.GREEN + "Corpses deleted");
      return true;
    } catch (NumberFormatException e) {
      p.sendMessage(ChatColor.RED + "Radius must be a number");
    }
    return true;
  }
}
