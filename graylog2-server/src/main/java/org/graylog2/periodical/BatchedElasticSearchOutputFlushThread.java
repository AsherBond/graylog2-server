package org.graylog2.periodical;

import org.graylog2.outputs.BatchedElasticSearchOutput;
<<<<<<< HEAD
import org.graylog2.plugin.outputs.MessageOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

=======
import org.graylog2.outputs.OutputRegistry;
import org.graylog2.plugin.outputs.MessageOutput;
import org.graylog2.plugin.periodical.Periodical;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

>>>>>>> 84813ab619e8dba994e3cdc5b4eafd3ae75c908e
/**
 * @author Dennis Oelkers <dennis@torch.sh>
 */
public class BatchedElasticSearchOutputFlushThread extends Periodical {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
<<<<<<< HEAD
=======
    private final OutputRegistry outputRegistry;

    @Inject
    public BatchedElasticSearchOutputFlushThread(OutputRegistry outputRegistry) {
        this.outputRegistry = outputRegistry;
    }
>>>>>>> 84813ab619e8dba994e3cdc5b4eafd3ae75c908e

    @Override
    public boolean runsForever() {
        return false;
    }

    @Override
    public boolean stopOnGracefulShutdown() {
        return false;
    }

    @Override
    public boolean masterOnly() {
        return false;
    }

    @Override
    public boolean startOnThisNode() {
        return true;
    }

    @Override
    public boolean isDaemon() {
        return false;
    }

    @Override
    public int getInitialDelaySeconds() {
        return 0;
    }

    @Override
    public int getPeriodSeconds() {
<<<<<<< HEAD
        return 1;
=======
        return 30;
>>>>>>> 84813ab619e8dba994e3cdc5b4eafd3ae75c908e
    }

    @Override
    public void run() {
<<<<<<< HEAD
        for (MessageOutput output : core.outputs().get()) {
=======
        for (MessageOutput output : outputRegistry.get()) {
>>>>>>> 84813ab619e8dba994e3cdc5b4eafd3ae75c908e
            if (output instanceof BatchedElasticSearchOutput) {
                BatchedElasticSearchOutput batchedOutput = (BatchedElasticSearchOutput)output;
                try {
                    batchedOutput.flush();
                } catch (Exception e) {
                    LOG.error("Caught exception while trying to flush output: {}", e);
                }
            }
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 84813ab619e8dba994e3cdc5b4eafd3ae75c908e
