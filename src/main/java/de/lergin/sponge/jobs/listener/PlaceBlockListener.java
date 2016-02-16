package de.lergin.sponge.jobs.listener;

import de.lergin.sponge.jobs.job.Job;
import de.lergin.sponge.jobs.job.JobAction;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;

import java.util.List;

public class PlaceBlockListener extends JobListener<BlockType> {
    public PlaceBlockListener(Job job, List<BlockType> blockTypes) {
        super(job, blockTypes);
    }

    @Listener
    public void onEvent(ChangeBlockEvent.Place event, @First Player player) {
        if (event.getCause().get("Source", Player.class).isPresent()) {
            for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
                final BlockType BLOCK_TYPE = transaction.getFinal().getState().getType();

                if (JOB_ITEM_TYPES.contains(BLOCK_TYPE)) {
                    event.setCancelled(
                            !JOB.onJobListener(BLOCK_TYPE, player, JobAction.PLACE)
                    );
                }
            }
        }
    }
}
