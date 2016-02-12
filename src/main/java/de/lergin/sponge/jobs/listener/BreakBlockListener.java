package de.lergin.sponge.jobs.listener;

import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.job.BreakBlockJob;
import de.lergin.sponge.jobs.job.Job;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;

import java.util.List;

public class BreakBlockListener {
    List<BlockType> blockTypes;
    BreakBlockJob job;

    public BreakBlockListener(BreakBlockJob job, List<BlockType> blockTypes) {
        this.blockTypes = blockTypes;
        this.job = job;
    }

    @Listener
    public void onBlockBreak(ChangeBlockEvent.Break event, @First Player player){
        if(event.getCause().get("Source", Player.class).isPresent()) {
            for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
                if (blockTypes.contains(transaction.getOriginal().getState().getType())) {
                    event.setCancelled(
                            !job.blockBreak(transaction.getOriginal().getState().getType(), player)
                    );
                }
            }
        }
    }
}
